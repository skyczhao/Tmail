package com.tmail.client;

import com.tmail.conf.ServerSettings;
import com.tmail.utils.ClientEmail;
import sun.misc.BASE64Encoder;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

/**
 * Created by tobin on 12/22/14.
 */
public class ClientFrame extends JFrame {

    private String username;
    private String password;

    private LoginPanel login;
    private RegistPanel regist;
    private MainPanel main;
    private MailPanel mail;
    private RcptPanel rcpt;
    private ForwPanel forw;

    /**
     * init the frame components
     * including the different stages of panel
     *
     */
    private void init() {
        this.login = new LoginPanel(this);
        this.regist = new RegistPanel(this);
        this.main = new MainPanel(this);
        this.mail = new MailPanel(this);
        this.rcpt = new RcptPanel(this);
        this.forw = new ForwPanel(this);
    }

    /**
     * constructor
     * the whole user interface frame
     *
     */
    public ClientFrame() {
        super();

        this.init();

        // get frame container
        Container container = this.getContentPane();

        // switch to the login panel
        container.removeAll();
        container.add(this.login, BorderLayout.CENTER);
        this.login.clear();
    }

    /**
     * select login panel
     *
     */
    public void switchToLogin() {
        // get frame container
        Container container = this.getContentPane();

        // switch to the login panel
        container.removeAll();
        this.invalidate();
        container.add(this.login, BorderLayout.CENTER);
        this.login.clear();
        this.repaint();
        this.setVisible(true);
    }

    /**
     * select main panel
     *
     */
    public void switchToMain() {
        // get frame container
        Container container = this.getContentPane();

        // switch to main panel
        container.removeAll();
        this.invalidate();
        container.add(this.main, BorderLayout.CENTER);
        this.main.clear();
        this.repaint();
        this.setVisible(true);
    }

    /**
     * select register panel
     *
     */
    public void switchToRegist() {
        // get frame container
        Container container = this.getContentPane();

        // switch to regist panel
        container.removeAll();
        this.invalidate();
        container.add(this.regist, BorderLayout.CENTER);
        this.regist.clear();
        this.repaint();
        this.setVisible(true);
    }

    /**
     * write a normal mail
     *
     */
    public void switchToMail() {
        // get frame container
        Container container = this.getContentPane();

        // switch to mail panel
        container.removeAll();
        this.invalidate();
        container.add(this.mail, BorderLayout.CENTER);
        this.mail.clear();
        this.repaint();
        this.setVisible(true);
    }

    /**
     * reply an email
     *
     * @param email
     */
    public void switchToReply(ClientEmail email) {
        // get frame container
        Container container = this.getContentPane();

        // switch to mail panel
        container.removeAll();
        this.invalidate();
        container.add(this.rcpt, BorderLayout.CENTER);
        this.rcpt.clear(email);
        this.repaint();
        this.setVisible(true);
    }

    /**
     * forward an email
     *
     * @param email
     */
    public void switchToForward(ClientEmail email) {
        // get frame container
        Container container = this.getContentPane();

        // switch to mail panel
        container.removeAll();
        this.invalidate();
        container.add(this.forw, BorderLayout.CENTER);
        this.forw.clear(email);
        this.repaint();
        this.setVisible(true);
    }

    /**
     * login into the system
     *
     * @param username
     * @param password
     * @return success of fail
     */
    public boolean loginTMail(String username, String password) {
        // handle empty error
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            return false;
        }

        String code = "535";
        try {
            // connect to server
            Socket socket = new Socket(ServerSettings.serverAddress, ServerSettings.smtpPort);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            String line = null;

            // communication
            reader.readLine();
            line = "AUTH LOGIN";
            line += "\r\n";
            writer.print(line);
            writer.flush();
            reader.readLine();

            // send username
            line = new BASE64Encoder().encode(username.getBytes());
            line += "\r\n";
            writer.print(line);
            writer.flush();
            reader.readLine();

            // send password
            // TODO: handle empty password!
            line = new BASE64Encoder().encode(password.getBytes());
            line += "\r\n";
            writer.print(line);
            writer.flush();
            // get authenticate state
            line = reader.readLine();
            code = line.split(" ")[0];

            // quit
            line = "QUIT";
            line += "\r\n";
            writer.print(line);
            writer.flush();
            reader.readLine();

        } catch (UnknownHostException uhe) {
            System.out.println(uhe);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

        // judgement
        if (code.equals("235")) {
            this.username = username;
            this.password = password;
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * loading the user email as inbox
     *
     * @return
     */
    public LinkedList<ClientEmail> loadTMail() {
        LinkedList<ClientEmail> emails = new LinkedList<ClientEmail>();
        try {
            // connect to pop3 server
            Socket socket = new Socket(ServerSettings.serverAddress, ServerSettings.pop3Port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            String line = null;

            // login
            reader.readLine();
            line = "USER " + this.username;
            line += "\r\n";
            writer.print(line);
            writer.flush();
            reader.readLine();

            line = "PASS " + this.password;
            line += "\r\n";
            writer.print(line);
            writer.flush();
            reader.readLine();

            // get state
            line = "STAT";
            line += "\r\n";
            writer.print(line);
            writer.flush();
            line = reader.readLine();
            String number = line.split(" ")[1];
            int num = Integer.parseInt(number);

            // get detail
            for (int idx = 1; idx <= num; idx++) {
                boolean flag = true;
                ClientEmail email = new ClientEmail();

                // get email
                line = "RETR " + idx;
                line += "\r\n";
                writer.print(line);
                writer.flush();
                // read data
                email.index = idx;
                while (flag) {
                    line = reader.readLine();
                    String[] items = line.split(":");
                    // get from
                    if (items[0].equals("From")) {
                        email.from = items[1].trim();
                    }
                    // get to
                    if (items[0].equals("To")) {
                        email.to = items[1].trim();
                    }
                    // get subject & content
                    if (items[0].equals("Subject")) {
                        email.subject = items[1].trim();
                        // expand subject
                        while (true) {
                            line = reader.readLine();

                            if (line.isEmpty()) {
                                break;
                            }
                            email.subject = email.subject + "\n" + line;
                        }
                        // TODO: handle empty line of subject
                        reader.readLine();

                        // content
                        email.content = "";
                        while (true) {
                            line = reader.readLine();
                            if (line.equals(".")) {
                                flag = false;
                                break;
                            }
                            email.content = email.content+ line + "\n" ;
                        }
                    }
                    // end
                }
                // add into list
                emails.add(email);
            }

            // quit
            line = "QUIT";
            line += "\r\n";
            writer.print(line);
            writer.flush();
            reader.readLine();

        } catch (UnknownHostException uhe) {
            System.out.println(uhe);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
        return emails;
    }

    /**
     * delete specified email
     *
     * @param index
     */
    public void deleteTMail(int index) {
        try {
            // connect to pop3 server
            Socket socket = new Socket(ServerSettings.serverAddress, ServerSettings.pop3Port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            String line = null;

            // login
            reader.readLine();
            line = "USER " + this.username;
            line += "\r\n";
            writer.print(line);
            writer.flush();
            reader.readLine();

            line = "PASS " + this.password;
            line += "\r\n";
            writer.print(line);
            writer.flush();
            reader.readLine();

            // delete
            line = "DELE " + index;
            line += "\r\n";
            writer.print(line);
            writer.flush();
            line = reader.readLine();

            // quit
            line = "QUIT";
            line += "\r\n";
            writer.print(line);
            writer.flush();
            reader.readLine();
        } catch (UnknownHostException uhe) {
            System.out.println(uhe);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    /**
     * send an email
     *
     * @param to
     * @param subject
     * @param content
     */
    public void sendTMail(String to, String subject, String content) {
        try {
            // connect to server
            Socket socket = new Socket(ServerSettings.serverAddress, ServerSettings.smtpPort);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            String line = null;
            String format = null;

            // communication
            reader.readLine();
            line = "AUTH LOGIN";
            line += "\r\n";
            writer.print(line);
            writer.flush();
            reader.readLine();

            // send username
            line = new BASE64Encoder().encode(username.getBytes());
            line += "\r\n";
            writer.print(line);
            writer.flush();
            reader.readLine();

            // send password
            // TODO: handle empty password!
            line = new BASE64Encoder().encode(password.getBytes());
            line += "\r\n";
            writer.print(line);
            writer.flush();
            // get authenticate state
            reader.readLine();

            // sending
            // from
            format = "MAIL FROM: <%s@%s>";
            line = String.format(format, this.username, ServerSettings.serverAddress);
            line += "\r\n";
            writer.print(line);
            writer.flush();
            reader.readLine();

            // to
            format = "RCPT TO: <%s@%s>";
            line = String.format(format, to, ServerSettings.serverAddress);
            line += "\r\n";
            writer.print(line);
            writer.flush();
            reader.readLine();

            // data
            line = "DATA";
            line += "\r\n";
            writer.print(line);
            writer.flush();
            reader.readLine();

            // email content
            format = "FROM: %s@%s";
            line = String.format(format, this.username, ServerSettings.serverAddress);
            line += "\r\n";
            writer.print(line);
            writer.flush();
            format = "TO: %s@%s";
            line = String.format(format, to, ServerSettings.serverAddress);
            line += "\r\n";
            writer.print(line);
            writer.flush();
            line = "SUBJECT: " + subject;
            line += "\r\n";
            writer.print(line);
            writer.flush();
            // crlf indirectly, or you can use it directly
            line = "\r\n";
            line += "\r\n";
            writer.print(line);
            writer.flush();
            // main content
            line = content;
            line += "\r\n";
            writer.print(line);
            writer.flush();
            // end
            line = ".";
            line += "\r\n";
            writer.print(line);
            writer.flush();
            reader.readLine();

            // quit
            line = "QUIT";
            line += "\r\n";
            writer.print(line);
            writer.flush();

        } catch (UnknownHostException uhe) {
            System.out.println(uhe);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}
