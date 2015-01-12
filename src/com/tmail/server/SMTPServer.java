package com.tmail.server;

import com.tmail.base.MailServer;
import com.tmail.base.SMTPCommand;
import com.tmail.utils.MailSessionThread;
import com.tmail.utils.SMTPCommandCollector;

import java.io.*;
import java.net.Socket;
import java.util.Locale;
import java.util.Map;

/**
 * Created by tobin on 10/29/14.
 */
public class SMTPServer extends MailServer {

    private SMTPCommandCollector commands = new SMTPCommandCollector();

    /**
     * transfer command key to command object
     *
     * @param key
     * @return SMTPCommand
     */
    public SMTPCommand getSMTPCommandByKey(String key) {
        Map<String, SMTPCommand> commandMap = this.commands.getCommandMap();
        String upperCaseKey = key.toUpperCase(Locale.ENGLISH);
        return commandMap.get(upperCaseKey);
    }

    // constructor with port
    public SMTPServer(int port) {
        super(port);
    }

    /**
     * run server by default settings
     *
     * @throws IOException
     */
    @Override
    public void run() throws IOException {
        if (this.isRunning()) {
            System.out.println("Error: smtp server is running, don't run repeatedly!");
            return;
        }

        // run server and accept link
        this.createServerSocket();
        while (this.isRunning()) {
            try {
                Socket socket = this.getSocket();
                SMTPSession session = new SMTPSession(this, socket);
                MailSessionThread sessionThread = new MailSessionThread(session);
                sessionThread.start();
            } catch (IOException e) {
                break;
            }
        }
    }

}
