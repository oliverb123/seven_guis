package eu.obrowne.timer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TimerApp extends JFrame {

    public TimerApp() {
        // Application setup
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        var ticker = new Ticker(1);
        var timer = new Timer();
        timer.setDuration(30_000);
        ticker.addTarget(timer::tick);

        var progress = new JProgressBar();
        progress.setMinimumSize(new Dimension(20, 100));
        timer.addConsumer((p) -> {
            progress.setMaximum((int) (p.first + p.second));
            progress.setValue(Math.toIntExact(p.first));
        });

        var elapsedLabel = new JLabel("0s");
        elapsedLabel.setMinimumSize(new Dimension(elapsedLabel.getHeight(), 100));
        elapsedLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        timer.addConsumer((p) -> {
            elapsedLabel.setText(String.format("%.1f", p.first/1000f));
        });
        elapsedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        getContentPane().add(new JPanel(){{
            add(new Label("Elapsed Time:"));
            add(progress);
            setAlignmentX(LEFT_ALIGNMENT);
        }});
        getContentPane().add(elapsedLabel);

        getContentPane().add(new JPanel(){{
            add(new JLabel("Duration: "));
            add(new JSlider(){{
                setMaximum(300_000);
                setMinimum(0);
                setValue(30_000);
                addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        timer.setDuration(((JSlider)e.getSource()).getValue());
                    }
                });
            }});
        }});

        getContentPane().add(new JButton("Reset"){{
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    timer.reset();
                }
            });
        }});

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new TimerApp();
    }
}
