/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.Dao;

import com.raven.Entity.KhachHang;
import com.raven.uitils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lê Minh Khôi
 */
public class KhachHangDao extends RavenDao<KhachHang, Integer>{
    final String INSERT_SQL = "INSERT INTO KHACHHANG (TENKH, SODT, DIACHI) VALUES (?, ?, ?)";
    final String UPDATE_SQL = "UPDATE KHACHHANG SET TENKH = ?, SODT = ?, DIACHI = ? WHERE MAKH = ?";
    final String DELETE_SQL = "DELETE FROM KHACHHANG WHERE MAKH = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM KHACHHANG";
    final String SELECT_BY_ID_SQL = "SELECT * FROM KHACHHANG WHERE MAKH = ?";

    @Override
    public void insert(KhachHang entity) {
        JdbcHelper.update(INSERT_SQL, entity.getTenKH(),entity.getSDT(),entity.getDchi());
    }

    @Override
    public void update(KhachHang entity) {
        JdbcHelper.update(UPDATE_SQL, entity.getTenKH(),entity.getSDT(),entity.getDchi(),entity.getMaKH());
    }

    @Override
    public void delete(Integer id) {
        JdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public List<KhachHang> selectAll() {
        return selectBysql(SELECT_ALL_SQL);
    }

    @Override
    public KhachHang selectByid(Integer id) {
        List<KhachHang> list = selectBysql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<KhachHang> selectBysql(String sql, Object... agrs) {
       List<KhachHang> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, agrs);
            while (rs.next()) {
                KhachHang entity = new KhachHang();
                entity.setMaKH(rs.getInt("MAKH"));
                entity.setTenKH(rs.getString("TENKH"));
                entity.setSDT(rs.getString("SODT"));
                entity.setDchi(rs.getString("DIACHI"));
                list.add(entity);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    
}
