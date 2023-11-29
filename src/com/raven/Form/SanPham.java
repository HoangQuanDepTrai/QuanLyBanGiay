/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.raven.Form;

import com.raven.Dao.LoaiDao;
import com.raven.Dao.SanPhamDao;
import com.raven.Entity.Loai;
import com.raven.Entity.Mau;
import com.raven.Entity.QLSanPham;
import com.raven.Entity.Size;
import com.raven.main.Main;
import com.raven.uitils.MsgBox;
import com.raven.uitils.XDate;
import com.raven.uitils.XImage;
import java.awt.Image;
import java.io.File;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Lê Minh Khôi
 */
public class SanPham extends javax.swing.JPanel {

    JFileChooser fileChooser = new JFileChooser();
    SanPhamDao spDao = new SanPhamDao();
    LoaiDao lDao = new LoaiDao();
    int row = -1;

    /**
     * Creates new form SanPham
     */
    public SanPham() {
        initComponents();
        fillTable();
        fillComboboxLoai();
        fillComboboxMau();
        fillComboboxSize();

    }

    private void hienThiHinhAnh(String fileName) {
        ImageIcon icon = XImage.read(fileName);
        if (icon != null) {
            Image scaledImage = icon.getImage().getScaledInstance(
                    lblHinh.getWidth(),
                    lblHinh.getHeight(),
                    Image.SCALE_DEFAULT);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            lblHinh.setIcon(scaledIcon);
            lblHinh.setToolTipText(fileName);
        }
    }

    private void chonAnh() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            XImage.save(file);
            hienThiHinhAnh(file.getName());
        }
    }

    private void setForm(QLSanPham sp) {
        txtMaSP.setText(sp.getMaSP());
        txtTenSP.setText(sp.getTenSp());
        txtGia.setText(sp.getGiaBan() + "");
        txtGiaNhap.setText(sp.getGiaNhap() + "");
        txtMoTa.setText(sp.getMoTa());
        txtSoLuong.setText(sp.getSoLuong() + "");
        cboLoai.setSelectedItem(sp.getLoai());
        cboMau.setSelectedItem(sp.getMau());
        cboSize.setSelectedItem(sp.getSize());
        if (sp.getHinhAnh() != null) {
            hienThiHinhAnh(sp.getHinhAnh());
        }
    }

    private boolean ktra() {
        if (txtTenSP.getText().isEmpty() || txtMaSP.getText().isEmpty() || txtSoLuong.getText().isEmpty() || txtGia.getText().isEmpty() || txtGiaNhap.getText().isEmpty()) {
            MsgBox.alert(this, "Không được bỏ trống");
            return false;
        }
          List<QLSanPham> sp = spDao.selectAll();
        for (QLSanPham qLSanPham : sp) {
            if (qLSanPham.getMaSP().equals(txtMaSP.getText())) {
                MsgBox.alert(this, "Mã đã tồn tại");
                return false;
            }
        }
        try {
            Integer.parseInt(txtSoLuong.getText());
        } catch (Exception e) {
            MsgBox.alert(this, "Số lượng không đúng định dạng");
            return false;
        }
        try {
            Double.parseDouble(txtGia.getText());
        } catch (Exception e) {
            MsgBox.alert(this, "Giá không đúng định dạng");
            return false;
        }
        try {
            Double.parseDouble(txtGiaNhap.getText());
        } catch (Exception e) {
            MsgBox.alert(this, "Giá nhập không đúng định dạng");
            return false;
        }
        if (lblHinh.getIcon() == null) {
            MsgBox.alert(this, "Không được bỏ trống hình");
            return false;
        }
      
        return true;
    }

    private QLSanPham getForm() {
        QLSanPham sp = new QLSanPham();
        sp.setMaSP(txtMaSP.getText());
        sp.setTenSp(txtTenSP.getText());
        sp.setGiaBan(Double.parseDouble(txtGia.getText()));
        sp.setGiaNhap(Double.parseDouble(txtGiaNhap.getText()));
        sp.setNgayNhap(new Date());
        sp.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
        sp.setMoTa(txtMoTa.getText());
        sp.setLoai(cboLoai.getSelectedItem() + "");
        sp.setMau(cboMau.getSelectedItem() + "");
        sp.setSize((int) cboSize.getSelectedItem());
        sp.setHinhAnh(lblHinh.getToolTipText());
        return sp;
    }

    private String getTrangThai(int soLuong) {
        if (soLuong == 0) {
            return "Không hoạt động";
        } else {
            return "Hoạt động";
        }
    }

    private void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblSanPham.getModel();
        model.setRowCount(0);
        try {
            List<QLSanPham> list = spDao.selectAll();
            for (QLSanPham qLSP : list) {
                Loai loai = new LoaiDao().selectByid(qLSP.getLoai());
                Object[] row = {
                    qLSP.getMaSP(),
                    qLSP.getTenSp(),
                    qLSP.getGiaBan(),
                    qLSP.getSoLuong(),
                    loai.getTenLoai(),
                    qLSP.getSize(),
                    qLSP.getMau(),
                    qLSP.getHinhAnh(),
                    getTrangThai(qLSP.getSoLuong()),
                    qLSP.getMoTa()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    private void fillComboboxLoai() {
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cboLoai.getModel();
        comboBoxModel.removeAllElements();
        List<Loai> list = lDao.selectAll();
        for (Loai loai : list) {
            comboBoxModel.addElement(loai.getTenLoai());
        }
    }

    private void fillComboboxMau() {
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cboMau.getModel();
        comboBoxModel.removeAllElements();
        List<Mau> list = spDao.selectMau();
        for (Mau spMau : list) {
            comboBoxModel.addElement(spMau.getTenMau());
        }
    }

    private void fillComboboxSize() {
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) cboSize.getModel();
        comboBoxModel.removeAllElements();
        List<Size> list = spDao.selectSize();
        for (Size sSize : list) {
            comboBoxModel.addElement(sSize.getSoSize());
        }
    }

    void updateStatus() {
        boolean edit = (row >= 0);
        boolean first = (row == 0);
        boolean last = (row == tblSanPham.getRowCount() - 1);
        txtMaSP.setEditable(!edit);
        btnLuu.setEnabled(!edit);
        btnSua.setEnabled(edit);
        btnXoa.setEnabled(edit);
        btnFirst.setEnabled(edit && !first);
        btnLast.setEnabled(edit && !last);
        btnNext.setEnabled(edit && !last);
        btnPrev.setEnabled(edit && !first);
    }

    private void edit() {
        try {
            String maSP = (String) tblSanPham.getValueAt(row, 0);
            String loai = tblSanPham.getValueAt(row, 4).toString();
            QLSanPham sp = spDao.selectByid(maSP);
            sp.setLoai(loai);
            if (sp != null) {
                setForm(sp);
                tbs.setSelectedIndex(0);
                updateStatus();
            } else {
                MsgBox.alert(this, "Không tìm thấy sản phẩm.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        row = -1;
        updateStatus();
        txtGia.setText("");
        txtGiaNhap.setText("");
        txtMaSP.setText("");
        txtMoTa.setText("");
        txtSoLuong.setText("");
        txtTenSP.setText("");
        txtTim.setText("");
        cboLoai.setSelectedIndex(0);
        cboMau.setSelectedIndex(0);
        cboSize.setSelectedIndex(0);
        lblHinh.setIcon(null);
    }

    void insert() {
        if (ktra()) {
            try {
                QLSanPham sp = getForm();
                sp.setLoai(cboLoai.getSelectedItem() + "");
                spDao.insert(sp);
                this.fillTable();
                this.clearForm();
                MsgBox.alert(this, "Thêm mới thành công");

            } catch (Exception e) {
                MsgBox.alert(this, "Thêm mới thất bại");
            }
        }

    }

    void update() {
        if (ktra()) {
            try {
                QLSanPham sp = getForm();
                String maSP = (String) tblSanPham.getValueAt(row, 0);
                sp.setMaSP(maSP);
                sp.setLoai(cboLoai.getSelectedItem() + "");
                spDao.update(sp);
                this.fillTable();
                clearForm();
                MsgBox.alert(this, "Cập nhật thành công");

            } catch (Exception e) {
                MsgBox.alert(this, "Cập nhật thất bại");
            }
        }

    }

    void delete() {
        if (MsgBox.confirm(this, "Bạn muốn xóa sản phẩm này không?")) {
            try {
                String maSP = (String) tblSanPham.getValueAt(row, 0);
                spDao.delete(maSP);
                this.fillTable();
                this.clearForm();
                MsgBox.alert(this, "Xóa thành công");
            } catch (Exception e) {
                MsgBox.alert(this, "Xóa thất bại");
            }
        }
    }

    private void first() {
        this.row = 0;
        this.edit();
    }

    private void prev() {
        if (row > 0) {
            row--;
            edit();
        }
    }

    private void next() {
        if (row < tblSanPham.getRowCount() - 1) {
            row++;
            edit();
        }
    }

    private void last() {
        row = tblSanPham.getRowCount() - 1;
        edit();
    }

    private void trangThai(String trangThaiSP) {
        DefaultTableModel model = (DefaultTableModel) tblSanPham.getModel();
        model.setRowCount(0);
        List<QLSanPham> list = spDao.selectAll();
        for (QLSanPham qLSP : list) {
            String trangThai = getTrangThai(qLSP.getSoLuong());
            if (trangThaiSP.equals(trangThai)) {
                Object[] row = {
                    qLSP.getMaSP(),
                    qLSP.getTenSp(),
                    qLSP.getGiaBan(),
                    qLSP.getSoLuong(),
                    qLSP.getLoai(),
                    qLSP.getSize(),
                    qLSP.getMau(),
                    qLSP.getHinhAnh(),
                    trangThai,
                    qLSP.getMoTa()
                };
                model.addRow(row);
            }
        }
    }

    private void tim() {
        DefaultTableModel model = (DefaultTableModel) tblSanPham.getModel();
        model.setRowCount(0);
        try {
            String tenSP = txtTim.getText();
            List<QLSanPham> list = spDao.selectByName(tenSP);
            for (QLSanPham qLSP : list) {
                Object[] row = {
                    qLSP.getMaSP(),
                    qLSP.getTenSp(),
                    qLSP.getGiaBan(),
                    qLSP.getSoLuong(),
                    qLSP.getLoai(),
                    qLSP.getSize(),
                    qLSP.getMau(),
                    qLSP.getHinhAnh(),
                    getTrangThai(qLSP.getSoLuong()),
                    qLSP.getMoTa()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
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
        tbs = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        lblHinh = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtTenSP = new javax.swing.JTextField();
        txtGia = new javax.swing.JTextField();
        cboLoai = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtMaSP = new javax.swing.JTextField();
        txtGiaNhap = new javax.swing.JTextField();
        cboSize = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMoTa = new javax.swing.JTextArea();
        btnLuu = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        cboMau = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        txtTim = new javax.swing.JTextField();
        btnHoatDong = new javax.swing.JButton();
        btnKhongHoatDong = new javax.swing.JButton();

        setBackground(new java.awt.Color(51, 51, 51));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Quản Lý Sản Phẩm");

        tbs.setBackground(new java.awt.Color(51, 51, 51));

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));

        lblHinh.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblHinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHinhMouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Tên SP");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Giá");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Loại");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Mã SP");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Giá nhập");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Size");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Mô tả");

        txtMoTa.setColumns(20);
        txtMoTa.setRows(5);
        jScrollPane2.setViewportView(txtMoTa);

        btnLuu.setBackground(new java.awt.Color(153, 153, 153));
        btnLuu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLuu.setForeground(new java.awt.Color(51, 51, 51));
        btnLuu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/Save.png"))); // NOI18N
        btnLuu.setText("Lưu");
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(153, 153, 153));
        btnSua.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSua.setForeground(new java.awt.Color(51, 51, 51));
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/Edit.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(153, 153, 153));
        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(51, 51, 51));
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/Delete.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnMoi.setBackground(new java.awt.Color(153, 153, 153));
        btnMoi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnMoi.setForeground(new java.awt.Color(51, 51, 51));
        btnMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/Refresh.png"))); // NOI18N
        btnMoi.setText("Mới");
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        btnFirst.setBackground(new java.awt.Color(153, 153, 153));
        btnFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/first.png"))); // NOI18N
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPrev.setBackground(new java.awt.Color(153, 153, 153));
        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/prev.png"))); // NOI18N
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnNext.setBackground(new java.awt.Color(153, 153, 153));
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/next.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setBackground(new java.awt.Color(153, 153, 153));
        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/last.png"))); // NOI18N
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        cboMau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMauActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Màu");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Số lượng");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(lblHinh, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(33, 33, 33)
                                .addComponent(txtTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel12))
                                .addGap(46, 46, 46)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboSize, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboMau, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnLuu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSua)
                        .addGap(18, 18, 18)
                        .addComponent(btnXoa)
                        .addGap(18, 18, 18)
                        .addComponent(btnMoi)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGiaNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(126, 126, 126))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(35, 35, 35)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(18, 18, 18)
                                .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblHinh, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(txtTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cboLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel13)
                                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(txtGiaNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(cboSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cboMau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12)))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnLuu)
                        .addComponent(btnSua)
                        .addComponent(btnXoa)
                        .addComponent(btnMoi))
                    .addComponent(btnFirst)
                    .addComponent(btnLast)
                    .addComponent(btnNext)
                    .addComponent(btnPrev))
                .addGap(81, 81, 81))
        );

        tbs.addTab("Cập nhật", jPanel4);

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));

        tblSanPham.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã SP", "Tên SP", "Đơn giá", "Số Lượng", "Loại", "Size", "Màu", "Ảnh", "Trạng thái", "Mô tả"
            }
        ));
        tblSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSanPhamMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblSanPham);

        jPanel7.setBackground(new java.awt.Color(51, 51, 51));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        txtTim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTimKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(txtTim, javax.swing.GroupLayout.PREFERRED_SIZE, 910, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        btnHoatDong.setBackground(new java.awt.Color(51, 255, 51));
        btnHoatDong.setForeground(new java.awt.Color(51, 51, 51));
        btnHoatDong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/Accept.png"))); // NOI18N
        btnHoatDong.setText("Hoạt động");
        btnHoatDong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoatDongActionPerformed(evt);
            }
        });

        btnKhongHoatDong.setBackground(new java.awt.Color(255, 51, 51));
        btnKhongHoatDong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/Icon/Stop_1.png"))); // NOI18N
        btnKhongHoatDong.setText("Không hoạt động");
        btnKhongHoatDong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhongHoatDongActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 303, Short.MAX_VALUE)
                        .addComponent(btnHoatDong, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64)
                        .addComponent(btnKhongHoatDong, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 297, Short.MAX_VALUE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHoatDong)
                    .addComponent(btnKhongHoatDong))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        tbs.addTab("Danh sách", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(312, 312, 312)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbs)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tbs))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void lblHinhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHinhMouseClicked
        chonAnh();
    }//GEN-LAST:event_lblHinhMouseClicked

    private void tblSanPhamMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamMousePressed
        if (evt.getClickCount() == 2) {
            row = tblSanPham.rowAtPoint(evt.getPoint());
            edit();
        }

    }//GEN-LAST:event_tblSanPhamMousePressed

    private void cboMauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMauActionPerformed

    }//GEN-LAST:event_cboMauActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        first();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        prev();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        next();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        last();
    }//GEN-LAST:event_btnLastActionPerformed

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        insert();
    }//GEN-LAST:event_btnLuuActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        update();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        delete();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        clearForm();
    }//GEN-LAST:event_btnMoiActionPerformed

    private void btnHoatDongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoatDongActionPerformed
        trangThai("Hoạt động");
    }//GEN-LAST:event_btnHoatDongActionPerformed

    private void btnKhongHoatDongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhongHoatDongActionPerformed
        trangThai("Không hoạt động");
    }//GEN-LAST:event_btnKhongHoatDongActionPerformed

    private void txtTimKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKeyReleased
        tim();
    }//GEN-LAST:event_txtTimKeyReleased

    private void txtTimKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKeyPressed
        //tim();
    }//GEN-LAST:event_txtTimKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnHoatDong;
    private javax.swing.JButton btnKhongHoatDong;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboLoai;
    private javax.swing.JComboBox<String> cboMau;
    private javax.swing.JComboBox<String> cboSize;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblHinh;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JTabbedPane tbs;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtGiaNhap;
    private javax.swing.JTextField txtMaSP;
    private javax.swing.JTextArea txtMoTa;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenSP;
    private javax.swing.JTextField txtTim;
    // End of variables declaration//GEN-END:variables
}
