package ventanas;

import java.awt.Graphics;
import java.awt.Point;

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
        g.drawLine(puntoInicial.x, puntoInicial.y,
                   puntoFinal.x, puntoFinal.y);
    }

    @Override
    public void actualizar(Point puntoFinal) {
        this.puntoFinal = puntoFinal;
    }

    public Point getPuntoFinal() { return puntoFinal; }
    public void setPuntoFinal(Point puntoFinal) { this.puntoFinal = puntoFinal; }

    public Point getPuntoInicial() { return puntoInicial; }
    public void setPuntoInicial(Point puntoInicial) { this.puntoInicial = puntoInicial; }
}
