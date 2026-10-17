public class Jaula {

    private int numero;
    private String tamano;
    private String zona;
    private Paciente ocupante;

    public Jaula(int numero, String tamano, String zona) {
        setNumero(numero);
        setTamano(tamano);
        setZona(zona);
        this.ocupante = null;
    }

    // Una jaula esta libre si no tiene ocupante
    public boolean estaLibre() {
        return ocupante == null;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        if (numero > 0) {
            this.numero = numero;
        }
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        if ("S".equals(tamano) || "M".equals(tamano) || "L".equals(tamano)) {
            this.tamano = tamano;
        }
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        if ("A".equals(zona) || "B".equals(zona)) {
            this.zona = zona;
        }
    }

    public Paciente getOcupante() {
        return ocupante;
    }

    public void setOcupante(Paciente ocupante) {
        this.ocupante = ocupante;
    }
}
