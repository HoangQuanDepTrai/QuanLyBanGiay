/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.Dao;

import com.raven.Entity.HoaDonCT;
import com.raven.uitils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lê Minh Khôi
 */
public class HoaDonCTDao extends RavenDao<HoaDonCT, Integer>{
    final String INSERT_SQL = "INSERT INTO HOADONCT (GIA, TENSP, SOLUONG, SIZE, MASP, MAHD) VALUES (?, ?, ?, ?, ?, ?)";
    final String UPDATE_SQL = "UPDATE HOADONCT SET GIA = ?, TENSP = ?, SOLUONG = ?, SIZE = ?, MASP = ?, MAHD = ? WHERE MACT = ?";
    final String DELETE_SQL = "DELETE FROM HOADONCT WHERE MACT = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM HOADONCT";
    final String SELECT_BY_ID_SQL = "SELECT * FROM HOADONCT WHERE MACT = ?";
    final String SELECT_DONCTMOI = "SELECT TOP 1 * FROM HOADONCT ORDER BY MACT DESC";
    final String SELECT_BY_MAHD = "SELECT * FROM HOADONCT WHERE MAHD LIKE ?";

    
    @Override
    public void insert(HoaDonCT entity) {
        JdbcHelper.update(INSERT_SQL, entity.getGia(),entity.getTenSP(),entity.getSoLuong(),entity.getSize(),entity.getMaSP(),entity.getMaHD());
    }

    @Override
    public void update(HoaDonCT entity) {
        JdbcHelper.update(UPDATE_SQL, entity.getGia(),entity.getTenSP(),entity.getSoLuong(),entity.getSize(),entity.getMaSP(),entity.getMaHD(),entity.getMaCT());
    }

    @Override
    public void delete(Integer id) {
        JdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public List<HoaDonCT> selectAll() {
        return selectBysql(SELECT_ALL_SQL);
    }
    
    public List<HoaDonCT> selectByMaHD(int MaHD) {
        return selectBysql(SELECT_BY_MAHD,MaHD);
    }

    @Override
    public HoaDonCT selectByid(Integer id) {
        List<HoaDonCT> list = selectBysql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    
    public HoaDonCT selectHDCTMoi() {
        List<HoaDonCT> list = selectBysql(SELECT_DONCTMOI);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<HoaDonCT> selectBysql(String sql, Object... agrs) {
        List<HoaDonCT> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, agrs);
            while (rs.next()) {
                HoaDonCT entity = new HoaDonCT();
                entity.setMaCT(rs.getInt("MACT"));
                entity.setGia(rs.getDouble("GIA"));
                entity.setTenSP(rs.getString("TENSP"));
                entity.setSoLuong(rs.getInt("SOLUONG"));
                entity.setSize(rs.getInt("SIZE"));
                entity.setMaSP(rs.getString("MASP"));
                entity.setMaHD(rs.getInt("MAHD"));
                list.add(entity);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    
}
