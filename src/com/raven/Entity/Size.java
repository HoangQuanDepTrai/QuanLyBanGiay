/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.Entity;

/**
 *
 * @author Lê Minh Khôi
 */
public class Size {
    private int soSize;

    public Size() {
    }

    public Size(int soSize) {
        this.soSize = soSize;
    }

    public int getSoSize() {
        return soSize;
    }

    public void setSoSize(int soSize) {
        this.soSize = soSize;
    }

    @Override
    public String toString() {
        return soSize+"";
    }

    
}
