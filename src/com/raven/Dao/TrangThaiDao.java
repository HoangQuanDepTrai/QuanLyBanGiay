/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.Dao;

import com.raven.Entity.TrangThai;
import com.raven.uitils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lê Minh Khôi
 */
public class TrangThaiDao extends RavenDao<TrangThai, Integer>{
    final String SELECT_ALL_SQL = "SELECT * FROM TRANGTHAI";
    final String SELECT_BY_ID_SQL = "SELECT * FROM TRANGTHAI WHERE MATRANGTHAI = ?";

    @Override
    public void insert(TrangThai entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(TrangThai entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<TrangThai> selectAll() {
        return selectBysql(SELECT_ALL_SQL);
    }

    @Override
    public TrangThai selectByid(Integer id) {
        List<TrangThai> list = selectBysql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<TrangThai> selectBysql(String sql, Object... agrs) {
        List<TrangThai> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, agrs);
            while (rs.next()) {
                TrangThai entity = new TrangThai();
                entity.setMaTrangThai(rs.getInt("MATRANGTHAI"));
                entity.setTenTrangThai(rs.getString("TENTRANGTHAI"));
                list.add(entity);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    
}
