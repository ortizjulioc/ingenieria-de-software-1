package ventanas;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Circulo extends Figura implements FiguraRellenable {

    private static final long serialVersionUID = 1L;

    private Point inicio;
    private Color colorRelleno;

    public Circulo(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Ahora el círculo usa TODO el bounds (que ya es cuadrado)
        Shape s = new Ellipse2D.Double(
                bounds.x,
                bounds.y,
                bounds.width,
                bounds.height
        );

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
        int x0 = inicio.x;
        int y0 = inicio.y;
        int x1 = puntoActual.x;
        int y1 = puntoActual.y;

        int w = Math.abs(x1 - x0);
        int h = Math.abs(y1 - y0);
        int d = Math.min(w, h);   // lado del cuadrado

        int nx, ny;

        if (x1 >= x0 && y1 >= y0) {
            // arrastrando hacia abajo-derecha
            nx = x0;
            ny = y0;
        } else if (x1 < x0 && y1 >= y0) {
            // abajo-izquierda
            nx = x0 - d;
            ny = y0;
        } else if (x1 >= x0 && y1 < y0) {
            // arriba-derecha
            nx = x0;
            ny = y0 - d;
        } else {
            // arriba-izquierda
            nx = x0 - d;
            ny = y0 - d;
        }

        bounds = new Rectangle(nx, ny, d, d);
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
        c.bounds = new Rectangle(
                this.bounds.x + dx,
                this.bounds.y + dy,
                this.bounds.width,
                this.bounds.height
        );
        return c;
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
