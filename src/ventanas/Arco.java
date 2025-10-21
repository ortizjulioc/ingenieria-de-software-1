package ventanas;

import java.awt.*;
import java.awt.geom.Arc2D;


public class Arco extends Figura {
    private static final long serialVersionUID = 1L;

    private Point inicio;

    public Arco(Point inicio) {
        this.inicio = inicio;
        setBoundsNormalized(inicio.x, inicio.y, inicio.x, inicio.y);
    }

 
    private Shape buildShape() {
        int x = bounds.x;
        int y = bounds.y;
        int w = bounds.width;
        int h = bounds.height;

        
        return new Arc2D.Double(x, y, w, h, 0, 180, Arc2D.OPEN);
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape s = buildShape();

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
        Arco copia = new Arco(new Point(inicio.x + dx, inicio.y + dy));
        copia.colorLinea = this.colorLinea;
        copia.bounds = new Rectangle(this.bounds.x + dx, this.bounds.y + dy, this.bounds.width, this.bounds.height);
        return copia;
    }
}
