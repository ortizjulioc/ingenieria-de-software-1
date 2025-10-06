package ventanas;

import java.awt.*;

public class Pentagono extends Figura {
    private Point inicio;
    private Point fin;

    public Pentagono(Point inicio) {
        this.inicio = inicio;
        this.fin = inicio;
    }

    private Polygon getPolygon() {
        int cx = (inicio.x + fin.x) / 2;
        int cy = (inicio.y + fin.y) / 2;
        int radio = Math.max(1, Math.min(Math.abs(fin.x - inicio.x), Math.abs(fin.y - inicio.y)) / 2);

        int[] xs = new int[5];
        int[] ys = new int[5];
        for (int i = 0; i < 5; i++) {
            double ang = 2 * Math.PI * i / 5 - Math.PI / 2;
            xs[i] = (int) (cx + radio * Math.cos(ang));
            ys[i] = (int) (cy + radio * Math.sin(ang));
        }
        return new Polygon(xs, ys, 5);
    }

    @Override
    public void dibujar(Graphics g) {
        Polygon p = getPolygon();
        g.setColor(colorRelleno);
        g.fillPolygon(p);
        g.setColor(colorLinea);
        g.drawPolygon(p);
    }

    @Override
    public void actualizar(Point puntoActual) { this.fin = puntoActual; }

    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        Pentagono copia = new Pentagono(new Point(inicio.x + dx, inicio.y + dy));
        copia.fin = new Point(fin.x + dx, fin.y + dy);
        copia.setColorLinea(colorLinea);
        copia.setColorRelleno(colorRelleno);
        return copia;
    }

    @Override
    public Rectangle getBounds() {
        return getPolygon().getBounds();
    }
}
