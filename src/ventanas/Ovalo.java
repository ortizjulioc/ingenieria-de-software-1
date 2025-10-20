package ventanas;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/** Óvalo rellenable (usa bounds y respeta proporciones si así lo decides desde el panel). */
public class Ovalo extends Figura implements FiguraRellenable {
    private static final long serialVersionUID = 1L;

    private Point inicio;

    public Ovalo(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape s = new Ellipse2D.Double(bounds.x, bounds.y, bounds.width, bounds.height);
        g2.setColor(colorRelleno);
        g2.fill(s);
        g2.setColor(colorLinea);
        g2.draw(s);
    }

    @Override
    public void actualizar(Point puntoActual) {
        setBoundsNormalized(inicio.x, inicio.y, puntoActual.x, puntoActual.y);
    }

    @Override
    public void desplazar(int dx, int dy) {
        bounds = new Rectangle(bounds.x + dx, bounds.y + dy, bounds.width, bounds.height);
        inicio = new Point(inicio.x + dx, inicio.y + dy);
    }

    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        Ovalo o = new Ovalo(new Point(inicio.x + dx, inicio.y + dy));
        o.colorLinea = this.colorLinea;
        o.colorRelleno = this.colorRelleno;
        o.bounds = new Rectangle(this.bounds.x + dx, this.bounds.y + dy, this.bounds.width, this.bounds.height);
        return o;
    }
}
