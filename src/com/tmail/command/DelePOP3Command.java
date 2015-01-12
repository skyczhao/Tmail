package com.tmail.command;

import com.tmail.base.POP3Command;
import com.tmail.server.POP3Session;
import com.tmail.utils.POP3EmailInfo;

import java.util.Vector;

/**
 * Created by tobin on 12/15/14.
 */
public class DelePOP3Command extends POP3Command {
    @Override
    public void execute(String command, POP3Session session) {
        // check authentication
        if (!session.getAuth()) {
            session.response("-ERR Command not valid in this state");
            return;
        }

        // list emails
        String[] parts = command.split(" ");
        if (parts.length > 1) {
            // check valid
            if (parts[1] != null && parts[1].length() > 0) {
                Vector<POP3EmailInfo> emails = session.getEmails();
                int length = emails.size();
                int id = Integer.parseInt(parts[1]);
                // check index
                if (id >= 1 && id <= length) {
                    emails.get(id - 1).setFlag();
                    session.response("+OK core mail");
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
