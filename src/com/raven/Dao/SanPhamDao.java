/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.Dao;

import com.raven.Entity.Mau;
import com.raven.Entity.QLSanPham;
import com.raven.Entity.Size;
import com.raven.uitils.JdbcHelper;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

/**
 *
 * @author Lê Minh Khôi
 */
public class SanPhamDao extends RavenDao<QLSanPham, String> {

    final String INSERT_SQL = "INSERT INTO SANPHAM (MASP, TENSP, GIANHAP, GIABAN, SIZE, MAU, SOLUONG, "
            + "HINHANH, NGAYNHAP, MOTA, LOAI) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT MALOAI FROM LOAI WHERE TENLOAI LIKE ?))";
    final String UPDATE_SQL = "UPDATE SANPHAM SET TENSP = ?, GIANHAP = ?, GIABAN = ?, SIZE = ?, MAU = ?,"
            + " SOLUONG = ?, HINHANH = ?, NGAYNHAP = ?, MOTA = ?, LOAI = (SELECT MALOAI FROM LOAI WHERE TENLOAI = ?) WHERE MASP = ?";
    final String DELETE_SQL = "DELETE FROM SANPHAM WHERE MASP = ?";
    final String SELECT_ALL_SQL = "SELECT * FROM SANPHAM";
    final String SELECT_BY_ID_SQL = "SELECT * FROM SANPHAM WHERE MASP = ?";
    final String SELECT_MAU = "SELECT DISTINCT MAU FROM SANPHAM";
    final String SELECT_SIZE = "SELECT DISTINCT SIZE FROM SANPHAM";

    @Override
    public void insert(QLSanPham entity) {
        JdbcHelper.update(INSERT_SQL, entity.getMaSP(), entity.getTenSp(), entity.getGiaNhap(), entity.getGiaBan(),
                entity.getSize(), entity.getMau(), entity.getSoLuong(), entity.getHinhAnh(), entity.getNgayNhap(),
                entity.getMoTa(), entity.getLoai());
    }

    @Override
    public void update(QLSanPham entity) {
        JdbcHelper.update(UPDATE_SQL, entity.getTenSp(), entity.getGiaNhap(), entity.getGiaBan(),
                entity.getSize(), entity.getMau(), entity.getSoLuong(), entity.getHinhAnh(), entity.getNgayNhap(),
                entity.getMoTa(), entity.getLoai(), entity.getMaSP());
    }

    @Override
    public void delete(String id) {
        JdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public List<QLSanPham> selectAll() {
        return selectBysql(SELECT_ALL_SQL);
    }

    @Override
    public QLSanPham selectByid(String id) {
        List<QLSanPham> list = selectBysql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<QLSanPham> selectBysql(String sql, Object... agrs) {
        List<QLSanPham> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, agrs);
            while (rs.next()) {
                QLSanPham entity = new QLSanPham();
                entity.setMaSP(rs.getString("MASP"));
                entity.setTenSp(rs.getString("TENSP"));
                entity.setGiaNhap(rs.getDouble("GIANHAP"));
                entity.setGiaBan(rs.getDouble("GIABAN"));
                entity.setSize(rs.getInt("SIZE"));
                entity.setMau(rs.getString("MAU"));
                entity.setSoLuong(rs.getInt("SOLUONG"));
                entity.setHinhAnh(rs.getString("HINHANH"));
                entity.setNgayNhap(rs.getDate("NGAYNHAP"));
                entity.setMoTa(rs.getString("MOTA"));
                entity.setLoai(rs.getString("LOAI"));
                list.add(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<QLSanPham> selectByName(String tenSP) {
        String SELECT_BY_NAME = "SELECT * FROM SANPHAM WHERE TENSP LIKE ?";
        return selectBysql(SELECT_BY_NAME, "%" + tenSP + "%");

    }

   

    public List<Mau> selectMau() {
        List<Mau> list = new ArrayList<>();
        ResultSet rs = JdbcHelper.query(SELECT_MAU);
        try {
            while (rs.next()) {
                Mau mau = new Mau();
                mau.setTenMau(rs.getString("MAU"));
                list.add(mau);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public List<Size> selectSize() {
        List<Size> list = new ArrayList<>();
        ResultSet rs = JdbcHelper.query(SELECT_SIZE);
        try {
            while (rs.next()) {
                Size soSize = new Size();
                soSize.setSoSize(rs.getInt("SIZE"));
                list.add(soSize);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public void update_by_tenloai(QLSanPham entity) {
        String UPDATE_BY_TENLOAI = "UPDATE SANPHAM SET TENSP = ?, GIANHAP = ?, GIABAN = ?, SIZE = ?, MAU = ?,"
                + " SOLUONG = ?, HINHANH = ?, NGAYNHAP = ?, MOTA = ?, LOAI = ? WHERE MASP = ?";
        JdbcHelper.update(UPDATE_BY_TENLOAI, entity.getTenSp(), entity.getGiaNhap(), entity.getGiaBan(),
                entity.getSize(), entity.getMau(), entity.getSoLuong(), entity.getHinhAnh(), entity.getNgayNhap(),
                entity.getMoTa(), entity.getLoai(), entity.getMaSP());
    }

}
