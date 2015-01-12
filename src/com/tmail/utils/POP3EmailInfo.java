package com.tmail.utils;


/**
 * Created by tobin on 12/15/14.
 */
public class POP3EmailInfo {
    // mark the email whether should be deleted
    private boolean flag;
    // simple email info
    private int id;
    private int size;

    /**
     * constructor with default parameter
     *
     * @param id
     * @param size
     */
    public POP3EmailInfo(int id, int size) {
        this.id = id;
        this.size = size;
        this.flag = false;
    }

    /**
     * get the delete flag
     *
     * @return flag
     */
    public boolean getFlag() {
        return this.flag;
    }

    /**
     * set the delete flag
     *
     */
    public synchronized void setFlag() {
        this.flag = true;
    }

    public int getId() {
        return this.id;
    }

    public int getSize() {
        return this.size;
    }
}
