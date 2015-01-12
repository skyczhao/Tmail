package com.tmail.tests;

import com.tmail.base.MailServer;
import com.tmail.base.MailSession;
import com.tmail.server.SMTPServer;
import com.tmail.server.SMTPSession;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by tobin on 1/6/15.
 */
public class serverSessionTest {

    @Test
    public void testSession() {
        // session constructor
        MailSession session = new SMTPSession(new SMTPServer(2000), new Socket());

        assertFalse(session.isRunning());
    }

    @Test
    public void testGetServer() {
        // session constructor
        MailSession session = new SMTPSession(new SMTPServer(2000), new Socket());

        MailServer server = session.getServer();

        assertEquals(2000, server.getPort());
    }
}
