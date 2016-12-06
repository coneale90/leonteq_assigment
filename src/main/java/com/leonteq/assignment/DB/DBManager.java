package com.leonteq.assignment.DB;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBManager {
	
	public static Connection getConnection(){
		Context initContext;
		try {
			initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env");
	        DataSource ds = (DataSource) envContext.lookup("jdbc/leonteqdb");
	        Connection conn = ds.getConnection();
	        return conn;
		} catch (NamingException e) {
			//TODO manage error
			e.printStackTrace();
		} catch (SQLException e) {
			//TODO manage error
			e.printStackTrace();
		}
		return null;
	}

}
