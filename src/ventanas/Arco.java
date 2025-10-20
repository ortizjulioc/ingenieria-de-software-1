package ventanas;

import java.awt.*;
import java.awt.geom.Arc2D;

public class Arco extends Figura implements FiguraRellenable {
    private static final long serialVersionUID = 1L;
    private Point inicio;
    // Parámetros simples para el arco
    private int startAngle = 30; // grados
    private int extent = 300;    // grados (positivo antihorario)

    public Arco(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

    public void setStartAngle(int deg) { this.startAngle = deg; }
    public void setExtent(int deg) { this.extent = deg; }

    @Override public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape s = new Arc2D.Double(bounds.x, bounds.y, bounds.width, bounds.height, startAngle, extent, Arc2D.PIE);
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
        Arco a = new Arco(new Point(inicio.x + dx, inicio.y + dy));
        a.colorLinea = this.colorLinea; a.colorRelleno = this.colorRelleno;
        a.bounds = new Rectangle(this.bounds.x + dx, this.bounds.y + dy, this.bounds.width, this.bounds.height);
        a.startAngle = this.startAngle; a.extent = this.extent;
        return a;
    }
}
