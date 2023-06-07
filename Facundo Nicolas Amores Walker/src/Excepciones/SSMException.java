package Excepciones;

import java.io.Serializable;

public class SSMException extends Exception implements Serializable {
    public SSMException(String message) {
        super(message);
    }
}
