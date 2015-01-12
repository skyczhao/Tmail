package com.tmail.command;

import com.tmail.base.SMTPCommand;
import com.tmail.server.SMTPSession;

/**
 * Created by tobin on 11/1/14.
 */
public class EhloSMTPCommand extends SMTPCommand {

    @Override
    public void execute(String command, SMTPSession session) {
        StringBuffer message = new StringBuffer();
        message.append("250-" + session.getServer().getHostname() + "\r\n");
        message.append("250-PIPELINING" + "\r\n");
        message.append("250-8BITMIME" + "\r\n");
        message.append("250-AUTH LOGIN PLAIN" + "\r\n");
        message.append("250-AUTH=LOGIN PLAIN" + "\r\n");
        // TODO: avoid the client to die for the blank line
        message.append("250 mail");
        session.response(message.toString());
    }
}
