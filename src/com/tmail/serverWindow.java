package com.tmail;

import com.tmail.conf.ServerSettings;
import com.tmail.server.POP3Server;
import com.tmail.server.SMTPServer;
import com.tmail.utils.MailServerThread;
import com.tmail.utils.PrintStreamUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

/**
 * Created by tobin on 12/22/14.
 */
public class serverWindow {
    public static void main(String[] args) {
        // set frame name
        String frameName = "TMail控制器";
        if (args.length > 0)
            frameName = args[0];

        // server threads
        final MailServerThread[] smtpThread = new MailServerThread[1];
        final MailServerThread[] pop3Thread = new MailServerThread[1];

        // global frame
        JFrame frame = new JFrame(frameName);

        // split the window into control pane and the output area
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setEnabled(false);

        // output area
        JTextArea textArea = new JTextArea(3, 20);
        textArea.setEnabled(false);
        // set content
        PrintStreamUtil psu = new PrintStreamUtil(System.out, textArea);
        System.setOut(psu);
        // put into split pane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        splitPane.setBottomComponent(scrollPane);

        // control pane
        JPanel ctlPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final JButton startBut = new JButton("启动");
        final JButton stopBut = new JButton("停止");
        startBut.setEnabled(true);
        stopBut.setEnabled(false);
        // set callback
        startBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                smtpThread[0] = new MailServerThread(new SMTPServer(ServerSettings.smtpPort));
                pop3Thread[0] = new MailServerThread(new POP3Server(ServerSettings.pop3Port));

                smtpThread[0].start();
                pop3Thread[0].start();

                startBut.setEnabled(false);
                stopBut.setEnabled(true);
                System.out.println("Server start: 1026/1027!");
            }
        });
        stopBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                smtpThread[0].stopServer();
                pop3Thread[0].stopServer();

                startBut.setEnabled(true);
                stopBut.setEnabled(false);
                System.out.println("Server stop!");
            }
        });
        // put into split pane
        ctlPane.add(startBut);
        ctlPane.add(stopBut);
        splitPane.setTopComponent(ctlPane);

        // frame setting
        frame.setContentPane(splitPane);
        // TODO: stop all threads before exit!
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
