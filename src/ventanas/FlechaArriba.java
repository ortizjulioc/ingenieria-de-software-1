package ventanas;

import java.awt.*;
import java.awt.geom.Path2D;

public class FlechaArriba extends Figura implements FiguraRellenable {
    private static final long serialVersionUID = 1L;
    private Point inicio;

    public FlechaArriba(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

    private Shape buildShape() {
        int x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;
        int shaftW = (int) (w * 0.3);
        int shaftX = x + (w - shaftW)/2;
        int headH = (int) (h * 0.4);

        Path2D p = new Path2D.Double();
        // cabeza del triángulo
        p.moveTo(x + w/2.0, y);
        p.lineTo(x + w, y + headH);
        p.lineTo(x,     y + headH);
        p.closePath();
        // asta del rectángulo
        p.moveTo(shaftX, y + headH);
        p.lineTo(shaftX + shaftW, y + headH);
        p.lineTo(shaftX + shaftW, y + h);
        p.lineTo(shaftX, y + h);
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
        FlechaArriba f = new FlechaArriba(new Point(inicio.x + dx, inicio.y + dy));
        f.colorLinea = this.colorLinea; f.colorRelleno = this.colorRelleno;
        f.bounds = new Rectangle(this.bounds.x + dx, this.bounds.y + dy, this.bounds.width, this.bounds.height);
        return f;
    }
}
