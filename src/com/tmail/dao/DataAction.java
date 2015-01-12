package com.tmail.dao;

import com.tmail.conf.Settings;
import com.tmail.utils.EncryptUtil;

import java.sql.*;
import java.sql.Statement;
import java.text.SimpleDateFormat;

/**
 * Created by tobin on 12/11/14.
 */
public class DataAction {
    // connection
    private Connection conn;

    // constructor
    public DataAction() {
        conn = null;
    }

    /**
     * create a connection linked to mysql server
     *
     */
    private synchronized void createConnection() {
        // create a new connection when the connection is null
        if (this.conn == null) {
            try {
                // load specified driver
                Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://" + Settings.dburl + ":" + Settings.dbport + "/" + Settings.dbname;
                // try to link server
                this.conn = DriverManager.getConnection(url, Settings.dbuser, Settings.dbpass);
            } catch (ClassNotFoundException e) {
                System.out.println("Error: " + e);
            } catch (SQLException e) {
                System.out.println("Error: " + e);
            }
        }
    }

    /**
     * close the server connection
     */
    public synchronized void closeConnection() {
        // close connection
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (SQLException e) {
                System.out.println("Error: " + e);
            }
            this.conn = null;
        }
    }

    /**
     * get the execute statement
     *
     * @return statement
     */
    public synchronized Statement getStatement() {
        Statement stm = null;
        try {
            // ensure connection
            if (this.conn == null) {
                this.createConnection();
            }

            // create statement
            stm = conn.createStatement();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

        // return Statement
        return stm;
    }

    /**
     * transform the register parameters into sql statement
     *
     * @param user
     * @param pass
     * @return sql string
     */
    public static String registerSQL(String user, String pass) {
        // construct format
        StringBuffer buffer = new StringBuffer();
        buffer.append("INSERT INTO user(name, pass) ");
        buffer.append("VALUES('%s', '%s')");
        // return sql statement
        String format = buffer.toString();
        String encryptPass = EncryptUtil.sha1(pass);
        return String.format(format, user, encryptPass);
    }

    /**
     * transform the login parameters into sql statement
     *
     * @param user
     * @param pass
     * @return sql string
     */
    public static String loginSQL(String user, String pass) {
        // construct format
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT * FROM user ");
        buffer.append("WHERE name='%s' AND pass='%s'");
        // return sql statement
        String format = buffer.toString();
        String encryptPass = EncryptUtil.sha1(pass);
        return String.format(format, user, encryptPass);
    }

    /**
     * user verifying sql
     *
     * @param user
     * @return sql string
     */
    public static String verifySQL(String user) {
        // construct format
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT * FROM user ");
        buffer.append("WHERE name='%s'");
        // return sql statement
        String format = buffer.toString();
        return String.format(format, user);
    }

    /**
     * email sending sql
     *
     * @param sender
     * @param receiver
     * @param from
     * @param to
     * @param subject
     * @param content
     * @return sql string
     */
    public static synchronized String sendSQL(
            String sender,
            String receiver,
            String from,
            String to,
            String subject,
            String content
    ) {
        // get current time
        java.util.Date current = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(current);
        // construct format
        StringBuffer buffer = new StringBuffer();
        buffer.append("INSERT INTO mail(sender, receiver, efrom, eto, subject, content, time) ");
        buffer.append("VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s')");
        // return sql statement
        String format = buffer.toString();
        return String.format(format, sender, receiver, from, to, subject, content, currentTime);
    }

    /**
     * list all receiver's emails
     *
     * @param receiver
     * @return sql string
     */
    public static String listSQL(String receiver) {
        // construct format
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT * FROM mail ");
        buffer.append("WHERE receiver='%s' ");
        buffer.append("ORDER BY time");
        // return sql statement
        String format = buffer.toString();
        return String.format(format, receiver);
    }

    /**
     * query email by id
     *
     * @param id
     * @return sql string
     */
    public static String querySQL(int id) {
        // construct format
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT * FROM mail ");
        buffer.append("WHERE id=%d");
        // return sql statement
        String format = buffer.toString();
        return String.format(format, id);
    }

    /**
     * delete email by id
     *
     * @param id
     * @return sql string
     */
    public static String deleteSQL(int id) {
        // construct format
        StringBuffer buffer = new StringBuffer();
        buffer.append("DELETE FROM mail ");
        buffer.append("WHERE id=%d");
        // return sql statement
        String format = buffer.toString();
        return String.format(format, id);
    }
}
