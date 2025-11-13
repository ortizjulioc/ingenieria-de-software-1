package ventanas;

import java.awt.*;
import java.awt.geom.Path2D;


public class Triangulo extends Figura implements FiguraRellenable {
    private static final long serialVersionUID = 1L;

    private Point inicio;
    private Color colorRelleno;

    public Triangulo(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

    private Shape buildShape() {
        int x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;
        double x1 = x + w / 2.0;     // vértice superior (centro)
        double y1 = y;
        double x2 = x;               // inferior izquierdo
        double y2 = y + h;
        double x3 = x + w;           // inferior derecho
        double y3 = y + h;

        Path2D p = new Path2D.Double();
        p.moveTo(x1, y1);
        p.lineTo(x2, y2);
        p.lineTo(x3, y3);
        p.closePath();
        return p;
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
        Triangulo t = new Triangulo(new Point(inicio.x + dx, inicio.y + dy));
        t.colorLinea = this.colorLinea;
        t.colorRelleno = this.colorRelleno;
        t.bounds = new Rectangle(this.bounds.x + dx, this.bounds.y + dy, this.bounds.width, this.bounds.height);
        return t;
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
