package com.tmail.utils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by tobin on 12/22/14.
 */
public class PrintStreamUtil extends PrintStream {
    private JTextComponent text;
    private StringBuffer buffer;

    // print on windows
    public PrintStreamUtil(OutputStream out, JTextComponent text) {
        super(out);
        this.text = text;
        this.buffer = new StringBuffer();
    }

    /**
     * override super.write
     * to print on text windows
     *
     * @param buf
     * @param off
     * @param len
     */
    @Override
    public void write(byte[] buf, int off, int len) {
        // message container
        final String message = new String(buf, off, len);
        // print on text windows
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // refresh buffer
                buffer.append(message);
                // set text
                text.setText(buffer.toString());
            }
        });
    }
}
