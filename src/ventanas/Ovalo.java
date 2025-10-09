/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ventanas;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author radhamerymartinez
 */
public class Ovalo extends Figura {
    private Point inicio;
    private Point fin;

    public Ovalo(Point puntoInicial) {
        this.inicio = puntoInicial;
        this.fin = puntoInicial;
    }
    
    @Override
    public void dibujar(Graphics g) {
        int x = Math.min(inicio.x, fin.x);
        int y = Math.min(inicio.y, fin.y);
        int ancho = Math.abs(inicio.x - fin.x);
        int alto = Math.abs(inicio.y - fin.y);

        g.setColor(colorRelleno);
        g.fillOval(x, y, ancho, alto);
        g.setColor(colorLinea);
        g.drawOval(x, y, ancho, alto);
    }

    @Override
    public void actualizar(Point puntoActual) {
        this.fin = puntoActual;
    }

    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        Ovalo copia = new Ovalo(new Point(inicio.x + dx, inicio.y + dy));
        copia.fin = new Point(fin.x + dx, fin.y + dy);
        copia.setColorLinea(colorLinea);
        copia.setColorRelleno(colorRelleno);
        return copia;
    }

    @Override
    public Rectangle getBounds() {
        int x = Math.min(inicio.x, fin.x);
        int y = Math.min(inicio.y, fin.y);
        int ancho = Math.abs(inicio.x - fin.x);
        int alto = Math.abs(inicio.y - fin.y);
        return new Rectangle(x, y, ancho, alto);
    }
    
}
