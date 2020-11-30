package eu.obrowne.converter;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;
import java.util.function.Function;

public class Converter extends JFrame {

    public Converter() {
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        var stringToOptionalFloat = new Function<String, Optional<Float>>() {
            @Override
            public Optional<Float> apply(String s) {
                try {
                    return Optional.of(Float.valueOf(s));
                } catch(Exception e) {
                    return Optional.empty();
                }
            }
        };
        // Assumes any float received is fahrenheit, to be converted to celsius
        var celsius = new AlertingTextDisplay<Float, Float>(
                "0",
                f -> Optional.of("" + ((f - 32.0f) * (5.0f/9.0f))),
                stringToOptionalFloat
        );

        // Assumes any float received is celsius, to be converted to fahrenheit
        var fahrenheit = new AlertingTextDisplay<Float, Float>(
                "0",
                c -> Optional.of("" + (c*(9f/5f) + 32)),
                stringToOptionalFloat
        );

        celsius.setListener("fahrenheit", fahrenheit::receive);
        fahrenheit.setListener("celsius", celsius::receive);
        getContentPane().add(celsius);
        getContentPane().add(new JLabel("Celsius = "));
        getContentPane().add(fahrenheit);
        getContentPane().add(new JLabel("Fahrenheit"));
        pack();
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Converter();
    }
}
