package ventanas;

import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Corazón rellenable que utiliza el diseño (proporciones Bezier) proporcionado por el usuario,
 * pero integrado al framework del proyecto (usa bounds, es FiguraRellenable, soporta mover/resize).
 */
public class Corazon extends Figura implements FiguraRellenable {
    private static final long serialVersionUID = 1L;

    private Point inicio;

    public Corazon(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

    /** Construye el Path con la misma geometría del snippet del usuario, adaptada a bounds. */
    private Shape buildShape() {
        int x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;
        double cx = x + w / 2.0;
        double topY = y + h * 0.3;

        Path2D path = new Path2D.Double();
        // Punta inferior (centro abajo)
        path.moveTo(cx, y + h);

        // CURVA 1: lado izquierdo
        path.curveTo(
                cx - w * 0.1, y + h * 0.9,  // Control 1
                x,            y + h * 0.6,  // Control 2
                x,            topY          // Fin
        );

        // CURVA 2: lóbulo superior izquierdo
        path.curveTo(
                x,            y - h * 0.1,   // Control 1 (sube)
                cx - w * 0.15, y - h * 0.1,  // Control 2 (arriba)
                cx,           topY           // Fin (centro superior)
        );

        // CURVA 3: lóbulo superior derecho
        path.curveTo(
                cx + w * 0.15, y - h * 0.1,  // Control 1 (arriba)
                x + w,         y - h * 0.1,  // Control 2 (sube)
                x + w,         topY          // Fin
        );

        // CURVA 4: lado derecho
        path.curveTo(
                x + w,         y + h * 0.6,  // Control 1
                cx + w * 0.1,  y + h * 0.9,  // Control 2
                cx,            y + h         // Fin (regresa a la punta)
        );

        path.closePath();
        return path;
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape s = buildShape();
        // Relleno
        g2.setColor(getColorRelleno());
        g2.fill(s);

        // Contorno
        g2.setColor(getColorLinea());
        g2.setStroke(new BasicStroke(2f));
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
        Corazon c = new Corazon(new Point(inicio.x + dx, inicio.y + dy));
        c.colorLinea = this.colorLinea;
        c.colorRelleno = this.colorRelleno;
        c.bounds = new Rectangle(this.bounds.x + dx, this.bounds.y + dy, this.bounds.width, this.bounds.height);
        return c;
    }
}
