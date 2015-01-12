package com.tmail;

import com.tmail.server.POP3Server;
import com.tmail.server.SMTPServer;
import com.tmail.utils.MailServerThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by tobin on 11/7/14.
 */
public class runserver {
    public static void main(String[] args) {
        // run smtp thread
        MailServerThread smtpThread = new MailServerThread(new SMTPServer(1026));
        smtpThread.start();
        // run pop3 thread
        MailServerThread pop3Thread = new MailServerThread(new POP3Server(1027));
        pop3Thread.start();

        // keyboard input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Enter <quit> to quit server!");
            System.out.print("Enter command: ");
            try {
                String command = reader.readLine();
                if (command.equals("quit"))
                    break;
            } catch (IOException e) {
                break;
            }
        }

        // quit
        smtpThread.stopServer();
        pop3Thread.stopServer();
        System.out.println("Bye!");
    }
}
