package com.tmail.command;

import com.tmail.base.SMTPCommand;
import com.tmail.server.SMTPSession;
import com.tmail.utils.SMTPCommandEmail;

/**
 * Created by tobin on 11/6/14.
 */
public class RcptSMTPCommand extends SMTPCommand {
    @Override
    public void execute(String command, SMTPSession session) {
        // check auth
        if (!session.getAuth()) {
            session.response("553 You are not authorized to send mail.");
            return;
        }

        // get email address from command
        String address = SMTPCommandEmail.getEmailAddress(command);
        if (address == null || address.length() == 0) {
            session.response("501 Syntax: RCPT TO: <address>");
            return;
        }

        // set session receiver
        session.setReceiver(address);
        String receiver = session.getReceiver();
        if (receiver == null || receiver.length() == 0) {
            session.response("553 Address invalid.");
            return;
        }

        session.response("250 OK, RCPT TO: " + session.getReceiver());
    }
}
