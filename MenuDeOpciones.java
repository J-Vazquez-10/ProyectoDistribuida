package codigo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MenuDeOpciones extends JFrame {
    private JButton add_button;
    private JButton info_button;
    private JButton remove_button;
    private JButton exit_button;
    private JButton calificar_button;
    private JLabel titulo;

    public MenuDeOpciones(JFrame parent) {
        setTitle("Menú de Opciones");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Fondo con gradiente
        JPanel mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(63, 94, 251);
                Color color2 = new Color(252, 70, 107);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Título estilizado
        titulo = new JLabel("\uD83D\uDCDA Menú Principal", JLabel.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 30));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        mainPanel.add(titulo, BorderLayout.NORTH);

        // Panel de botones con transparencia
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 15, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        // Botones estilizados
        add_button = createStyledButton(" ✏\uFE0F Añadir Alumno");
        info_button = createStyledButton("\uD83D\uDCD6 Mostrar Alumnos");
        remove_button = createStyledButton("❌ Dar de Baja");
        exit_button = createStyledButton("Salir");
        calificar_button = createStyledButton("Calificar");

        buttonPanel.add(add_button);
        buttonPanel.add(calificar_button);
        buttonPanel.add(info_button);
        buttonPanel.add(remove_button);
        buttonPanel.add(exit_button);

        add_button.addActionListener(e -> {
            GUINuevoAlumno nuevo = new GUINuevoAlumno(null);
            nuevo.setVisible(true);
        });

        info_button.addActionListener(e -> {
            GUIMostrarInfo info = new GUIMostrarInfo(null);
            info.setVisible(true);
        });

        remove_button.addActionListener(e -> {
            String UNIQUE_BINDING_NAME = "usuario.creacion";
            String usuario = JOptionPane.showInputDialog(MenuDeOpciones.this, "Ingrese el usuario del alumno a dar de baja:");
            if (usuario != null) {
                try {
                    final Registry registry = LocateRegistry.getRegistry("172.31.2.9",3333);
                    Interfaz interfaz = (Interfaz) registry.lookup(UNIQUE_BINDING_NAME);
                    JOptionPane.showMessageDialog(MenuDeOpciones.this, interfaz.borrarUsuario(usuario));

                } catch (RemoteException | NotBoundException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(MenuDeOpciones.this,
                            "Error al conectar con el servidor: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        exit_button.addActionListener(e -> System.exit(0));

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);

        setVisible(true);
    }

    // Método para crear botones con estilo moderno
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 144, 255)); // Azul sólido moderno (DodgerBlue)
        button.setFocusPainted(false);
        button.setOpaque(true); // Muy importante para evitar transparencias y glitches
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto de hover con cambio de color
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 120, 215)); // Azul más oscuro al pasar el mouse
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 144, 255)); // Vuelve al color original
            }
        });

        return button;
    }

}
