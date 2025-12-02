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
        // 1) Primero obtenemos el rectángulo normalizado según el arrastre
        setBoundsNormalized(inicio.x, inicio.y, puntoActual.x, puntoActual.y);

        // 2) Forzamos que sea un cuadrado usando el lado mínimo,
        //    pero SIN mover la esquina superior izquierda
        int d = Math.min(bounds.width, bounds.height);
        bounds = new Rectangle(bounds.x, bounds.y, d, d);
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
