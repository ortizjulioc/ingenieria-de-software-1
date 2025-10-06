package ventanas;

import java.awt.*;

public class Linea extends Figura {
    private Point puntoInicial;
    private Point puntoFinal;

    public Linea(Point puntoInicial) {
        this.puntoInicial = puntoInicial;
        this.puntoFinal = puntoInicial;
    }

    @Override
    public void dibujar(Graphics g) {
        g.setColor(colorLinea);
        g.drawLine(puntoInicial.x, puntoInicial.y, puntoFinal.x, puntoFinal.y);
    }

    @Override
    public void actualizar(Point puntoFinal) { this.puntoFinal = puntoFinal; }

    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        Linea copia = new Linea(new Point(puntoInicial.x + dx, puntoInicial.y + dy));
        copia.puntoFinal = new Point(puntoFinal.x + dx, puntoFinal.y + dy);
        copia.setColorLinea(colorLinea);
        copia.setColorRelleno(colorRelleno);
        return copia;
    }

    @Override
    public Rectangle getBounds() {
        int x = Math.min(puntoInicial.x, puntoFinal.x);
        int y = Math.min(puntoInicial.y, puntoFinal.y);
        int w = Math.abs(puntoFinal.x - puntoInicial.x);
        int h = Math.abs(puntoFinal.y - puntoInicial.y);
        // margen pequeño para poder hacer click en líneas finas
        int pad = 6;
        return new Rectangle(x - pad/2, y - pad/2, Math.max(1, w + pad), Math.max(1, h + pad));
    }
}
