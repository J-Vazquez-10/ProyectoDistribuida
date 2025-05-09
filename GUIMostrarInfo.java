package codigo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class GUIMostrarInfo extends JDialog {
    private JTable datos;
    private JLabel titulo;

    public static final String UNIQUE_BINDING_NAME = "usuario.creacion";

    public GUIMostrarInfo(JFrame parent) {
        setTitle("Mostrar Lista de Alumnos");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Título con estilo
        titulo = new JLabel("Lista de Alumnos", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setOpaque(true);
        titulo.setBackground(new Color(30, 144, 255)); // Azul brillante
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        // Nombres de las columnas
        String[] columnNames = {
                "Nombre", "Apellido Paterno", "Apellido Materno",
                "Edad", "Email", "CURP", "Username", "Password", "Promedio"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try {
            final Registry registry = LocateRegistry.getRegistry("172.31.2.9",3333);
            Interfaz interfaz = (Interfaz) registry.lookup(UNIQUE_BINDING_NAME);
            List<Alumno> alumnos = interfaz.mostrarInfo();

            // Llenar modelo de la tabla
            for (Alumno alumno : alumnos) {
                String promedio;
                if (alumno.kardex==null){
                    promedio="N/A";
                }else {
                    promedio = String.valueOf(alumno.kardex.getPromedio()); // Convierte float a String
                }
                Object[] rowData = {
                        alumno.getNombre(),
                        alumno.getApePaterno(),
                        alumno.getApeMaterno(),
                        alumno.getEdad(),
                        alumno.getEmail(),
                        alumno.getCurp(),
                        alumno.getUsername(),
                        alumno.getPassword(),
                        promedio

                };
                model.addRow(rowData);
            }

            // Configurar la JTable
            datos = new JTable(model);
            datos.setRowHeight(25);
            datos.getTableHeader().setReorderingAllowed(false);
            datos.setFont(new Font("SansSerif", Font.PLAIN, 14));
            datos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
            datos.setGridColor(Color.LIGHT_GRAY);

            JScrollPane scrollPane = new JScrollPane(datos);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            add(scrollPane, BorderLayout.CENTER);

        } catch (RemoteException | NotBoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(GUIMostrarInfo.this,
                    "Error al conectar con el servidor: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Botón de cierre
        JButton cerrarBtn = new JButton("Cerrar");
        cerrarBtn.setFocusPainted(false);
        cerrarBtn.setBackground(new Color(220, 53, 69));
        cerrarBtn.setForeground(Color.WHITE);
        cerrarBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        cerrarBtn.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(cerrarBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}

