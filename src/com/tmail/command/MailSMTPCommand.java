package com.tmail.command;

import com.tmail.base.SMTPCommand;
import com.tmail.server.SMTPSession;
import com.tmail.utils.SMTPCommandEmail;

/**
 * Created by tobin on 11/6/14.
 */
public class MailSMTPCommand extends SMTPCommand {

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
            session.response("501 Syntax: MAIL FROM: <address>");
            return;
        }

        // set session sender
        session.setSender(address);
        String sender = session.getSender();
        if (sender == null || sender.length() == 0) {
            session.response("553 Address invalid.");
            return;
        }

        session.response("250 OK, MAIL FROM: " + session.getSender());
    }
}
