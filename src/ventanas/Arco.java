package ventanas;

import java.awt.*;

public class Arco extends Figura {
    private Point inicio;
    private Point fin;

    public Arco(Point inicio) {
        this.inicio = inicio;
        this.fin = inicio;
    }

    @Override
    public void dibujar(Graphics g) {
        int x = Math.min(inicio.x, fin.x);
        int y = Math.min(inicio.y, fin.y);
        int w = Math.abs(fin.x - inicio.x);
        int h = Math.abs(fin.y - inicio.y);
        
        Graphics2D g2d = (Graphics2D) g;
        
        // Dibujar solo el arco (sin relleno, solo línea)
        if (colorLinea != null) {
            g2d.setColor(colorLinea);
            g2d.setStroke(new BasicStroke(2));
            // Dibujar un arco de 180 grados (medio círculo)
            g2d.drawArc(x, y, w, h, 0, 180);
        }
    }

    @Override
    public void actualizar(Point puntoActual) { 
        this.fin = puntoActual; 
    }

    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        Arco copia = new Arco(new Point(inicio.x + dx, inicio.y + dy));
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