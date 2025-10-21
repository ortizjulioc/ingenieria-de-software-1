package ventanas;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Modelo (M de MVC)
 * Mantiene el estado global de edición: herramienta activa, colores, grosores, etc.
 * Notifica cambios vía PropertyChangeSupport para futuras vistas/controles que se suscriban.
 */
public class ModeloDibujo {

    public enum Herramienta {
        SELECCION,
        DIBUJO_LIBRE,
        BORRADOR,
        CUBETA,
        // Figuras:
        LINEA, RECTANGULO, CIRCULO, OVALO, TRIANGULO,
        CORAZON, ROMBO, TRAPECIO, PENTAGONO, HEXAGONO, ESTRELLA,
        FLECHA_ARRIBA, FLECHA_ABAJO, FLECHA_IZQUIERDA, FLECHA_DERECHA,
        NUBE, ARCO
    }

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private Herramienta herramienta = Herramienta.SELECCION;
    private Color colorLinea = Color.BLACK;
    private Color colorRelleno = Color.WHITE;
    private float grosorPincel = 2f;
    private float tamBorrador = 12f;
    private Color colorBorrador = Color.WHITE;

    private boolean modificado = false;

    public Herramienta getHerramienta() { return herramienta; }
    public void setHerramienta(Herramienta nueva) {
        Herramienta vieja = this.herramienta;
        this.herramienta = nueva;
        pcs.firePropertyChange("herramienta", vieja, nueva);
    }

    public Color getColorLinea() { return colorLinea; }
    public void setColorLinea(Color c) {
        Color old = this.colorLinea;
        this.colorLinea = c;
        pcs.firePropertyChange("colorLinea", old, c);
    }

    public Color getColorRelleno() { return colorRelleno; }
    public void setColorRelleno(Color c) {
        Color old = this.colorRelleno;
        this.colorRelleno = c;
        pcs.firePropertyChange("colorRelleno", old, c);
    }

    public float getGrosorPincel() { return grosorPincel; }
    public void setGrosorPincel(float g) {
        float old = this.grosorPincel;
        this.grosorPincel = g;
        pcs.firePropertyChange("grosorPincel", old, g);
    }

    public float getTamBorrador() { return tamBorrador; }
    public void setTamBorrador(float t) {
        float old = this.tamBorrador;
        this.tamBorrador = t;
        pcs.firePropertyChange("tamBorrador", old, t);
    }

    public Color getColorBorrador() { return colorBorrador; }
    public void setColorBorrador(Color c) {
        Color old = this.colorBorrador;
        this.colorBorrador = c;
        pcs.firePropertyChange("colorBorrador", old, c);
    }

    public boolean isModificado() { return modificado; }
    public void setModificado(boolean modificado) {
        boolean old = this.modificado;
        this.modificado = modificado;
        pcs.firePropertyChange("modificado", old, modificado);
    }

    // Observers
    public void addPropertyChangeListener(PropertyChangeListener l) { pcs.addPropertyChangeListener(l); }
    public void removePropertyChangeListener(PropertyChangeListener l) { pcs.removePropertyChangeListener(l); }
}
