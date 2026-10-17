public class AveExotica extends Paciente {

    public static final double PESO_MAXIMO = 15;

    private double envergadura;

    public AveExotica(String nombre, double peso, int edad, double envergadura) {
        super(nombre, peso, edad, "INGRESADO", 3);
        setEnvergadura(envergadura);
    }

    @Override
    public double getPesoMaximo() {
        return PESO_MAXIMO;
    }

    @Override
    public String getTipo() {
        return "Ave";
    }

    public double getEnvergadura() {
        return envergadura;
    }

    public void setEnvergadura(double envergadura) {
        if (envergadura > 0) {
            this.envergadura = envergadura;
        }
    }
}
