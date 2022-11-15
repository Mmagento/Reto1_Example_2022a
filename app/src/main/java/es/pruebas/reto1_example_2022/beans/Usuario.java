package es.pruebas.reto1_example_2022.beans;

import java.io.Serializable;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 5428782614293183346L;

    private long id;
    private String nombre;
    private String apellidos;
    private String email;
    private String password;

    public Usuario(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
