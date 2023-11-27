/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.Dao;

import com.raven.uitils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Lê Minh Khôi
 */
public class ThongKeDao {
    public List<Object[]>getListOfArray(String sql,String[] cols,Object...agrs){
        try {
            List<Object[]> list = new ArrayList<>();
            ResultSet rs = JdbcHelper.query(sql, agrs);
            while (rs.next()) {                
                Object[] vals = new Object[cols.length];
                for (int i = 0; i < cols.length; i++) {
                    vals[i] = rs.getObject(cols[i]);
                }
                list.add(vals);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
   
    public List<Object[]> getTK_DT_NGAY(Date ngay,String trangThai){
        String sql = "{CALL TK_DT_NGAY(?,?)}";
        String col[] = {"TONGDOANHTHU","SOLUONGGIAY","SOLUONGHOADON","HOADONNHONHAT","HOADONLONNHAT","HOADONTRUNGBING"};
        return getListOfArray(sql, col, ngay,trangThai);
    }
    public List<Object[]> getTK_DT_TABLENGAY(Date ngay,String trangThai){
        String sql = "{CALL TK_DT_TABLENGAY(?,?)}";
        String col[] = {"MAHD","TENKH","TENNV","THANHTIEN"};
        return getListOfArray(sql, col, ngay,trangThai);
    }
    public List<Object[]> getTK_DT_THANG(Integer thang,Integer nam,String trangThai){
        String sql = "{CALL TK_DT_THANG(?,?,?)}";
        String col[] = {"TONGDOANHTHU","SOLUONGGIAY","SOLUONGHOADON","HOADONNHONHAT","HOADONLONNHAT","HOADONTRUNGBING"};
        return getListOfArray(sql, col, thang,nam,trangThai);
    }
    public List<Object[]> getTK_DT_TABLETHANG(Integer thang,Integer nam,String trangThai){
        String sql = "{CALL TK_DT_TABLETHANG(?,?,?)}";
        String col[] = {"MAHD","TENKH","TENNV","THANHTIEN"};
        return getListOfArray(sql, col, thang,nam,trangThai);
    }
    public List<Object[]> getTK_DT_NAM(Integer nam,String trangThai){
        String sql = "{CALL TK_DT_NAM(?,?)}";
        String col[] = {"TONGDOANHTHU","SOLUONGGIAY","SOLUONGHOADON","HOADONNHONHAT","HOADONLONNHAT","HOADONTRUNGBING"};
        return getListOfArray(sql, col, nam,trangThai);
    }
}
