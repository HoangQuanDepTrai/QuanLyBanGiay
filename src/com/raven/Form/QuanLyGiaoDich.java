/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.raven.Form;

import com.raven.Dao.HoaDonCTDao;
import com.raven.Dao.HoaDonDao;
import com.raven.Dao.KhachHangDao;
import com.raven.Dao.LoaiDao;
import com.raven.Dao.NhanVienDao;
import com.raven.Dao.SanPhamDao;
import com.raven.Dao.TrangThaiDao;
import com.raven.Entity.HoaDon;
import com.raven.Entity.HoaDonCT;
import com.raven.Entity.KhachHang;
import com.raven.Entity.Loai;
import com.raven.Entity.QLNhanVien;
import com.raven.Entity.QLSanPham;
import com.raven.Entity.TrangThai;
import com.raven.main.Main;
import com.raven.uitils.Auth;
import com.raven.uitils.KhachHangGiaoHang;
import com.raven.uitils.MsgBox;
import com.raven.uitils.XDate;
import java.awt.Color;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;

public class QuanLyGiaoDich extends javax.swing.JPanel {

    HoaDonDao hDDao = new HoaDonDao();
    HoaDonCTDao hDCTDao = new HoaDonCTDao();
    SanPhamDao spDao = new SanPhamDao();
    TrangThaiDao ttDao = new TrangThaiDao();
    KhachHangDao khDao = new KhachHangDao();
    NhanVienDao nvDao = new NhanVienDao();
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    int row = -1;
    int rowHD = -1;

    public QuanLyGiaoDich() {
        initComponents();
        init();
    }

    void init() {
        dcNgayTao.setDateFormatString("dd-MM-yyyy");
        fillTableSP();
        loadCBOTrangThai();
    }

    void upDateHD(HoaDon hd) {
        hDDao.update(hd);
        lamMoi();
        fillTableHD();
    }

    public void thanhToan() {
        if (ktHD()) {
            if (rdoGiaoHang.isSelected()) {
                new Main().showGiaoHang();
                if (KhachHangGiaoHang.khachHang.getMaKH() == 1) {
                    MsgBox.alert(this, "Nhập thông tin khách giao hàng");
                } else {
                    HoaDon hd = getHoaDon();
                    hd.setMaTT(1);
                    upDateHD(hd);
                    MsgBox.alert(this, "Thành công");
                }
            } else {
                HoaDon hd = getHoaDon();
                hd.setMaTT(2);
                upDateHD(hd);
                MsgBox.alert(this, "Thành công");
            }
        }
    }

//    int getMaKH() {
//        if (rdoTaiQuay.isSelected()) {
//            return 1;
//        } else {
//            KhachHang khMoi = khDao.selectKHMoi();
//            return khMoi.getMaKH();
//        }
//    }
    int getMaTT() {
        TrangThai tt = new TrangThai();
        tt = (TrangThai) cboTT.getSelectedItem();
        return tt.getMaTrangThai();
    }

    HoaDon getHoaDon() {
        TrangThai tt = (TrangThai) cboTT.getSelectedItem();
        HoaDon hd = new HoaDon();
        hd.setMaHD(Integer.parseInt(txtMaHD.getText()));
        hd.setHinhThuc(rdoTaiQuay.isSelected());
        hd.setPhiGiao(Double.parseDouble(txtPhi.getText()));
        hd.setThanhTien(Double.parseDouble(txtThanhTien.getText()));
        hd.setTienKhachTra(Double.parseDouble(txtTienKhachTra.getText()));
        hd.setNgayTao(dcNgayTao.getDate());
        hd.setGioTao(XDate.toDate(txtGioTao.getText(), "hh:MM:ss"));
        hd.setMaKH(KhachHangGiaoHang.khachHang.getMaKH());
        hd.setMaTT(getMaTT());
        hd.setMaNV(Auth.user.getManv());
        return hd;
    }

    void taoDon() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:MM:ss");
        Date ngayTao = new Date();
        dcNgayTao.setDate(ngayTao);
        txtMaNV.setText(Auth.user.getManv());
        txtGioTao.setText(sdf.format(ngayTao) + "");
        HoaDon hd = new HoaDon();
        hd.setNgayTao(ngayTao);
        hd.setGioTao(new Date(ngayTao.getTime()));
        hd.setMaNV(Auth.user.getManv());
        TrangThai tt = new TrangThai();
        tt = (TrangThai) cboTT.getSelectedItem();
        hd.setMaTT(tt.getMaTrangThai());
        hd.setMaKH(KhachHangGiaoHang.khachHang.getMaKH());
        hDDao.insert(hd);
        txtMaHD.setText(hDDao.selectHDMoi().getMaHD() + "");
    }

    private String getTrangThai(int soLuong) {
        if (soLuong == 0) {
            return "Không hoạt động";
        } else {
            return "Hoạt động";
        }
    }

    private void fillTableSP() {
        DefaultTableModel model = (DefaultTableModel) tblGiaoDichSanPham.getModel();
        model.setRowCount(0);
        try {
            List<QLSanPham> list = spDao.selectAll();
            for (QLSanPham qLSP : list) {
                Loai loai = new LoaiDao().selectByid(qLSP.getLoai());
                Object[] row = {
                    qLSP.getMaSP(),
                    qLSP.getTenSp(),
                    qLSP.getSize(),
                    loai.getTenLoai(),
                    qLSP.getSoLuong(),
                    qLSP.getGiaBan(),};
                model.addRow(row);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu ");
        }
    }

    private void fillTableHDCT(int MaHD) {
        DefaultTableModel model = (DefaultTableModel) tblHDCT.getModel();
        model.setRowCount(0);
        try {
            List<HoaDonCT> list = hDCTDao.selectByMaHD(MaHD);
            for (HoaDonCT hdct : list) {
                QLSanPham sp = spDao.selectByid(hdct.getMaSP());
                Object[] row = {
                    hdct.getMaCT(),
                    hdct.getTenSP(),
                    hdct.getSize(),
                    hdct.getSoLuong(),
                    sp.getGiaBan(),
                    hdct.getGia(),
                    hdct.getMaSP(),};
                model.addRow(row);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu hdct");
        }
    }

    void fillTableHD() {
        TrangThai tt = (TrangThai) cboTT.getSelectedItem();
        DefaultTableModel model = (DefaultTableModel) tblHD.getModel();
        model.setRowCount(0);
        try {
            List<HoaDon> listHD = hDDao.selectByTrangThai(tt.getMaTrangThai());
            for (HoaDon hd : listHD) {
                KhachHang kh = khDao.selectByid(hd.getMaKH());
                QLNhanVien nv = nvDao.selectByid(hd.getMaNV());
                Object[] row = {
                    hd.getMaHD(),
                    kh.getTenKH(),
                    nv.getTenNV(),
                    hd.getThanhTien(),};
                model.addRow(row);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu hd");
        }
    }

    void tim() {
        DefaultTableModel model = (DefaultTableModel) tblGiaoDichSanPham.getModel();
        model.setRowCount(0);
        List<QLSanPham> list = spDao.selectByName(txtTim.getText());
        for (QLSanPham qLSP : list) {
            Loai loai = new LoaiDao().selectByid(qLSP.getLoai());
            Object[] row = {
                qLSP.getMaSP(),
                qLSP.getTenSp(),
                qLSP.getSize(),
                loai.getTenLoai(),
                qLSP.getSoLuong(),
                qLSP.getGiaBan(),};
            model.addRow(row);
        }
    }

    double getThanhTien() {
        double thanhTien = 0;
        for (int i = 0; i < tblHDCT.getRowCount(); i++) {
            thanhTien += Double.parseDouble(tblHDCT.getValueAt(i, 5).toString());
        }
        return thanhTien + Double.parseDouble(txtPhi.getText());
    }

    Loai getLoaiByMaLoai(String maLoai) {
        Loai Loai = new LoaiDao().selectByid(maLoai);
        return Loai;
    }

    boolean ktSoLuongNhap(int soLuong, int soLuongSP) {
        if (soLuong > soLuongSP || soLuong <= 0) {
            return false;
        }
        return true;
    }

    boolean ktSoLuongSP(QLSanPham sp) {
        return sp.getSoLuong() > 0;
    }

    boolean ktHDCTChuaCo(int maHD, String maSP) {
        List<HoaDonCT> listHDCT = hDCTDao.selectByMaHD(maHD);
        for (HoaDonCT hoaDonCT : listHDCT) {
            if (maSP.equals(hoaDonCT.getMaSP())) {
                return false;
            }
        }
        return true;
    }
    
    int getMaHDCTTonTai(String maSP,int maHD){
        List<HoaDonCT> listHDCT = hDCTDao.selectByMaHD(maHD);
        for (HoaDonCT hoaDonCT : listHDCT) {
            if (maSP.equals(hoaDonCT.getMaSP())) {
                return hoaDonCT.getMaCT();
            }
        }
        return 0;
    }

    void taoHDCTMoi(QLSanPham sp, int soLuong, int maHD) {
        HoaDonCT hdct = new HoaDonCT(0, (sp.getGiaBan() * soLuong), sp.getTenSp(), soLuong, sp.getSize(), sp.getMaSP(), maHD);
        hDCTDao.insert(hdct);
        tabs.setSelectedIndex(0);
        fillTableHDCT(maHD);
        fillTableSP();
    }

    void capNhatHDCTDaCo(QLSanPham sp, int soLuong, int maHD, String maSP) {
        HoaDonCT hdct = hDCTDao.selectByid(getMaHDCTTonTai(maSP, maHD));
        hdct.setSoLuong(hdct.getSoLuong() + soLuong);
        hdct.setGia(hdct.getSoLuong()*sp.getGiaBan());
        hDCTDao.update(hdct);
        tabs.setSelectedIndex(0);
        fillTableHDCT(maHD);
        fillTableSP();
    }

    void edit() {
        int soLuong = Integer.parseInt(JOptionPane.showInputDialog("Nhap so luong"));
        String maSP = (String) tblGiaoDichSanPham.getValueAt(this.row, 0);
        QLSanPham sp = spDao.selectByid(maSP);
        if (ktSoLuongSP(sp)) {
            if (!ktSoLuongNhap(soLuong, sp.getSoLuong())) {
                MsgBox.alert(this, "Số lượng không hợp lệ");
                return;
            } else {
                int maHD = Integer.parseInt(txtMaHD.getText());
                sp.setSoLuong(sp.getSoLuong() - soLuong);
                Loai loai = getLoaiByMaLoai(sp.getLoai());
                sp.setLoai(loai.getTenLoai());
                spDao.update(sp);
                if (ktHDCTChuaCo(maHD, maSP)) {
                    taoHDCTMoi(sp, soLuong, maHD);
                } else {
                    capNhatHDCTDaCo(sp, soLuong, maHD, maSP);
                }
                txtThanhTien.setText(getThanhTien() + "");
            }
        } else {
            MsgBox.alert(this, "Hết hàng");
            return;
        }
    }

    boolean ktHD() {
        if (txtMaHD.getText().isEmpty()) {
            MsgBox.alert(this, "Chưa tạo hóa đơn");
            return false;
        }
        if (txtTienKhachTra.getText().isEmpty()) {
            MsgBox.alert(this, "Chưa nhập tiền khách trả");
            return false;
        }
        if (txtPhi.getText().isEmpty()) {
            txtPhi.setText("0");
            return false;
        }
        try {
            Double.parseDouble(txtTienKhachTra.getText());
        } catch (Exception e) {
            MsgBox.alert(this, "Nhập số");
            txtTienKhachTra.setText("");
            return false;
        }

        try {
            Double.parseDouble(txtPhi.getText());
        } catch (Exception e) {
            MsgBox.alert(this, "Nhập số");
            txtPhi.setText("0");
            return false;
        }
        return true;
    }

    boolean ktTienKhachTra() {
        Double tKT;
        Double thanhTien;
        LineBorder borderRed = new LineBorder(Color.RED, 1);
        LineBorder borderCyan = new LineBorder(Color.CYAN, 1);
        if (txtTienKhachTra.getText().equals("")) {
            return false;
        }
        if (!txtTienKhachTra.getText().equals("")) {
            try {
                tKT = Double.parseDouble(txtTienKhachTra.getText());
                thanhTien = Double.parseDouble(txtThanhTien.getText());
                if (tKT < thanhTien) {
                    txtTienKhachTra.setBorder(borderRed);
                    return false;
                } else {
                    txtTienKhachTra.setBorder(borderCyan);
                    return true;
                }
            } catch (Exception e) {
                MsgBox.alert(this, "Nhập số");
                return false;
            }
        }
        return true;
    }

    void loadCBOTrangThai() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboTT.getModel();
        model.removeAllElements();
        List<TrangThai> listTrangThai = ttDao.selectAll();
        for (TrangThai trangThai : listTrangThai) {
            model.addElement(trangThai);
        }
    }

    void huy() {
        if (ktMaHD()) {
            if (MsgBox.confirm(this, "Bạn muốn hủy đơn hàng")) {
            List<HoaDonCT> listHDCT = hDCTDao.selectByMaHD(Integer.parseInt(txtMaHD.getText()));
            for (HoaDonCT hoaDonCT : listHDCT) {
                QLSanPham sp = spDao.selectByid(hoaDonCT.getMaSP());
                sp.setSoLuong(sp.getSoLuong() + hoaDonCT.getSoLuong());
                Loai loai = getLoaiByMaLoai(sp.getLoai());
                sp.setLoai(loai.getTenLoai());
                spDao.update(sp);
                fillTableSP();
            }
            hDDao.delete(Integer.parseInt(txtMaHD.getText()));
            lamMoi();
        }
        }
    }

    void lamMoi() {
        txtMaHD.setText("");
        txtMaNV.setText("");
        txtPhi.setText("0");
        txtGioTao.setText("");
        txtTienKhachTra.setText("");
        txtThanhTien.setText("");
        txtTienDu.setText("");
        dcNgayTao.cleanup();
        dcNgayTao.setDate(null);
        buttonGroup1.clearSelection();
        DefaultTableModel a = (DefaultTableModel) tblHDCT.getModel();
        a.setRowCount(0);
    }

    void hoanThanh() {
        int maHD = (int) tblHD.getValueAt(rowHD, 0);
        HoaDon hd = hDDao.selectByid(maHD);
        hd.setMaTT(2);
        upDateHD(hd);
        MsgBox.alert(this, "Hoàn thành");
        fillTableHD();
    }

    void hoanTra() {
        int maHD = (int) tblHD.getValueAt(rowHD, 0);
        HoaDon hd = hDDao.selectByid(maHD);
        hd.setMaTT(3);
        upDateHD(hd);
        List<HoaDonCT> listHDCT = hDCTDao.selectByMaHD(maHD);
        for (HoaDonCT hoaDonCT : listHDCT) {
            QLSanPham sp = spDao.selectByid(hoaDonCT.getMaSP());
            sp.setSoLuong(sp.getSoLuong() + hoaDonCT.getSoLuong());
            Loai loai = getLoaiByMaLoai(sp.getLoai());
            sp.setLoai(loai.getTenLoai());
            spDao.update(sp);
        }
        fillTableHD();
        fillTableSP();
        MsgBox.alert(this, "Hoàn tra");
    }

    void hienPM(java.awt.event.MouseEvent evt) {
        JPopupMenu pm = new JPopupMenu("pmTrangThai");
        JMenuItem mniHoanThanh = new JMenuItem("Hoàn thành");
        JMenuItem mniHoanTra = new JMenuItem("Hoàn trả");
        mniHoanThanh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hoanThanh();
            }
        });
        mniHoanTra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hoanTra();
            }
        });
        pm.add(mniHoanThanh);
        pm.add(mniHoanTra);
        pm.show(tblHD, evt.getX(), evt.getY());
    }

    boolean ktMaHD() {
        if (txtMaHD.getText().isEmpty()) {
            MsgBox.alert(this, "Chưa tạo hóa đơn");
            return false;
        }
        return true;
    }

    void them() {
        if (ktMaHD()) {
            this.row = tblGiaoDichSanPham.getSelectedRow();
            edit();
            txtThanhTien.setText(getThanhTien() + "");
        }
    }

    boolean ktPhi() {
        try {
            double phi = Double.parseDouble(txtPhi.getText());
            if (phi < 0) {
                MsgBox.alert(this, "Nhập phí >=0");
                return false;
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Phí không hợp lệ");
            return false;
        }
        return true;
    }

//    boolean ktHDCTDaTonTai(String maSP, int maHD) {
//        List<HoaDonCT> listHDCT = hDCTDao.selectByMaHD(maHD);
//        if (listHDCT.size()==0) {
//            return false;
//        }
//        for (HoaDonCT hoaDonCT : listHDCT) {
//            if (maSP.equals(hoaDonCT.getMaSP())) {
//                System.out.println(hoaDonCT.getMaCT());
//                return true;
//            }
//        }
//        return false;
//    }
//
//    int getMaHDCTTonTai(String maSP, int maHD) {
//        List<HoaDonCT> listHDCT = hDCTDao.selectByMaHD(maHD);
//        for (HoaDonCT hoaDonCT : listHDCT) {
//            if (maSP.equals(hoaDonCT.getMaSP())) {
//                return hoaDonCT.getMaCT();
//            }
//        }
//        return 0;
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        tabs = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHDCT = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtMaHD = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtThanhTien = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        rdoTaiQuay = new javax.swing.JRadioButton();
        rdoGiaoHang = new javax.swing.JRadioButton();
        txtTienDu = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        btnTaoDon = new javax.swing.JButton();
        btnThanhToan = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        dcNgayTao = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        txtGioTao = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        txtPhi = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtTienKhachTra = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHD = new javax.swing.JTable();
        cboTT = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        txtTim = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblGiaoDichSanPham = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        jMenuItem1.setText("jMenuItem1");
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("jMenuItem2");
        jPopupMenu1.add(jMenuItem2);

        setBackground(new java.awt.Color(51, 51, 51));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 255, 255));
        jLabel1.setText("Quản Lý Giao Dịch");

        tabs.setBackground(new java.awt.Color(51, 51, 51));
        tabs.setForeground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 204, 255), new java.awt.Color(0, 204, 204)), "Hóa Đơn Chi Tiết", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(0, 255, 255))); // NOI18N

        tblHDCT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MaHDCT", "Tên SP", "Size", " Số lượng", "Đơn giá", "Giá", "MaSP"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblHDCT);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 204, 255), new java.awt.Color(0, 204, 204)), "Hóa Đơn ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(0, 255, 255))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 255, 255));
        jLabel2.setText("Mã HD");

        txtMaHD.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtMaHD.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 255, 255), null));
        txtMaHD.setEnabled(false);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 255, 255));
        jLabel4.setText("Ngày tạo");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 255, 255));
        jLabel6.setText("Thành tiền ");

        txtThanhTien.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtThanhTien.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 255, 255), null));
        txtThanhTien.setEnabled(false);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 255, 255));
        jLabel7.setText("Khách trả");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 255, 255));
        jLabel8.setText("Hình thức");

        buttonGroup1.add(rdoTaiQuay);
        rdoTaiQuay.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rdoTaiQuay.setForeground(new java.awt.Color(255, 255, 255));
        rdoTaiQuay.setSelected(true);
        rdoTaiQuay.setText("Tại Quầy");

        buttonGroup1.add(rdoGiaoHang);
        rdoGiaoHang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rdoGiaoHang.setForeground(new java.awt.Color(255, 255, 255));
        rdoGiaoHang.setText("Giao Hàng");

        txtTienDu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtTienDu.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 255, 255), null));
        txtTienDu.setEnabled(false);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 255, 255));
        jLabel9.setText("Tiền dư");

        btnTaoDon.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTaoDon.setForeground(new java.awt.Color(51, 51, 51));
        btnTaoDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/Create.png"))); // NOI18N
        btnTaoDon.setText("Tạo đơn");
        btnTaoDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoDonActionPerformed(evt);
            }
        });

        btnThanhToan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThanhToan.setForeground(new java.awt.Color(51, 51, 51));
        btnThanhToan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/tien.png"))); // NOI18N
        btnThanhToan.setText("Thanh toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(51, 51, 51));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/Delete.png"))); // NOI18N
        jButton4.setText("Hủy");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        dcNgayTao.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                dcNgayTaoAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 255, 255));
        jLabel11.setText("Giờ tạo");

        txtGioTao.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtGioTao.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 255, 255), null));
        txtGioTao.setEnabled(false);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 255, 255));
        jLabel3.setText("Mã NV");

        txtMaNV.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtMaNV.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 255, 255), null));
        txtMaNV.setEnabled(false);

        txtPhi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtPhi.setText("0");
        txtPhi.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 255, 255), null));
        txtPhi.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                txtPhiMouseMoved(evt);
            }
        });
        txtPhi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtPhiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtPhiMouseExited(evt);
            }
        });
        txtPhi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPhiKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPhiKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPhiKeyTyped(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 255, 255));
        jLabel10.setText("Phí giao ");

        txtTienKhachTra.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtTienKhachTra.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 255, 255), null));
        txtTienKhachTra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTienKhachTraKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnTaoDon)
                        .addGap(10, 10, 10)
                        .addComponent(btnThanhToan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(dcNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(rdoTaiQuay)
                                    .addGap(35, 35, 35)
                                    .addComponent(rdoGiaoHang)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtPhi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                                .addComponent(txtGioTao, javax.swing.GroupLayout.Alignment.TRAILING))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel9)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTienDu)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTienKhachTra, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3)))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPhi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(rdoTaiQuay)
                        .addComponent(rdoGiaoHang)))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtGioTao)
                        .addComponent(jLabel11))
                    .addComponent(jLabel4)
                    .addComponent(dcNgayTao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txtTienKhachTra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTienDu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTaoDon, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblHD.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 255, 255), new java.awt.Color(0, 255, 255)));
        tblHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã DH", "Tên KH", "Tên NV", "Thành tiền"
            }
        ));
        tblHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHDMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblHD);

        cboTT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cboTT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboTTMouseClicked(evt);
            }
        });
        cboTT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane2)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(cboTT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboTT, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabs.addTab("Hóa Đơn", jPanel4);

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(51, 51, 255), new java.awt.Color(51, 102, 255)), "Thông Tin Sản Phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(51, 51, 51))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/Search.png"))); // NOI18N
        jButton6.setText("Tìm kiếm");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        txtTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimActionPerformed(evt);
            }
        });
        txtTim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTim)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 5, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(txtTim)
                .addContainerGap())
        );

        tblGiaoDichSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã SP", "Tên SP", "Size", "Loại", "Số lượng", "Đơn giá"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGiaoDichSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGiaoDichSanPhamMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblGiaoDichSanPhamMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(tblGiaoDichSanPham);

        jButton1.setForeground(new java.awt.Color(51, 51, 51));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/Add.png"))); // NOI18N
        jButton1.setText("Thêm");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 931, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );

        tabs.addTab("Sản Phẩm", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(315, 315, 315)
                .addComponent(jLabel1)
                .addContainerGap(336, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cboTTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTTActionPerformed
        cboTT.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                fillTableHD();
            }
        });
    }//GEN-LAST:event_cboTTActionPerformed

    private void dcNgayTaoAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_dcNgayTaoAncestorAdded

    }//GEN-LAST:event_dcNgayTaoAncestorAdded

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        thanhToan();
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void btnTaoDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoDonActionPerformed
        taoDon();
    }//GEN-LAST:event_btnTaoDonActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        tim();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void tblGiaoDichSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGiaoDichSanPhamMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGiaoDichSanPhamMouseClicked

    private void tblGiaoDichSanPhamMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGiaoDichSanPhamMousePressed
        if (evt.getClickCount() == 2) {
            this.row = tblGiaoDichSanPham.rowAtPoint(evt.getPoint());
            if (ktMaHD()) {
//                int maHD = Integer.parseInt(txtMaHD.getText());
//                String maSP = (String) tblGiaoDichSanPham.getValueAt(this.row, 0);
//                if (ktHDCTDaTonTai(maSP, maHD)) {
                edit();

//                }
            }
        }
    }//GEN-LAST:event_tblGiaoDichSanPhamMousePressed

    private void txtTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimActionPerformed

    private void txtTimKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKeyReleased
        tim();
    }//GEN-LAST:event_txtTimKeyReleased

    private void cboTTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboTTMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cboTTMouseClicked

    private void txtPhiMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPhiMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhiMouseMoved

    private void txtPhiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPhiMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhiMouseEntered

    private void txtPhiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPhiMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhiMouseExited

    private void txtPhiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhiKeyPressed

    private void txtPhiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhiKeyReleased
        if (!txtPhi.getText().equals("")) {
            if (ktPhi()) {
                txtThanhTien.setText(getThanhTien() + ""
                        + "");
            }
        }
    }//GEN-LAST:event_txtPhiKeyReleased

    private void txtPhiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhiKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhiKeyTyped

    private void txtTienKhachTraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTienKhachTraKeyReleased
        if (ktTienKhachTra()) {
            double tKT = Double.parseDouble(txtTienKhachTra.getText());
            double thanhTien = Double.parseDouble(txtThanhTien.getText());
            double tienDu = tKT - thanhTien;
            txtTienDu.setText(tienDu + "");
        }
    }//GEN-LAST:event_txtTienKhachTraKeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        huy();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void tblHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHDMouseClicked
        this.rowHD = tblHD.rowAtPoint(evt.getPoint());
        if (SwingUtilities.isRightMouseButton(evt)) {
            hienPM(evt);
        }
    }//GEN-LAST:event_tblHDMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        them();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTaoDon;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboTT;
    private com.toedter.calendar.JDateChooser dcNgayTao;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JRadioButton rdoGiaoHang;
    private javax.swing.JRadioButton rdoTaiQuay;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblGiaoDichSanPham;
    private javax.swing.JTable tblHD;
    private javax.swing.JTable tblHDCT;
    private javax.swing.JTextField txtGioTao;
    private javax.swing.JTextField txtMaHD;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtPhi;
    private javax.swing.JTextField txtThanhTien;
    private javax.swing.JTextField txtTienDu;
    private javax.swing.JTextField txtTienKhachTra;
    private javax.swing.JTextField txtTim;
    // End of variables declaration//GEN-END:variables
}
