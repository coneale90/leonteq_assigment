package com.leonteq.assignment.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.leonteq.assignment.DB.DBManager;
import com.leonteq.assignment.structure.SimpleCache;

public class DataLeonteq {
	
	public static String getShortAuthority(String authority){
		SimpleCache sc = SimpleCache.getInstance();
		if (sc.containsKey(authority)){
			return sc.get(authority);
		}
		Connection conn = DBManager.getConnection();
		PreparedStatement st = null;
		String shortAuthority = null;
		if (conn != null){
			try{
				String sql = "SELECT SHORT_AUTHORITY FROM AUTHORITIES WHERE AUTHORITY = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, authority);
				st.execute();
				ResultSet rs = st.getResultSet();
				if (rs != null && rs.next()){
					shortAuthority = rs.getString("SHORT_AUTHORITY");
					sc.put(authority, shortAuthority);
					sc.put(shortAuthority,authority);
				}
			}catch(Exception e){
				//TODO manage exception
				e.printStackTrace();
			}finally{
				try {
					if (st != null && !st.isClosed()){
						st.close();
					}
				} catch (SQLException e) {
					//TODO manage exception
				}
			}
		}
		return shortAuthority;
	}
	
	public static String retrieveShortUrl(String url){
		SimpleCache sc = SimpleCache.getInstance();
		if (sc.containsKey(url)){
			return sc.get(url);
		}
		Connection conn = DBManager.getConnection();
		PreparedStatement st = null;
		String shortUrl = null;
		if (conn != null){
			try{
				String sql = "SELECT SHORT_PATH FROM PATH_CONVERSION WHERE LONG_PATH = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, url);
				st.execute();
				ResultSet rs = st.getResultSet();
				if (rs != null &&  rs.next()){
					shortUrl = rs.getString("SHORT_PATH");
					sc.put(shortUrl, url);
					sc.put(url, shortUrl);
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					if (st != null && !st.isClosed()){
						st.close();
					}
				} catch (SQLException e) {
					//TODO manage exception
				}
			}	
		}
		return shortUrl;
	}
	
	public static String retrieveLongUrl(String url){
		SimpleCache sc = SimpleCache.getInstance();
		if (sc.containsKey(url)){
			return sc.get(url);
		}
		Connection conn = DBManager.getConnection();
		PreparedStatement st = null;
		String shortUrl = null;
		if (conn != null){
			try{
				String sql = "SELECT LONG_PATH FROM PATH_CONVERSION WHERE SHORT_PATH = ?";
				st = conn.prepareStatement(sql); 
				st.setString(1, url);
				st.execute();
				ResultSet rs = st.getResultSet();
				if (rs != null && rs.next()){
					shortUrl = rs.getString("LONG_PATH");
					sc.put(shortUrl, url);
					sc.put(url, shortUrl);
				}
			}catch(SQLException e){
				//TODO manage exception
				e.printStackTrace();
			}finally{
				try {
					if (st != null && !st.isClosed()){
						st.close();
					}
				} catch (SQLException e) {
					//TODO manage exception
				}
			}
		}
		return shortUrl;
	}
	
	public static boolean insertConversion(String longUrl, String shortUrl){
		SimpleCache sc = SimpleCache.getInstance();
		Connection conn = DBManager.getConnection();
		PreparedStatement st = null;
		boolean result = false;
		if (conn != null){
			try{
				String sql = "INSERT INTO PATH_CONVERSION(LONG_PATH,SHORT_PATH) VALUES(?,?)";
				st = conn.prepareStatement(sql); 
				st.setString(1, longUrl);
				st.setString(2, shortUrl);
				st.execute();
				sc.put(longUrl, shortUrl);
				sc.put(shortUrl, longUrl);
				result = true;
			}catch(SQLException e){
				//TODO manage exception
				e.printStackTrace();
			}finally{
				try {
					if (st != null && !st.isClosed()){
						st.close();
					}
				} catch (SQLException e) {
					//TODO manage exception
				}
			}
		}
		return result;
	}

	public static boolean insertShortAuthority(String authority,String shortAuthority) {
		SimpleCache sc = SimpleCache.getInstance();
		Connection conn = DBManager.getConnection();
		PreparedStatement st = null;
		boolean result = false;
		if (conn != null){
			try{
				String sql = "INSERT INTO AUTHORITIES(AUTHORITY,SHORT_AUTHORITY) VALUES(?,?)";
				st = conn.prepareStatement(sql); 
				st.setString(1, authority);
				st.setString(2, shortAuthority);
				st.execute();
				sc.put(authority, shortAuthority);
				sc.put(shortAuthority, authority);
				result = true;
			}catch(Exception e){
				//TODO manage exception
				e.printStackTrace();
				result = false;
			}finally{
				try {
					if (st != null && !st.isClosed()){
						st.close();
					}
				} catch (SQLException e) {
					//TODO manage exception
				}
			}
		}
		return result;
	}
	
	public static boolean fillCacheAuthorities(SimpleCache sc){
		Connection conn = DBManager.getConnection();
		PreparedStatement st = null;
		if (conn != null){
			try{
				String sql = "SELECT AUTHORITY, SHORT_AUTHORITY FROM AUTHORITIES ORDER BY CREATED_AT LIMIT 250";
				st = conn.prepareStatement(sql); 
				st.execute();
				ResultSet rs = st.getResultSet();
				while(rs != null && rs.next()){
					String la = rs.getString("AUTHORITY");
					String sa = rs.getString("SHORT_AUTHORITY");
					sc.put(la, sa);
					sc.put(sa, la);
				}
			}catch(SQLException e){
				//TODO manage exception
				e.printStackTrace();
				return false;
			}finally{
				try {
					if (st != null && !st.isClosed()){
						st.close();
					}
				} catch (SQLException e) {
					//TODO manage exception
				}
			}
		}
		return true;
	}
	
	public static boolean fillCachePathConversion(SimpleCache sc){
		Connection conn = DBManager.getConnection();
		PreparedStatement st = null;
		if (conn != null){
			try{
				String sql = "SELECT LONG_PATH, SHORT_PATH FROM PATH_CONVERSION ORDER BY CREATED_AT LIMIT 250";
				st = conn.prepareStatement(sql); 
				st.execute();
				ResultSet rs = st.getResultSet();
				while(rs != null && rs.next()){
					String lp = rs.getString("LONG_PATH");
					String sp = rs.getString("SHORT_PATH");
					sc.put(lp, sp);
					sc.put(sp, lp);
				}
			}catch(SQLException e){
				//TODO manage exception
				e.printStackTrace();
				return false;
			}finally{
				try {
					if (st != null && !st.isClosed()){
						st.close();
					}
				} catch (SQLException e) {
					//TODO manage exception
				}
			}
		}
		return true;
	}
	
}
