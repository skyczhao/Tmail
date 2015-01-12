package com.tmail.base;

import com.tmail.server.SMTPSession;

import java.net.Socket;

/**
 * Created by tobin on 11/1/14.
 */
public abstract class SMTPCommand {

    public abstract void execute(String command, SMTPSession session);
}
