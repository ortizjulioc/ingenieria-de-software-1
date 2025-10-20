package ventanas;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Circulo extends Figura implements FiguraRellenable {
    private static final long serialVersionUID = 1L;

    private Point inicio;

    public Circulo(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int d = Math.min(bounds.width, bounds.height); // círculo puro
        int x = bounds.x + (bounds.width - d) / 2;
        int y = bounds.y + (bounds.height - d) / 2;
        Shape s = new Ellipse2D.Double(x, y, d, d);
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
        Circulo c = new Circulo(new Point(inicio.x + dx, inicio.y + dy));
        c.colorLinea = this.colorLinea;
        c.colorRelleno = this.colorRelleno;
        c.bounds = new Rectangle(this.bounds.x + dx, this.bounds.y + dy, this.bounds.width, this.bounds.height);
        return c;
    }
}
