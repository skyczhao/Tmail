package com.tmail.utils;

/**
 * Created by tobin on 12/31/14.
 */
public class ClientEmail {
    public int index;
    public String from;
    public String to;
    public String subject;
    public String content;

    @Override
    public String toString() {
        return this.subject;
    }
}
