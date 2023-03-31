/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tnguyen.hcmute.myspotifyapp;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author Legion
 */
public class DBcontext {
    Connection conn = null;

    public Connection getConnection() throws Exception {
        System.out.println("Test 212313");
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/MUSICPLAYER";
        String user = "root";
        String password = "nguyen12";
        conn = DriverManager.getConnection(url, user, password);
        return conn;
    }
//
//    public static void main(String[] args) {
//        try {
//            System.out.println(new DBcontext().getConnection());
//
//        } catch (Exception e) {
//
//        }
//    }
}
