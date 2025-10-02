package ventanas;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

public class Pentagono extends Figura {
    private Point inicio;
    private Point fin;

    public Pentagono(Point inicio) {
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

        int[] xs = new int[5];
        int[] ys = new int[5];

        
        
        
        // figura del pentagono
        for (int i = 0; i < 5; i++) {
           double angle = Math.toRadians( 72 * i - 20);
            xs[i] = (int) (centerX + radio * Math.cos(angle));
            ys[i] = (int) (centerY + radio * Math.sin(angle));
        }



        Polygon pentagono = new Polygon(xs, ys, 5);

        // Relleno
        g.setColor(colorRelleno);
        g.fillPolygon(pentagono);

        // Contorno
        g.setColor(colorLinea);
        g.drawPolygon(pentagono);
    }

    @Override
    public void actualizar(Point puntoActual) {
        this.fin = puntoActual;
    }
}