package ventanas;

import java.awt.*;

public abstract class Figura {
    protected Color colorLinea = Color.BLACK;
    protected Color colorRelleno = Color.WHITE;
    protected Rectangle bounds = new Rectangle();

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
    
    protected boolean seleccionada = false;

    public void setSeleccionada(boolean seleccionada) {
    this.seleccionada = seleccionada;
    }

    public boolean isSeleccionada() {
    return seleccionada;
    } 
    
    protected void dibujarContornoSeleccion(Graphics g) {
    if (seleccionada) {
        Graphics2D g2 = (Graphics2D) g;
        Rectangle r = getBounds();
        float[] dash = {5f, 5f}; // crea una linea discontinua tipo ---
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash, 0f));
        g2.drawRect(r.x - 3, r.y - 3, r.width + 6, r.height + 6);
    }
    }
         public void mover(int dx, int dy) {
             bounds.x += dx;
             bounds.y += dy;
             actualizarBounds();
            }

         public void redimensionar(int dx, int dy) {
            bounds.width += dx;
            bounds.height += dy;
          if (bounds.width < 5) bounds.width = 5;
          if (bounds.height < 5) bounds.height = 5;
            actualizar(new Point(bounds.x + bounds.width, bounds.y + bounds.height));
          }
       protected void actualizarBounds() {
         this.bounds = getBounds();
            }
}
