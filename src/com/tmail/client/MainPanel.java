package com.tmail.client;

import com.tmail.utils.ClientEmail;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by tobin on 12/24/14.
 */
public class MainPanel extends JPanel {

    // outer container
    ClientFrame frame;

    // main panel
    JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JList inbox = new JList();
    // each mail
    JTextArea mailContent = new JTextArea();
    JButton mailDelete = new JButton();
    JButton mailReply = new JButton();
    JButton mailForward = new JButton();

    // control button
    JButton write = new JButton();
    JButton exit = new JButton();

    /**
     * upgrade inbox list
     *
     */
    private void loadTMail() {
        LinkedList<ClientEmail> emails = this.frame.loadTMail();
        this.inbox.setListData(emails.toArray());
    }

    /**
     * set button listener
     *
     */
    private void setListener() {
        // write click
        this.write.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                frame.switchToMail();
            }
        });

        // exit click
        this.exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                frame.switchToLogin();
            }
        });

        // list click
        this.inbox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                // update email content window
                ClientEmail email = (ClientEmail)inbox.getSelectedValue();
                StringBuffer buffer = new StringBuffer();
                buffer.append("FROM\t: %s\n");
                buffer.append("TO\t: %s\n");
                buffer.append("SUBJECT\t: %s\n");
                buffer.append("CONTENT\t: \n%s");
                String format = buffer.toString();
                String content = String.format(format, email.from, email.to, email.subject, email.content);
                mailContent.setText(content);
            }
        });

        // delete click
        this.mailDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                ClientEmail email = (ClientEmail)inbox.getSelectedValue();
                if (email != null) {
                    frame.deleteTMail(email.index);
                    clear();
                }
            }
        });

        // reply click
        this.mailReply.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                ClientEmail email = (ClientEmail)inbox.getSelectedValue();
                if (email != null) {
                    frame.switchToReply(email);
                }
            }
        });

        // forward click
        this.mailForward.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                ClientEmail email = (ClientEmail)inbox.getSelectedValue();
                if (email != null) {
                    frame.switchToForward(email);
                }
            }
        });
    }

    /**
     * init the main user window
     *
     */
    private void init() {
        // title
        JPanel titPane =  new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel title = new JLabel("Welcome to TMail!");
        titPane.add(title);
        this.add(titPane, BorderLayout.NORTH);

        // main panel
        // mail title
        JScrollPane mailsPane = new JScrollPane(this.inbox);
        mailsPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.mainPane.setLeftComponent(mailsPane);

        // each mail
        JPanel eachMail = new JPanel(new BorderLayout());
        // mail control
        this.mailReply.setText("Reply");
        this.mailForward.setText("Forward");
        this.mailDelete.setText("Delete");
        JPanel mailControl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        mailControl.add(mailReply);
        mailControl.add(mailForward);
        mailControl.add(mailDelete);
        eachMail.add(mailControl, BorderLayout.NORTH);
        // mail content
        this.mailContent.setEnabled(false);
        this.mailContent.setDisabledTextColor(Color.BLACK);
        JScrollPane conPane = new JScrollPane(this.mailContent);
        conPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        eachMail.add(conPane, BorderLayout.CENTER);
        this.mainPane.setRightComponent(eachMail);
        this.add(this.mainPane, BorderLayout.CENTER);

        // control panel
        JPanel ctlPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.write.setText("Write");
        this.exit.setText("Exit");
        ctlPane.add(this.write);
        ctlPane.add(this.exit);
        this.add(ctlPane, BorderLayout.SOUTH);

        // set listener
        this.setListener();
    }

    /**
     * init main user window by outer frame
     *
     * @param frame
     */
    public MainPanel(ClientFrame frame) {
        super(new BorderLayout());

        // init outer container
        this.frame = frame;
        // init window
        this.init();
    }

    /**
     * reset current panel and reset outer frame
     *
     */
    public void clear() {
        // width: 600 & height: 600
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int screenWidth = (int)toolkit.getScreenSize().getWidth();
        int screenHeight = (int)toolkit.getScreenSize().getHeight();
        int frameWidth = 600;
        int frameHeight = 600;
        this.frame.setSize(frameWidth, frameHeight);
//        this.frame.setResizable(true);
        this.frame.setLocation((screenWidth - frameWidth) / 2, (screenHeight - frameHeight) / 2);
        this.frame.validate();

        // load emails
        this.mainPane.setDividerLocation(0.3);
        this.mailContent.setText("Please select an email from left panel!");
        this.loadTMail();
    }
}
