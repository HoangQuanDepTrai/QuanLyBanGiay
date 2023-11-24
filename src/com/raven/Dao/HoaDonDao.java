/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.Dao;

import com.raven.Entity.HoaDon;
import com.raven.uitils.JdbcHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Lê Minh Khôi
 */
public class HoaDonDao extends RavenDao<HoaDon, Integer> {

    final String INSERT_SQL = "INSERT INTO HOADON (HINHTHUC, PHIGIAO, THANHTIEN, TIENKHACHTRA, NGAYTAO, GIOTAO, MAKH, "
            + "MATRANGTHAI, MANV) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    final String UPDATE_SQL = "UPDATE HOADON HINHTHUC = ?, PHIGIAO = ?, THANHTIEN = ?, TIENKHACHTRA = ?, NGAYTAO = ?, "
            + "GIOTAO = ?, MAKH = ?, MATRANGTHAI = ?, MANV = ? WHERE MAHD = ?";
    final String DELETE_SQL = "DELETE FROM HOADON WHERE MAHD = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM HOADON";
    final String SELECT_BY_ID_SQL = "SELECT * FROM HOADON WHERE MAHD = ?";

    @Override
    public void insert(HoaDon entity) {
        JdbcHelper.update(INSERT_SQL, entity.isHinhThuc(), entity.getPhiGiao(), entity.getThanhTien(),
                entity.getTienKhachTra(), entity.getNgayTao(), entity.getGioTao(), entity.getMaKH(),
                entity.getMaTT(), entity.getMaNV());
    }

    @Override
    public void update(HoaDon entity) {
        JdbcHelper.update(UPDATE_SQL, entity.isHinhThuc(), entity.getPhiGiao(), entity.getThanhTien(),
                entity.getTienKhachTra(), entity.getNgayTao(), entity.getGioTao(), entity.getMaKH(),
                entity.getMaTT(), entity.getMaNV(), entity.getMaHD());
    }

    @Override
    public void delete(Integer id) {
        JdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public List<HoaDon> selectAll() {
        return selectBysql(SELECT_ALL_SQL);
    }

    @Override
    public HoaDon selectByid(Integer id) {
        List<HoaDon> list = selectBysql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<HoaDon> selectBysql(String sql, Object... agrs) {
        List<HoaDon> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, agrs);
            while (rs.next()) {
                HoaDon entity = new HoaDon();
                entity.setMaHD(rs.getInt("MAHD"));
                entity.setHinhThuc(rs.getBoolean("HINHTHUC"));
                entity.setPhiGiao(rs.getDouble("PHIGIAO"));
                entity.setThanhTien(rs.getDouble("THANHTIEN"));
                entity.setTienKhachTra(rs.getDouble("TIENKHACHTRA"));
                entity.setNgayTao(rs.getDate("NGAYTAO"));
                entity.setGioTao(rs.getTime("GIOTAO"));
                entity.setMaKH(rs.getInt("MAKH"));
                entity.setMaTT(rs.getInt("MATRANGTHAI"));
                entity.setMaNV(rs.getString("MANV"));
                list.add(entity);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<HoaDon> selectByTKNgay(String maTT, Date ngayTao) {
        List<HoaDon> list = new ArrayList<>();
        String SELECT_HD_TK = "SELECT MAHD,KHACHHANG.TENKH AS TENKH,\n"
                + "NHANVIEN.TENNV AS TENNV,THANHTIEN FROM HOADON\n"
                + "INNER JOIN KHACHHANG ON KHACHHANG.MAKH = HOADON.MAKH\n"
                + "INNER JOIN NHANVIEN ON NHANVIEN.MANV = HOADON.MANV\n"
                + "WHERE MATRANGTHAI = (SELECT MATRANGTHAI FROM TRANGTHAI WHERE TENTRANGTHAI LIKE ?) AND NGAYTAO = ?";
        try {
            ResultSet rs = JdbcHelper.query(SELECT_HD_TK, maTT, ngayTao);
            while (rs.next()) {
                HoaDon entity = new HoaDon(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4));
                list.add(entity);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    

}
