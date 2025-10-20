package ventanas;

import java.awt.Color;

/**
 * Marcador para figuras que pueden ser rellenadas.
 * Las herramientas (p. ej. Cubeta) y el panel usarán este tipo
 * para decidir si mostrar bounding box/handles y permitir relleno.
 */
public interface FiguraRellenable {
    void setColorRelleno(Color c);
    Color getColorRelleno();

    /** Permite consultar si es rellenable (por claridad semántica). */
    default boolean esRellenable() { return true; }
}
