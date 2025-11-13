package ventanas;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Rectangulo extends Figura implements FiguraRellenable {
    private static final long serialVersionUID = 1L;

    private Point inicio;
    private boolean mantenerProporcion = false;
    private double aspectRatioInicial = 1.0;
    private Color colorRelleno;

    public Rectangulo(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape s = new Rectangle2D.Double(bounds.x, bounds.y, bounds.width, bounds.height);
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
        if (mantenerProporcion) {
            int dx = puntoActual.x - inicio.x;
            int dy = puntoActual.y - inicio.y;
            int w = Math.abs(dx);
            int h = (int) Math.round(w / aspectRatioInicial);
            if (h == 0) h = 1;
            // Ajusta según sentido vertical
            if (Math.abs(dy) < h) {
                h = Math.abs(dy);
                w = (int) Math.round(h * aspectRatioInicial);
            }
            int x2 = inicio.x + (dx < 0 ? -w : w);
            int y2 = inicio.y + (dy < 0 ? -h : h);
            setBoundsNormalized(inicio.x, inicio.y, x2, y2);
        } else {
            setBoundsNormalized(inicio.x, inicio.y, puntoActual.x, puntoActual.y);
        }
    }

 
    public void iniciarProporcional() {
        mantenerProporcion = true;
        aspectRatioInicial = (bounds.height == 0) ? 1.0 :
                (double) bounds.width / (double) bounds.height;
        if (aspectRatioInicial <= 0) aspectRatioInicial = 1.0;
    }

    public void terminarProporcional() { mantenerProporcion = false; }

    @Override
    public void desplazar(int dx, int dy) {
        bounds = new Rectangle(bounds.x + dx, bounds.y + dy, bounds.width, bounds.height);
        inicio = new Point(inicio.x + dx, inicio.y + dy);
    }

    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        Rectangulo r = new Rectangulo(new Point(inicio.x + dx, inicio.y + dy));
        r.colorLinea = this.colorLinea;
        r.colorRelleno = this.colorRelleno;
        r.bounds = new Rectangle(this.bounds.x + dx, this.bounds.y + dy, this.bounds.width, this.bounds.height);
        return r;
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
