package com.link_intersystems.swing.text;


import javax.swing.*;
import java.awt.*;


public class LineNumberViewport extends JComponent {

    public static void main(String[] args) {
        JFrame test = new JFrame("Test");
        test.setSize(600, 600);
        Container contentPane = test.getContentPane();

        JTextArea jTextArea = new JEditorPane();
        jTextArea.setText("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt\r\n ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero\n eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren\n, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit\n amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat,\n sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no\n sea takimata sanctus est Lorem ipsum dolor sit amet.");
        jTextArea.setLineWrap(true);
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        contentPane.add(jScrollPane);
        jScrollPane.setViewportView(jTextArea);

        test.setVisible(true);

    }


}