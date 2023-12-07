/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to editHD this template
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
import com.raven.uitils.MsgBox;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Lê Minh Khôi
 */
public class TraHang extends javax.swing.JPanel {

    HoaDonDao hddao = new HoaDonDao();
    TrangThaiDao ttdao = new TrangThaiDao();
    NhanVienDao nvdao = new NhanVienDao();
    KhachHangDao khdao = new KhachHangDao();
    HoaDonCTDao hdctdao = new HoaDonCTDao();
    SanPhamDao spdao = new SanPhamDao();
    SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
    int rowHD = -1;
    int rowHDCT = -1;
    int rowHT = -1;

    /**
     * Creates new form TraHang
     */
    public TraHang() {
        initComponents();
        init();
    }

    private void init() {
        loadCBOTrangThai();
        fillTableHD();
    }

    private String getHanTra(Date ngayTao) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ngayTao);
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date ngayHanTra = calendar.getTime();
        return simpledateformat.format(ngayHanTra);
    }

    private void loadCBOTrangThai() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboTT.getModel();
        model.removeAllElements();
        List<TrangThai> listTrangThai = ttdao.selectAll();
        for (TrangThai trangThai : listTrangThai) {
            model.addElement(trangThai);
        }
    }

    private void fillTableHD() {
        TrangThai tt = (TrangThai) cboTT.getSelectedItem();
        DefaultTableModel model = (DefaultTableModel) tblHoaDon.getModel();
        model.setRowCount(0);
        try {
            List<HoaDon> listHD = hddao.selectByTrangThai(tt.getMaTrangThai());
            for (HoaDon hd : listHD) {
                QLNhanVien nv = nvdao.selectByid(hd.getMaNV());
                KhachHang kh = khdao.selectByid(hd.getMaKH());
                Object[] row = {
                    hd.getMaHD(),
                    kh.getTenKH(),
                    hd.getNgayTao(),
                    getHanTra(hd.getNgayTao()),
                    hd.getThanhTien(),
                    nv.getTenNV()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    private void fillTableHDCT(int MaHD) {
        DefaultTableModel model = (DefaultTableModel) tblHoaDonChiTiet.getModel();
        model.setRowCount(0);
        try {
            List<HoaDonCT> list = hdctdao.selectByMaHD(MaHD);
            for (HoaDonCT hdct : list) {
                Object[] row = {
                    hdct.getMaCT(),
                    hdct.getMaHD(),
                    hdct.getTenSP(),
                    hdct.getSoLuong(),
                    hdct.getGia()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    private void tim() {
        DefaultTableModel model = (DefaultTableModel) tblHoaDon.getModel();
        model.setRowCount(0);
        List<HoaDon> list = hddao.selectByName(txtTimKiem.getText());
        for (HoaDon hd : list) {
            QLNhanVien nv = nvdao.selectByid(hd.getMaNV());
            KhachHang kh = khdao.selectByid(hd.getMaKH());
            Object[] row = {
                hd.getMaHD(),
                kh.getTenKH(),
                hd.getNgayTao(),
                getHanTra(hd.getNgayTao()),
                hd.getThanhTien(),
                nv.getTenNV()
            };
            model.addRow(row);
        }
    }

    private double getTienHoanTra(double thanhtien) {
        return thanhtien * 0.9;
    }

    private void setForm(HoaDonCT hdct, int soLuong) {
        txtMaHD.setText(hdct.getMaHD() + "");
        txtTenKH.setText(getTenKH(hdct));
        double tienThanhToan = getTienThanhToan(hdct, soLuong);
        txtTienThanhToan.setText(tienThanhToan + "");
        txtTienTra.setText(getTienHoanTra(tienThanhToan) + "");
        txtSoLuong.setText(soLuong + "");
    }

    private void setFormHT() {
        int maHD = (int) tblHoanTra.getValueAt(rowHT, 1);
        String tenKH = (String) tblHoanTra.getValueAt(rowHT, 2);
        String tienTraString = tblHoanTra.getValueAt(rowHT, 4).toString();
        double tienTra = Double.parseDouble(tienTraString);
        String soLuongString = tblHoanTra.getValueAt(rowHT, 3).toString();
        int soLuong = 0;
        try {
            soLuong = Integer.parseInt(soLuongString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        double tienThanhToan = tienTra + tienTra * 0.1;
        txtMaHD.setText(maHD + "");
        txtTenKH.setText(tenKH);
        txtTienThanhToan.setText(tienThanhToan + "");
        txtTienTra.setText(tienTra + "");
        txtSoLuong.setText(soLuong + "");
    }

    private String getTenKH(HoaDonCT hdct) {
        HoaDon hd = hddao.selectByid(hdct.getMaHD());
        KhachHang kh = khdao.selectByid(hd.getMaKH());
        return kh.getTenKH();
    }

    private double getTienThanhToan(HoaDonCT hdct, int soLuongNhap) {
        QLSanPham sp = spdao.selectByid(hdct.getMaSP());
        double tienThanhToan = sp.getGiaBan() * soLuongNhap;
        return tienThanhToan;
    }

    private void fillTableHoanTra(int maHDCT) {
        DefaultTableModel model = (DefaultTableModel) tblHoanTra.getModel();
        try {
            int rowCount = model.getRowCount();
            boolean found = false;

            for (int i = 0; i < rowCount; i++) {
                int currentMaHDCT = (int) model.getValueAt(i, 0);
                if (currentMaHDCT == maHDCT) {
                    found = true;
                    int soLuongCu = Integer.parseInt(model.getValueAt(i, 3).toString());
                    int soLuongNhap = Integer.parseInt(txtSoLuong.getText());
                    int soLuongMoi = soLuongCu + soLuongNhap;
                    double tienTraCu = Double.parseDouble(model.getValueAt(i, 4).toString());
                    double tienTraText = Double.parseDouble(txtTienTra.getText());
                    double tienTraMoi = tienTraCu + tienTraText;

                    model.setValueAt(soLuongMoi, i, 3);
                    model.setValueAt(tienTraMoi, i, 4);
                    break;
                }
            }

            if (!found) {
                HoaDonCT hdct = hdctdao.selectByid(maHDCT);
                if (hdct != null) {
                    Object[] row = {
                        hdct.getMaCT(),
                        hdct.getMaHD(),
                        getTenKH(hdct),
                        txtSoLuong.getText(),
                        txtTienTra.getText()
                    };
                    model.addRow(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    private boolean ktraSLNhap(int soLuongNhap, int soLuongMua) {
        if (soLuongNhap > soLuongMua || soLuongNhap <= 0) {
            return false;
        }
        return true;
    }

    private double getGiaHDCT(QLSanPham sp, int soLuong, HoaDonCT hdct) {
        double gia = hdct.getGia() - (sp.getGiaBan() * soLuong);
        return gia;
    }

    private double getThanhTienHD(QLSanPham sp, int soLuong, HoaDon hd) {
        double thanhtTien = hd.getThanhTien() - (sp.getGiaBan() * soLuong);
        return thanhtTien;
    }

//    private int rowCountCT() {
//        DefaultTableModel model = (DefaultTableModel) tblHoaDonChiTiet.getModel();
//        int rowCount = model.getRowCount();
//        return rowCount;
//    }
    private void editHDCT() {
        try {
            int soLuong = Integer.parseInt(JOptionPane.showInputDialog("Nhập số lượng:"));
            int soLuongMua = (int) tblHoaDonChiTiet.getValueAt(rowHDCT, 3);
            if (ktraSLNhap(soLuong, soLuongMua)) {
                int maHDCT = (int) tblHoaDonChiTiet.getValueAt(rowHDCT, 0);
                HoaDonCT hdct = hdctdao.selectByid(maHDCT);
                setForm(hdct, soLuong);
                int soLuongConLai = soLuongMua - soLuong;
                String tenSP = tblHoaDonChiTiet.getValueAt(rowHDCT, 2).toString();
                QLSanPham sp = spdao.selectByIDName(tenSP, maHDCT);
                hdct.setSoLuong(soLuongConLai);
                hdct.setGia(getGiaHDCT(sp, soLuong, hdct));
                hdctdao.update(hdct);
                fillTableHoanTra(maHDCT);
                if (soLuongConLai <= 0 && hdct.getMaCT() == maHDCT) {
                    hdctdao.delete(maHDCT);
                    rowHDCT = -1;
                }
            } else {
                MsgBox.alert(this, "Số lượng không hợp lệ");
                return;
            }
        } catch (NumberFormatException e) {
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void editHD() {
        int maHD = (int) tblHoaDon.getValueAt(rowHD, 0);
        fillTableHDCT(maHD);
        tabs.setSelectedIndex(0);
    }

    private void reset() {
        rowHD = -1;
        txtMaHD.setText("");
        txtTenKH.setText("");
        txtTienThanhToan.setText("");
        txtTienTra.setText("");
        txtSoLuong.setText("");
        DefaultTableModel model = (DefaultTableModel) tblHoanTra.getModel();
        model.setRowCount(0);
    }

    private void hoanTra() {
        if (rowHDCT >= 0 && rowHDCT < tblHoaDonChiTiet.getRowCount()) {
            int maHDCT = (int) tblHoaDonChiTiet.getValueAt(rowHDCT, 0);
            int soLuong = Integer.parseInt(txtSoLuong.getText());
            String tenSP = tblHoaDonChiTiet.getValueAt(rowHDCT, 2).toString();
            QLSanPham sp = spdao.selectByIDName(tenSP, maHDCT);
            int soLuongSPMoi = sp.getSoLuong() + soLuong;
            sp.setSoLuong(soLuongSPMoi);
            spdao.update(sp);
            int maHD = (int) tblHoaDonChiTiet.getValueAt(rowHDCT, 1);
            HoaDon hd = hddao.selectByid(maHD);
            hd.setThanhTien(getThanhTienHD(sp, soLuong, hd));
            hd.setMaTT(3);
            hddao.update(hd);
            reset();
        } else {
            MsgBox.alert(this, "Giá trị row không hợp lệ");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tabs = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTenKH = new javax.swing.JTextField();
        txtMaHD = new javax.swing.JTextField();
        txtTienThanhToan = new javax.swing.JTextField();
        txtTienTra = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoanTra = new javax.swing.JTable();
        txtSoLuong = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblHoaDonChiTiet = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        cboTT = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(51, 51, 51));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Quản Lý Trả Hàng");

        tabs.setBackground(new java.awt.Color(51, 51, 51));

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102)), "Hoàn Trả", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Tên KH");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Mã HD");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Tiền đã thanh toán");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Số lượng");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Tổng tiền hoàn trả");

        txtTenKH.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtMaHD.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtTienThanhToan.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTienThanhToan.setEnabled(false);

        txtTienTra.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTienTra.setEnabled(false);

        jButton1.setBackground(new java.awt.Color(52, 228, 52));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 51, 51));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/Refresh.png"))); // NOI18N
        jButton1.setText("Hoàn Trả");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        tblHoanTra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã HDCT", "Mã HD", "Tên KH", "Số lượng ", "Tổng tiền trả"
            }
        ));
        tblHoanTra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblHoanTraMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoanTra);

        txtSoLuong.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton1)
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtTienThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtTienTra, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addGap(68, 68, 68)
                                    .addComponent(txtSoLuong))
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(80, 80, 80)
                                    .addComponent(txtMaHD)))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(77, 77, 77)
                        .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtTienThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtTienTra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102)), "Hóa Đơn Chi Tiết", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        tblHoaDonChiTiet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã HDCT", "Mã HD", "Tên SP", "Số lượng ", "Tổng tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDonChiTiet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblHoaDonChiTietMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(tblHoaDonChiTiet);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        tabs.addTab("Hoàn Trả", jPanel2);

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));

        jPanel7.setBackground(new java.awt.Color(51, 51, 51));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(102, 102, 102), new java.awt.Color(102, 102, 102)), "Hóa Đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã HD", "Tên KH", "Ngày mua", "Hạn trả", "Thành tiền", "Người tạo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblHoaDonMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(tblHoaDon);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Tìm kiếm:");

        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyReleased(evt);
            }
        });

        cboTT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(cboTT, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 5, Short.MAX_VALUE))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cboTT))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabs.addTab("Hóa Đơn", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(304, 304, 304)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblHoaDonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMousePressed
        if (evt.getClickCount() == 2) {
            rowHD = tblHoaDon.rowAtPoint(evt.getPoint());
            if (rowHD != -1) {
                Object value = tblHoaDon.getValueAt(rowHD, 3);
                if (value instanceof String) {
                    String dateStr = (String) value;
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date hanTra = dateFormat.parse(dateStr);
                        if (hanTra.before(new Date())) {
                            MsgBox.alert(this, "Đã quá hạn trả hàng");
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    editHD();
                }

            }
        }
    }//GEN-LAST:event_tblHoaDonMousePressed

    private void cboTTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTTActionPerformed
        cboTT.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                fillTableHD();
            }
        });
    }//GEN-LAST:event_cboTTActionPerformed

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyReleased
        tim();
    }//GEN-LAST:event_txtTimKiemKeyReleased


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        hoanTra();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tblHoaDonChiTietMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonChiTietMousePressed
        if (evt.getClickCount() == 2) {
            rowHDCT = tblHoaDonChiTiet.rowAtPoint(evt.getPoint());
            editHDCT();
            int maHD = (int) tblHoaDon.getValueAt(rowHD, 0);
            fillTableHDCT(maHD);
        }
    }//GEN-LAST:event_tblHoaDonChiTietMousePressed

    private void tblHoanTraMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoanTraMousePressed
        if (evt.getClickCount() == 2) {
            rowHT = tblHoanTra.rowAtPoint(evt.getPoint());
            setFormHT();
        }
    }//GEN-LAST:event_tblHoanTraMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboTT;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTable tblHoaDonChiTiet;
    private javax.swing.JTable tblHoanTra;
    private javax.swing.JTextField txtMaHD;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenKH;
    private javax.swing.JTextField txtTienThanhToan;
    private javax.swing.JTextField txtTienTra;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
