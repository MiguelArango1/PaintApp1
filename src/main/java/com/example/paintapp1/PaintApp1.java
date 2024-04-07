
package com.example.paintapp1;

import javax.swing.SwingUtilities; // Agrega esta línea para importar SwingUtilities

public class PaintApp1 {

    public static void main(String[] args) {
        // Creamos una instancia de PaintAppSwing y la hacemos visible
        SwingUtilities.invokeLater(() -> new PaintAppSwing().setVisible(true));
    }
}
