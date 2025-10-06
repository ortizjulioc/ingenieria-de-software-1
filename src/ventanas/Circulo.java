package ventanas;

import java.awt.*;

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
        int d = Math.min(w, h);

        g.setColor(colorRelleno);
        g.fillOval(x, y, d, d);
        g.setColor(colorLinea);
        g.drawOval(x, y, d, d);
    }

    @Override
    public void actualizar(Point puntoActual) { this.fin = puntoActual; }

    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        Circulo copia = new Circulo(new Point(inicio.x + dx, inicio.y + dy));
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
        int d = Math.min(w, h);
        return new Rectangle(x, y, d, d);
    }
}
