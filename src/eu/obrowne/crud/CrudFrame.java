package eu.obrowne.crud;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

public class CrudFrame extends JFrame {

    public CrudFrame() {
        getContentPane().setLayout(new GridBagLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        var b = new EmptyBorder(10, 10, 10, 10);

        // prefix filter + spacer
        GridBagConstraints c = new GridBagConstraints() {{
            fill = HORIZONTAL;
            weightx = 0.5;
            gridwidth = 1;
            gridx = 0;
            gridy = 0;
        }};
        var prefix = new LabelledTextField("Filter prefix: ", "");
        prefix.setBorder(b);
        getContentPane().add(prefix, c);
        c.gridx = 1;
        c.weightx = 0.5;
        getContentPane().add(new JPanel(){{setBorder(b);}}, c); // Add a spacer

        // Names declared
        var firstName = new LabelledTextField("Name: ", "");
        firstName.setBorder(b);
        var secondName = new LabelledTextField("Surname: ", "");
        secondName.setBorder(b);

        // List
        var repo = new NameRepo();
        var listUI = repo.getUi();
        listUI.setBorder(b);
        listUI.setSelectionMode(SINGLE_SELECTION);
        c = new GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            weighty = 1;
            fill = BOTH;
        }};
        getContentPane().add(new JScrollPane(listUI), c);

        // Names added beside list
        c.gridx = 1;
        getContentPane().add(new JPanel(new GridBagLayout()){{
            var c = new GridBagConstraints() {{
                gridx = 0;
                gridy = 0;
                weightx = 1;
                gridwidth = 1;
                fill = HORIZONTAL;
            }};
            add(firstName, c);
            c.gridy = 1;
            add(secondName, c);
            c.fill = GridBagConstraints.BOTH;
            c.gridy = 2;
            c.weighty = 1;
            add(new JPanel(), c);
        }}, c);

        // Filtering
        prefix.field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                repo.setFilter((s) -> s.startsWith(prefix.field.getText()));
            }
        });

        // Delete button
        var deleteButton = new JButton("Delete"){{
            setEnabled(false);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(!isEnabled()) return;
                    repo.delete(listUI.getSelectedIndex());
                }
            });
        }};

        // Update button
        var updateButton = new JButton("Update"){{
            setEnabled(false);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    var f = firstName.field.getText();
                    var s = secondName.field.getText();
                    if(s.trim().length() == 0 || s.trim().length() == 0) {
                        return;
                    }
                    if(!isEnabled()) return;
                    repo.update(listUI.getSelectedIndex(), s + ", " + f);
                }
            });
        }};

        // enable/disable of buttons
        listUI.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()) return;
                deleteButton.setEnabled(!listUI.getSelectionModel().isSelectionEmpty());
                updateButton.setEnabled(!listUI.getSelectionModel().isSelectionEmpty());
            }
        });


        // Toolbar
        getContentPane().add(new JPanel(new GridBagLayout()) {{
                setBorder(b);
                var c = new GridBagConstraints(){{
                    fill=HORIZONTAL;
                    weightx = 0.33;
                    gridy = 0;
                    gridx = 0;
                }};
                add(new JButton("Create"){{
                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            var f = firstName.field.getText();
                            var s = secondName.field.getText();
                            if(s.trim().length() == 0 || s.trim().length() == 0) {
                                return;
                            }
                            repo.add(s + ", " + f);
                        }
                    });
                }}, c);
                c.gridx = 1;
                add(updateButton, c);
                c.gridx = 2;
                add(deleteButton, c);
            }},
            new GridBagConstraints() {{
                fill = HORIZONTAL;
                weightx = 1.0;
                gridwidth = 2;
                gridy = 3;
                gridx = 0;
            }}
        );
        pack();
        setMinimumSize(getSize());
        setVisible(true);
    }

    public static void main(String[] args) {
        new CrudFrame();
    }
}
