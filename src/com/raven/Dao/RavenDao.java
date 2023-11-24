/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.Dao;

import java.util.List;

/**
 *
 * @author Lê Minh Khôi
 */
public abstract class RavenDao<RavenType,KeyType> {
    public abstract void insert(RavenType entity);
    public abstract void update(RavenType entity);
    public abstract void delete(KeyType id);
    public abstract List<RavenType> selectAll();
    public abstract RavenType selectByid(KeyType id);
    public abstract List<RavenType> selectBysql(String sql,Object...agrs);
}
