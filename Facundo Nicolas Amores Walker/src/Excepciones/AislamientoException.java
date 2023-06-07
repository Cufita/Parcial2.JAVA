package Excepciones;

import java.io.Serial;
import java.io.Serializable;

public class AislamientoException extends Exception implements Serializable {
    private int numeroKit;
    private String barrio;

    public AislamientoException(int numeroKit, String barrio) {
        this.numeroKit = numeroKit;
        this.barrio = barrio;
    }

    public int getNumeroKit() {
        return numeroKit;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setNumeroKit(int numeroKit) {
        this.numeroKit = numeroKit;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }
}
