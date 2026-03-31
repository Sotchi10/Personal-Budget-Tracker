package com.budgettracker.gui;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Personal Budget Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null); 

        setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}