/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.Dao;

import com.raven.Entity.QLNhanVien;
import com.raven.uitils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lê Minh Khôi
 */
public class NhanVienDao extends RavenDao<QLNhanVien, String> {

    final String INSERT_SQL = "INSERT INTO NHANVIEN (MANV, MATKHAU, TENNV, SODT, VAITRO) VALUES (?, ?, ?, ?, ?)";
    final String UPDATE_SQL = "UPDATE NHANVIEN SET  MATKHAU = ?, TENNV = ?, SODT = ?, VAITRO = ? WHERE MANV = ?";
    final String DELETE_SQL = "DELETE FROM NHANVIEN WHERE MANV = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM NHANVIEN";
    final String SELECT_BY_ID_SQL = "SELECT * FROM NHANVIEN WHERE MANV = ?";

    @Override
    public void insert(QLNhanVien entity) {
        JdbcHelper.update(INSERT_SQL, entity.getManv(), entity.getMatKhau(), entity.getTenNV(), entity.getSDT(), entity.isVaiTro());
    }

    @Override
    public void update(QLNhanVien entity) {
        JdbcHelper.update(UPDATE_SQL, entity.getMatKhau(), entity.getTenNV(), entity.getSDT(), entity.isVaiTro(), entity.getManv());
    }

    @Override
    public void delete(String id) {
        JdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public List<QLNhanVien> selectAll() {
        return selectBysql(SELECT_ALL_SQL);
    }

    @Override
    public QLNhanVien selectByid(String id) {
        List<QLNhanVien> list = selectBysql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<QLNhanVien> selectBysql(String sql, Object... agrs) {
        List<QLNhanVien> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, agrs);
            while (rs.next()) {
                QLNhanVien entity = new QLNhanVien();
                entity.setManv(rs.getString("MANV"));
                entity.setMatKhau(rs.getString("MATKHAU"));
                entity.setTenNV(rs.getString("TENNV"));
                entity.setSDT(rs.getString("SODT"));
                entity.setVaiTro(rs.getBoolean("VAITRO"));
                list.add(entity);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

}
