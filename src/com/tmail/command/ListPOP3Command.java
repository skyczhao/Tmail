package com.tmail.command;

import com.tmail.base.POP3Command;
import com.tmail.server.POP3Session;
import com.tmail.utils.POP3EmailInfo;

import java.util.Vector;

/**
 * Created by tobin on 12/15/14.
 */
public class ListPOP3Command extends POP3Command {
    @Override
    public void execute(String command, POP3Session session) {
        // check authentication
        if (!session.getAuth()) {
            session.response("-ERR Command not valid in this state");
            return;
        }

        // list emails
        String[] parts = command.split(" ");
        Vector<POP3EmailInfo> emails = session.getEmails();
        int length = emails.size();
        // different case
        if (parts.length > 1) {
            // list parameter message
            if (parts[1] != null && parts[1].length() > 0) {
                int id = Integer.parseInt(parts[1]);
                if (id >= 1 && id <= length) {
                    session.response("+OK " + id + " " + emails.get(id - 1).getSize());
                } else {
                    session.response("-ERR Unknown message");
                }
            } else {
                session.response("-ERR Unknown message");
            }
        } else {
            // list all emails message
            int size = 0;
            // construct return message
            StringBuffer message = new StringBuffer();
            for (int i = 0; i < length; i++) {
                int tmp = emails.get(i).getSize();
                size += tmp;
                message.append((i + 1) + " " + tmp + "\r\n");
            }
            message.append('.');

            // return message
            session.response("+OK " + length + " " + size);
            session.response(message.toString());
        }

        // end
    }
}
