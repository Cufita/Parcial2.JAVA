// El Sistema de Salud Municipal (SSM) nos encarga el desarrollo de un sistema para
//gestionar y controlar la crisis por el COVID-19. En primera instancia vamos a registrar a las
//personas a las que hacemos el test. Nombre y Apellido, edad, barrio, DNI y ocupación serán
//los datos a pedir. Tenemos una cantidad limitada de reactivos por lo cual al realizar un registro
//sin el kit debemos lanzar una excepción creada por nosotros. Al ser lanzada la excepción le
//preguntaremos al SSM si cuenta con más test y si es positivo, ingresaremos la nueva
//cantidad. Las personas se almacenan por orden de llegada. Debemos controlar que no se
//repita el DNI. Al ingresar esa persona, le asignaremos un número de kit (generado por el
//sistema).
//Luego de ingresar a estas personas vamos a invocar un método llamado “testear” donde
//evaluaremos la temperatura de cada una de las personas. Con cada evaluación generamos
//una tabla donde la clave será el número de kit y el valor contendrá un registro que
//contendrá el DNI y la temperatura (generada de manera random entre 36 y 39 grados).
//Una vez realizados todos los test, invocamos un método llamado “aislar” donde si la
//temperatura supera los 38 grados (inclusive) lanzaremos una excepción que contendrá el
//número de test y el barrio. Como tratamiento de ese error, esos datos se deben almacenar
//en un archivo binario de objetos llamado “urgente.dat” (opcional).
//Para finalizar nuestro trabajo generamos un JSON (que también debemos persistir en disco)
//donde tendremos un objeto con dos claves (“sanos” y “aislar”) que serán arreglos. En el
//primer arreglo guardaremos los datos de la persona y en el segundo, kit, temperatura y
//barrio. En la primera clave mostraremos la información de las personas que no superen los
//38 grados de temperatura tomada y en el segundo los casos sospechosos (opcional).


//IMPORTACION DE LOS ARCHIVOS JACKSON.

import com.fasterxml.jackson.databind.ObjectMapper;

//IMPORTACIONES DE LAS LIBRERIAS DE JAVA.

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;


//IMPORTACIONES DE LAS CLASES.

import Clases.Persona;
import Clases.RegistroTest;

//IMPORTACIONES DE LAS EXCEPCIONES

import Excepciones.SSMException;
import Excepciones.AislamientoException;

public class Main {
    private static List<Persona> personasRegistradas = new ArrayList<>();
    private static int numeroKit = 1;
    private static boolean hayMasTests = true;
    private static Map<Integer, RegistroTest> tablaResultados = new HashMap<>();

    public static void main(String[] args) {
        while (hayMasTests) {
            try {
                //SE SOLICITA INGRESAR LOS DATOS DE UNA PERSONA.
                Persona persona = ingresarPersona();
                personasRegistradas.add(persona);
                //SE LE ASIGNA UN KIT EN CASO DE SER CREADO CORRECTAMENTE
                System.out.println("Registro exitoso. Número de kit asignado: " + numeroKit);
                //SE AUMENTA EN 1 LA CANTIDAD DE NUEVOS KIT POR CADA PERSONA NUEVA REGISTRADA.
                numeroKit++;
            } catch (SSMException e) {
                System.out.println(e.getMessage());
                if (consultarMasTests()) {
                    incrementarCantidadTests();
                } else {
                    hayMasTests = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese los datos correctamente.");
            }
        }

        System.out.println("Tabla de Resultados:");
        for (Map.Entry<Integer, RegistroTest> entry : tablaResultados.entrySet()) {
            Integer numeroKit = entry.getKey();
            RegistroTest registro = entry.getValue();
            System.out.println("Número de kit: " + numeroKit + ", DNI: " + registro.getDni() + ", Temperatura: " + registro.getTemperatura());
        }

        try {
            aislarPersonas();
            System.out.println("Personas aisladas correctamente.");
        } catch (AislamientoException e) {
            System.out.println("Error al aislar personas. Número de kit: " + e.getNumeroKit() + ", Barrio: " + e.getBarrio());
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("./urgente.dat"));
                outputStream.writeObject(e);
                outputStream.close();
                System.out.println("Datos almacenados en el archivo urgente.dat");
            } catch (IOException ex) {
                System.out.println("Error al almacenar los datos en el archivo urgente.dat");
            }
        }

        //FUNCION EXTRA PARA GENERAR UN ARCHIVO JSON.
        generarArchivoJSON();
    }


    //FUNCION QUE CARGA UNA NUEVA PERSONA CON LA FUNCIONALIDAD DE TENER EXCEPCIONES SSMException INCLUIDA CONTROLANDO POR DNI.
    private static Persona ingresarPersona() throws SSMException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el nombre ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese el apellido: ");
        String apellido = scanner.nextLine();

        System.out.print("Ingrese la edad: ");
        int edad = scanner.nextInt();
        // LLIMPIAR EL BUFFER DEL SCANNER (int)
        scanner.nextLine();

        System.out.print("Ingrese el barrio: ");
        String barrio = scanner.nextLine();

        System.out.print("Ingrese el DNI: ");
        String dni = scanner.nextLine();
        if (existeDNI(dni)) {
            throw new SSMException("El DNI ya existe en los registros.");
        }

        System.out.print("Ingrese la ocupación: ");
        String ocupacion = scanner.nextLine();

        return new Persona(nombre,apellido, edad, barrio, dni, ocupacion);
    }

    //METODO BOOLEAN QUE COMPARA EL DNI QUE SE SOLICITA CON EL REGISTRO PARA SABER SI EXISTE O NO.
    private static boolean existeDNI(String dni) {
        for (Persona persona : personasRegistradas) {
            if (persona.getDni().equals(dni)) {
                return true;
            }
        }
        return false;
    }

    //METODO BOOLEAN QUE CONSULTA SI EXISTEN MAS TEST DISPONIBLES PARA USAR.
    private static boolean consultarMasTests() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("¿El SSM cuenta con más tests? (S/N): ");
        String respuesta = scanner.nextLine();

        return respuesta.equalsIgnoreCase("S");
    }

    //FUNCION CREADA PARA AGREGAR NUEVA CANTIDAD DE TEST DISPONIBLES PARA USAR.

    private static void incrementarCantidadTests() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese la cantidad de nuevos tests disponibles: ");
        int nuevosTests = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer del scanner

        numeroKit += nuevosTests;
        System.out.println("Se agregaron " + nuevosTests + " nuevos tests. Número de kit asignado: " + numeroKit);
    }

    //METODO TESTEARPERSONA SOLICITADO EN EL PARRAFO 2.
    private static void testearPersona(int numeroKit) {
        //SE GUARDA EN TEMPERATURA UNA ALEATORIA ENTRE LOS VALORES ASIGNADOS EN LA FUNCION.
        double temperatura = generarTemperaturaAleatoria();
        RegistroTest registro = new RegistroTest(personasRegistradas.get(numeroKit - 1).getDni(), temperatura);
        tablaResultados.put(numeroKit, registro);
    }

    //FUNCION QUE SE USA EN EL METODO TESTEARPERSONA QUE GENERA UNA TEMPERATURA (random) ENTRE 38 y 37 GRADOS
    private static double generarTemperaturaAleatoria() {
        return 36 + Math.random() * (39 - 36);
    }


    //METODO SOLICITADO EN EL PARRAFO NUMERO 3
    private static void aislarPersonas() throws AislamientoException {
        for (Map.Entry<Integer, RegistroTest> entry : tablaResultados.entrySet()) {
            Integer numeroKit = entry.getKey();
            RegistroTest registro = entry.getValue();

            if (registro.getTemperatura() >= 38) {
                Persona persona = personasRegistradas.get(numeroKit - 1);
                throw new AislamientoException(numeroKit, persona.getBarrio());
            }
        }
    }


    //FUNCION EXTRA PARA GENERAR UN ARCHIVO JSON CON LA LIBRERIA JACKSON.
    private static void generarArchivoJSON() {
        List<Persona> sanos = new ArrayList<>();
        List<RegistroTest> casosSospechosos = new ArrayList<>();

        for (Map.Entry<Integer, RegistroTest> entry : tablaResultados.entrySet()) {
            Integer numeroKit = entry.getKey();
            RegistroTest registro = entry.getValue();
            Persona persona = personasRegistradas.get(numeroKit - 1);

            if (registro.getTemperatura() < 38) {
                sanos.add(persona);
            } else {
                casosSospechosos.add(registro);
            }
        }

        Map<String, Object> datosJSON = new HashMap<>();
        datosJSON.put("sanos", sanos);
        datosJSON.put("aislar", casosSospechosos);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(new File("./datos.json"), datosJSON);
            System.out.println("Archivo JSON generado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al generar el archivo JSON.");
        }
    }
}
