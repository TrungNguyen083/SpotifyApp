/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tnguyen.hcmute.myspotifyapp;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author NGUYEN
 */
public class DAOSong
{
    static Connection conn = null;
    static PreparedStatement ps = null;
    static ResultSet rs = null;
    
    public static ArrayList<Song> getAllSong()
    {
        ArrayList<Song> listSong = new ArrayList<>();
        String query = "select * from Song";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()){
                Song song = new Song();
                song.setId(rs.getInt("ID"));
                song.setTitle(rs.getString("SONGTITLE"));
                song.setSingle(rs.getString("SINGLETITLE"));
                song.setResource(rs.getInt("PATH"));
                song.setDuration(rs.getString("DURATION"));
                song.setImage(rs.getInt("IMGSONG"));
                listSong.add(song);
            }
            conn.close();
            
        }catch (Exception e){
            System.out.println(e);
        }
        return listSong;
    }
}
    
   