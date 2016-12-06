package com.leonteq.assignment.service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.leonteq.assignment.structure.SimpleCache;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by Conese on 01/12/2016.
 */

/****
 * 
 * Listener started at the begin. It initializes the threads and the queue element
 * 
 ****/
@WebListener
public class InitSystem implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //TODO add log4j to log error
        SimpleCache sc = SimpleCache.getInstance();
		DataLeonteq.fillCacheAuthorities(sc);
		DataLeonteq.fillCachePathConversion(sc);
        System.out.println("ServletContextListener started");
         
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("ServletContextListener destroyed");
    }
}
