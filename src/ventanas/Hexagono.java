package ventanas;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Hexagono extends Figura implements FiguraRellenable {

    private static final long serialVersionUID = 1L;
    private Point inicio;
    private Color colorRelleno;

    public Hexagono(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

    private Shape buildShape() {
        int x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;

        if (w <= 0 || h <= 0) {
            return new Path2D.Double();
        }

        Path2D p = new Path2D.Double();
        double cx = 0.5;
        double cy = 0.5;
        double r = 0.5;

        for (int i = 0; i < 6; i++) {
            double ang = -Math.PI / 2 + i * 2 * Math.PI / 6;
            double px = cx + r * Math.cos(ang);
            double py = cy + r * Math.sin(ang);
            if (i == 0) {
                p.moveTo(px, py);
            } else {
                p.lineTo(px, py);
            }
        }
        p.closePath();

        Rectangle2D ub = p.getBounds2D();

        double sx = w / ub.getWidth();
        double sy = h / ub.getHeight();

        AffineTransform at = new AffineTransform();
        at.translate(x, y);              
        at.scale(sx, sy);                
        at.translate(-ub.getX(), -ub.getY()); 

        return at.createTransformedShape(p);
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape s = buildShape();
        if (colorRelleno != null) {
            g2.setColor(colorRelleno);
            g2.fill(s);
        }

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
        Hexagono h = new Hexagono(new Point(inicio.x + dx, inicio.y + dy));
        h.colorLinea = this.colorLinea;
        h.colorRelleno = this.colorRelleno;
        h.bounds = new Rectangle(this.bounds.x + dx, this.bounds.y + dy, this.bounds.width, this.bounds.height);
        return h;
    }

    @Override
    public void setColorRelleno(Color c) {
        this.colorRelleno = c;
    }

    @Override
    public Color getColorRelleno() {
        return colorRelleno;
    }

    @Override
    public boolean esRellenable() {
        return true;
    }
}
