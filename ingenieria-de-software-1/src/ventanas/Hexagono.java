package ventanas;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

public class Hexagono extends Figura {
    private Point inicio;
    private Point fin;

    public Hexagono(Point inicio) {
        this.inicio = inicio;
        this.fin = inicio;
    }

    @Override
    public void dibujar(Graphics g) {
        int centerX = inicio.x;
        int centerY = inicio.y;

        
        double dx = fin.x - inicio.x;
        double dy = fin.y - inicio.y;
        double radio = Math.sqrt(dx * dx + dy * dy);

        int[] xs = new int[6];
        int[] ys = new int[6];

        
        
        
        // figura del hexagono
        for (int i = 0; i < 6; i++) {
           double angle = Math.toRadians( 60 * i - 0);
            xs[i] = (int) (centerX + radio * Math.cos(angle));
            ys[i] = (int) (centerY + radio * Math.sin(angle));
        }



        Polygon hexagono = new Polygon(xs, ys, 6);

        // Relleno
        g.setColor(colorRelleno);
        g.fillPolygon(hexagono);

        // Contorno
        g.setColor(colorLinea);
        g.drawPolygon(hexagono);
    }

    @Override
    public void actualizar(Point puntoActual) {
        this.fin = puntoActual;
    }
}