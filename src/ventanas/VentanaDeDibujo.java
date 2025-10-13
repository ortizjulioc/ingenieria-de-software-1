package ventanas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VentanaDeDibujo extends JFrame {

    private int xInicio, yInicio, xFin, yFin;
    private boolean dibujando = false;

    public VentanaDeDibujo(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (dibujando) {
                    g.setColor(Color.BLACK);
                    g.drawLine(xInicio, yInicio, xFin, yFin);
                }
            }
        };

        panel.setBackground(Color.WHITE);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                xInicio = e.getX();
                yInicio = e.getY();
                xFin = xInicio;
                yFin = yInicio;
                dibujando = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                xFin = e.getX();
                yFin = e.getY();
                dibujando = false;
                repaint();
            }
        });

       
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                xFin = e.getX();
                yFin = e.getY();
                repaint();  
            }
        });

        add(panel);
    }

    public static void main(String[] args) {
        new VentanaDeDibujo("Dibujar Línea en Tiempo Real").setVisible(true);
    }
}
