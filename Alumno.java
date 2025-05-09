package codigo;

import java.io.Serializable;

public class Alumno implements Serializable {
    private String nombre;
    private String apePaterno;
    private String apeMaterno;
    private int edad;
    private String email;
    private String curp;
    private String username;
    private String password;
    public Kardex kardex;

    public String getNombre() {
        return nombre;
    }

    public String getApePaterno() {
        return apePaterno;
    }

    public String getApeMaterno() {
        return apeMaterno;
    }

    public int getEdad() {
        return edad;
    }

    public String getEmail() {
        return email;
    }

    public String getCurp() {
        return curp;
    }

    public String getUsername() {return username;}

    public String getPassword() {return password;}

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApePaterno(String apePaterno) {
        this.apePaterno = apePaterno;
    }

    public void setApeMaterno(String apeMaterno) {
        this.apeMaterno = apeMaterno;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public void setUsername(String username) {this.username = username;}

    public void setPassword(String password) {this.password = password;}

}

