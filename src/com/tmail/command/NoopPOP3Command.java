package com.tmail.command;

import com.tmail.base.POP3Command;
import com.tmail.server.POP3Session;

/**
 * Created by tobin on 12/15/14.
 */
public class NoopPOP3Command extends POP3Command {
    @Override
    public void execute(String command, POP3Session session) {
        session.response("+OK core mail");
    }
}
