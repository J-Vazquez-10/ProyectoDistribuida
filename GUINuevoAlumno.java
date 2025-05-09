package codigo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class GUINuevoAlumno extends JDialog {
    private JPanel contenedorPrincipal;
    private JLabel Encabezado;
    private JLabel nameLabel;
    private JTextField nameTF;
    private JLabel apellido1Label;
    private JTextField apellido1TF;
    private JLabel apellido2Label;
    private JTextField apellido2TF;
    private JLabel edadLabel;
    private JTextField edadTF;
    private JLabel curpLabel;
    private JTextField curpTF;
    private JLabel emailLabel;
    private JTextField emailTF;
    private JButton ValidationButton;
    private JButton BackButton;

    public static final String UNIQUE_BINDING_NAME = "usuario.creacion";

    public GUINuevoAlumno(JFrame parent) {
        super(parent);
        setTitle("Registro");

        // Inicialización de componentes
        contenedorPrincipal = new JPanel();
        Encabezado = new JLabel("Registro de Alumno");

        nameLabel = new JLabel("Ingrese el nombre del alumno:");
        nameTF = new JTextField();

        apellido1Label = new JLabel("Ingrese el apellido paterno:");
        apellido1TF = new JTextField();

        apellido2Label = new JLabel("Ingrese el apellido materno:");
        apellido2TF = new JTextField();

        edadLabel = new JLabel("Ingrese la edad del alumno:");
        edadTF = new JTextField();

        curpLabel = new JLabel("Ingrese el CURP del alumno:");
        curpTF = new JTextField();

        emailLabel = new JLabel("Ingrese el Email del alumno:");
        emailTF = new JTextField();

        ValidationButton = new JButton("Registrar");
        BackButton = new JButton("Regresar");

        setContentPane(contenedorPrincipal);

        Color fondoAzulTenue = new Color(220, 235, 250);
        contenedorPrincipal.setBackground(fondoAzulTenue);

        Encabezado.setFont(new Font("Segoe UI", Font.BOLD, 22));
        Encabezado.setHorizontalAlignment(SwingConstants.CENTER);

        // Panel de datos
        JPanel datosPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        datosPanel.setBorder(BorderFactory.createTitledBorder("Datos del alumno"));
        datosPanel.setOpaque(false);

        datosPanel.add(nameLabel);      datosPanel.add(nameTF);
        datosPanel.add(apellido1Label); datosPanel.add(apellido1TF);
        datosPanel.add(apellido2Label); datosPanel.add(apellido2TF);
        datosPanel.add(edadLabel);      datosPanel.add(edadTF);
        datosPanel.add(curpLabel);      datosPanel.add(curpTF);
        datosPanel.add(emailLabel);     datosPanel.add(emailTF);

        for (Component c : datosPanel.getComponents()) {
            if (c instanceof JTextField) {
                c.setFont(new Font("Segoe UI", Font.BOLD, 14));
            }
        }


        contenedorPrincipal.setLayout(new BorderLayout(10, 10));
        contenedorPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contenedorPrincipal.add(Encabezado, BorderLayout.NORTH);
        contenedorPrincipal.add(datosPanel, BorderLayout.CENTER);

        ValidationButton.setBackground(new Color(33, 150, 243));
        ValidationButton.setForeground(Color.WHITE);
        BackButton.setBackground(new Color(200, 200, 200));
        BackButton.setForeground(Color.BLACK);

        JPanel botonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        botonPanel.setOpaque(false);
        botonPanel.add(BackButton);
        botonPanel.add(ValidationButton);
        contenedorPrincipal.add(botonPanel, BorderLayout.SOUTH);

        setMinimumSize(new Dimension(650, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Acciones
        ValidationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (apellido1TF.getText().length() < 3 || apellido2TF.getText().length() < 3 ||
                        edadTF.getText().length() < 1 || !esEntero(edadTF.getText()) ||
                        emailTF.getText().length() < 3 || nameTF.getText().length() < 3 ||
                        curpTF.getText().length() < 3) {

                    JOptionPane.showMessageDialog(GUINuevoAlumno.this,
                            "Primero ingrese todos los datos correctamente.");
                } else {
                    Alumno alumno = new Alumno();
                    alumno.setNombre(nameTF.getText());
                    alumno.setApePaterno(apellido1TF.getText());
                    alumno.setApeMaterno(apellido2TF.getText());
                    alumno.setEdad(Integer.parseInt(edadTF.getText()));
                    alumno.setCurp(curpTF.getText());
                    alumno.setEmail(emailTF.getText());

                    try {
                        final Registry registry = LocateRegistry.getRegistry("172.31.2.9", 3333);
                        Interfaz interfaz = (Interfaz) registry.lookup(UNIQUE_BINDING_NAME);

                        String resultado = interfaz.crearUsuario(alumno);

                        JOptionPane.showMessageDialog(GUINuevoAlumno.this,
                                resultado, "Usuario creado", JOptionPane.INFORMATION_MESSAGE);

                    } catch (RemoteException | NotBoundException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(GUINuevoAlumno.this,
                                "Error al conectar con el servidor: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    for (Component c : datosPanel.getComponents()) {
                        if (c instanceof JTextField) {
                            ((JTextField) c).setText("");
                        }
                    }
                }
            }

            private boolean esEntero(String texto) {
                try {
                    Integer.parseInt(texto);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        });

        BackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }
}
