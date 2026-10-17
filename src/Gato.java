public class Gato extends Paciente {

    private int nivelEstres;

    public Gato(String nombre, double peso, int edad, int nivelEstres) {
        super(nombre, peso, edad, "INGRESADO", 6);
        setNivelEstres(nivelEstres);
    }

    @Override
    public double getPesoMaximo() {
        return 15;
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
