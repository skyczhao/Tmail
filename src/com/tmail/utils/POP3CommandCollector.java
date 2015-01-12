package com.tmail.utils;

import com.tmail.command.*;
import com.tmail.base.POP3Command;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tobin on 11/13/14.
 */
public class POP3CommandCollector {
    public final static Map<String, POP3Command> commandMap = new HashMap<String, POP3Command>();

    /**
     * set the POP3 command mapping here
     *
     */
    public void setCommandMap() {
        // TODO: remove hard coding
        this.commandMap.put("USER", new UserPOP3Command());
        this.commandMap.put("PASS", new PassPOP3Command());
        this.commandMap.put("NOOP", new NoopPOP3Command());
        this.commandMap.put("STAT", new StatPOP3Command());
        this.commandMap.put("LIST", new ListPOP3Command());
        this.commandMap.put("DELE", new DelePOP3Command());
        this.commandMap.put("RETR", new RetrPOP3Command());
        this.commandMap.put("QUIT", new QuitPOP3Command());
    }

    /**
     * access to the POP3 command mapping
     *
     * @return command mapping
     */
    public Map<String, POP3Command> getCommandMap() {
        if (this.commandMap.isEmpty())
            this.setCommandMap();
        return this.commandMap;
    }
}
