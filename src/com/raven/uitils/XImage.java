package com.raven.uitils;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.nio.file.*;
import javax.swing.ImageIcon;

/**
 *
 * @author Lê Minh Khôi
 */
public class XImage {
    public static Image getAppIcon(){
        File file = new File("logos\\fpt.png");
        return new ImageIcon(file.getAbsolutePath()).getImage();
    }
    public static boolean save(File src){
        File dst = new File("EduSys\\logos",src.getName());
        if(!dst.getParentFile().exists()){
            dst.getParentFile().mkdirs(); //tao thu muc
        }
        try {
            Path from = Paths.get(src.getAbsolutePath());
            Path to = Paths.get(dst.getAbsolutePath());
            Files.copy(from, to,StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static ImageIcon read(String fileName){
        File path = new File ("EduSys\\logos",fileName);
        return new ImageIcon(path.getAbsolutePath());
    }
}
