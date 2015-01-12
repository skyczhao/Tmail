package com.tmail.command;

import com.tmail.base.SMTPCommand;
import com.tmail.server.SMTPSession;

import java.io.IOException;

/**
 * Created by tobin on 11/6/14.
 */
public class QuitSMTPCommand extends SMTPCommand {
    @Override
    public void execute(String command, SMTPSession session) {
        session.response("221 Bye");
        try {
            session.closeSocket();
        } catch (IOException e) {
            System.out.println("Error: smtp session socket stop fail!");
        }
    }
}
