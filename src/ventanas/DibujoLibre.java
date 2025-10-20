package ventanas;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Figura de dibujo libre (tipo lápiz).
 * Mantiene una lista de puntos y un grosor de línea.
 */
public class DibujoLibre extends Figura implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Point> puntos = new ArrayList<>();
    private float grosor = 2.0f;

    public DibujoLibre(Point inicio, float grosor) {
        this.grosor = grosor;
        puntos.add(inicio);
        bounds = new Rectangle(inicio.x, inicio.y, 1, 1);
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(colorLinea);
        g2.setStroke(new BasicStroke(grosor, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (int i = 1; i < puntos.size(); i++) {
            Point p1 = puntos.get(i - 1);
            Point p2 = puntos.get(i);
            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    @Override
    public void actualizar(Point puntoActual) {
        puntos.add(puntoActual);
        actualizarBounds();
    }

    private void actualizarBounds() {
        if (puntos.isEmpty()) return;
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        for (Point p : puntos) {
            minX = Math.min(minX, p.x);
            minY = Math.min(minY, p.y);
            maxX = Math.max(maxX, p.x);
            maxY = Math.max(maxY, p.y);
        }
        bounds = new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public void desplazar(int dx, int dy) {
        for (int i = 0; i < puntos.size(); i++) {
            Point p = puntos.get(i);
            puntos.set(i, new Point(p.x + dx, p.y + dy));
        }
        bounds.translate(dx, dy);
    }

    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        DibujoLibre d = new DibujoLibre(new Point(0, 0), this.grosor);
        d.puntos.clear();
        for (Point p : this.puntos) {
            d.puntos.add(new Point(p.x + dx, p.y + dy));
        }
        d.colorLinea = this.colorLinea;
        d.grosor = this.grosor;
        d.actualizarBounds();
        return d;
    }

    public float getGrosor() { return grosor; }
    public void setGrosor(float g) { this.grosor = g; }
}
