package com.tmail.command;

import com.tmail.base.POP3Command;
import com.tmail.server.POP3Session;

/**
 * Created by tobin on 11/14/14.
 */
public class PassPOP3Command extends POP3Command {
    @Override
    public void execute(String command, POP3Session session) {
        // check username
        String username = session.getUsername();
        if (username == null || username.length() == 0) {
            session.response("-ERR Command not valid in this state");
            return;
        }

        // check password
        String[] parts = command.split(" ");
        if (parts.length > 1) {
            String password = parts[1];
            session.setAuth(password);
            if (session.getAuth()) {
                session.response("+OK log on successfully");
            } else {
                session.response("-ERR fail to log on");
            }
        } else {
            session.response("-ERR Unable to log on");
        }
    }
}
