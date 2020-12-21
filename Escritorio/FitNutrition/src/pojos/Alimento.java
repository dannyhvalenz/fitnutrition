package pojos;

public class Alimento {
    private int idAlimento;
    private String nombre, calorias_por_porcion;
    private String porcion; //(pieza, gramos, porciones, ,mililitros)

    public Alimento() {
    }

    public Alimento(int idAlimento, String nombre, String calorias_por_porcion, String porcion) {
        this.idAlimento = idAlimento;
        this.nombre = nombre;
        this.calorias_por_porcion = calorias_por_porcion;
        this.porcion = porcion;
    }
    
    public Alimento(String nombre, String calorias_por_porcion, String porcion) {
        this.nombre = nombre;
        this.calorias_por_porcion = calorias_por_porcion;
        this.porcion = porcion;
    }

    public int getIdAlimento() {
        return idAlimento;
    }

    public void setIdAlimento(int idAlimento) {
        this.idAlimento = idAlimento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCalorias_por_porcion() {
        return calorias_por_porcion;
    }

    public void setCalorias_por_porcion(String calorias_por_porcion) {
        this.calorias_por_porcion = calorias_por_porcion;
    }

    public String getPorcion() {
        return porcion;
    }

    public void setPorcion(String porcion) {
        this.porcion = porcion;
    }
    
}
