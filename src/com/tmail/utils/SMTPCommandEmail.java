package com.tmail.utils;

/**
 * Created by tobin on 11/6/14.
 */
public class SMTPCommandEmail {
    public static String getEmailAddress(String command) {
        int start = command.indexOf("<");
        int end = command.indexOf(">");

        if (start == -1 || end == -1)
            return null;
        else
            return command.substring(start + 1, end).trim();
    }
}
