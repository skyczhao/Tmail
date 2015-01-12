package com.tmail.utils;


import com.tmail.base.MailSession;

/**
 * Created by tobin on 11/12/14.
 */
public class MailSessionThread extends Thread {
    private MailSession session;

    /**
     * constructor
     *
     * @param session
     */
    public MailSessionThread(MailSession session) {
        this.session = session;
    }

    /**
     * run session under thread
     *
     */
    @Override
    public void run() {
        session.run();
    }
}
