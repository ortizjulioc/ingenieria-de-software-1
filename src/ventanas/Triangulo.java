package ventanas;

import java.awt.*;

public class Triangulo extends Figura {
    private Point inicio;
    private Point fin;

    public Triangulo(Point inicio) {
        this.inicio = inicio;
        this.fin = inicio;
    }

    @Override
    public void dibujar(Graphics g) {
        Polygon poly = getPolygon();
        g.setColor(colorRelleno);
        g.fillPolygon(poly);
        g.setColor(colorLinea);
        g.drawPolygon(poly);
    }

    private Polygon getPolygon() {
        int x1 = inicio.x, y1 = inicio.y;
        int x2 = fin.x, y2 = fin.y;

        int bx1 = x1, by1 = y2;      // base izquierda
        int bx2 = x2, by2 = y2;      // base derecha
        int vx  = (x1 + x2) / 2;     // vértice superior/ inferior según arrastre
        int vy  = y1;

        Polygon p = new Polygon();
        p.addPoint(bx1, by1);
        p.addPoint(bx2, by2);
        p.addPoint(vx, vy);
        return p;
    }

    @Override
    public void actualizar(Point puntoActual) { this.fin = puntoActual; }

    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        Triangulo copia = new Triangulo(new Point(inicio.x + dx, inicio.y + dy));
        copia.fin = new Point(fin.x + dx, fin.y + dy);
        copia.setColorLinea(colorLinea);
        copia.setColorRelleno(colorRelleno);
        return copia;
    }

    @Override
    public Rectangle getBounds() {
        return getPolygon().getBounds();
    }
}
