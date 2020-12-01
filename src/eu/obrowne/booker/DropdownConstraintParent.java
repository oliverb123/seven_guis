package eu.obrowne.booker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DropdownConstraintParent extends JComboBox<String> implements ExternalConstraintParent<String>, ActionListener {
    private List<ExternalConstraintChild<String>> children = new ArrayList<>();

    DropdownConstraintParent(String[] choices) {
        super(choices);
        setFont(new Font(getFont().getName(), Font.PLAIN, 20));
        addActionListener(this);
    }

    @Override
    public void addConstrainedChild(ExternalConstraintChild<String> child) {
        children.add(child);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        children.forEach(c -> c.accept(this, (String)((JComboBox<String>)e.getSource()).getSelectedItem()));
    }
}
