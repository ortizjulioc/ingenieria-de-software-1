package ventanas;

import java.awt.Graphics;
import java.awt.Point;

public class Rectangulo extends Figura {
    private Point inicio;
    private Point fin;

    public Rectangulo(Point inicio) {
        this.inicio = inicio;
        this.fin = inicio;
    }

    @Override
    public void dibujar(Graphics g) {
        int x = Math.min(inicio.x, fin.x);
        int y = Math.min(inicio.y, fin.y);
        int w = Math.abs(fin.x - inicio.x);
        int h = Math.abs(fin.y - inicio.y);

        // Relleno
        g.setColor(colorRelleno);
        g.fillRect(x, y, w, h);

        // Contorno
        g.setColor(colorLinea);
        g.drawRect(x, y, w, h);
    }

    @Override
    public void actualizar(Point puntoActual) {
        this.fin = puntoActual;
    }
}
