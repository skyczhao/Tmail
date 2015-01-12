package com.tmail;

import com.tmail.client.ClientFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by tobin on 12/22/14.
 */
public class clientWindow {

    public static void main(String[] args) {
        ClientFrame cf = new ClientFrame();

        cf.setTitle("TMail Client");
        cf.setVisible(true);
        cf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
