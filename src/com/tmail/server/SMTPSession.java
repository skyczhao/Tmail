package com.tmail.server;

import com.tmail.base.MailServer;
import com.tmail.base.MailSession;
import com.tmail.base.SMTPCommand;
import com.tmail.dao.DataAction;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by tobin on 10/30/14.
 */
public class SMTPSession extends MailSession {
    private String id;

    // authentication state
    private boolean auth;
    private String username;
    // Email content
    private String sender;
    private String receiver;

    /**
     * access to session server
     *
     * @return SMTPServer
     */
    @Override
    public SMTPServer getServer() {
        return (SMTPServer)super.getServer();
    }

    /**
     * access to the session id
     *
     * @return session id
     */
    public String getId() {
        return this.id;
    }

    /**
     * access to session sender
     *
     * @return send address
     */
    public String getSender() {
        return this.sender;
    }

    /**
     * check sender and set
     *
     * @param sender
     */
    public synchronized void setSender(String sender) {
        String user = sender.split("@")[0];
        if (this.verifyUser(user))
            this.sender = sender;
    }

    /**
     * access to session receiver
     *
     * @return receiver address
     */
    public String getReceiver() {
        return this.receiver;
    }

    /**
     * check receiver and set
     *
     * @param receiver
     */
    public synchronized void setReceiver(String receiver) {
        String user = receiver.split("@")[0];
        if (this.verifyUser(user))
            this.receiver = receiver;
    }

    /**
     * access to session authentication state
     *
     * @return authentication state
     */
    public boolean getAuth() {
        return this.auth;
    }

    /**
     * access to session auth username
     *
     * @return auth username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * auth the user, according to the username and password
     *
     * @param username
     * @param password
     */
    public synchronized void authUser(String username, String password) {
        // connect to database
        DataAction da = this.getDatabase();
        Statement stm = da.getStatement();
        // check database record
        String sql = da.loginSQL(username, password);
        int count = 0;
        try {
            if (stm != null) {
                // check the number of rows
                ResultSet rs = stm.executeQuery(sql);
                while (rs.next()) {
                    if (rs.getString(2).equals(username)) {
                        count++;
                    }
                }
                rs.close();
                stm.close();
            }
        } catch (SQLException e) {
            // null
        }

        // result
        if (count == 1) {
            this.auth = true;
        } else {
            this.auth = false;
        }

        // change state
        if (this.getAuth())
            this.username = username;
    }

    /**
     * verify user in database
     *
     * @param username
     * @return flag
     */
    public synchronized boolean verifyUser(String username) {
        // connect to database
        DataAction da = this.getDatabase();
        Statement stm = da.getStatement();
        // check database record
        String sql = da.verifySQL(username);
        int count = 0;
        try {
            if (stm != null) {
                // check the number of rows
                ResultSet rs = stm.executeQuery(sql);
                while (rs.next()) {
                    if (rs.getString(2).equals(username)) {
                        count++;
                    }
                }
                rs.close();
                stm.close();
            }
        } catch (SQLException e) {
            // null
        }

        // result
        if (count == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * record email message in database
     *
     * @param from
     * @param to
     * @param subject
     * @param data
     * @return flag
     */
    public synchronized boolean sendEmail(String from, String to, String subject, String data) {
        // connect to database
        DataAction da = this.getDatabase();
        Statement stm = da.getStatement();
        // record data
        String send = this.sender.split("@")[0];
        String receive = this.receiver.split("@")[0];
        subject = subject.replace("'", "''");
        String content = data.replace("'", "''");
        String sql = da.sendSQL(send, receive, from, to, subject, content);
        // try to insert
        int count = 0;
        try {
            if (stm != null) {
                // execute the insertion
                count = stm.executeUpdate(sql);
                stm.close();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

        // result
        if (count == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * constructor
     *
     * @param server
     * @param socket
     */
    public SMTPSession(MailServer server, Socket socket) {
        super(server, socket);
    }

    /**
     * handle link
     *
     */
    @Override
    public void run() {
        // check running
        if (this.isRunning()) {
            System.out.println("Error: smtp session is running! Don't run again.");
            return;
        }

        // start listening
        try {
            this.startSocket();
        } catch (IOException e) {
            System.out.println("Error: fail to start smtp socket!");
            return;
        }

        // response
        this.response("220 " + this.getServer().getHostname());

        String line = null;
        String key = null;
        SMTPCommand command;
        // loop for the commands
        while (this.isRunning()) {
            try {
                line = this.receiveLine();
            } catch (IOException e) {
                System.out.println("Error: " + e);
                this.response("421 Timeout waiting for data from client.");
                try {
                    this.closeSocket();
                } catch (IOException ioe) {
                    // null
                }
                break;
            }

            if (line == null) {
                System.out.println("Error: unexpected connection error");
                break;
            }

            // handle command
            key = line.split(" ")[0];
            command = this.getServer().getSMTPCommandByKey(key);
            // check valid
            if (command != null) {
                command.execute(line, this);
            } else {
                this.response("500 Syntax error, command unrecognised");
            }
        }

        // exit and close socket
        if (this.isRunning()) {
            try {
                this.closeSocket();
            } catch (IOException e) {
                // null
            }
        }

    }
}
