public class Hospital {

    // Los double no guardan exacto algunos decimales (0.7 * 6 da 4.1999...),
    // asi que al comparar dosis se deja este margen para no rechazar una dosis valida
    private static final double MARGEN = 0.0001;

    private Paciente[] pacientes;
    private int contadorPacientes;
    private Jaula[] jaulas;

    public Hospital() {
        pacientes = new Paciente[10];
        contadorPacientes = 0;
        jaulas = new Jaula[6];
        cargarDatosIniciales();
    }

    public void cargarDatosIniciales() {
        jaulas[0] = new Jaula(1, "S", "A");
        jaulas[1] = new Jaula(2, "M", "A");
        jaulas[2] = new Jaula(3, "M", "A");
        jaulas[3] = new Jaula(4, "L", "A");
        jaulas[4] = new Jaula(5, "S", "B");
        jaulas[5] = new Jaula(6, "M", "B");

        ingresarPerro("Rocky", 30.0, 4, "labrador", true);
        ingresarGato("Misifu", 4.0, 3);
        ingresarAve("Kiwi", 0.8, 2, 70);
    }

    // ---------- Ingresar (R1, R2, R3) ----------

    public String ingresarPerro(String nombre, double peso, int edad, String raza, boolean vacunado) {
        if (!datosValidos(nombre, peso, edad, Perro.PESO_MAXIMO) || raza == null || raza.trim().isEmpty()) {
            return "ERROR: datos invalidos";
        }
        return agregarPaciente(new Perro(nombre, peso, edad, raza, vacunado));
    }

    public String ingresarGato(String nombre, double peso, int edad) {
        if (!datosValidos(nombre, peso, edad, Gato.PESO_MAXIMO)) {
            return "ERROR: datos invalidos";
        }
        return agregarPaciente(new Gato(nombre, peso, edad, 0));
    }

    public String ingresarAve(String nombre, double peso, int edad, double envergadura) {
        if (!datosValidos(nombre, peso, edad, AveExotica.PESO_MAXIMO) || envergadura <= 0) {
            return "ERROR: datos invalidos";
        }
        return agregarPaciente(new AveExotica(nombre, peso, edad, envergadura));
    }

    private boolean datosValidos(String nombre, double peso, int edad, double pesoMaximo) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        if (peso <= 0 || peso > pesoMaximo) {
            return false;
        }
        return edad >= 0 && edad <= 30;
    }

    // Revisa duplicado y cupo, y guarda al paciente en el vector
    private String agregarPaciente(Paciente paciente) {
        if (buscarPaciente(paciente.getNombre()) != -1) {
            return "ERROR: nombre duplicado";
        }
        if (contadorPacientes == pacientes.length) {
            return "ERROR: hospital lleno";
        }
        pacientes[contadorPacientes] = paciente;
        contadorPacientes++;
        if (paciente.getEstado().equals("CUARENTENA")) {
            return "OK: " + paciente.getNombre() + " ingresado en CUARENTENA";
        }
        return "OK: " + paciente.getNombre() + " ingresado";
    }

    // Devuelve la posicion del paciente en el vector o -1 si no existe
    private int buscarPaciente(String nombre) {
        for (int i = 0; i < contadorPacientes; i++) {
            if (pacientes[i].getNombre().equalsIgnoreCase(nombre)) {
                return i;
            }
        }
        return -1;
    }

    // ---------- Vacunar ----------

    public String vacunar(String nombre) {
        int posicion = buscarPaciente(nombre);
        if (posicion == -1) {
            return "ERROR: paciente no encontrado";
        }
        Paciente paciente = pacientes[posicion];
        if (!(paciente instanceof Perro)) {
            return "ERROR: solo aplica a perros";
        }
        Perro perro = (Perro) paciente;
        if (perro.isVacunado()) {
            return "ERROR: ya vacunado";
        }
        perro.setVacunado(true);
        perro.setEstado("INGRESADO");
        return "OK: " + perro.getNombre() + " vacunado";
    }

    // ---------- Asignar (R4, R5, R6) ----------

    public String asignar(String nombre) {
        int posicion = buscarPaciente(nombre);
        if (posicion == -1) {
            return "ERROR: paciente no encontrado";
        }
        Paciente paciente = pacientes[posicion];
        if (paciente.getEstado().equals("CUARENTENA")) {
            return "ERROR: paciente en cuarentena";
        }
        if (paciente.getJaula() != null) {
            return "ERROR: paciente ya tiene jaula";
        }
        String tamano = tamanoRequerido(paciente);
        String zona = zonaRequerida(paciente);
        for (int i = 0; i < jaulas.length; i++) {
            Jaula jaula = jaulas[i];
            if (jaula.estaLibre() && jaula.getTamano().equals(tamano) && jaula.getZona().equals(zona)) {
                jaula.setOcupante(paciente);
                paciente.setJaula(jaula);
                return "OK: " + paciente.getNombre() + " asignado a jaula " + jaula.getNumero();
            }
        }
        return "ERROR: no hay jaula compatible";
    }

    // R4: tamano segun el peso; un ave con envergadura mayor a 60 cm necesita minimo M
    private String tamanoRequerido(Paciente paciente) {
        String tamano;
        if (paciente.getPeso() < 5) {
            tamano = "S";
        } else if (paciente.getPeso() <= 25) {
            tamano = "M";
        } else {
            tamano = "L";
        }
        if (paciente instanceof AveExotica && ((AveExotica) paciente).getEnvergadura() > 60 && tamano.equals("S")) {
            tamano = "M";
        }
        return tamano;
    }

    // R5: perros y gatos en zona A, aves en zona B
    private String zonaRequerida(Paciente paciente) {
        if (paciente instanceof AveExotica) {
            return "B";
        }
        return "A";
    }

    // ---------- Medicar (R7, R8, R9) ----------

    public String medicar(String nombre, double mg) {
        int posicion = buscarPaciente(nombre);
        if (posicion == -1) {
            return "ERROR: paciente no encontrado";
        }
        if (mg <= 0) {
            return "ERROR: datos invalidos";
        }
        Paciente paciente = pacientes[posicion];
        if (paciente.getEstado().equals("CUARENTENA")) {
            return "ERROR: paciente en cuarentena";
        }
        if (paciente.getJaula() == null) {
            return "ERROR: paciente sin jaula";
        }
        if (paciente instanceof Gato && ((Gato) paciente).getNivelEstres() >= 8) {
            return "ERROR: gato con estres alto";
        }
        if (mg > paciente.getDosisMaxima() + MARGEN) {
            return "ERROR: dosis excede el maximo";
        }
        if (paciente.getDosisAcumulada() + mg > 3 * paciente.getDosisMaxima() + MARGEN) {
            return "ERROR: tope acumulado superado";
        }
        paciente.setDosisAcumulada(paciente.getDosisAcumulada() + mg);
        paciente.setEstado("EN_TRATAMIENTO");
        if (paciente instanceof Gato) {
            Gato gato = (Gato) paciente;
            gato.setNivelEstres(Math.min(gato.getNivelEstres() + 2, 10));
        }
        return "OK: " + paciente.getNombre() + " medicado con " + formatearMg(mg) + " mg";
    }

    // Muestra 50 en vez de 50.0, pero conserva decimales como 12.5
    private String formatearMg(double mg) {
        if (mg == Math.floor(mg)) {
            return String.valueOf((long) mg);
        }
        return String.valueOf(mg);
    }

    // ---------- Alta (R10) ----------

    public String alta(String nombre) {
        int posicion = buscarPaciente(nombre);
        if (posicion == -1) {
            return "ERROR: paciente no encontrado";
        }
        Paciente paciente = pacientes[posicion];
        if (!paciente.getEstado().equals("EN_TRATAMIENTO")) {
            return "ERROR: paciente no apto para alta";
        }
        if (paciente.getJaula() != null) {
            paciente.getJaula().setOcupante(null);
            paciente.setJaula(null);
        }
        // Compactar: los siguientes pacientes se corren una posicion
        for (int i = posicion; i < contadorPacientes - 1; i++) {
            pacientes[i] = pacientes[i + 1];
        }
        pacientes[contadorPacientes - 1] = null;
        contadorPacientes--;
        return "OK: " + paciente.getNombre() + " dado de alta";
    }

    // ---------- Reporte ----------

    public String reporte() {
        String texto = "--- REPORTE ---\n";
        texto += "Pacientes: " + contadorPacientes + "/" + pacientes.length + "\n";
        for (int i = 0; i < contadorPacientes; i++) {
            Paciente p = pacientes[i];
            String jaula = "-";
            if (p.getJaula() != null) {
                jaula = String.valueOf(p.getJaula().getNumero());
            }
            double pesoRedondeado = Math.round(p.getPeso() * 10) / 10.0;
            texto += p.getNombre() + " | " + p.getTipo() + " | " + pesoRedondeado + " kg | "
                    + p.getEstado() + " | jaula: " + jaula + "\n";
        }
        int libres = 0;
        for (int i = 0; i < jaulas.length; i++) {
            if (jaulas[i].estaLibre()) {
                libres++;
            }
        }
        texto += "Jaulas libres: " + libres;
        return texto;
    }
}
