package ventanas;

import java.awt.Graphics;
import java.awt.Point;

public class Circulo extends Figura {
    private Point inicio;
    private Point fin;

    public Circulo(Point inicio) {
        this.inicio = inicio;
        this.fin = inicio;
    }

    @Override
    public void dibujar(Graphics g) {
        int x = Math.min(inicio.x, fin.x);
        int y = Math.min(inicio.y, fin.y);
        int w = Math.abs(fin.x - inicio.x);
        int h = Math.abs(fin.y - inicio.y);

        // Tomamos el menor de w y h para que sea círculo
        int d = Math.min(w, h);

        // Relleno
        g.setColor(colorRelleno);
        g.fillOval(x, y, d, d);

        // Contorno
        g.setColor(colorLinea);
        g.drawOval(x, y, d, d);
    }

    @Override
    public void actualizar(Point puntoActual) {
        this.fin = puntoActual;
    }
}
