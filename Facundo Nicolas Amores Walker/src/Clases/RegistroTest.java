package Clases;

import java.io.Serializable;

public class RegistroTest implements Serializable {
    private String dni;
    private double temperatura;

    public RegistroTest() {
    }

    public RegistroTest(String dni, double temperatura) {
        this.dni = dni;
        this.temperatura = temperatura;
    }

    public String getDni() {
        return dni;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    @Override
    public String toString() {
        return "RegistroTest{" +
                "dni='" + dni + '\'' +
                ", temperatura=" + temperatura +
                '}';
    }


}
