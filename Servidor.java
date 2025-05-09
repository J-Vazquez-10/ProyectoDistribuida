package codigo;

import java.io.*;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Servidor {

    private static final int PUERTO = 3333;
    private static final String NOMBRE_OBJETO = "usuario.creacion";
    private static final String ARCHIVO = "Alumnos.dat";

    private static final Object lock = new Object();

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        System.setProperty("java.rmi.server.hostname", "172.31.2.9");
        Remote remote = UnicastRemoteObject.exportObject(new Interfaz() {

            @Override
            public String crearUsuario(Alumno alumno) throws RemoteException {
                String usuario = user(alumno.getNombre(), 3) +
                        user(alumno.getApePaterno(), 3) +
                        user(alumno.getApeMaterno(), 3);

                String password = user(alumno.getCurp(), 3) +
                        user(alumno.getNombre(), 3) +
                        alumno.getEdad();

                alumno.setUsername(usuario);
                alumno.setPassword(password);

                synchronized (lock) {
                    guardarAlumno(alumno);
                }

                return "Usuario: " + usuario + "\nContraseña: " + password;
            }

            @Override
            public String borrarUsuario(String usuario) throws RemoteException {
                synchronized (lock) {
                    List<Alumno> personas = new ArrayList<>();
                    boolean encontrado = false;
                    File archivo = new File(ARCHIVO);
                    if (!archivo.exists()) return "No existen datos";

                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                        while (true) {
                            try {
                                Alumno persona = (Alumno) ois.readObject();
                                if (!persona.getUsername().equals(usuario)) {
                                    personas.add(persona);
                                } else {
                                    encontrado = true;
                                }
                            } catch (EOFException e) {
                                break;
                            }
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
                        for (Alumno p : personas) {
                            oos.writeObject(p);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return encontrado ? "Alumno dado de baja" : "No se encontró el usuario";
                }
            }

            @Override
            public List<Alumno> mostrarInfo() throws RemoteException {
                synchronized (lock) {
                    List<Alumno> personas = new ArrayList<>();
                    File archivo = new File(ARCHIVO);
                    if (!archivo.exists()) return personas;

                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                        while (true) {
                            try {
                                Alumno persona = (Alumno) ois.readObject();
                                personas.add(persona);
                            } catch (EOFException e) {
                                break;
                            }
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    return personas;
                }
            }

            private void guardarAlumno(Alumno alumno) {
                File archivo = new File(ARCHIVO);
                boolean append = archivo.exists() && archivo.length() > 0;

                try (
                        FileOutputStream fos = new FileOutputStream(archivo, true);
                        ObjectOutputStream oos = append ? new ObjectOutputStream(fos) {
                            @Override
                            protected void writeStreamHeader() throws IOException {
                                reset(); // evita cabecera duplicada
                            }
                        } : new ObjectOutputStream(fos)
                ) {
                    oos.writeObject(alumno);
                    System.out.println("Alumno guardado correctamente.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private String user(String texto, int longitud) {
                return texto.substring(0, Math.min(longitud, texto.length()));
            }

        }, 0);



        Registry registro = LocateRegistry.createRegistry(PUERTO);
        registro.bind(NOMBRE_OBJETO, remote);

        System.out.println("Servidor RMI iniciado en el puerto " + PUERTO);
    }
}
