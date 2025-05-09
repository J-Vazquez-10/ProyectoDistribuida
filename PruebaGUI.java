package codigo;

public class PruebaGUI {
    public static void main(String[] args) {
        // Iniciar el servidor en un hilo aparte
        /*new Thread(() -> {
            try {
                Servidor servidor = new Servidor();
                servidor.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();*/

        // Iniciar la GUI en el hilo principal (AWT/Swing requiere esto)
        javax.swing.SwingUtilities.invokeLater(() -> {
            MenuDeOpciones c1 = new MenuDeOpciones(null);
            c1.setVisible(true);
        });
    }
}
