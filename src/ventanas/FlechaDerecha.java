package ventanas;

import java.awt.*;
import java.awt.geom.Path2D;

public class FlechaDerecha extends Figura implements FiguraRellenable {
    private static final long serialVersionUID = 1L;
    private Point inicio;

    public FlechaDerecha(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

    private Shape buildShape() {
        int x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;
        int shaftH = (int) (h * 0.3);
        int shaftY = y + (h - shaftH)/2;
        int headW = (int) (w * 0.4);

        Path2D p = new Path2D.Double();
        // asta
        p.moveTo(x,     shaftY);
        p.lineTo(x + w - headW, shaftY);
        p.lineTo(x + w - headW, shaftY + shaftH);
        p.lineTo(x,     shaftY + shaftH);
        p.closePath();
        // cabeza
        p.moveTo(x + w - headW, y);
        p.lineTo(x + w,         y + h/2.0);
        p.lineTo(x + w - headW, y + h);
        p.closePath();
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
        FlechaDerecha f = new FlechaDerecha(new Point(inicio.x + dx, inicio.y + dy));
        f.colorLinea = this.colorLinea; f.colorRelleno = this.colorRelleno;
        f.bounds = new Rectangle(this.bounds.x + dx, this.bounds.y + dy, this.bounds.width, this.bounds.height);
        return f;
    }
}
