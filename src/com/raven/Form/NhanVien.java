/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.raven.Form;

import com.raven.Dao.NhanVienDao;
import com.raven.Entity.QLNhanVien;
import com.raven.uitils.Auth;
import com.raven.uitils.MsgBox;
import java.awt.Color;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Lê Minh Khôi
 */
public class NhanVien extends javax.swing.JPanel {

    int row;

    NhanVienDao NhanVienDao = new NhanVienDao();

    /**
     * Creates new form NhanVien
     */
    public NhanVien() {
        initComponents();
        init();
    }

    DefaultTableModel tblModel;

    void init() {
        fillTable();
        this.fillTable();
        this.row = -1;
        this.updateStatus();
        if (Auth.isManager()) {
            setForm(Auth.user);
            tblNhanVien.setEnabled(false);
            btnLuu.setEnabled(false);
            btnSua.setEnabled(true);
            txtMaNV.setEditable(false);
        }

    }

    void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblNhanVien.getModel();
        model.setRowCount(0);
        try {
            String keyword = txtTimKiem.getText();
            List<QLNhanVien> list = NhanVienDao.selectByKeyword(keyword);
            for (QLNhanVien nv : list) {
                Object[] row = {
                    nv.getManv(),
                    nv.getTenNV(),
                    "********",
                    nv.isGioiTinh() ? "Nam" : "Nữ",
                    nv.getSDT(),
                    nv.getEmail(),
                    nv.isVaiTro() ? "Nhân Viên" : "Quản Lý",
                    nv.isTrangThaiNV() ? "Hoạt động" : "Không hoạt động"
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setForm(QLNhanVien nv) {
        txtMaNV.setText(nv.getManv());
        txtHoten.setText(nv.getTenNV());
        txtMatKhau.setText(nv.getMatKhau());
        txtSDT.setText(nv.getSDT());
        txtEmail.setText(nv.getEmail());
        txtXNMK.setText(nv.getMatKhau());
        rdoNam.setSelected(nv.isGioiTinh());
        rdoNu.setSelected(!nv.isGioiTinh());
        rdoQuanLy.setSelected(!nv.isVaiTro());
        rdoNhanVien.setSelected(nv.isVaiTro());
        rdoHoatDong.setSelected(nv.isTrangThaiNV());
        rdoKhongHoatDong.setSelected(!nv.isTrangThaiNV());
    }

    QLNhanVien getForm() {
        QLNhanVien nv = new QLNhanVien();
        nv.setManv(txtMaNV.getText());
        nv.setTenNV(txtHoten.getText());
        nv.setMatKhau(txtMatKhau.getText());
        nv.setSDT(txtSDT.getText());
        nv.setEmail(txtEmail.getText());
        nv.setMatKhau(new String(txtMatKhau.getText()));
        nv.setVaiTro(rdoNhanVien.isSelected());
        nv.setGioiTinh(rdoNam.isSelected());
        nv.setTrangThaiNV(rdoHoatDong.isSelected());
        return nv;
    }

    void updateStatus() {
        boolean edit = (this.row >= 0);
        boolean first = (this.row == 0);
        boolean last = (this.row == tblNhanVien.getRowCount() - 1);
        //Trạng thái form
        txtMaNV.setEditable(!edit);
        btnLuu.setEnabled(!edit);
        btnSua.setEnabled(edit);
        btnXoa.setEnabled(edit);
        //Trạng thái điều hướng
        btnFirst.setEnabled(edit && !first);
        btnPrev.setEnabled(edit && !first);
        btnNext.setEnabled(edit && !last);
        btnLast.setEnabled(edit && !last);
    }

    void edit() {
        String manv = (String) tblNhanVien.getValueAt(this.row, 0);
        QLNhanVien nv = NhanVienDao.selectByid(manv);
        this.setForm(nv);
        tabs.setSelectedIndex(0);
        this.updateStatus();
    }

    void clearForm() {
        QLNhanVien nv = new QLNhanVien();
        this.setForm(nv);
        btgChucVu.clearSelection();
        btgGioiTinh.clearSelection();
        btgTrangThai.clearSelection();
        this.row = -1;
        this.updateStatus();
    }

    void insert() {
        if (Auth.isLogin()) {
            QLNhanVien nv = getForm();
            String mk2 = new String(txtXNMK.getText());
            if (!mk2.equals(nv.getMatKhau())) {
                MsgBox.alert(this, "Mật khẩu không trùng khớp!!");
            } else {
                try {
                    NhanVienDao.insert(nv);
                    this.fillTable();
                    this.clearForm();
                    MsgBox.alert(this, "Thêm nhân viên thành công!!");
                } catch (Exception e) {
                    MsgBox.alert(this, "Thêm nhân viên không thành công!!");
                }

            }
        } else {
            MsgBox.alert(this, "Vui lòng đăng nhập để thêm nhân viên!!");
        }
    }

    void update() {
        if (Auth.isLogin()) {
            QLNhanVien nv = getForm();
            String mk2 = new String(txtXNMK.getText());
            if (!mk2.equals(nv.getMatKhau())) {
                MsgBox.alert(this, "Xác nhận mật khẩu không đúng!!");
            } else {
                try {
                    NhanVienDao.update(nv);
                    this.fillTable();
                    clearForm();
                    MsgBox.alert(this, "Cập nhật nhân viên thành công!!");
                } catch (Exception e) {
                    MsgBox.alert(this, "Cập nhật nhân viên không thành công!!");
                }
            }
        } else {
            MsgBox.alert(this, "Vui lòng đăng nhập để sửa nhân viên!!");
        }
    }

    void delete() {
        if (Auth.isLogin()) {
            if (Auth.isManager()) {
                MsgBox.alert(this, "Bạn không có quyền xóa nhân viên!!");
            } else {
                String manv = txtMaNV.getText();
                if (manv.equals(Auth.user.getManv())) {
                    MsgBox.alert(this, "Bạn không thể xóa tài khoản của bạn!!");
                } else if (MsgBox.confirm(this, "Xóa nhân viên đã chọn??")) {
                    try {
                        NhanVienDao.delete(manv);
                        this.fillTable();
                        this.clearForm();
                        MsgBox.alert(this, "Xóa nhân viên thành công!!");
                    } catch (Exception e) {
                        MsgBox.alert(this, "Xóa nhân viên thất bại!!");
                    }
                }
            }
        } else {
            MsgBox.alert(this, "Vui lòng đăng nhập để xóa nhân viên!!");
        }
    }

    void timKiem() {
        this.fillTable();
        this.clearForm();
        this.row = -1;
        updateStatus();
    }

    void First() {
        this.row = 0;
        this.edit();
    }

    void Prev() {
        if (this.row > 0) {
            this.row--;
            this.edit();
        }
    }

    void Next() {
        if (this.row < tblNhanVien.getRowCount() - 1) {
            this.row++;
            this.edit();
        }
    }

    void Last() {
        this.row = tblNhanVien.getRowCount() - 1;
        this.edit();
    }

    boolean kTTrungMa() {
        NhanVienDao nvDAO = new NhanVienDao();
        List<QLNhanVien> listNV = nvDAO.selectAll();
        for (QLNhanVien qLNhanVien : listNV) {
            if (txtMaNV.getText().equalsIgnoreCase(qLNhanVien.getManv())) {
                MsgBox.alert(this, "Mã nhân viên đã tồn tại!!");
                return false;
            }
        }
        return true;
    }

    boolean KiemTra() {
//        if(Auth.isManager()){
//            MsgBox.alert(this, "Bạn không có quyền thêm nhân viên!!");
//            return false;
//        }
        if (txtMaNV.getText().length() == 0) {
            MsgBox.alert(this, "Vui lòng nhập mã nhân viên!!");
            txtMaNV.setBackground(Color.yellow);
            return false;
        } else {
            txtMaNV.setBackground(Color.white);
            txtMaNV.setForeground(Color.black);
            String pantent = "^[A-Za-z0-9]+$";
            if (!txtMaNV.getText().matches(pantent)) {
                MsgBox.alert(this, "Mã nhân viên không có ký tự đặt biệt!!");
                txtMaNV.setBackground(Color.orange);
                txtMaNV.setForeground(Color.red);
                return false;
            }
        }

        if (txtMatKhau.getText().length() == 0) {
            MsgBox.alert(this, "Vui lòng nhập mật khẩul!!");
            txtMatKhau.setBackground(Color.yellow);
            return false;
        } else {
            txtMatKhau.setBackground(Color.white);
            txtMatKhau.setForeground(Color.black);
        }

        if (txtXNMK.getText().length() == 0) {
            MsgBox.alert(this, "Vui lòng nhập xác nhận mật khẩu!!");
            txtXNMK.setBackground(Color.yellow);
            return false;
        } else {
            txtXNMK.setBackground(Color.white);
            txtMatKhau.setForeground(Color.black);
        }

        if (txtHoten.getText().length() == 0) {
            MsgBox.alert(this, "Vui lòng nhập họ và tên!!");
            txtHoten.setBackground(Color.yellow);
            return false;
        } else {
            txtHoten.setBackground(Color.white);
            txtHoten.setForeground(Color.black);
            String pantent = "^[^!-@]+$"; //Biểu thức chính quy tiếng Việt
            if (!txtHoten.getText().matches(pantent)) {
                MsgBox.alert(this, "Họ và Tên không không chứa ký tự đặc biệt và số!!");
                txtHoten.setForeground(Color.red);
                txtHoten.setBackground(Color.orange);
                return false;
            }
        }

        if (txtSDT.getText().length() == 0) {
            MsgBox.alert(this, "Vui lòng nhập số điện thoại!!");
            txtSDT.setBackground(Color.yellow);
            return false;
        } else {
            txtSDT.setBackground(Color.white);
            txtSDT.setForeground(Color.black);
            String pantent = "^0[0-9]{9}+$"; //Biểu thức chính quy email
            if (!txtSDT.getText().matches(pantent)) {
                MsgBox.alert(this, "Số điện thoại không đúng định dạng!!");
                txtSDT.setBackground(Color.orange);
                txtSDT.setForeground(Color.red);
                return false;
            }
        }

        if (btgGioiTinh.getSelection() == null) {
            MsgBox.alert(this, "Vui lòng chọn giới tính!!");
            rdoNam.setForeground(Color.red);
            rdoNu.setForeground(Color.red);
            return false;
        } else {
            rdoNam.setForeground(Color.white);
            rdoNu.setForeground(Color.white);
        }

        if (btgChucVu.getSelection() == null) {
            MsgBox.alert(this, "Vui lòng chọn chức vụ!!");
            rdoQuanLy.setForeground(Color.red);
            rdoNhanVien.setForeground(Color.red);
            return false;
        } else {
            rdoQuanLy.setForeground(Color.white);
            rdoNhanVien.setForeground(Color.white);
        }

        if (btgTrangThai.getSelection() == null) {
            MsgBox.alert(this, "Vui lòng chọn trạng thái!!");
            rdoHoatDong.setForeground(Color.red);
            rdoKhongHoatDong.setForeground(Color.red);
            return false;
        } else {
            rdoHoatDong.setForeground(Color.white);
            rdoKhongHoatDong.setForeground(Color.white);
        }

        if (txtEmail.getText().length() == 0) {
            MsgBox.alert(this, "Vui lòng nhập Email!!");
            txtEmail.setBackground(Color.yellow);
            return false;
        } else {
            txtEmail.setBackground(Color.white);
            txtEmail.setForeground(Color.black);
            String pantent = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"; //Biểu thức chính quy Email
            if (!txtEmail.getText().matches(pantent)) {
                MsgBox.alert(this, "Email không đúng định dạng!!");
                txtEmail.setBackground(Color.orange);
                txtEmail.setForeground(Color.red);
                return false;
            }
        }
        NhanVienDao nvDAO = new NhanVienDao();
        List<QLNhanVien> listNV = nvDAO.selectAll();
        for (QLNhanVien qLNhanVien : listNV) {
            if (txtMaNV.getText().equalsIgnoreCase(qLNhanVien.getManv())) {
                MsgBox.alert(this, "Mã nhân viên đã tồn tại!!");
                return false;
            }
        }
        return true;
    }

    boolean KiemTra1() {
        if (txtMaNV.getText().length() == 0) {
            MsgBox.alert(this, "Vui lòng nhập mã nhân viên!!");
            txtMaNV.setBackground(Color.yellow);
            return false;
        } else {
            txtMaNV.setBackground(Color.white);
            txtMaNV.setForeground(Color.black);
            String pantent = "^[A-Za-z0-9]+$";
            if (!txtMaNV.getText().matches(pantent)) {
                MsgBox.alert(this, "Mã nhân viên không có ký tự đặt biệt!!");
                txtMaNV.setBackground(Color.orange);
                txtMaNV.setForeground(Color.red);
                return false;
            }
        }

        if (txtMatKhau.getText().length() == 0) {
            MsgBox.alert(this, "Vui lòng nhập mật khẩul!!");
            txtMatKhau.setBackground(Color.yellow);
            return false;
        } else {
            txtMatKhau.setBackground(Color.white);
            txtMatKhau.setForeground(Color.black);
        }

        if (txtXNMK.getText().length() == 0) {
            MsgBox.alert(this, "Vui lòng nhập xác nhận mật khẩu!!");
            txtXNMK.setBackground(Color.yellow);
            return false;
        } else {
            txtXNMK.setBackground(Color.white);
            txtMatKhau.setForeground(Color.black);
        }

        if (txtHoten.getText().length() == 0) {
            MsgBox.alert(this, "Vui lòng nhập họ và tên!!");
            txtHoten.setBackground(Color.yellow);
            return false;
        } else {
            txtHoten.setBackground(Color.white);
            txtHoten.setForeground(Color.black);
            String pantent = "^[^!-@]+$"; //Biểu thức chính quy tiếng Việt
            if (!txtHoten.getText().matches(pantent)) {
                MsgBox.alert(this, "Họ và Tên không không chứa ký tự đặc biệt và số!!");
                txtHoten.setForeground(Color.red);
                txtHoten.setForeground(Color.orange);
                return false;
            }
        }

        if (txtSDT.getText().length() == 0) {
            MsgBox.alert(this, "Vui lòng nhập số điện thoại!!");
            txtSDT.setBackground(Color.yellow);
            return false;
        } else {
            txtSDT.setBackground(Color.white);
            txtSDT.setForeground(Color.black);
            String pantent = "^0[0-9]{9}+$"; //Biểu thức chính quy email
            if (!txtSDT.getText().matches(pantent)) {
                MsgBox.alert(this, "Số điện thoại không đúng định dạng!!");
                txtSDT.setBackground(Color.orange);
                txtSDT.setForeground(Color.red);
                return false;
            }
        }

        if (btgGioiTinh.getSelection() == null) {
            MsgBox.alert(this, "Vui lòng chọn giới tính!!");
            rdoNam.setForeground(Color.red);
            rdoNu.setForeground(Color.red);
            return false;
        } else {
            rdoNam.setForeground(Color.white);
            rdoNu.setForeground(Color.white);
        }

        if (btgChucVu.getSelection() == null) {
            MsgBox.alert(this, "Vui lòng chọn chức vụ!!");
            rdoQuanLy.setForeground(Color.red);
            rdoNhanVien.setForeground(Color.red);
            return false;
        } else {
            rdoQuanLy.setForeground(Color.white);
            rdoNhanVien.setForeground(Color.white);
        }

        if (btgTrangThai.getSelection() == null) {
            MsgBox.alert(this, "Vui lòng chọn trạng thái!!");
            rdoHoatDong.setForeground(Color.red);
            rdoKhongHoatDong.setForeground(Color.red);
            return false;
        } else {
            rdoHoatDong.setForeground(Color.white);
            rdoKhongHoatDong.setForeground(Color.white);
        }

        if (txtEmail.getText().length() == 0) {
            MsgBox.alert(this, "Vui lòng nhập Email!!");
            txtEmail.setBackground(Color.yellow);
            return false;
        } else {
            txtEmail.setBackground(Color.white);
            txtEmail.setForeground(Color.black);
            String pantent = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"; //Biểu thức chính quy Email
            if (!txtEmail.getText().matches(pantent)) {
                MsgBox.alert(this, "Email không đúng định dạng!!");
                txtEmail.setBackground(Color.orange);
                txtEmail.setForeground(Color.red);
                return false;
            }
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btgGioiTinh = new javax.swing.ButtonGroup();
        btgChucVu = new javax.swing.ButtonGroup();
        btgTrangThai = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtHoten = new javax.swing.JTextField();
        txtMatKhau = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        rdoQuanLy = new javax.swing.JRadioButton();
        rdoNhanVien = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        txtXNMK = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        rdoHoatDong = new javax.swing.JRadioButton();
        rdoKhongHoatDong = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();

        setBackground(new java.awt.Color(51, 51, 51));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 255, 255));
        jLabel1.setText("Quản Lý Nhân Viên");

        tabs.setBackground(new java.awt.Color(51, 51, 51));

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 255, 255));
        jLabel2.setText("Họ tên");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 255, 255));
        jLabel3.setText("Mật khẩu");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 255, 255));
        jLabel4.setText("SDT");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 255, 255));
        jLabel5.setText("Giới tính");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 255, 255));
        jLabel6.setText("Chức vụ");

        txtHoten.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 255, 255), null));

        txtMatKhau.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 255, 255), null));

        txtSDT.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 255, 255), null));

        btgGioiTinh.add(rdoNam);
        rdoNam.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rdoNam.setForeground(new java.awt.Color(255, 255, 255));
        rdoNam.setText("Nam");

        btgGioiTinh.add(rdoNu);
        rdoNu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rdoNu.setForeground(new java.awt.Color(255, 255, 255));
        rdoNu.setText("Nữ");

        btgChucVu.add(rdoQuanLy);
        rdoQuanLy.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rdoQuanLy.setForeground(new java.awt.Color(255, 255, 255));
        rdoQuanLy.setText("Quản lý");

        btgChucVu.add(rdoNhanVien);
        rdoNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rdoNhanVien.setForeground(new java.awt.Color(255, 255, 255));
        rdoNhanVien.setText("Nhân viên");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 255, 255));
        jLabel7.setText("Trạng thái");

        txtMaNV.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 255, 255), null));

        txtXNMK.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 255, 255), null));

        txtEmail.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 255, 255), null));

        btgTrangThai.add(rdoHoatDong);
        rdoHoatDong.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rdoHoatDong.setForeground(new java.awt.Color(255, 255, 255));
        rdoHoatDong.setText("Hoạt động");

        btgTrangThai.add(rdoKhongHoatDong);
        rdoKhongHoatDong.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rdoKhongHoatDong.setForeground(new java.awt.Color(255, 255, 255));
        rdoKhongHoatDong.setText("Không hoạt động");
        rdoKhongHoatDong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoKhongHoatDongActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 255, 255));
        jLabel9.setText("Mã NV");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 255, 255));
        jLabel10.setText("Xác nhận");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 255, 255));
        jLabel11.setText("Email");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(67, 67, 67)
                        .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))
                                .addGap(36, 36, 36)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtMatKhau)
                                    .addComponent(txtMaNV)
                                    .addComponent(txtHoten, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel9)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(36, 36, 36)
                                .addComponent(txtXNMK, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(97, 97, 97)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel5))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(95, 95, 95)
                                        .addComponent(rdoNam, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(rdoNu, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(244, 244, 244)
                                        .addComponent(rdoQuanLy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(59, 59, 59)
                                .addComponent(txtEmail))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(85, 85, 85)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(rdoNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(rdoHoatDong, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(57, 57, 57)
                                .addComponent(rdoKhongHoatDong)))))
                .addGap(48, 48, 48))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtXNMK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtHoten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(rdoNam)
                                    .addComponent(rdoNu))
                                .addGap(18, 18, 18)
                                .addComponent(rdoQuanLy)
                                .addGap(38, 38, 38))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(rdoNhanVien))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(rdoHoatDong)
                                    .addComponent(rdoKhongHoatDong))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(159, Short.MAX_VALUE))
        );

        tabs.addTab("Cập nhật", jPanel1);

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MaNV", "Họ tên", "Mật khẩu", "Giới tính", "SDT", "Email", "Chức vụ", "Trạng thái"
            }
        ));
        tblNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblNhanVienMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblNhanVien);
        if (tblNhanVien.getColumnModel().getColumnCount() > 0) {
            tblNhanVien.getColumnModel().getColumn(3).setResizable(false);
        }

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyReleased(evt);
            }
        });

        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/Search.png"))); // NOI18N
        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 796, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabs.addTab("Danh sách", jPanel2);

        btnLuu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLuu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/Save.png"))); // NOI18N
        btnLuu.setText("Lưu");
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        btnSua.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/Edit.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/Delete.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnMoi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/Refresh.png"))); // NOI18N
        btnMoi.setText("Mới");
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        btnFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/first.png"))); // NOI18N
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/prev.png"))); // NOI18N
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/next.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/last.png"))); // NOI18N
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(298, 298, 298)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabs)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnLuu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSua)
                        .addGap(18, 18, 18)
                        .addComponent(btnXoa)
                        .addGap(18, 18, 18)
                        .addComponent(btnMoi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(tabs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnFirst)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnLuu)
                        .addComponent(btnSua)
                        .addComponent(btnXoa)
                        .addComponent(btnMoi))
                    .addComponent(btnLast)
                    .addComponent(btnNext)
                    .addComponent(btnPrev))
                .addGap(17, 17, 17))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void rdoKhongHoatDongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoKhongHoatDongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoKhongHoatDongActionPerformed

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        // TODO add your handling code here:
        if (KiemTra() == true) {
            insert();
        }
    }//GEN-LAST:event_btnLuuActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        if (KiemTra1() == true) {
            update();
        }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btnMoiActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        First();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        Prev();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        Next();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        Last();
    }//GEN-LAST:event_btnLastActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
        timKiem();
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyReleased
        // TODO add your handling code here:
        timKiem();
    }//GEN-LAST:event_txtTimKiemKeyReleased

    private void tblNhanVienMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhanVienMousePressed
        // TODO add your handling code here:
        if (Auth.isManager()) {
            setForm(Auth.user);
            tblNhanVien.setEnabled(false);
            return;
        } else {
            if (evt.getClickCount() == 2) {
                this.row = tblNhanVien.getSelectedRow();
                this.edit();
            }
        }
    }//GEN-LAST:event_tblNhanVienMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btgChucVu;
    private javax.swing.ButtonGroup btgGioiTinh;
    private javax.swing.ButtonGroup btgTrangThai;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rdoHoatDong;
    private javax.swing.JRadioButton rdoKhongHoatDong;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNhanVien;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JRadioButton rdoQuanLy;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHoten;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtMatKhau;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtXNMK;
    // End of variables declaration//GEN-END:variables
}
