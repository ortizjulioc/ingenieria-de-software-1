package ventanas;

import java.awt.*;
import java.awt.geom.Path2D;

public class Estrella extends Figura {
    private Point inicio;
    private Point fin;

    public Estrella(Point inicio) {
        this.inicio = inicio;
        this.fin = inicio;
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Shape estrella = crearEstrella();

        g2.setColor(colorRelleno);
        g2.fill(estrella);
        g2.setColor(colorLinea);
        g2.setStroke(new BasicStroke(2));
        g2.draw(estrella);
    }

    @Override
    public void actualizar(Point puntoActual) {
        this.fin = puntoActual;
    }

    private Shape crearEstrella() {
        double xCentro = (inicio.x + fin.x) / 2.0;
        double yCentro = (inicio.y + fin.y) / 2.0;
        double radioExterior = Math.max(Math.abs(fin.x - inicio.x), Math.abs(fin.y - inicio.y)) / 2.0;
        double radioInterior = radioExterior / 2.5;
        int puntas = 5;

        Path2D path = new Path2D.Double();
        double anguloInicial = -Math.PI / 2;
        double anguloPaso = Math.PI / puntas;

        for (int i = 0; i < puntas * 2; i++) {
            double radio = (i % 2 == 0) ? radioExterior : radioInterior;
            double x = xCentro + Math.cos(anguloInicial + i * anguloPaso) * radio;
            double y = yCentro + Math.sin(anguloInicial + i * anguloPaso) * radio;
            if (i == 0) path.moveTo(x, y);
            else path.lineTo(x, y);
        }
        path.closePath();
        return path;
    }

    @Override
    public Rectangle getBounds() {
        int x = Math.min(inicio.x, fin.x);
        int y = Math.min(inicio.y, fin.y);
        int w = Math.abs(fin.x - inicio.x);
        int h = Math.abs(fin.y - inicio.y);
        return new Rectangle(x, y, w, h);
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
