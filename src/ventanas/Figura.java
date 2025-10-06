package ventanas;

import java.awt.*;

public abstract class Figura {
    protected Color colorLinea = Color.BLACK;
    protected Color colorRelleno = Color.WHITE;

    public abstract void dibujar(Graphics g);
    public abstract void actualizar(Point puntoActual);

    /** Clona la figura desplazando todas sus coordenadas (dx, dy) */
    public abstract Figura clonarConDesplazamiento(int dx, int dy);

    /** Devuelve un rectángulo que encierra la figura (para selección/click) */
    public abstract Rectangle getBounds();

    public void setColorLinea(Color c) { this.colorLinea = c; }
    public Color getColorLinea() { return colorLinea; }

    public void setColorRelleno(Color c) { this.colorRelleno = c; }
    public Color getColorRelleno() { return colorRelleno; }
}
