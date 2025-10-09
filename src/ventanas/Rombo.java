package ventanas;

import java.awt.*;

public class Rombo extends Figura {
    private Point inicio;
    private Point fin;

    public Rombo(Point inicio) {
        this.inicio = inicio;
        this.fin = inicio;
    }

    @Override
    public void dibujar(Graphics g) {
        int x = Math.min(inicio.x, fin.x);
        int y = Math.min(inicio.y, fin.y);
        int w = Math.abs(fin.x - inicio.x);
        int h = Math.abs(fin.y - inicio.y);
        
        int centroX = x + w / 2;
        int centroY = y + h / 2;
        
        int[] puntosX = {centroX, x + w, centroX, x};
        int[] puntosY = {y, centroY, y + h, centroY};

        if (colorRelleno != null) {
            g.setColor(colorRelleno);
            g.fillPolygon(puntosX, puntosY, 4);
        }
        
        if (colorLinea != null) {
            g.setColor(colorLinea);
            g.drawPolygon(puntosX, puntosY, 4);
        }
    }

    @Override
    public void actualizar(Point puntoActual) { 
        this.fin = puntoActual; 
    }

    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        Rombo copia = new Rombo(new Point(inicio.x + dx, inicio.y + dy));
        copia.fin = new Point(fin.x + dx, fin.y + dy);
        copia.setColorLinea(colorLinea);
        copia.setColorRelleno(colorRelleno);
        return copia;
    }

    @Override
    public Rectangle getBounds() {
        int x = Math.min(inicio.x, fin.x);
        int y = Math.min(inicio.y, fin.y);
        int w = Math.abs(fin.x - inicio.x);
        int h = Math.abs(fin.y - inicio.y);
        return new Rectangle(x, y, w, h);
    }
}