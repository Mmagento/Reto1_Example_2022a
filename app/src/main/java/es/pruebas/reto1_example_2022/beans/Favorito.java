package es.pruebas.reto1_example_2022.beans;

public class Favorito {

    long idUsuario;
    long IdCancion;



    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public long getIdCancion() {
        return IdCancion;
    }

    public void setIdCancion(long idCancion) {
        IdCancion = idCancion;
    }

    @Override
    public String toString() {
        return "Favorito{" +
                "idUsuario=" + idUsuario +
                ", IdCancion=" + IdCancion +
                '}';
    }
}
