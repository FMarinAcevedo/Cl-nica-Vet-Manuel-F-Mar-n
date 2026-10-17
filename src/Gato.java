public class Gato extends Paciente {

    public static final double PESO_MAXIMO = 15;

    private int nivelEstres;

    public Gato(String nombre, double peso, int edad, int nivelEstres) {
        super(nombre, peso, edad, "INGRESADO", 6);
        setNivelEstres(nivelEstres);
    }

    @Override
    public double getPesoMaximo() {
        return PESO_MAXIMO;
    }

    @Override
    public String getTipo() {
        return "Gato";
    }

    public int getNivelEstres() {
        return nivelEstres;
    }

    public void setNivelEstres(int nivelEstres) {
        if (nivelEstres >= 0 && nivelEstres <= 10) {
            this.nivelEstres = nivelEstres;
        }
    }
}
