package com.tmail.server;

import com.tmail.base.MailServer;
import com.tmail.base.MailSession;
import com.tmail.base.POP3Command;
import com.tmail.dao.DataAction;
import com.tmail.utils.POP3EmailDetail;
import com.tmail.utils.POP3EmailInfo;

import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * Created by tobin on 11/12/14.
 */
public class POP3Session extends MailSession {
    // authentication state
    private boolean auth;
    private String username;
    private Vector<POP3EmailInfo> emails = new Vector<POP3EmailInfo>();

    /**
     * update user emails
     *
     * @return
     */
    private synchronized boolean setEmails() {
        // check authentication
        if (!this.auth) {
            this.emails.clear();
            return false;
        }

        this.emails.clear();
        // connect to database
        DataAction da = this.getDatabase();
        Statement stm = da.getStatement();
        // query database
        String user = this.username.split("@")[0];
        String sql = da.listSQL(user);
        try {
            if (stm != null) {
                // loop for each record
                ResultSet rs = stm.executeQuery(sql);
                int length = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    // record the simple information
                    int id = rs.getInt("id");
                    int size = 0;
                    for (int i = 0; i < length; i++) {
                        size += rs.getString(i + 1).length();
                    }
                    this.emails.addElement(new POP3EmailInfo(id, size));
                }
                rs.close();
                stm.close();
            }
        } catch (SQLException e) {
            // null
        }
        return true;
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
     * set the authentication state
     *
     * @param password
     */
    public synchronized void setAuth(String password) {
        // check username
        if (this.username == null || this.username.length() == 0) {
            this.auth = false;
            return;
        }

        // connect to database
        DataAction da = this.getDatabase();
        Statement stm = da.getStatement();
        // check database record
        String user = this.username.split("@")[0];
        String sql = da.loginSQL(user, password);
        int count = 0;
        try {
            if (stm != null) {
                // check the number of rows
                ResultSet rs = stm.executeQuery(sql);
                while (rs.next()) {
                    if (rs.getString(2).equals(user)) {
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
        this.setEmails();
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
     * set the username
     *
     * @param username
     */
    public synchronized void setUsername(String username) {
        this.username = username;
        this.auth = false;
        this.emails.clear();
    }

    /**
     * access to user emails
     *
     * @return
     */
    public Vector<POP3EmailInfo> getEmails() {
        return this.emails;
    }

    /**
     * get email by index
     * transform index to id
     *
     * @param index
     * @return
     */
    public synchronized POP3EmailDetail getEmail(int index) {
        // check authentication
        if (!this.auth) {
            return null;
        }

        // check index
        POP3EmailDetail result = null;
        int length = this.emails.size();
        if (index >= 0 && index < length) {
            // connect to database
            DataAction da = this.getDatabase();
            Statement stm = da.getStatement();
            // get record
            int id = this.emails.get(index).getId(); // transformation
            String sql = da.querySQL(id);
            try {
                if (stm != null) {
                    ResultSet rs = stm.executeQuery(sql);
                    // store
                    while (rs.next()) {
                        // TODO: json data?
                        if (rs.getInt("id") == id) {
                            result = new POP3EmailDetail();
                            result.id = rs.getInt("id");
                            result.sender = rs.getString("sender");
                            result.receiver = rs.getString("receiver");
                            result.from = rs.getString("efrom");
                            result.to = rs.getString("eto");
                            result.subject = rs.getString("subject");
                            result.content = rs.getString("content");
                            result.time = rs.getString("time");
                        }
                    }
                    rs.close();
                    stm.close();
                }
            } catch (SQLException e) {
                // null
            }
        }
        return result;
    }

    /**
     * delete marked emails when quiting
     *
     */
    public synchronized void quitEmails() {
        // check authentication
        if (!this.auth) {
            return;
        }

        // delete emails
        if (this.emails != null) {
            int length = this.emails.size();
            // connect to database
            DataAction da = this.getDatabase();
            Statement stm = da.getStatement();
            // check database connection
            if (stm != null) {
                // check flags
                for (int i = 0; i < length; i++) {
                    // when flag is true
                    if (this.emails.get(i).getFlag()) {
                        // delete
                        int id = this.emails.get(i).getId();
                        String sql = da.deleteSQL(id);
                        try {
                            stm.executeUpdate(sql);
                        } catch (SQLException e) {
                            // null
                        }
                    }
                }

                // close statement
                try {
                    stm.close();
                } catch (SQLException e) {
                    // null
                }
            }
        }

        // null
    }

    /**
     * access to session server
     *
     * @return POP3Server
     */
    @Override
    public POP3Server getServer() {
        return (POP3Server)super.getServer();
    }

    /**
     * constructor
     *
     * @param server
     * @param socket
     */
    public POP3Session(MailServer server, Socket socket) {
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
            System.out.println("Error: pop3 session is running! Don't run again.");
            return;
        }

        // start listening
        try {
            this.startSocket();
        } catch (IOException e) {
            System.out.println("Error: fail to start pop3 socket!");
        }

        // response
        this.response("+OK " + this.getServer().getHostname());

        String line = null;
        String key = null;
        POP3Command command;
        // loop for the commands
        while (this.isRunning()) {
            try {
                line = this.receiveLine();
            } catch (IOException e) {
                System.out.println("Error: " + e);
                this.response("-ERR connection timeout");
                try {
                    this.closeSocket();
                } catch (IOException ioe) {
                    // void
                }
                break;
            }

            if (line == null) {
                System.out.println("Error: unexpected connection error");
                break;
            }

            // handle command
            key = line.split(" ")[0];
            command = this.getServer().getPOP3CommandByKey(key);
            // check valid
            if (command != null)
                command.execute(line, this);
            else {
                this.response("-ERR command unrecognised");
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
