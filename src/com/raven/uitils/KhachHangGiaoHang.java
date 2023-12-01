/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.uitils;

import com.raven.Entity.KhachHang;

/**
 *
 * @author ADMIN
 */
public class KhachHangGiaoHang {

    public static KhachHang khachHang = new KhachHang(1, "", "", "");

    public static void clear() {
        khachHang = new KhachHang(1, "", "", "");
    }

}
