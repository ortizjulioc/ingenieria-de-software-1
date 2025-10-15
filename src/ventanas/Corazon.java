/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ventanas;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Path2D;

public class Corazon extends Figura {
    private Point inicio;
    private Point fin;

    public Corazon(Point inicio) {
        this.inicio = inicio;
        this.fin = inicio;
    }

   
    @Override
    public void dibujar(Graphics g) {
        int x = Math.min(inicio.x, fin.x);
        int y = Math.min(inicio.y, fin.y);
        int w = Math.abs(fin.x - inicio.x);
        int h = Math.abs(fin.y - inicio.y);
        
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
                           java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        
        Path2D path = new Path2D.Double();
        
        double cx = x + w / 2.0;
        double topY = y + h * 0.3;
        
        // Comenzar desde la punta inferior (centro abajo)
        path.moveTo(cx, y + h);
        
        // CURVA 1: Lado izquierdo - desde la punta hasta arriba a la izquierda
        path.curveTo(
            cx - w * 0.1, y + h * 0.9,     // Control 1
            x, y + h * 0.6,                 // Control 2
            x, topY                         // Fin
        );
        
        // CURVA 2: Lóbulo superior izquierdo - forma el círculo/arco izquierdo
        path.curveTo(
            x, y - h * 0.1,                 // Control 1 (sube)
            cx - w * 0.15, y - h * 0.1,     // Control 2 (arriba)
            cx, topY                        // Fin (centro superior)
        );
        
        // CURVA 3: Lóbulo superior derecho - forma el círculo/arco derecho
        path.curveTo(
            cx + w * 0.15, y - h * 0.1,     // Control 1 (arriba)
            x + w, y - h * 0.1,             // Control 2 (sube)
            x + w, topY                     // Fin
        );
        
        // CURVA 4: Lado derecho - desde arriba a la derecha hasta la punta
        path.curveTo(
            x + w, y + h * 0.6,             // Control 1
            cx + w * 0.1, y + h * 0.9,      // Control 2
            cx, y + h                       // Fin (regresa a la punta)
        );
        
        path.closePath();
        
        // Rellenar
        g2.setColor(getColorRelleno());
        g2.fill(path);
        
        // Dibujar contorno
        g2.setColor(getColorLinea());
        g2.setStroke(new java.awt.BasicStroke(2));
        g2.draw(path);
    }
    
    @Override
    public void actualizar(Point puntoActual) {
        this.fin = puntoActual;
    }
     
    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        Corazon copia = new Corazon(new Point(inicio.x + dx, inicio.y + dy));
        copia.fin = new Point(fin.x + dx, fin.y + dy);
        copia.setColorLinea(getColorLinea());
        copia.setColorRelleno(getColorRelleno());
        return copia;
    }
    
    @Override
    public Rectangle getBounds() {
        int x = Math.min(inicio.x, fin.x);
        int y = Math.min(inicio.y, fin.y);
        int w = Math.abs(fin.x - inicio.x);
        int h = Math.abs(fin.y - inicio.y);
        return new Rectangle(x, y, w, h);
    }
}