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
public class HoaDon {

    private int maHD;
    private boolean hinhThuc;
    private double phiGiao;
    private double thanhTien;
    private double tienKhachTra;
    private Date ngayTao;
    private Date gioTao;
    private int maKH;
    private int maTT;
    private String maNV;
    private String tenKH;
    private String tenNV;

    public HoaDon() {
        
    }

    public HoaDon(int maHD, boolean hinhThuc, double phiGiao, double thanhTien, double tienKhachTra, Date ngayTao, Date gioTao, int maKH, int maTT, String maNV) {
        this.maHD = maHD;
        this.hinhThuc = hinhThuc;
        this.phiGiao = phiGiao;
        this.thanhTien = thanhTien;
        this.tienKhachTra = tienKhachTra;
        this.ngayTao = ngayTao;
        this.gioTao = gioTao;
        this.maKH = maKH;
        this.maTT = maTT;
        this.maNV = maNV;
    }

    public HoaDon(int maHD, String tenKH, String tenNV, double thanhTien) {
        this.maHD = maHD;
        this.tenKH = tenKH;
        this.tenNV = tenNV;
        this.thanhTien = thanhTien;
    }



    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    
    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public int getMaHD() {
        return maHD;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
    }

    public boolean isHinhThuc() {
        return hinhThuc;
    }

    public void setHinhThuc(boolean hinhThuc) {
        this.hinhThuc = hinhThuc;
    }

    public double getPhiGiao() {
        return phiGiao;
    }

    public void setPhiGiao(double phiGiao) {
        this.phiGiao = phiGiao;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public double getTienKhachTra() {
        return tienKhachTra;
    }

    public void setTienKhachTra(double tienKhachTra) {
        this.tienKhachTra = tienKhachTra;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Date getGioTao() {
        return gioTao;
    }

    public void setGioTao(Date gioTao) {
        this.gioTao = gioTao;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public int getMaTT() {
        return maTT;
    }

    public void setMaTT(int maTT) {
        this.maTT = maTT;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

}
