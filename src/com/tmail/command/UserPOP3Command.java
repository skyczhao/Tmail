package com.tmail.command;

import com.tmail.base.POP3Command;
import com.tmail.server.POP3Session;

/**
 * Created by tobin on 11/14/14.
 */
public class UserPOP3Command extends POP3Command {
    @Override
    public void execute(String command, POP3Session session) {
        String[] parts = command.split(" ");
        if (parts.length > 1) {
            String username = parts[1];
            session.setUsername(username);
            session.response("+OK core mail");
        } else {
            session.response("-ERR username not valid");
        }

    }
}
