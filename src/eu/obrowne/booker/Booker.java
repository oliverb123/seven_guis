package eu.obrowne.booker;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;

public class Booker extends JFrame {
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public Booker() {
        // Application setup
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Widget declaration
        var tripTypeSelection = new DropdownConstraintParent(new String[]{"one-way flight", "return flight"});
        var departure = new ConstrainedTextField("", 20);
        var returnTrip = new ConstrainedTextField("", 20);
        var button = new ConstrainedButton("Book");


        returnTrip.setEnabled(false);
        button.setEnabled(false);

        tripTypeSelection.addConstrainedChild(returnTrip);


        returnTrip.addExternalConstraint(
                (v, p) -> tripTypeSelection.getSelectedItem().equals("one-way flight") ? ConstraintState.DISABLED : ConstraintState.NONE);


        Predicate<String> validDate = (s) -> s.matches("[0-1][1-9]\\.[0-1][1-9]\\.[0-9]{4}");

        departure.addInternalConstraint((s, t) -> validDate.test(s) || s.isEmpty() || s.length() < 10? ConstraintState.NONE : ConstraintState.INVALID);
        returnTrip.addInternalConstraint((s, t) -> validDate.test(s) || s.isEmpty() || s.length() < 10 ? ConstraintState.NONE : ConstraintState.INVALID);

        departure.addConstrainedChild(returnTrip);
        returnTrip.addExternalConstraint((s, p) -> {
            if(!validDate.test(departure.getText())) return ConstraintState.NONE;
            if(!validDate.test(returnTrip.getText())) return ConstraintState.NONE;
            return LocalDate.parse(departure.getText(), fmt).isAfter(LocalDate.parse(returnTrip.getText(), fmt)) ? ConstraintState.INVALID : ConstraintState.NONE;
        });
        returnTrip.addInternalConstraint((val, self) -> {
            if(!validDate.test(val)) return ConstraintState.NONE;
            if(!validDate.test(departure.getText())) return ConstraintState.NONE;
            return LocalDate.parse(departure.getText(), fmt).isAfter(LocalDate.parse(val, fmt)) ? ConstraintState.INVALID : ConstraintState.NONE;
        });


        button.addExternalConstraint((s, p) -> {
            if(!validDate.test(departure.getText())) return ConstraintState.DISABLED;
            if(tripTypeSelection.getSelectedItem().equals("one-way flight")) return ConstraintState.NONE;
            if(!validDate.test(returnTrip.getText())) return ConstraintState.DISABLED;
            return LocalDate.parse(departure.getText(), fmt).isAfter(LocalDate.parse(returnTrip.getText(), fmt)) ? ConstraintState.DISABLED : ConstraintState.NONE;
        });

        departure.addConstrainedChild(button);
        returnTrip.addConstrainedChild(button);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(!button.isEnabled()) return;
                boolean oneWay = tripTypeSelection.getSelectedItem().equals("one-way flight");
                var oneWayMessage = "one-way flight booked for " + departure.getText();
                var returnMessage = "two-way flight booked, departing " + departure.getText() + ", returning " + returnTrip.getText();
                JOptionPane.showMessageDialog(null, oneWay ? oneWayMessage : returnMessage);
            }
        });

        add(tripTypeSelection);
        add(departure);
        add(returnTrip);
        add(button);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new Booker();
    }
}
