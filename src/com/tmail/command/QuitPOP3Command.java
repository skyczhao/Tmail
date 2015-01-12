package com.tmail.command;

import com.tmail.base.POP3Command;
import com.tmail.server.POP3Session;

import java.io.IOException;

/**
 * Created by tobin on 11/13/14.
 */
public class QuitPOP3Command extends POP3Command {

    @Override
    public void execute(String command, POP3Session session) {
        session.quitEmails();
        session.response("+OK core mail");
        try {
            session.closeSocket();
        } catch (IOException e) {
            System.out.println("Error: pop3 session socket stop fail!");
        }
    }
}
