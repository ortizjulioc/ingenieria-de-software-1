
package ventanas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Ventana extends JFrame{
    public Ventana() {
        //COMPONENTES DE LA GUI (GRAPHICAL USER INTERFACE)
        //java.awt(Frame,Label,TextField,TextArea,ComboBox,Button,Panel)
        //javax.swing(JFrame,JLabel,JTextField,JTextArea,JComboBox,JButton, JPanel)
        this.setTitle("mi sas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        setLayout(new GridLayout(0,1));
        
        //administradores de esquema (Layout managers)
        //BorderLayout, FlowLayout, GridLayout
        JButton boton1 = new JButton("Boton 1");
        JButton boton2 = new JButton("Boton 2");
        JButton boton3 = new JButton("Boton 3");
        JButton boton4 = new JButton("Boton 4");
        JButton boton5 = new JButton("Boton 5");
        
        add(boton1, BorderLayout.NORTH);
        add(boton2, BorderLayout.WEST);
        add(boton3, BorderLayout.CENTER);
        add(boton4, BorderLayout.EAST);
        add(boton5, BorderLayout.SOUTH);
        
        //pasos para manejar eventos en java
        //1- Crear una clase que represente al manejador de eventos
        //2- Implementar la interfaz apropiada para el evento que se quiere manejar
        //3- Decirle al componente de la GUI a que objeto de la clase de los pasos 1 y 2 avisarle cuando el evento suceda
        
        class Manejador implements MouseListener {

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Se hizo click en " + ((JButton)e.getSource()).getText());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("El mouse se presiono en " + ((JButton)e.getSource()).getText());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("El mouse se libero en " + ((JButton)e.getSource()).getText());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("El mouse entró en " + ((JButton)e.getSource()).getText());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("El mouse salio de " + ((JButton)e.getSource()).getText());
            }  
        }
        Manejador manejador = new Manejador();
        
        boton1.addMouseListener(manejador);
        boton2.addMouseListener(manejador);
        boton3.addMouseListener(manejador);
        boton4.addMouseListener(manejador);
        boton5.addMouseListener(manejador);
    }
    
    public static void main(String[] args) {
        Ventana miVentana = new Ventana();
        miVentana.setVisible(true); 
    }  
}
