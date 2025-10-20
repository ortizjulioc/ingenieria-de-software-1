package ventanas;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

public class Nube extends Figura implements FiguraRellenable {
    private static final long serialVersionUID = 1L;
    private Point inicio;

    public Nube(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

    private Shape buildShape() {
        int x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;
        Path2D p = new Path2D.Double();
        // Aproximamos la nube con 4 óvalos superpuestos
        Ellipse2D e1 = new Ellipse2D.Double(x + 0.05*w, y + 0.35*h, 0.4*w, 0.45*h);
        Ellipse2D e2 = new Ellipse2D.Double(x + 0.30*w, y + 0.20*h, 0.45*w, 0.55*h);
        Ellipse2D e3 = new Ellipse2D.Double(x + 0.55*w, y + 0.35*h, 0.35*w, 0.40*h);
        Ellipse2D e4 = new Ellipse2D.Double(x + 0.15*w, y + 0.55*h, 0.55*w, 0.35*h);
        p.append(e1, false); p.append(e2, false); p.append(e3, false); p.append(e4, false);
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
        Nube n = new Nube(new Point(inicio.x + dx, inicio.y + dy));
        n.colorLinea = this.colorLinea; n.colorRelleno = this.colorRelleno;
        n.bounds = new Rectangle(this.bounds.x + dx, this.bounds.y + dy, this.bounds.width, this.bounds.height);
        return n;
    }
}
