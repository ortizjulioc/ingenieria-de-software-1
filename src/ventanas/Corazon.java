package ventanas;

import java.awt.*;
import java.awt.geom.Path2D;

/** Corazón rellenable basado en curvas Bezier, ajustado al bounds. */
public class Corazon extends Figura implements FiguraRellenable {
    private static final long serialVersionUID = 1L;

    private Point inicio;

    public Corazon(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

    private Shape buildShape() {
        double x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;
        double cx = x + w / 2.0;
        double cy = y + h / 2.0;

        // Normalizado en [0,1], luego escalado a bounds
        Path2D p = new Path2D.Double();
        // punto inferior
        double px0 = cx;
        double py0 = y + h;
        p.moveTo(px0, py0);

        // lóbulo izquierdo
        p.curveTo(cx - 0.25*w, y + 0.85*h,
                  x,            y + 0.60*h,
                  x + 0.20*w,   y + 0.35*h);
        p.curveTo(x + 0.30*w,  y + 0.15*h,
                  cx - 0.05*w, y,
                  cx,           y + 0.20*h);

        // lóbulo derecho
        p.curveTo(cx + 0.05*w, y,
                  x + 0.70*w,  y + 0.15*h,
                  x + 0.80*w,  y + 0.35*h);
        p.curveTo(x + w,       y + 0.60*h,
                  cx + 0.25*w, y + 0.85*h,
                  px0,         py0);

        p.closePath();
        return p;
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape s = buildShape();
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
        Corazon c = new Corazon(new Point(inicio.x + dx, inicio.y + dy));
        c.colorLinea = this.colorLinea;
        c.colorRelleno = this.colorRelleno;
        c.bounds = new Rectangle(this.bounds.x + dx, this.bounds.y + dy, this.bounds.width, this.bounds.height);
        return c;
    }
}
