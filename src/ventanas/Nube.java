package ventanas;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Nube extends Figura implements FiguraRellenable {

    private static final long serialVersionUID = 1L;

    private Point inicio;
    private Color colorRelleno;

    public Nube(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

    private Shape buildShape() {
        int x = bounds.x;
        int y = bounds.y;
        int w = bounds.width;
        int h = bounds.height;

        if (w <= 0 || h <= 0) {
            return new Area();
        }

        // 1) Construimos una nube "unidad" en coordenadas relativas (0..1)
        Area nube = new Area();

        // Estos números son los mismos porcentajes que usabas, pero
        // ahora en un espacio 0..1 en lugar de usar w y h directamente.
        nube.add(new Area(new Ellipse2D.Double(0.05, 0.40, 0.30, 0.30))); // círculo izquierdo
        nube.add(new Area(new Ellipse2D.Double(0.30, 0.20, 0.30, 0.30))); // sup. izq
        nube.add(new Area(new Ellipse2D.Double(0.05, 0.20, 0.30, 0.30))); // sup. central-izq
        nube.add(new Area(new Ellipse2D.Double(0.30, 0.20, 0.30, 0.40))); // sup. der
        nube.add(new Area(new Ellipse2D.Double(0.25, 0.40, 0.30, 0.40))); // der
        nube.add(new Area(new Ellipse2D.Double(0.30, 0.20, 0.40, 0.30))); // inf. centro 1
        nube.add(new Area(new Ellipse2D.Double(0.30, 0.40, 0.40, 0.30))); // inf. centro 2

        // 2) Bounds de esa nube unidad
        Rectangle2D ub = nube.getBounds2D();

        // 3) Escalamos para que esa nube llene EXACTAMENTE (x, y, w, h)
        double sx = w / ub.getWidth();
        double sy = h / ub.getHeight();

        AffineTransform at = new AffineTransform();
        at.translate(x, y);          // mover al rectángulo destino
        at.scale(sx, sy);            // escalar
        at.translate(-ub.getX(), -ub.getY()); // alinear el origen con el bounds de la nube

        return at.createTransformedShape(nube);
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape s = buildShape();

        // Rellenar solo si hay color
        if (colorRelleno != null) {
            g2.setColor(getColorRelleno());
            g2.fill(s);
        }

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
        Nube n = new Nube(new Point(inicio.x + dx, inicio.y + dy));
        n.colorLinea = this.colorLinea;
        n.colorRelleno = this.colorRelleno;
        n.bounds = new Rectangle(this.bounds.x + dx, this.bounds.y + dy, this.bounds.width, this.bounds.height);
        return n;
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
