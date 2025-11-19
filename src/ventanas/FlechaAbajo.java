package ventanas;

import java.awt.*;
import java.awt.geom.Path2D;

public class FlechaAbajo extends Figura implements FiguraRellenable {
    private static final long serialVersionUID = 1L;
    private Point inicio;
    private Color colorRelleno;

    public FlechaAbajo(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

    private Shape buildShape() {
        int x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;
        int shaftW = (int) (w * 0.3);
        int shaftX = x + (w - shaftW)/2;
        int headH = (int) (h * 0.4);

        Path2D p = new Path2D.Double();
        // asta
        p.moveTo(shaftX, y);
        p.lineTo(shaftX + shaftW, y);
        p.lineTo(shaftX + shaftW, y + h - headH);
        p.lineTo(shaftX, y + h - headH);
        p.closePath();
        // cabeza
        p.moveTo(x, y + h - headH);
        p.lineTo(x + w, y + h - headH);
        p.lineTo(x + w/2.0, y + h);
        p.closePath();
        return p;
    }

    @Override public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape s = buildShape();
        // Rellenar solo si hay color
        if (colorRelleno != null) {
            g2.setColor(colorRelleno);
            g2.fill(s);
        }
        
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
        FlechaAbajo f = new FlechaAbajo(new Point(inicio.x + dx, inicio.y + dy));
        f.colorLinea = this.colorLinea; f.colorRelleno = this.colorRelleno;
        f.bounds = new Rectangle(this.bounds.x + dx, this.bounds.y + dy, this.bounds.width, this.bounds.height);
        return f;
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
