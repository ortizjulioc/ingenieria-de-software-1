package ventanas;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;


public class Borrador extends Figura {
    private static final long serialVersionUID = 1L;

    private final List<Point> puntos = new ArrayList<>();
    private float tamBorrador = 12f; // px

    public Borrador(Point inicio, float tamInicial) {
        this.tamBorrador = Math.max(1f, tamInicial);
        puntos.add(inicio);
        bounds = new Rectangle(inicio.x, inicio.y, 1, 1);
    }

    public void setTamBorrador(float t) { this.tamBorrador = Math.max(1f, t); }
    public float getTamBorrador() { return tamBorrador; }

    public void agregarPunto(Point p) {
        puntos.add(p);
        actualizarBounds();
    }

    @Override
    public void dibujar(Graphics g) {
        if (puntos.size() < 2) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(colorLinea != null ? colorLinea : Color.WHITE);
        g2.setStroke(new BasicStroke(tamBorrador, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        Point prev = puntos.get(0);
        for (int i = 1; i < puntos.size(); i++) {
            Point p = puntos.get(i);
            g2.draw(new Line2D.Float(prev.x, prev.y, p.x, p.y));
            prev = p;
        }
    }

    @Override
    public void actualizar(Point puntoActual) {
        agregarPunto(puntoActual);
    }

    @Override
    public void desplazar(int dx, int dy) {
        for (int i = 0; i < puntos.size(); i++) {
            Point p = puntos.get(i);
            puntos.set(i, new Point(p.x + dx, p.y + dy));
        }
        actualizarBounds();
    }

    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        Borrador copia = new Borrador(new Point(0, 0), tamBorrador);
        copia.puntos.clear();
        for (Point p : puntos) {
            copia.puntos.add(new Point(p.x + dx, p.y + dy));
        }
        copia.colorLinea = this.colorLinea; // color "borrador"
        copia.colorRelleno = this.colorRelleno;
        copia.actualizarBounds();
        return copia;
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
}
