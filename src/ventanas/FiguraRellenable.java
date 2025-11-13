package ventanas;

import java.awt.Color;


public interface FiguraRellenable {
    void setColorRelleno(Color c);
    Color getColorRelleno();

    /** Permite consultar si es rellenable (por claridad semántica). */
    default boolean esRellenable() { return true; }
}
