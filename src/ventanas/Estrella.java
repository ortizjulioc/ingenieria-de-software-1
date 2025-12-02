package ventanas;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Estrella extends Figura implements FiguraRellenable {

    private static final long serialVersionUID = 1L;
    private Point inicio;
    private Color colorRelleno;

    public Estrella(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

    private Shape buildShape() {
        int x = bounds.x;
        int y = bounds.y;
        int w = bounds.width;
        int h = bounds.height;

        if (w <= 0 || h <= 0) {
            return new Path2D.Double();
        }

        // 1) Construimos una estrella "unidad" alrededor del origen (0,0)
        Path2D unit = new Path2D.Double();
        double rOuter = 1.0;
        double rInner = 0.5;      // relación entre punta exterior e interior
        int puntos = 5;
        for (int i = 0; i < 2 * puntos; i++) {
            double ang = -Math.PI / 2 + i * Math.PI / puntos;
            double r = (i % 2 == 0) ? rOuter : rInner;
            double px = r * Math.cos(ang);
            double py = r * Math.sin(ang);
            if (i == 0) {
                unit.moveTo(px, py);
            } else {
                unit.lineTo(px, py);
            }
        }
        unit.closePath();

        // 2) Bounds de esa estrella unidad
        Rectangle2D ub = unit.getBounds2D();

        // 3) Escala para que esa estrella ocupe EXACTAMENTE el rectángulo (x,y,w,h)
        double sx = w / ub.getWidth();
        double sy = h / ub.getHeight();

        AffineTransform at = new AffineTransform();
        // Primero llevamos la estrella unidad al rectángulo destino
        at.translate(x, y);
        at.scale(sx, sy);
        // Corregimos el origen usando el bounds de la estrella unidad
        at.translate(-ub.getX(), -ub.getY());

        return at.createTransformedShape(unit);
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape s = buildShape();
        // Rellenar solo si hay color
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
        Estrella e = new Estrella(new Point(inicio.x + dx, inicio.y + dy));
        e.colorLinea = this.colorLinea;
        e.colorRelleno = this.colorRelleno;
        e.bounds = new Rectangle(this.bounds.x + dx, this.bounds.y + dy, this.bounds.width, this.bounds.height);
        return e;
    }

    @Override
    public boolean esRellenable() {
        return true;
    }

    @Override
    public void setColorRelleno(Color c) {
        this.colorRelleno = c;
    }

    @Override
    public Color getColorRelleno() {
        return colorRelleno;
    }
}
