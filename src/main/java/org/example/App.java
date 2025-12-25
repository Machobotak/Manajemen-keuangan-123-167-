package org.example;


import org.example.ui.AuthFrame;

import javax.swing.*;

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
