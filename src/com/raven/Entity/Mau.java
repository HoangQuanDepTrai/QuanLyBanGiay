/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.Entity;

/**
 *
 * @author Lê Minh Khôi
 */
public class Mau {
    private String tenMau;

    public Mau() {
    }

    public Mau(String tenMau) {
        this.tenMau = tenMau;
    }

    public String getTenMau() {
        return tenMau;
    }

    public void setTenMau(String tenMau) {
        this.tenMau = tenMau;
    }

    @Override
    public String toString() {
        return tenMau;
    }
    
}
