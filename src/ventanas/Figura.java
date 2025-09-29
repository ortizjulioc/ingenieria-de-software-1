package ventanas;

import java.awt.*;

public abstract class Figura {
    protected Color colorLinea = Color.BLACK;
    protected Color colorRelleno = Color.WHITE;

    public abstract void dibujar(Graphics g);
    public abstract void actualizar(Point puntoActual);

    public void setColorLinea(Color c) { this.colorLinea = c; }
    public Color getColorLinea() { return colorLinea; }

    public void setColorRelleno(Color c) { this.colorRelleno = c; }
    public Color getColorRelleno() { return colorRelleno; }
}
