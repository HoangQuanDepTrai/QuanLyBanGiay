/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.Entity;

import java.util.Date;

/**
 *
 * @author Lê Minh Khôi
 */
public class QLSanPham {
    private String MaSP;
    private String tenSp;
    private double giaNhap;
    private double giaBan;
    private int size;
    private String mau;
    private int soLuong;
    private String hinhAnh;
    private Date ngayNhap;
    private String moTa;
    private String loai;

    public QLSanPham() {
    }

    public QLSanPham(String MaSP, String tenSp, double giaNhap, double giaBan, int size, String mau, int soLuong, String hinhAnh, Date ngayNhap, String moTa, String loai) {
        this.MaSP = MaSP;
        this.tenSp = tenSp;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
        this.size = size;
        this.mau = mau;
        this.soLuong = soLuong;
        this.hinhAnh = hinhAnh;
        this.ngayNhap = ngayNhap;
        this.moTa = moTa;
        this.loai = loai;
    }

    public String getMaSP() {
        return MaSP;
    }

    public void setMaSP(String MaSP) {
        this.MaSP = MaSP;
    }

    public String getTenSp() {
        return tenSp;
    }

    public void setTenSp(String tenSp) {
        this.tenSp = tenSp;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMau() {
        return mau;
    }

    public void setMau(String mau) {
        this.mau = mau;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public Date getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(Date ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    @Override
    public String toString() {
        return tenSp;
    }
    
}
