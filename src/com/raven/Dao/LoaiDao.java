/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.Dao;

import com.raven.Entity.Loai;
import com.raven.uitils.JdbcHelper;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 *
 * @author Lê Minh Khôi
 */
public class LoaiDao extends RavenDao<Loai, String>{
    final String INSERT_SQL = "INSERT INTO LOAI (MALOAI, TENLOAI) VALUES (?, ?)";
    final String UPDATE_SQL = "UPDATE LOAI SET TENLOAI = ? WHERE MALOAI = ?";
    final String DELETE_SQL = "DELETE FROM LOAI WHERE MALOAI = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM LOAI";
    final String SELECT_BY_ID_SQL = "SELECT * FROM LOAI WHERE MALOAI = ?";

    @Override
    public void insert(Loai entity) {
        JdbcHelper.update(INSERT_SQL, entity.getMaLoai(),entity.getTenLoai());
    }

    @Override
    public void update(Loai entity) {
        JdbcHelper.update(UPDATE_SQL, entity.getTenLoai(),entity.getMaLoai());
    }

    @Override
    public void delete(String id) {
        JdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public List<Loai> selectAll() {
        return selectBysql(SELECT_ALL_SQL);
    }

    @Override
    public Loai selectByid(String id) {
        List<Loai> list = selectBysql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<Loai> selectBysql(String sql, Object... agrs) {
        List<Loai> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, agrs);
            while(rs.next()){
                Loai entity = new Loai();
                entity.setMaLoai(rs.getString("MALOAI"));
                entity.setTenLoai(rs.getString("TENLOAI"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
        
    }
    
}
