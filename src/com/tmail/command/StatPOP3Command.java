package com.tmail.command;

import com.tmail.base.POP3Command;
import com.tmail.server.POP3Session;
import com.tmail.utils.POP3EmailInfo;

import java.util.Vector;

/**
 * Created by tobin on 12/15/14.
 */
public class StatPOP3Command extends POP3Command {
    @Override
    public void execute(String command, POP3Session session) {
        // check authentication
        if (!session.getAuth()) {
            session.response("-ERR Command not valid in this state");
            return;
        }

        // update and get emails
        Vector<POP3EmailInfo> emails = session.getEmails();
        int length = emails.size();
        int size = 0;
        for (int i = 0; i < length; i++) {
            size += emails.get(i).getSize();
        }
        session.response("+OK " + length + " " + size);
    }
}
