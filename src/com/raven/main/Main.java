/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.raven.main;

import com.raven.Entity.KhachHang;
import com.raven.Form.DangNhap;
import com.raven.Form.DoiMatKhau;
import com.raven.Form.GiaoHangJDialog;
import com.raven.Form.NhanVien;
import com.raven.Form.QuanLyGiaoDich;
import com.raven.Form.SanPham;
import com.raven.Form.ThongKe;
import com.raven.Form.TraHang;
import com.raven.Form.WellcomesJDialog;
import com.raven.Swing.JPanel_Login;
import com.raven.event.EventMenuSelected;
import com.raven.uitils.Auth;
import com.raven.uitils.KhachHangGiaoHang;
import com.raven.uitils.MsgBox;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Lê Minh Khôi
 */
public class Main extends javax.swing.JFrame {
    
    public static boolean welcomeDialogShown = false;

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        setBackground(new Color(0, 0, 0, 0));
        menu.iniMoving(this);
        if (!welcomeDialogShown) {
            new WellcomesJDialog(this, true).setVisible(true);
            showDangNhap();
            welcomeDialogShown = true;
        }
        menu.addEventMenuSelected(new EventMenuSelected() {
            @Override
            public void selected(int index) {
                if (index == 1) {
                    showDoiMatKhau();
                } else if (index == 2) {
                    showDangNhap();
                } else if (index == 3) {
                    if (MsgBox.confirm(jPanelBorder1, "Bạn có muốn thoát")) {
                        ketThuc();
                    }
                } else if (index == 6) {
                    setForm(new SanPham());
                } else if (index == 7) {
                    setForm(new NhanVien());
                } else if (index == 8) {
                    setForm(new QuanLyGiaoDich());
                } else if (index == 9) {
                    setForm(new TraHang());
                } else if (index == 10) {
                    if (Auth.isManager()) {
                        MsgBox.alert(jPanelBorder1, "Bạn không có quyền xem doanh thu");
                        return;
                    }
                    setForm(new ThongKe());
                }
                
            }
            
        });
    }
    
    private void ketThuc() {
        this.dispose();
    }
    
    private void showDangNhap() {
        new DangNhap(this, true).setVisible(true);
    }
    
    private void showDoiMatKhau() {
        new DoiMatKhau(this, true).setVisible(true);
    }
    
    public void showGiaoHang() {
//        new GiaoHangJDialog(this, true).setVisible(true);
        new com.raven.Form.KhachHang(this, true).setVisible(true);
    }
    
    private void setForm(JComponent com) {
        mainPanel.removeAll();
        mainPanel.add(com);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelBorder1 = new com.raven.Swing.JPanelBorder();
        menu = new com.raven.component.Menu();
        mainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanelBorder1.setBackground(new java.awt.Color(51, 51, 51));

        mainPanel.setBackground(new java.awt.Color(51, 51, 51));
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanelBorder1Layout = new javax.swing.GroupLayout(jPanelBorder1);
        jPanelBorder1.setLayout(jPanelBorder1Layout);
        jPanelBorder1Layout.setHorizontalGroup(
            jPanelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBorder1Layout.createSequentialGroup()
                .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE))
        );
        jPanelBorder1Layout.setVerticalGroup(
            jPanelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelBorder1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(169, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        //showDangNhap();
    }//GEN-LAST:event_formWindowOpened

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.raven.Swing.JPanelBorder jPanelBorder1;
    private javax.swing.JPanel mainPanel;
    private com.raven.component.Menu menu;
    // End of variables declaration//GEN-END:variables
}
