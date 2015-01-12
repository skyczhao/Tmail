package com.tmail.utils;

import com.tmail.command.*;
import com.tmail.base.SMTPCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tobin on 11/1/14.
 *
 * mapping the command string to command object
 */
public class SMTPCommandCollector {
    public final static Map<String, SMTPCommand> commandMap = new HashMap<String, SMTPCommand>();

    /**
     * all the SMTP command mapping in here
     *
     */
    public void setCommandMap() {
        // TODO: remove hard coding
        this.commandMap.put("EHLO", new EhloSMTPCommand());
        this.commandMap.put("AUTH", new AuthSMTPCommand());
        this.commandMap.put("MAIL", new MailSMTPCommand());
        this.commandMap.put("RCPT", new RcptSMTPCommand());
        this.commandMap.put("DATA", new DataSMTPCommand());
        this.commandMap.put("QUIT", new QuitSMTPCommand());
    }

    /**
     * access to object properties
     *
     * @return command map
     */
    public Map<String, SMTPCommand> getCommandMap() {
        if (this.commandMap.isEmpty())
            this.setCommandMap();
        return this.commandMap;
    }
}
