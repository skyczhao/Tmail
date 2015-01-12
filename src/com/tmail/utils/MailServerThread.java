package com.tmail.utils;

import com.tmail.base.MailServer;

import java.io.IOException;

/**
 * Created by tobin on 11/14/14.
 */
public class MailServerThread extends Thread {
    private MailServer server;

    public MailServerThread(MailServer server) {
        this.server = server;
    }

    /**
     * stop server firstly
     * instead of stopping the thread
     *
     */
    public void stopServer() {
        try {
            this.server.stopServerSocket();
        } catch (IOException e) {
            System.out.println("Message: " + e);
        }
    }

    /**
     * run single server thread
     *
     */
    @Override
    public void run() {
        try {
            this.server.run();
        } catch (IOException e) {
            System.out.println("Message: " + e);
        }
    }
}
