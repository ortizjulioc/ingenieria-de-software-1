package ventanas;

import java.awt.*;
import java.util.ArrayList;

public class DibujoLibre extends Figura {
    private ArrayList<Point> puntos = new ArrayList<>();

    public DibujoLibre(Point puntoInicial) {
        this.puntos.add(puntoInicial);
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(colorLinea);

        // ✅ Si el color de línea es igual al color de relleno => es borrador
        if (colorLinea.equals(colorRelleno)) {
            g2.setStroke(new BasicStroke(20)); // grosor borrador
        } else {
            g2.setStroke(new BasicStroke(2));  // grosor normal
        }

        for (int i = 1; i < puntos.size(); i++) {
            g2.drawLine(puntos.get(i - 1).x, puntos.get(i - 1).y,
                        puntos.get(i).x, puntos.get(i).y);
        }
    }

    @Override
    public void actualizar(Point puntoActual) {
        puntos.add(puntoActual);
    }
}
