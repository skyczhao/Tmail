package com.tmail.tests;

import com.tmail.base.MailServer;
import com.tmail.server.POP3Server;
import com.tmail.server.SMTPServer;
import com.tmail.utils.MailServerThread;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by tobin on 1/5/15.
 */
public class serverClassTest {
    @Test
    public void testGetPort() {
        // test smtp server
        SMTPServer smtp = new SMTPServer(1200);
        assertEquals(1200, smtp.getPort());

        // test pop3 server
        POP3Server pop3 = new POP3Server(1201);
        assertEquals(1201, pop3.getPort());
    }

    @Test
    public void testTimeout() {
        // base class
        MailServer server = new SMTPServer(1200);
        // set timeout
        server.setTimeout(100);
        // test
        assertEquals(100, server.getTimeout());
    }

    @Test
    public void testMaxConnection() {
        // base class
        MailServer server = new POP3Server(1200);
        // max connection
        server.setMaxConnection(100);
        // test
        assertEquals(100, server.getMaxConnection());
    }

    @Test
    public void testServerRun() {
        // base class
        MailServer server = new SMTPServer(1200);

        // check
        assertFalse(server.isRunning());
    }
}
