package ventanas;

import java.awt.*;
import java.util.ArrayList;

public class DibujoLibre extends Figura {
    private final ArrayList<Point> puntos = new ArrayList<>();
    private final int grosor;

    public DibujoLibre(Point puntoInicial, int grosor) {
        this.puntos.add(puntoInicial);
        this.grosor = grosor;
    }

    @Override
    public void dibujar(Graphics g) {
        if (puntos.size() < 2) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(colorLinea);

        // Si el color de línea es igual al relleno => tratamos como borrador
        if (colorLinea.equals(colorRelleno)) {
            g2.setStroke(new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        } else {
            g2.setStroke(new BasicStroke(grosor, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        }

        for (int i = 1; i < puntos.size(); i++) {
            Point a = puntos.get(i - 1);
            Point b = puntos.get(i);
            g2.drawLine(a.x, a.y, b.x, b.y);
        }
    }

    @Override
    public void actualizar(Point puntoActual) {
        puntos.add(puntoActual);
    }

    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        if (puntos.isEmpty()) return null;
        DibujoLibre copia = new DibujoLibre(new Point(puntos.get(0).x + dx, puntos.get(0).y + dy), grosor);
        for (int i = 1; i < puntos.size(); i++) {
            Point p = puntos.get(i);
            copia.puntos.add(new Point(p.x + dx, p.y + dy));
        }
        copia.setColorLinea(colorLinea);
        copia.setColorRelleno(colorRelleno);
        return copia;
    }

    @Override
    public Rectangle getBounds() {
        if (puntos.isEmpty()) return new Rectangle(0, 0, 0, 0);
        int minX = puntos.get(0).x, maxX = puntos.get(0).x;
        int minY = puntos.get(0).y, maxY = puntos.get(0).y;
        for (Point p : puntos) {
            minX = Math.min(minX, p.x);
            maxX = Math.max(maxX, p.x);
            minY = Math.min(minY, p.y);
            maxY = Math.max(maxY, p.y);
        }
        int pad = colorLinea.equals(colorRelleno) ? 12 : Math.max(2, grosor / 2);
        return new Rectangle(minX - pad, minY - pad, (maxX - minX) + 2 * pad, (maxY - minY) + 2 * pad);
    }
}
