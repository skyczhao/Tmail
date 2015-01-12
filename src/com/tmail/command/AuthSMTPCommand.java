package com.tmail.command;

import com.tmail.base.SMTPCommand;
import com.tmail.server.SMTPSession;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * Created by tobin on 11/6/14.
 */
public class AuthSMTPCommand extends SMTPCommand {

    @Override
    public void execute(String command, SMTPSession session) {
        BASE64Encoder encoder = new BASE64Encoder();
        BASE64Decoder decoder = new BASE64Decoder();
        String message, line;

        try {
            // receive username
            message = "334 " + encoder.encode("Username:".getBytes());
            session.response(message);
            line = session.receiveLine();
            String username = new String(decoder.decodeBuffer(line));

            // receive password
            message = "334 " + encoder.encode("Password:".getBytes());
            session.response(message);
            line = session.receiveLine();
            String password = new String(decoder.decodeBuffer(line));

            // auth
            session.authUser(username, password);
            if (session.getAuth())
                message = "235 OK, go ahead";
            else
                message = "535 authentication failed";
            session.response(message);
        } catch (IOException e) {
            System.out.println("Error: " + e);
            session.response("502 Can't handle command!");
        }
    }
}
