package ventanas;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;


public class Nube extends Figura implements FiguraRellenable {
    private static final long serialVersionUID = 1L;

    private Point inicio;
    private Color colorRelleno;

    public Nube(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

   
    private Shape buildShape() {
        double x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;
        Area nube = new Area();

       
        // Círculo izquierdo
        nube.add(new Area(new Ellipse2D.Double(x + w * 0.05, y + h * 0.40, w * 0.30, h * 0.30)));

        // Círculo superior izquierdo
        nube.add(new Area(new Ellipse2D.Double(x + w * 0.30, y + h * 0.20, w * 0.30, h * 0.30)));

        // Círculo superior "central-izq" (según snippet había dos con misma zona superior)
        nube.add(new Area(new Ellipse2D.Double(x + w * 0.05, y + h * 0.20, w * 0.30, h * 0.30)));

        // Círculo superior derecho (ligeramente más alto)
        nube.add(new Area(new Ellipse2D.Double(x + w * 0.30, y + h * 0.20, w * 0.30, h * 0.40)));

        // Círculo derecho
        nube.add(new Area(new Ellipse2D.Double(x + w * 0.25, y + h * 0.40, w * 0.30, h * 0.40)));

        // Círculo inferior central (dos refuerzos inferiores)
        nube.add(new Area(new Ellipse2D.Double(x + w * 0.30, y + h * 0.20, w * 0.40, h * 0.30)));
        nube.add(new Area(new Ellipse2D.Double(x + w * 0.30, y + h * 0.40, w * 0.40, h * 0.30)));

        return nube;
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
