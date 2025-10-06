package ventanas;

import java.awt.*;

public class Rectangulo extends Figura {
    private Point inicio;
    Point fin; // paquete para facilitar clon si lo necesitas externamente

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

        g.setColor(colorRelleno);
        g.fillRect(x, y, w, h);

        g.setColor(colorLinea);
        g.drawRect(x, y, w, h);
    }

    @Override
    public void actualizar(Point puntoActual) { this.fin = puntoActual; }

    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        Rectangulo copia = new Rectangulo(new Point(inicio.x + dx, inicio.y + dy));
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
