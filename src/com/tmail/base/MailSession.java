package com.tmail.base;

import com.tmail.base.MailServer;
import com.tmail.dao.DataAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by tobin on 11/12/14.
 */
public abstract class MailSession {
    /* running state */
    private boolean running;
    private byte[] lock = new byte[0];

    private MailServer server;     // socket related server
    private DataAction database;   // database server
    private Socket socket;         // socket link

    /* communication properties */
    private BufferedReader reader;
    private PrintWriter writer;

    /**
     * get the running state
     *
     * @return running state
     */
    public final boolean isRunning() {
        return this.running;
    }

    /**
     * access to session server
     *
     * @return server
     */
    public MailServer getServer() {
        return this.server;
    }

    /**
     * close socket
     *
     */
    public void closeSocket() throws IOException {
        synchronized (lock) {
            // check not running
            if (!this.running) {
                System.out.println("Error: session is not running!");
                return;
            }

            // set not running
            this.running = false;
        }

        this.socket.close();
    }

    /**
     * start session with socket
     *
     * @throws IOException
     */
    public void startSocket() throws IOException {
        synchronized (lock) {
            // check running
            if (this.running) {
                System.out.println("Error: session is running!");
                return;
            }

            // set running
            this.running = true;
        }

        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.writer = new PrintWriter(this.socket.getOutputStream());

        // session timeout
        this.socket.setSoTimeout(this.server.getTimeout());
    }

    /**
     * receive the input stream from client
     * which is terminated with <CRLF>
     *
     * @return the line from client
     * @throws IOException
     */
    public synchronized String receiveLine() throws IOException {
        synchronized (lock) {
            // check not running
            if (!this.running) {
                System.out.println("Error: session is not running!");
                return null;
            }
        }

        return this.reader.readLine();
    }

    /**
     * response to client
     *
     * @param message
     */
    public synchronized void response(String message) {
        synchronized (lock) {
            // check not running
            if (!this.running) {
                System.out.println("Error: session is not running!");
                return;
            }
        }

        this.writer.print(message + "\r\n");
        this.writer.flush();
    }

    /**
     * constructor
     *
     * @param server
     * @param socket
     */
    public MailSession(MailServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
        this.database = new DataAction();
    }

    // abstract run function
    public abstract void run();

    /**
     * access to database
     *
     * @return
     */
    protected DataAction getDatabase() {
        return this.database;
    }

    /**
     * recycling the database connection
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        this.database.closeConnection();
        super.finalize();
    }
}
