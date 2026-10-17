public class AveExotica extends Paciente {

    private double envergadura;

    public AveExotica(String nombre, double peso, int edad, double envergadura) {
        super(nombre, peso, edad, "INGRESADO", 3);
        setEnvergadura(envergadura);
    }

    @Override
    public double getPesoMaximo() {
        return 15;
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
