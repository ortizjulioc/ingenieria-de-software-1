package ventanas;

import java.awt.*;
import java.util.ArrayList;

public class DibujoLibre extends Figura {

    private ArrayList<Point> puntos = new ArrayList<>();
    private int grosor;

    public DibujoLibre(Point puntoInicial, int grosor) {
        this.puntos.add(puntoInicial);
        this.grosor = grosor;
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(colorLinea);

        if (colorLinea.equals(colorRelleno)) {
            g2.setStroke(new BasicStroke(20)); // borrador fijo
        } else {
            g2.setStroke(new BasicStroke(grosor)); // ✅ grosor de pincel/lápiz
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
