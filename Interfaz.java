package codigo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.io.File;

public interface Interfaz extends Remote {
    String crearUsuario(Alumno alumno) throws RemoteException;
    String borrarUsuario(String usuario) throws RemoteException;
    List<Alumno> mostrarInfo() throws RemoteException;
}
