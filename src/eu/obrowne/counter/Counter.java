package eu.obrowne.counter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Counter extends JFrame{
    private int count = 0;

    public Counter() {
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        final var button = new JButton("Count");
        final var label = new JTextField(8);
        label.setText("0");
        label.setEditable(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                count += 1;
                label.setText("" + count);
            }
        });
        getContentPane().add(label);
        getContentPane().add(button);
        pack();
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
	    new Counter();
    }
}
