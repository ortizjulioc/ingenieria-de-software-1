package ventanas;

import java.awt.*;

public class FlechaArriba extends Figura {
    private Point inicio;
    private Point fin;

    public FlechaArriba(Point inicio) {
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
        
        // Puntos de la flecha (punta arriba)
        int[] puntosX = {
            centroX,           // punta de la flecha
            x + w,             // esquina inferior derecha
            x + w * 3/4,       // base derecha
            x + w * 3/4,       // esquina derecha del palo
            x + w * 1/4,       // esquina izquierda del palo
            x + w * 1/4,       // base izquierda
            x                  // esquina inferior izquierda
        };
        
        int[] puntosY = {
            y,                 // punta de la flecha
            y + h * 2/3,       // esquina inferior derecha
            y + h * 2/3,       // base derecha
            y + h,             // esquina derecha del palo
            y + h,             // esquina izquierda del palo
            y + h * 2/3,       // base izquierda
            y + h * 2/3        // esquina inferior izquierda
        };

        if (colorRelleno != null) {
            g.setColor(colorRelleno);
            g.fillPolygon(puntosX, puntosY, 7);
        }
        
        if (colorLinea != null) {
            g.setColor(colorLinea);
            g.drawPolygon(puntosX, puntosY, 7);
        }
    }

    @Override
    public void actualizar(Point puntoActual) { 
        this.fin = puntoActual; 
    }

    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        FlechaArriba copia = new FlechaArriba(new Point(inicio.x + dx, inicio.y + dy));
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