/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ventanas;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

/**
 *
 * @author Yaneyri
 */


public class Estrella extends Figura {
    
    private Point inicio;
    private Point fin;
    
    public Estrella(Point inicio){
        this.inicio = inicio;
        this.fin = fin;
    }
    
    private Polygon getPolygon() {
        int cx = (inicio.x + fin.x) / 2;
        int cy = (inicio.y + fin.y) / 2;
        int radioExterior = Math.max(1, Math.min(Math.abs(fin.x - inicio.x), Math.abs(fin.y - inicio.y)) / 2);
        int radioInterior = radioExterior / 2;

        Polygon p = new Polygon();
        for (int i = 0; i < 10; i++) {
            double ang = Math.PI / 2 + i * Math.PI / 5;
            int r = (i % 2 == 0) ? radioExterior : radioInterior;
            int x = (int) (cx + r * Math.cos(ang));
            int y = (int) (cy - r * Math.sin(ang));
            p.addPoint(x, y);
        }
        return p;
    }
    
    @Override
    public void dibujar(Graphics g) {
        Polygon poly = getPolygon();
        g.setColor(colorRelleno);
        g.fillPolygon(poly);
        g.setColor(colorLinea);
        g.drawPolygon(poly);
    }

    @Override
    public void actualizar(Point puntoActual) {
        this.fin = puntoActual;
    }

    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        Estrella copia = new Estrella(new Point(inicio.x + dx, inicio.y + dy));
        copia.fin = new Point(fin.x + dx, fin.y + dy);
        copia.setColorLinea(colorLinea);
        copia.setColorRelleno(colorRelleno);
        return copia;
    }

    @Override
    public Rectangle getBounds() {
        return getPolygon().getBounds();
    } 
}
