package ventanas;

import java.awt.Color;


public interface FiguraRellenable {
    void setColorRelleno(Color c);
    Color getColorRelleno();

    default boolean esRellenable() { return true; }
}
