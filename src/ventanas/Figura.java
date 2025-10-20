package ventanas;

import java.awt.*;
import java.io.Serializable;

/**
 * Clase base de todas las figuras.
 * Incluye color de línea, color de relleno y un bounds común para
 * selección/redimensionado. Es serializable para Guardar/Abrir proyecto.
 */
public abstract class Figura implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Color colorLinea = Color.BLACK;
    protected Color colorRelleno = Color.WHITE;

    /** Rectángulo de control de la figura (para selección/redimensionado). */
    protected Rectangle bounds = new Rectangle(0, 0, 0, 0);

    /** Dibuja la figura. */
    public abstract void dibujar(Graphics g);

    /** Actualiza la figura con el punto actual (durante la creación/redimensionado). */
    public abstract void actualizar(Point puntoActual);

    /** Desplaza la figura (para moverla al arrastrar). */
    public abstract void desplazar(int dx, int dy);

    /** Clona la figura desplazándola (dx, dy). */
    public abstract Figura clonarConDesplazamiento(int dx, int dy);

    /** Devuelve el rectángulo que encierra la figura. */
    public Rectangle getBounds() { return bounds; }

    /** Define bounds normalizados (independiente de la dirección del arrastre). */
    protected void setBoundsNormalized(int x1, int y1, int x2, int y2) {
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int w = Math.abs(x2 - x1);
        int h = Math.abs(y2 - y1);
        this.bounds = new Rectangle(x, y, w, h);
    }

    public void setColorLinea(Color c) { this.colorLinea = c; }
    public Color getColorLinea() { return colorLinea; }

    public void setColorRelleno(Color c) { this.colorRelleno = c; }
    public Color getColorRelleno() { return colorRelleno; }
}
