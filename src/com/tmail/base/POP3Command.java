package com.tmail.base;

import com.tmail.server.POP3Session;

/**
 * Created by tobin on 11/13/14.
 */
public abstract class POP3Command {

    public abstract void execute(String command, POP3Session session) ;
}
