package ventanas;

import java.awt.*;

/**
 * Figura no rellenable. No muestra bounding box ni handles.
 */
public class Linea extends Figura {
    private static final long serialVersionUID = 1L;
    private Color colorRelleno;

    private Point p1, p2;

    public Linea(Point inicio) {
        this.p1 = inicio;
        this.p2 = inicio;
        actualizarBounds();
    }

    private void actualizarBounds() {
        int x = Math.min(p1.x, p2.x);
        int y = Math.min(p1.y, p2.y);
        int w = Math.abs(p2.x - p1.x);
        int h = Math.abs(p2.y - p1.y);
        this.bounds = new Rectangle(x, y, w, h);
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(colorLinea);
        g2.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    @Override
    public void actualizar(Point puntoActual) {
        this.p2 = puntoActual;
        actualizarBounds();
    }

    @Override
    public void desplazar(int dx, int dy) {
        this.p1 = new Point(p1.x + dx, p1.y + dy);
        this.p2 = new Point(p2.x + dx, p2.y + dy);
        actualizarBounds();
    }

    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        Linea l = new Linea(new Point(p1.x + dx, p1.y + dy));
        l.p2 = new Point(p2.x + dx, p2.y + dy);
        l.colorLinea = this.colorLinea;
        l.colorRelleno = this.colorRelleno; // no se usa pero se conserva
        l.actualizarBounds();
        return l;
    }
}
