package com.tmail.command;

import com.tmail.base.POP3Command;
import com.tmail.server.POP3Session;
import com.tmail.utils.POP3EmailDetail;
import com.tmail.utils.POP3EmailInfo;

import java.util.Vector;

/**
 * Created by tobin on 12/15/14.
 */
public class RetrPOP3Command extends POP3Command {
    @Override
    public void execute(String command, POP3Session session) {
        // check authentication
        if (!session.getAuth()) {
            session.response("-ERR Command not valid in this state");
            return;
        }

        String[] parts = command.split(" ");
        if (parts.length > 1) {
            // check valid
            if (parts[1] != null && parts[1].length() > 0) {
                Vector<POP3EmailInfo> emails = session.getEmails();
                int length = emails.size();
                int index = Integer.parseInt(parts[1]);
                // check range
                if (index >= 1 && index <= length) {
                    // get email record
                    POP3EmailDetail email = session.getEmail(index - 1);
                    // check
                    if (email != null) {
                        // response information
                        session.response("+OK " + emails.get(index - 1).getSize() + " characters");
                        // main email content
                        StringBuffer message = new StringBuffer();
                        message.append("Received: from " + email.sender + "; " + email.time + "\r\n");
                        message.append("From: " + email.from + "\r\n");
                        message.append("To: " + email.to + "\r\n");
                        message.append("Subject: " + email.subject + "\r\n");
//                        message.append("Content: " + "\r\n");
                        message.append("\r\n" + "\r\n");
                        message.append(email.content + "\r\n");
                        message.append(".");
                        session.response(message.toString());
                    } else {
                        session.response("-ERR Unknown message");
                    }
                } else {
                    session.response("-ERR Unknown message");
                }
            } else {
                session.response("-ERR Unknown message");
            }
        } else {
            session.response("-ERR Unknown message");
        }

        // null
    }
}
