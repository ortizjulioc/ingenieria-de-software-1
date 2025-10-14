package ventanas;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Area;

public class Nube extends Figura {
    private Point centro;
    private int ancho;
    private int alto;

    public Nube(Point centro) {
        this.centro = centro;
        this.ancho = ancho;
        this.alto = alto;
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Relleno
        g2d.setColor(colorRelleno != null ? colorRelleno : Color.WHITE);

        int x = centro.x - ancho / 2;
        int y = centro.y - alto / 2;

        Area nube = new Area();

        // Círculo izquierdo
        nube.add(new Area(new Ellipse2D.Double(x + ancho * 0.05, y + alto * 0.4, ancho * 0.3, alto * 0.3)));
     
        // Círculo superior izquierdo
        nube.add(new Area(new Ellipse2D.Double(x + ancho * 0.30, y + alto * 0.2, ancho * 0.3, alto * 0.3)));

        // Círculo superior derecho
        nube.add(new Area(new Ellipse2D.Double(x + ancho * 0.05, y + alto * 0.2, ancho * 0.3, alto * 0.3)));

        // Círculo superior derecho
        nube.add(new Area(new Ellipse2D.Double(x + ancho * 0.30, y + alto * 0.2, ancho * 0.3, alto * 0.4)));
        // Círculo derecho
        nube.add(new Area(new Ellipse2D.Double(x + ancho * 0.25, y + alto * 0.4, ancho * 0.3, alto * 0.4)));

        // Círculo inferior central
        nube.add(new Area(new Ellipse2D.Double(x + ancho * 0.30, y + alto * 0.2, ancho * 0.4, alto * 0.3)));
        
        // Círculo inferior central
        nube.add(new Area(new Ellipse2D.Double(x + ancho * 0.30, y + alto * 0.4, ancho * 0.4, alto * 0.3)));


        // Rellenar
        g2d.fill(nube);

        // Contorno
        g2d.setColor(colorLinea != null ? colorLinea : Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(nube);
    }

    @Override
    public void actualizar(Point puntoActual) {
        this.ancho = Math.abs(puntoActual.x - centro.x) * 2;
        this.alto = Math.abs(puntoActual.y - centro.y) * 2;
    }

    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        Nube copia = new Nube(new Point(centro.x + dx, centro.y + dy));
        copia.setColorLinea(colorLinea);
        copia.setColorRelleno(colorRelleno);
        return copia;
    }

    @Override
    public Rectangle getBounds() {
        int x = centro.x - ancho / 2;
        int y = centro.y - alto / 2;
        return new Rectangle(x, y, ancho, alto);
    }

    public void mover(int dx, int dy) {
        centro.translate(dx, dy);
    }
}
