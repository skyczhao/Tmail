package com.tmail.base;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by tobin on 11/11/14.
 */
public abstract class MailServer {
    private boolean running;                // running state
    private byte[] lock = new byte[0];

    private ServerSocket serverSocket;      // core variable

    private final static String DEFAULT_HOSTNAME = "localhost";
    private String hostname = null;         // default null

    private InetAddress bindAddress = null;	// default to all server
    private int port = 0;                   // useless value

    private int timeout = 1000 * 60 * 1;    // the timeout for each session connection, 1 min.
    private int maxConnection = 1000;       // the max connection number to the server

    /**
     * set the server hostname
     *
     * @param hostname
     */
    public synchronized void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * access to the server hostname
     *
     * @return hostname
     */
    public String getHostname() {
        if (this.hostname == null)
            return DEFAULT_HOSTNAME;
        else
            return this.hostname;
    }

    /**
     * set the IP bindAddress
     *
     * @param bindAddress
     */
    public synchronized void setBindAddress(InetAddress bindAddress) {
        this.bindAddress = bindAddress;
    }

    /**
     * access to the IP bind Address
     *
     * @return bindAddress
     */
    public InetAddress getBindAddress() {
        return this.bindAddress;
    }

    /**
     * set the socket port
     *
     * @param port
     */
    public synchronized void setPort(int port) {
        this.port = port;
    }

    /**
     * access to the socket port
     *
     * @return port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * set the connection timeout
     *
     * @param timeout
     */
    public synchronized void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * access to the connection timeout
     *
     * @return timeout
     */
    public int getTimeout() {
        return this.timeout;
    }

    /**
     * set the max connection number
     *
     * @param maxConnection
     */
    public synchronized void setMaxConnection(int maxConnection) {
        this.maxConnection = maxConnection;
    }

    /**
     * access to max connection number
     *
     * @return the max number of connections
     */
    public int getMaxConnection() {
        return this.maxConnection;
    }

    /**
     * server running state
     *
     * @return running state
     */
    public final boolean isRunning() {
        return this.running;
    }

    /**
     * stop server socket
     *
     * @throws IOException
     */
    public void stopServerSocket() throws IOException {
        synchronized (this.lock) {
            // detect not running
            if (!this.running) {
                System.out.println("Error: server is not running!");
                return;
            }
            // set not running
            this.running = false;
        }

        this.serverSocket.close();
    }

    /**
     * create server socket.
     *
     * @throws IOException
     */
    public void createServerSocket() throws IOException {
        synchronized (this.lock) {
            // detect running
            if (this.running) {
                System.out.println("Error: server is running!");
                return;
            }
            // set running
            this.running = true;
        }

        /* binding domain address */
        InetSocketAddress isa;
        if (this.bindAddress == null)
            isa = new InetSocketAddress(this.port);
        else
            isa = new InetSocketAddress(this.bindAddress, this.port);

        /* create socket to listen the port */
        ServerSocket socket = new ServerSocket();
        socket.bind(isa);

        if (this.port == 0)
            this.port = socket.getLocalPort();

        this.serverSocket = socket;
    }

    /**
     * get connection socket
     *
     * @return connection socket
     * @throws IOException
     */
    public Socket getSocket() throws IOException {
        Socket socket = this.serverSocket.accept();
        return socket;
    }

    // default constructor
    public MailServer() {
    }

    // constructor with port
    public MailServer(int port) {
        this.setPort(port);
    }

    /**
     * abstract run function
     *
     * @throws IOException
     */
    public abstract void run() throws IOException ;

}
