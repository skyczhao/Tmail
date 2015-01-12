package com.tmail.server;

import com.tmail.base.MailServer;
import com.tmail.base.POP3Command;
import com.tmail.utils.MailSessionThread;
import com.tmail.utils.POP3CommandCollector;

import java.io.IOException;
import java.net.Socket;
import java.util.Locale;
import java.util.Map;

/**
 * Created by tobin on 11/11/14.
 */
public class POP3Server extends MailServer {

    private POP3CommandCollector commands = new POP3CommandCollector();

    /**
     * transfer command key to command object
     *
     * @param key
     * @return POP3Command object
     */
    public POP3Command getPOP3CommandByKey(String key) {
        Map<String, POP3Command> commandMap = this.commands.getCommandMap();
        String upperCaseKey = key.toUpperCase(Locale.ENGLISH);
        return commandMap.get(upperCaseKey);
    }

    // constructor with port
    public POP3Server(int port) {
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
            System.out.println("Error: pop3 server is running, don't run repeatedly!");
            return;
        }

        // run server and accept link
        this.createServerSocket();
        while (this.isRunning()) {
            try {
                Socket socket = this.getSocket();
                POP3Session session = new POP3Session(this, socket);
                MailSessionThread sessionThread = new MailSessionThread(session);
                sessionThread.start();
            } catch (IOException e) {
                break;
            }
        }
    }

}
