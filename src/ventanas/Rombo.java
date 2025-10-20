package ventanas;

import java.awt.*;
import java.awt.geom.Path2D;

public class Rombo extends Figura implements FiguraRellenable {
    private static final long serialVersionUID = 1L;
    private Point inicio;

    public Rombo(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

    private Shape buildShape() {
        int x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;
        Path2D p = new Path2D.Double();
        p.moveTo(x + w/2.0, y);         // arriba
        p.lineTo(x + w,     y + h/2.0); // derecha
        p.lineTo(x + w/2.0, y + h);     // abajo
        p.lineTo(x,         y + h/2.0); // izquierda
        p.closePath();
        return p;
    }

    @Override public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape s = buildShape();
        g2.setColor(colorRelleno); g2.fill(s);
        g2.setColor(colorLinea);   g2.draw(s);
    }

    @Override public void actualizar(Point puntoActual) {
        setBoundsNormalized(inicio.x, inicio.y, puntoActual.x, puntoActual.y);
    }

    @Override public void desplazar(int dx, int dy) {
        bounds = new Rectangle(bounds.x + dx, bounds.y + dy, bounds.width, bounds.height);
        inicio = new Point(inicio.x + dx, inicio.y + dy);
    }

    @Override public Figura clonarConDesplazamiento(int dx, int dy) {
        Rombo r = new Rombo(new Point(inicio.x + dx, inicio.y + dy));
        r.colorLinea = this.colorLinea; r.colorRelleno = this.colorRelleno;
        r.bounds = new Rectangle(this.bounds.x + dx, this.bounds.y + dy, this.bounds.width, this.bounds.height);
        return r;
    }
}
