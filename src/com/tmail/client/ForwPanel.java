package com.tmail.client;

import com.tmail.utils.ClientEmail;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by tobin on 1/1/15.
 */
public class ForwPanel extends JPanel {
    ClientFrame frame;

    JTextField to = new JTextField();
    JTextField subject = new JTextField();
    JTextArea content = new JTextArea();

    JButton send = new JButton();
    JButton back = new JButton();

    /**
     * set button listener
     *
     */
    private void setListener() {
        // send click
        this.send.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                String receiver = to.getText().trim();
                String title = subject.getText().trim();
                String main = content.getText().trim();
                frame.sendTMail(receiver, title, main);

                frame.switchToMain();
            }
        });

        // back click
        this.back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                frame.switchToMain();
            }
        });
    }

    /**
     * init the forward window
     *
     */
    private void init() {
        // init position
        int begin = 10;
        int height = 20;
        int margin = 10;
        int next = height + margin;

        // title
        JPanel titPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel title = new JLabel("Forward TMail");
        titPane.add(title);
        this.add(titPane, BorderLayout.NORTH);

        // mail
        JPanel mailPane = new JPanel();
        mailPane.setLayout(null);
        // to label
        JLabel toLabel = new JLabel("To:");
        toLabel.setLabelFor(this.to);
        toLabel.setBounds(20, begin, 80, height);
        // to input
        this.to.setBounds(100, begin, 480, height);
//        this.to.setEnabled(false);
        mailPane.add(toLabel);
        mailPane.add(this.to);

        // subject label
        JLabel subLabel = new JLabel("Subject:");
        subLabel.setLabelFor(this.subject);
        subLabel.setBounds(20, begin + next, 80, height);
        // subject input
        this.subject.setBounds(100, begin + next, 480, height);
//        this.subject.setEnabled(false);
        mailPane.add(subLabel);
        mailPane.add(this.subject);

        // content label
        JLabel conLabel = new JLabel("Content:");
        conLabel.setLabelFor(this.content);
        conLabel.setBounds(20, begin + 2 * next, 80, height);
        // content input
        this.content.setLineWrap(true);
        this.content.setWrapStyleWord(true);
        JScrollPane conScroll = new JScrollPane(this.content);
        conScroll.setBounds(100, begin + 2 * next, 480, 24 * height);
        mailPane.add(conLabel);
        mailPane.add(conScroll);
        this.add(mailPane, BorderLayout.CENTER);

        // control button
        this.send.setText("Send");
        this.back.setText("Back");
        JPanel ctlPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ctlPane.add(this.send);
        ctlPane.add(this.back);
        this.add(ctlPane, BorderLayout.SOUTH);

        // set listener
        this.setListener();
    }

    /**
     * construct forward panel by outer frame
     *
     * @param frame
     */
    public ForwPanel(ClientFrame frame) {
        super(new BorderLayout());

        // init outer container
        this.frame = frame;
        // init window
        this.init();
    }

    /**
     * reset current panel by email and reset outer frame
     *
     * @param email
     */
    public void clear(ClientEmail email) {
        // width: 600 & height: 650
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int screenWidth = (int)toolkit.getScreenSize().getWidth();
        int screenHeight = (int)toolkit.getScreenSize().getHeight();
        int frameWidth = 600;
        int frameHeight = 650;
        this.frame.setSize(frameWidth, frameHeight);
//        this.frame.setResizable(false);
        this.frame.setLocation((screenWidth - frameWidth) / 2, (screenHeight - frameHeight) / 2);
        this.frame.validate();

        // clear
        this.to.setText("");
        this.subject.setText("Fw. " + email.subject);
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n\n\n");
        buffer.append("---\n");
        buffer.append("from\t: %s\n");
        buffer.append("to\t: %s\n");
        buffer.append("subject\t: %s\n");
        buffer.append("%s");
        String format = buffer.toString();
        String article = String.format(format, email.from, email.to, email.subject, email.content);
        this.content.setText(article);
    }
}
