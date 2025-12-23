package org.example;

import org.example.Login.Session;
import org.example.Login.UserService;
import org.example.ui.AuthFrame;

import javax.swing.*;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(() -> new AuthFrame().setVisible(true));
    }
}
