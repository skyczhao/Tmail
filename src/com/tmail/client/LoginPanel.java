package com.tmail.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by tobin on 12/24/14.
 */
public class LoginPanel extends JPanel {

    // outer container
    ClientFrame frame;

    // login information
    JTextField username = new JTextField();
    JPasswordField password = new JPasswordField();
    JButton loginBut = new JButton();
    JButton registBut = new JButton();
    JLabel message = new JLabel();

    /**
     * login system
     *
     */
    private void login() {
        String user = username.getText();
        String pass = new String(password.getPassword());

        // check authentication
        boolean flag = frame.loginTMail(user, pass);
        if (flag) {
            frame.switchToMain();
        }
        else {
            message.setText("Password error!");
        }
    }

    /**
     * set the button listener and keyboard listener
     *
     */
    private void setListener() {
        // login click
        this.loginBut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                login();
            }
        });

        // regist click
        this.registBut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                frame.switchToRegist();
            }
        });

        // username enter to input password
        this.username.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    password.requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        // password enter to login
        this.password.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                    loginBut.doClick();
                    login();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    /**
     * init the login window
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
        JLabel title = new JLabel("Login into TMail");
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
        // login & register button
        this.loginBut.setText("Log In");
        this.registBut.setText("Sign Up");
        ctlPane.add(this.loginBut);
        ctlPane.add(this.registBut);
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
     * init login panel by outer frame
     *
     * @param frame
     */
    public LoginPanel(ClientFrame frame) {
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
