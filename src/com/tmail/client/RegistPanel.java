package com.tmail.client;

import com.tmail.dao.DataAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by tobin on 12/24/14.
 */
public class RegistPanel extends JPanel {

    // outer container
    ClientFrame frame;

    // login information
    JTextField username = new JTextField();
    JPasswordField password = new JPasswordField();
    JButton registBut = new JButton();
    JButton backBut = new JButton();
    JLabel message = new JLabel();

    /**
     * set button listener
     *
     */
    private void setListener() {
        // regist click
        this.registBut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                String user = username.getText();
                String pass = new String(password.getPassword());

                // TODO: replace ugly register
                DataAction da = new DataAction();
                Statement stm = da.getStatement();
                // TODO: safety?
                String sql = da.registerSQL(user, pass);
                if (stm != null) {
                    try {
                        int count = stm.executeUpdate(sql);
                        if (count == 1) {
                            frame.switchToLogin();
                        } else {
                            message.setText("Unexpected error!");
                        }
                    } catch (SQLException except) {
                        message.setText("This username has been used!");
                    }
                }
                // return or show message
            }
        });

        // back click
        this.backBut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                frame.switchToLogin();
            }
        });
    }

    /**
     * init the register window
     *
     */
    private void init() {
        // init position
        int begin = 10;
        int height = 20;
        int margin = 10;
        int next = height + margin;

        // set title
        JPanel titPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel title = new JLabel("Sign up TMail");
//        title.setHorizontalAlignment(SwingConstants.CENTER);
//        title.setVerticalAlignment(SwingConstants.TOP);
        titPane.add(title);
        this.add(titPane, BorderLayout.NORTH);

        // main
        JPanel mainPane = new JPanel();
        mainPane.setLayout(null);
        // set username label
        JLabel userLabel = new JLabel("Username:");
        userLabel.setLabelFor(this.username);
        userLabel.setBounds(30, begin, 90, height);
        // set username input
        this.username.setBounds(120, begin, 150, height);
        mainPane.add(userLabel);
        mainPane.add(this.username);

        // set password label
        JLabel passLabel = new JLabel("Password:");
        passLabel.setLabelFor(this.password);
        passLabel.setBounds(30, begin + next, 90, height);
        // set password input
        this.password.setBounds(120, begin + next, 150, height);
        mainPane.add(passLabel);
        mainPane.add(this.password);

        // control panel
        JPanel ctlPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.registBut.setText("Sign Up");
        this.backBut.setText("Back");
        ctlPane.add(this.registBut);
        ctlPane.add(this.backBut);
        ctlPane.setBounds(30, begin + 2 * next, 240, 2 * height);
        mainPane.add(ctlPane);
        this.add(mainPane, BorderLayout.CENTER);

        // message
        JPanel msgPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.message.setText("Please input username & password");
        this.message.setForeground(Color.RED);
        msgPane.add(this.message);
        this.add(msgPane, BorderLayout.SOUTH);

        // set listener
        this.setListener();
    }

    /**
     * init register panel
     *
     * @param frame
     */
    public RegistPanel(ClientFrame frame) {
        super(new BorderLayout());

        // init outer container
        this.frame = frame;
        // init window
        this.init();
    }

    /**
     * reset register panel and reset outer frame
     *
     */
    public void clear() {
        // width: 300 & height: 190
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int screenWidth = (int)toolkit.getScreenSize().getWidth();
        int screenHeight = (int)toolkit.getScreenSize().getHeight();
        int frameWidth = 300;
        int frameHeight = 190;
        this.frame.setSize(frameWidth, frameHeight);
//        this.frame.setResizable(false);
        this.frame.setLocation((screenWidth - frameWidth) / 2, (screenHeight - frameHeight) / 2);
        this.frame.validate();

        // clear pane
        this.username.setText("");
        this.password.setText("");
        this.message.setText("Please input username & password");
    }
}
