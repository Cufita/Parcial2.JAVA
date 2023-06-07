package Clases;
import java.io.Serializable;
import java.util.*;

public class Persona implements Serializable {
    private String Nombre;
    private String Apellido;
    private Integer Edad;
    private String Barrio;
    private String Dni;
    private String Ocupacion;

    public Persona() {
    }

    public Persona(String nombre, String apellido, Integer edad, String barrio, String dni, String ocupacion) {
        Nombre = nombre;
        Apellido = apellido;
        Edad = edad;
        Barrio = barrio;
        Dni = dni;
        Ocupacion = ocupacion;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public Integer getEdad() {
        return Edad;
    }

    public String getBarrio() {
        return Barrio;
    }

    public String getDni() {
        return Dni;
    }

    public String getOcupacion() {
        return Ocupacion;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public void setEdad(Integer edad) {
        Edad = edad;
    }

    public void setBarrio(String barrio) {
        Barrio = barrio;
    }

    public void setDNI(String Dni) {
        this.Dni = Dni;
    }

    public void setOcupacion(String ocupacion) {
        Ocupacion = ocupacion;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "Nombre='" + Nombre + '\'' +
                ", Apellido='" + Apellido + '\'' +
                ", Edad=" + Edad +
                ", Barrio='" + Barrio + '\'' +
                ", Dni='" + Dni + '\'' +
                ", Ocupacion='" + Ocupacion + '\'' +
                '}';
    }
}
