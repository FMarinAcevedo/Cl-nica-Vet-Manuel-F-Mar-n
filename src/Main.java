import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hospital hospital = new Hospital();

        while (scanner.hasNextLine()) {
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                continue;
            }
            String[] partes = linea.split("\\s+");
            String comando = partes[0];

            if (comando.equals("salir")) {
                break;
            } else if (comando.equals("ingresar") && partes.length >= 2) {
                System.out.println(ingresar(hospital, partes));
            } else if (comando.equals("vacunar")) {
                if (partes.length == 2) {
                    System.out.println(hospital.vacunar(partes[1]));
                } else {
                    System.out.println("ERROR: datos invalidos");
                }
            } else if (comando.equals("asignar")) {
                if (partes.length == 2) {
                    System.out.println(hospital.asignar(partes[1]));
                } else {
                    System.out.println("ERROR: datos invalidos");
                }
            } else if (comando.equals("medicar")) {
                if (partes.length == 3) {
                    System.out.println(hospital.medicar(partes[1], leerDecimal(partes[2])));
                } else {
                    System.out.println("ERROR: datos invalidos");
                }
            } else if (comando.equals("alta")) {
                if (partes.length == 2) {
                    System.out.println(hospital.alta(partes[1]));
                } else {
                    System.out.println("ERROR: datos invalidos");
                }
            } else if (comando.equals("reporte")) {
                System.out.println(hospital.reporte());
            } else {
                System.out.println("ERROR: comando desconocido");
            }
        }
        scanner.close();
    }

    // Lee los datos del comando ingresar segun el tipo de paciente
    private static String ingresar(Hospital hospital, String[] partes) {
        String tipo = partes[1];
        if (tipo.equals("perro")) {
            if (partes.length != 7) {
                return "ERROR: datos invalidos";
            }
            String vacunado = partes[6];
            if (!vacunado.equals("si") && !vacunado.equals("no")) {
                return "ERROR: datos invalidos";
            }
            return hospital.ingresarPerro(partes[2], leerDecimal(partes[3]), leerEntero(partes[4]),
                    partes[5], vacunado.equals("si"));
        } else if (tipo.equals("gato")) {
            if (partes.length != 5) {
                return "ERROR: datos invalidos";
            }
            return hospital.ingresarGato(partes[2], leerDecimal(partes[3]), leerEntero(partes[4]));
        } else if (tipo.equals("ave")) {
            if (partes.length != 6) {
                return "ERROR: datos invalidos";
            }
            return hospital.ingresarAve(partes[2], leerDecimal(partes[3]), leerEntero(partes[4]),
                    leerDecimal(partes[5]));
        }
        return "ERROR: comando desconocido";
    }

    // Si el texto no es un numero devuelve -1, que el hospital rechaza como dato invalido
    private static double leerDecimal(String texto) {
        try {
            double numero = Double.parseDouble(texto);
            if (Double.isNaN(numero) || Double.isInfinite(numero)) {
                return -1;
            }
            return numero;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static int leerEntero(String texto) {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
