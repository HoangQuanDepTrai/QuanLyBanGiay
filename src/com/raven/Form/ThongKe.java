/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.raven.Form;

import com.raven.Dao.HoaDonCTDao;
import com.raven.Dao.HoaDonDao;
import com.raven.Dao.ThongKeDao;
import com.raven.Dao.TrangThaiDao;
import com.raven.Entity.HoaDon;
import com.raven.Entity.HoaDonCT;
import com.raven.Entity.TrangThai;
import com.raven.uitils.MsgBox;
import com.raven.uitils.XDate;
import com.toedter.calendar.JMonthChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.Timer;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class ThongKe extends javax.swing.JPanel {

    TrangThaiDao ttDao = new TrangThaiDao();
    ThongKeDao tkDao = new ThongKeDao();
    HoaDonCTDao hdctDao = new HoaDonCTDao();

    /**
     * Creates new form ThongKe
     */
    public ThongKe() {
        initComponents();
        fillComBoBoxTT(cboTTNgay);
        fillComBoBoxTT(cboTTThang);
        fillComBoBoxTT(cboTTNam);
        clickTTNgayDate();
        clickTTThangNam();
        dcTKThang.setMonth(0);
    }

    private void clickTTNgayDate() {
        Timer timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dcNgay.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("date".equals(evt.getPropertyName())) {
                            fillThongKeNgay();
                            fillTableThongKeNgay();
                        }
                    }
                });
                dcTKThang.addPropertyChangeListener("month", new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("month".equals(evt.getPropertyName())) {
                            fillTableThongKeThang();
                            fillThongKeThang();
                        }
                    }
                });
                ycTKThang.addPropertyChangeListener("year", new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("year".equals(evt.getPropertyName())) {
                            fillTableThongKeThang();
                            fillThongKeThang();
                        }
                    }
                });

            }
        });
        timer.start();
    }

 

    private void clickTTThangNam() {
        ycTKThang.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    fillThongKeThang();
                    fillTableThongKeThang();
                }
            }
        });
    }

    private void fillComBoBoxTT(JComboBox cbotrangThai) {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbotrangThai.getModel();
        model.removeAllElements();
        List<TrangThai> list = ttDao.selectAll();
        for (TrangThai tt : list) {
            model.addElement(tt.getTenTrangThai());
        }
    }

    private void fillThongKeThang() {
        List<Object[]> list = tkDao.getTK_DT_THANG(dcTKThang.getMonth() + 1, ycTKThang.getYear(), cboTTThang.getSelectedItem().toString());
        if (!list.isEmpty()) {
            Object[] data = list.get(0);
            lblTongDTThang.setText(String.valueOf(data[0] != null ? String.valueOf(data[0]) : "0"));
            lblSLGiayThang.setText(String.valueOf(data[1] != null ? String.valueOf(data[1]) : "0"));
            lblSLHDThang.setText(String.valueOf(data[2]));
            lblHDNNThang.setText(String.valueOf(data[3] != null ? String.valueOf(data[3]) : "0"));
            lblHDLNThang.setText(String.valueOf(data[4] != null ? String.valueOf(data[4]) : "0"));
            lblHDTBThang.setText(String.valueOf(data[5] != null ? String.valueOf(data[5]) : "0"));
        }
    }

    private void fillThongKeNgay() {
        List<Object[]> list = tkDao.getTK_DT_NGAY(dcNgay.getDate(), cboTTNgay.getSelectedItem().toString());
        if (!list.isEmpty()) {
            Object[] data = list.get(0);
            lblTongDoanhThuNgay.setText(String.valueOf(data[0] != null ? String.valueOf(data[0]) : "0"));
            lblSoGiayNgay.setText(String.valueOf(data[1] != null ? String.valueOf(data[1]) : "0"));
            lblSoHoaDonNgay.setText(String.valueOf(data[2]));
            lblHoaDonNhoNgay.setText(String.valueOf(data[3] != null ? String.valueOf(data[3]) : "0"));
            lblHoaDonLonNgay.setText(String.valueOf(data[4] != null ? String.valueOf(data[4]) : "0"));
            lblHoaDonTBNgay.setText(String.valueOf(data[5] != null ? String.valueOf(data[5]) : "0"));
        }
    }

    private void fillTableThongKeNgay() {
        DefaultTableModel model = (DefaultTableModel) tblDTNgay.getModel();
        model.setRowCount(0);
        List<Object[]> list = tkDao.getTK_DT_TABLENGAY(dcNgay.getDate(), cboTTNgay.getSelectedItem().toString());
        for (Object[] row : list) {
            model.addRow(new Object[]{
                row[0],
                row[1],
                row[2],
                row[3]
            });
        }
    }

    private void fillTableThongKeThang() {
        DefaultTableModel model = (DefaultTableModel) tblDTThang.getModel();
        model.setRowCount(0);
        List<Object[]> list = tkDao.getTK_DT_TABLETHANG(dcTKThang.getMonth() + 1, ycTKThang.getYear(), cboTTThang.getSelectedItem().toString());
        for (Object[] row : list) {
            model.addRow(new Object[]{
                row[0],
                row[1],
                row[2],
                row[3]
            });
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblTongDoanhThuNgay = new javax.swing.JLabel();
        lblSoHoaDonNgay = new javax.swing.JLabel();
        lblSoGiayNgay = new javax.swing.JLabel();
        lblHoaDonNhoNgay = new javax.swing.JLabel();
        lblHoaDonLonNgay = new javax.swing.JLabel();
        lblHoaDonTBNgay = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDTNgay = new javax.swing.JTable();
        cboTTNgay = new javax.swing.JComboBox<>();
        dcNgay = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        cboTTThang = new javax.swing.JComboBox<>();
        lblTongDTThang = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lblSLHDThang = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lblSLGiayThang = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        lblHDNNThang = new javax.swing.JLabel();
        lblHDLNThang = new javax.swing.JLabel();
        lblHDTBThang = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDTThang = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        dcTKThang = new com.toedter.calendar.JMonthChooser();
        ycTKThang = new com.toedter.calendar.JYearChooser();
        jPanel3 = new javax.swing.JPanel();
        lblHDLNNam = new javax.swing.JLabel();
        lblHDTBNam = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblDTNam = new javax.swing.JTable();
        cboTTNam = new javax.swing.JComboBox<>();
        lblTongDTNam = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        lblSLHDNam = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        lblSLGiayNam = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        lblHDNNNam = new javax.swing.JLabel();
        ycTKNam = new com.toedter.calendar.JYearChooser();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setBackground(new java.awt.Color(51, 51, 51));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Thống Kê");
        jLabel1.setToolTipText("");

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Tổng doanh thu");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Số lượng hóa đơn");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Số lượng giày ");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Hóa đơn lớn nhất ");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Hóa đơn nhỏ nhất");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Hóa đơn trung bình");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Ngày ");

        lblTongDoanhThuNgay.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTongDoanhThuNgay.setForeground(new java.awt.Color(255, 255, 255));
        lblTongDoanhThuNgay.setText("0");

        lblSoHoaDonNgay.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSoHoaDonNgay.setForeground(new java.awt.Color(255, 255, 255));
        lblSoHoaDonNgay.setText("0");

        lblSoGiayNgay.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSoGiayNgay.setForeground(new java.awt.Color(255, 255, 255));
        lblSoGiayNgay.setText("0");

        lblHoaDonNhoNgay.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblHoaDonNhoNgay.setForeground(new java.awt.Color(255, 255, 255));
        lblHoaDonNhoNgay.setText("0");

        lblHoaDonLonNgay.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblHoaDonLonNgay.setForeground(new java.awt.Color(255, 255, 255));
        lblHoaDonLonNgay.setText("0");

        lblHoaDonTBNgay.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblHoaDonTBNgay.setForeground(new java.awt.Color(255, 255, 255));
        lblHoaDonTBNgay.setText("0");

        tblDTNgay.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã DH", "Tên KH", "Tên NV", "Thành tiền"
            }
        ));
        jScrollPane2.setViewportView(tblDTNgay);

        cboTTNgay.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cboTTNgay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTTNgayActionPerformed(evt);
            }
        });

        dcNgay.setNextFocusableComponent(cboTTNgay);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTongDoanhThuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSoHoaDonNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSoGiayNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 359, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblHoaDonNhoNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblHoaDonLonNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblHoaDonTBNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(53, 53, 53))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(295, 295, 295)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(dcNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cboTTNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(dcNgay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(cboTTNgay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(lblTongDoanhThuNgay)
                    .addComponent(jLabel6)
                    .addComponent(lblHoaDonNhoNgay))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(lblHoaDonLonNgay))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(lblSoHoaDonNgay)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(lblHoaDonTBNgay))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(lblSoGiayNgay)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addGap(34, 34, 34))
        );

        jTabbedPane1.addTab("Thống kê theo ngày ", jPanel1);

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        cboTTThang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cboTTThang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTTThangActionPerformed(evt);
            }
        });

        lblTongDTThang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTongDTThang.setForeground(new java.awt.Color(255, 255, 255));
        lblTongDTThang.setText("0");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Tổng doanh thu");

        lblSLHDThang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSLHDThang.setForeground(new java.awt.Color(255, 255, 255));
        lblSLHDThang.setText("0");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Số lượng hóa đơn");

        lblSLGiayThang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSLGiayThang.setForeground(new java.awt.Color(255, 255, 255));
        lblSLGiayThang.setText("0");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Số lượng giày ");

        lblHDNNThang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblHDNNThang.setForeground(new java.awt.Color(255, 255, 255));
        lblHDNNThang.setText("0");

        lblHDLNThang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblHDLNThang.setForeground(new java.awt.Color(255, 255, 255));
        lblHDLNThang.setText("0");

        lblHDTBThang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblHDTBThang.setForeground(new java.awt.Color(255, 255, 255));
        lblHDTBThang.setText("0");

        tblDTThang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã DH", "Tên KH", "Tên NV", "Thành tiền"
            }
        ));
        jScrollPane3.setViewportView(tblDTThang);

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Hóa đơn lớn nhất ");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Hóa đơn nhỏ nhất");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Hóa đơn trung bình");

        dcTKThang.setNextFocusableComponent(cboTTThang);
        dcTKThang.setYearChooser(ycTKThang);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblTongDTThang, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblSLHDThang, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblSLGiayThang, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 359, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblHDNNThang, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblHDLNThang, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblHDTBThang, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(cboTTThang, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(134, 134, 134)))
                        .addGap(47, 47, 47))
                    .addComponent(jScrollPane3))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(327, 327, 327)
                .addComponent(dcTKThang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(ycTKThang, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dcTKThang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ycTKThang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cboTTThang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(lblTongDTThang)
                    .addComponent(jLabel26)
                    .addComponent(lblHDNNThang))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel25)
                        .addComponent(lblHDLNThang))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19)
                        .addComponent(lblSLHDThang)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel27)
                        .addComponent(lblHDTBThang))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21)
                        .addComponent(lblSLGiayThang)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addGap(34, 34, 34))
        );

        jTabbedPane1.addTab("Thống kê theo tháng", jPanel2);

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));

        lblHDLNNam.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblHDLNNam.setForeground(new java.awt.Color(255, 255, 255));
        lblHDLNNam.setText("0");

        lblHDTBNam.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblHDTBNam.setForeground(new java.awt.Color(255, 255, 255));
        lblHDTBNam.setText("0");

        tblDTNam.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã DH", "Tên KH", "Tên NV", "Thành tiền"
            }
        ));
        jScrollPane4.setViewportView(tblDTNam);

        cboTTNam.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cboTTNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTTNamActionPerformed(evt);
            }
        });

        lblTongDTNam.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTongDTNam.setForeground(new java.awt.Color(255, 255, 255));
        lblTongDTNam.setText("0");

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Hóa đơn lớn nhất ");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Tổng doanh thu");

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Hóa đơn nhỏ nhất");

        lblSLHDNam.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSLHDNam.setForeground(new java.awt.Color(255, 255, 255));
        lblSLHDNam.setText("0");

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Hóa đơn trung bình");

        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Số lượng hóa đơn");

        lblSLGiayNam.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSLGiayNam.setForeground(new java.awt.Color(255, 255, 255));
        lblSLGiayNam.setText("0");

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Số lượng giày ");

        lblHDNNNam.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblHDNNNam.setForeground(new java.awt.Color(255, 255, 255));
        lblHDNNNam.setText("0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTongDTNam, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblSLHDNam, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblSLGiayNam, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 359, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblHDNNNam, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblHDLNNam, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblHDTBNam, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(47, 47, 47))
                    .addComponent(jScrollPane4))
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(317, 317, 317)
                .addComponent(ycTKNam, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(cboTTNam, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboTTNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ycTKNam, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(lblTongDTNam)
                    .addComponent(jLabel33)
                    .addComponent(lblHDNNNam))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel31)
                        .addComponent(lblHDLNNam))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel36)
                        .addComponent(lblSLHDNam)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel35)
                        .addComponent(lblHDTBNam))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel38)
                        .addComponent(lblSLGiayNam)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addGap(34, 34, 34))
        );

        jTabbedPane1.addTab("Thống kê theo năm", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(layout.createSequentialGroup()
                .addGap(391, 391, 391)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cboTTNgayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTTNgayActionPerformed
        cboTTNgay.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                fillThongKeNgay();
                fillTableThongKeNgay();

            }
        });
    }//GEN-LAST:event_cboTTNgayActionPerformed

    private void cboTTThangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTTThangActionPerformed
        cboTTThang.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                fillThongKeThang();
                fillTableThongKeThang();
            }
        });
    }//GEN-LAST:event_cboTTThangActionPerformed

    private void cboTTNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTTNamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboTTNamActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboTTNam;
    private javax.swing.JComboBox<String> cboTTNgay;
    private javax.swing.JComboBox<String> cboTTThang;
    private com.toedter.calendar.JDateChooser dcNgay;
    private com.toedter.calendar.JMonthChooser dcTKThang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblHDLNNam;
    private javax.swing.JLabel lblHDLNThang;
    private javax.swing.JLabel lblHDNNNam;
    private javax.swing.JLabel lblHDNNThang;
    private javax.swing.JLabel lblHDTBNam;
    private javax.swing.JLabel lblHDTBThang;
    private javax.swing.JLabel lblHoaDonLonNgay;
    private javax.swing.JLabel lblHoaDonNhoNgay;
    private javax.swing.JLabel lblHoaDonTBNgay;
    private javax.swing.JLabel lblSLGiayNam;
    private javax.swing.JLabel lblSLGiayThang;
    private javax.swing.JLabel lblSLHDNam;
    private javax.swing.JLabel lblSLHDThang;
    private javax.swing.JLabel lblSoGiayNgay;
    private javax.swing.JLabel lblSoHoaDonNgay;
    private javax.swing.JLabel lblTongDTNam;
    private javax.swing.JLabel lblTongDTThang;
    private javax.swing.JLabel lblTongDoanhThuNgay;
    private javax.swing.JTable tblDTNam;
    private javax.swing.JTable tblDTNgay;
    private javax.swing.JTable tblDTThang;
    private com.toedter.calendar.JYearChooser ycTKNam;
    private com.toedter.calendar.JYearChooser ycTKThang;
    // End of variables declaration//GEN-END:variables
}
