public abstract class Paciente {

    private String nombre;
    private double peso;
    private int edad;
    private String estado;
    private double factorDosis;
    private double dosisAcumulada;
    private Jaula jaula;

    public Paciente(String nombre, double peso, int edad, String estado, double factorDosis) {
        setNombre(nombre);
        setPeso(peso);
        setEdad(edad);
        setEstado(estado);
        this.factorDosis = factorDosis;
        this.dosisAcumulada = 0;
        this.jaula = null;
    }

    // Cada tipo de paciente define su peso maximo y su nombre de tipo
    public abstract double getPesoMaximo();

    public abstract String getTipo();

    // R7: dosis maxima por aplicacion = peso x factorDosis
    public double getDosisMaxima() {
        return peso * factorDosis;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.nombre = nombre;
        }
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        if (peso > 0 && peso <= getPesoMaximo()) {
            this.peso = peso;
        }
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        if (edad >= 0 && edad <= 30) {
            this.edad = edad;
        }
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        if ("INGRESADO".equals(estado) || "CUARENTENA".equals(estado) || "EN_TRATAMIENTO".equals(estado)) {
            this.estado = estado;
        }
    }

    public double getFactorDosis() {
        return factorDosis;
    }

    public double getDosisAcumulada() {
        return dosisAcumulada;
    }

    public void setDosisAcumulada(double dosisAcumulada) {
        if (dosisAcumulada >= 0) {
            this.dosisAcumulada = dosisAcumulada;
        }
    }

    public Jaula getJaula() {
        return jaula;
    }

    public void setJaula(Jaula jaula) {
        this.jaula = jaula;
    }
}
