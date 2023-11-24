/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.Entity;

/**
 *
 * @author Lê Minh Khôi
 */
public class QLNhanVien {

    private String Manv;
    private String matKhau;
    private String tenNV;
    private String SDT;
    private String Email;
    private boolean GioiTinh;
    private boolean TrangThaiNV;
    private boolean vaiTro;

    public QLNhanVien() {
    }

    public QLNhanVien(String Manv, String matKhau, String tenNV, String SDT, String Email, boolean GioiTinh, boolean TrangThaiNV, boolean vaiTro) {
        this.Manv = Manv;
        this.matKhau = matKhau;
        this.tenNV = tenNV;
        this.SDT = SDT;
        this.Email = Email;
        this.GioiTinh = GioiTinh;
        this.TrangThaiNV = TrangThaiNV;
        this.vaiTro = vaiTro;
    }

    public String getManv() {
        return Manv;
    }

    public void setManv(String Manv) {
        this.Manv = Manv;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public boolean isGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(boolean GioiTinh) {
        this.GioiTinh = GioiTinh;
    }

    public boolean isTrangThaiNV() {
        return TrangThaiNV;
    }

    public void setTrangThaiNV(boolean TrangThaiNV) {
        this.TrangThaiNV = TrangThaiNV;
    }

    public boolean isVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(boolean vaiTro) {
        this.vaiTro = vaiTro;
    }

    @Override
    public String toString() {
        return this.tenNV;
    }

}
