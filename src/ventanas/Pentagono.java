package ventanas;

import java.awt.*;
import java.awt.geom.Path2D;

public class Pentagono extends Figura implements FiguraRellenable {
    private static final long serialVersionUID = 1L;
    private Point inicio;
    private Color colorRelleno;

    public Pentagono(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

    private Shape buildShape() {
        int x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;
        double cx = x + w/2.0, cy = y + h/2.0;
        double r = Math.min(w, h) / 2.0;
        Path2D p = new Path2D.Double();
        for (int i = 0; i < 5; i++) {
            double ang = -Math.PI/2 + i * 2*Math.PI/5;
            double px = cx + r * Math.cos(ang);
            double py = cy + r * Math.sin(ang);
            if (i == 0) p.moveTo(px, py); else p.lineTo(px, py);
        }
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
        Pentagono p = new Pentagono(new Point(inicio.x + dx, inicio.y + dy));
        p.colorLinea = this.colorLinea; p.colorRelleno = this.colorRelleno;
        p.bounds = new Rectangle(this.bounds.x + dx, this.bounds.y + dy, this.bounds.width, this.bounds.height);
        return p;
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
