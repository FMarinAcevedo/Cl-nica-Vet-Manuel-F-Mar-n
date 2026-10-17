public class Perro extends Paciente {

    private String raza;
    private boolean vacunado;

    public Perro(String nombre, double peso, int edad, String raza, boolean vacunado) {
        // R3: un perro no vacunado ingresa en CUARENTENA
        super(nombre, peso, edad, vacunado ? "INGRESADO" : "CUARENTENA", 10);
        setRaza(raza);
        this.vacunado = vacunado;
    }

    @Override
    public double getPesoMaximo() {
        return 80;
    }

    @Override
    public String getTipo() {
        return "Perro";
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        if (raza != null && !raza.trim().isEmpty()) {
            this.raza = raza;
        }
    }

    public boolean isVacunado() {
        return vacunado;
    }

    public void setVacunado(boolean vacunado) {
        this.vacunado = vacunado;
    }
}
