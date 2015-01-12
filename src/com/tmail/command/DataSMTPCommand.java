package com.tmail.command;

import com.tmail.base.SMTPCommand;
import com.tmail.server.SMTPSession;

import java.io.IOException;

/**
 * Created by tobin on 11/6/14.
 */
public class DataSMTPCommand extends SMTPCommand {
    @Override
    public void execute(String command, SMTPSession session){
        // check auth
        if (!session.getAuth()) {
            session.response("553 You are not authorized to send mail.");
            return;
        }

        String sender = session.getSender();
        String receiver = session.getReceiver();
        String line, from, to, subject, data;
        String[] parts;

        // check more
        if (sender == null || sender.length() == 0) {
            session.response("503 Bad sequence of commands");
            return;
        }
        if (receiver == null || receiver.length() == 0) {
            session.response("503 Bad sequence of commands");
            return;
        }

        // ready
        session.response("354 Start mail input, end with <CRLF>.<CRLF>");

        // read mail data
        try {
            // get from
            line = session.receiveLine();
            parts = line.split(":");
            if (parts.length < 2) {
                session.response("501 Syntax error in parameters or arguments");
                return;
            }
            from = parts[1].trim();

            // get to
            line = session.receiveLine();
            parts = line.split(":");
            if (parts.length < 2) {
                session.response("501 Syntax error in parameters or arguments");
                return;
            }
            to = parts[1].trim();

            // get subject, skip <CRLF><CRLF>
            boolean mark = false;
            StringBuffer subjectBuf = new StringBuffer();
            while ((line = session.receiveLine()) != null) {
                if (!mark) {
                    if (line.isEmpty())
                        mark = true;
                    else
                        subjectBuf.append(line);
                } else {
                    // <CRLF> has been input
                    if (line.isEmpty())
                        break;
                    subjectBuf.append(line);
                    mark = false;
                }
            }
            parts = subjectBuf.toString().split(":");
            if (parts.length < 2) {
                session.response("501 Syntax error in parameters or arguments");
                return;
            }
            subject = parts[1].trim();

            // get data
            StringBuffer dataBuf = new StringBuffer();
            while ((line = session.receiveLine()) != null) {
                if (line.length() == 1 && line.equals("."))
                    break;
                dataBuf.append(line + "\n");
            }
            data = dataBuf.toString().trim();

            // send email
            mark = session.sendEmail(from, to, subject, data);

            // send email successfully
            if (mark)
                session.response("250 OK");
            else
                session.response("550 Send email fail!");
        } catch (IOException e) {
            System.out.println("Error: " + e);
            session.response("502 Can't handle command!");
        }
    }
}
