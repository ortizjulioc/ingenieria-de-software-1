package ventanas;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

public class Triangulo extends Figura {
    private Point inicio;
    private Point fin;

    public Triangulo(Point inicio) {
        this.inicio = inicio;
        this.fin = inicio;
    }

    @Override
    public void dibujar(Graphics g) {
        // Calculamos los tres puntos del triángulo
        int x1 = inicio.x;
        int y1 = inicio.y;
        int x2 = fin.x;
        int y2 = fin.y;

        // Punto base izquierda
        int bx1 = x1;
        int by1 = y2;

        // Punto base derecha
        int bx2 = x2;
        int by2 = y2;

        // Punto superior (vértice)
        int vx = (x1 + x2) / 2;
        int vy = y1;

        int[] xs = {bx1, bx2, vx};
        int[] ys = {by1, by2, vy};

        Polygon triangulo = new Polygon(xs, ys, 3);

        // Relleno
        g.setColor(colorRelleno);
        g.fillPolygon(triangulo);

        // Contorno
        g.setColor(colorLinea);
        g.drawPolygon(triangulo);
    }

    @Override
    public void actualizar(Point puntoActual) {
        this.fin = puntoActual;
    }
}
