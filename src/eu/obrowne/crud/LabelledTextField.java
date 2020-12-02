package eu.obrowne.crud;

import javax.swing.*;
import java.awt.*;

public class LabelledTextField extends JPanel {
    public JTextField field;
    public JLabel label;

    public LabelledTextField(String labelVal, String initVal) {
        super();
        setLayout(new BorderLayout());
        label = new JLabel(labelVal);
        field = new JTextField(initVal);
        add(label, BorderLayout.WEST);
        add(field, BorderLayout.CENTER);
        setMinimumSize(getSize());
    }

}
