package ventanas;

import java.awt.*;

public class FlechaIzquierda extends Figura {
    private Point inicio;
    private Point fin;

    public FlechaIzquierda(Point inicio) {
        this.inicio = inicio;
        this.fin = inicio;
    }

    @Override
    public void dibujar(Graphics g) {
        int x = Math.min(inicio.x, fin.x);
        int y = Math.min(inicio.y, fin.y);
        int w = Math.abs(fin.x - inicio.x);
        int h = Math.abs(fin.y - inicio.y);
        
        int centroY = y + h / 2;
        
        int[] puntosX = {
            x,                 // punta de la flecha
            x + w * 2/3,       // base de la punta
            x + w * 2/3,       // esquina del palo
            x + w,             // fin del palo
            x + w,             // otra esquina del palo
            x + w * 2/3,       // base derecha
            x + w * 2/3        // base de la punta
        };
        
        int[] puntosY = {
            centroY,           // punta de la flecha
            y,                 // base superior
            y + h * 1/4,       // esquina superior del palo
            y + h * 1/4,       // fin superior del palo
            y + h * 3/4,       // fin inferior del palo
            y + h * 3/4,       // esquina inferior del palo
            y + h              // base inferior
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
        FlechaIzquierda copia = new FlechaIzquierda(new Point(inicio.x + dx, inicio.y + dy));
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