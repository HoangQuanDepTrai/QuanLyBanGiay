/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.Entity;

/**
 *
 * @author Lê Minh Khôi
 */
public class KhachHang {
    private int maKH;
    private String tenKH;
    private String SDT;
    private String Dchi;

    public KhachHang() {
    }

    public KhachHang(int maKH, String tenKH, String SDT, String Dchi) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.SDT = SDT;
        this.Dchi = Dchi;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getDchi() {
        return Dchi;
    }

    public void setDchi(String Dchi) {
        this.Dchi = Dchi;
    }

    @Override
    public String toString() {
        return tenKH;
    }
    
}
