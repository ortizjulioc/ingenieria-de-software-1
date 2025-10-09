package ventanas;

import java.awt.*;

public class FlechaAbajo extends Figura {
    private Point inicio;
    private Point fin;

    public FlechaAbajo(Point inicio) {
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
        
        // Puntos de la flecha (punta abajo)
        int[] puntosX = {
            centroX,           // punta de la flecha
            x + w,             // esquina superior derecha
            x + w * 3/4,       // base derecha
            x + w * 3/4,       // esquina derecha del palo
            x + w * 1/4,       // esquina izquierda del palo
            x + w * 1/4,       // base izquierda
            x                  // esquina superior izquierda
        };
        
        int[] puntosY = {
            y + h,             // punta de la flecha
            y + h * 1/3,       // esquina superior derecha
            y + h * 1/3,       // base derecha
            y,                 // esquina derecha del palo
            y,                 // esquina izquierda del palo
            y + h * 1/3,       // base izquierda
            y + h * 1/3        // esquina superior izquierda
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
        FlechaAbajo copia = new FlechaAbajo(new Point(inicio.x + dx, inicio.y + dy));
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