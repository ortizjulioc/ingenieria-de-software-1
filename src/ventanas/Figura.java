package ventanas;

import java.awt.*;
import java.io.Serializable;


public abstract class Figura implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Color colorLinea = Color.BLACK;
    protected Color colorRelleno = Color.WHITE;

   
    protected Rectangle bounds = new Rectangle(0, 0, 0, 0);

   
    public abstract void dibujar(Graphics g);

    
    public abstract void actualizar(Point puntoActual);

   
    public abstract void desplazar(int dx, int dy);

   
    public abstract Figura clonarConDesplazamiento(int dx, int dy);

   
    public Rectangle getBounds() { return bounds; }

    
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
