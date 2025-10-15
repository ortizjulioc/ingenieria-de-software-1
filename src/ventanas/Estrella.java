package ventanas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    private Polygon poly;
    private boolean seleccionada = false;
    
    public Estrella(Point inicio){
        this.inicio = inicio;
        this.fin = new Point(inicio);
        generarPuntos();
        actualizarBounds();    
    }
    
    private void generarPuntos() {
        int cx = (inicio.x + fin.x) / 2;
        int cy = (inicio.y + fin.y) / 2;
        int radioExterior = Math.max(1, Math.min(Math.abs(fin.x - inicio.x), Math.abs(fin.y - inicio.y)) / 2);
        int radioInterior = radioExterior / 2;

        poly = new Polygon();
         for (int i = 0; i < 10; i++) {
            double ang = Math.PI / 2 + i * Math.PI / 5;
            int r = (i % 2 == 0) ? radioExterior : radioInterior;
            int x = (int) (cx + r * Math.cos(ang));
            int y = (int) (cy - r * Math.sin(ang));
            poly.addPoint(x, y);
        }
    }

    public void setSeleccionada(boolean seleccionada) {
        this.seleccionada = seleccionada;
    }
    @Override
    public void dibujar(Graphics g) {
        generarPuntos();
        g.setColor(colorRelleno);
        g.fillPolygon(poly);
        g.setColor(colorLinea);
        g.drawPolygon(poly);

        if (seleccionada) {
            Rectangle r = poly.getBounds();
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.RED);
            float[] dash = {5f, 5f};
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash, 0f));
            g2.drawRect(r.x - 3, r.y - 3, r.width + 6, r.height + 6);
            
            //llama al contorno desde Figura
           dibujarContornoSeleccion(g);
        }
    }

    @Override
    public void actualizar(Point puntoActual) {
        this.fin = puntoActual;
        actualizarBounds();
    }

//    private Shape crearEstrella() {
//        double xCentro = (inicio.x + fin.x) / 2.0;
//        double yCentro = (inicio.y + fin.y) / 2.0;
//        double radioExterior = Math.max(Math.abs(fin.x - inicio.x), Math.abs(fin.y - inicio.y)) / 2.0;
//        double radioInterior = radioExterior / 2.5;
//        int puntas = 5;
//
//        Path2D path = new Path2D.Double();
//        double anguloInicial = -Math.PI / 2;
//        double anguloPaso = Math.PI / puntas;
//
//        for (int i = 0; i < puntas * 2; i++) {
//            double radio = (i % 2 == 0) ? radioExterior : radioInterior;
//            double x = xCentro + Math.cos(anguloInicial + i * anguloPaso) * radio;
//            double y = yCentro + Math.sin(anguloInicial + i * anguloPaso) * radio;
//            if (i == 0) path.moveTo(x, y);
//            else path.lineTo(x, y);
//        }
//        path.closePath();
//        return path;
//    }
    @Override
    public Rectangle getBounds() {
        generarPuntos();
        return poly.getBounds();
    }

    @Override
    public Figura clonarConDesplazamiento(int dx, int dy) {
        Estrella copia = new Estrella(new Point(inicio.x + dx, inicio.y + dy));
        copia.fin = new Point(fin.x + dx, fin.y + dy);
        copia.setColorLinea(this.colorLinea);
        copia.setColorRelleno(this.colorRelleno);
        return copia;
    }
}
