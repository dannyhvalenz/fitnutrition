package pojos;

public class Dieta {
    int idDieta, idAlimento;
    String nombre, nombreAlimento, cantidad, horaDia, observaciones;
    float caloriasDieta;

    
    public Dieta(){
        
    }
    
    public Dieta(int idDieta, int idAlimento, String nombre, String nombreAlimento, String cantidad, String horaDia, String observaciones, float caloriasDieta) {
        this.idDieta = idDieta;
        this.idAlimento = idAlimento;
        this.nombre = nombre;
        this.nombreAlimento = nombreAlimento;
        this.cantidad = cantidad;
        this.horaDia = horaDia;
        this.observaciones = observaciones;
        this.caloriasDieta = caloriasDieta;
    }

    public Dieta(int idDieta, int idAlimento, String nombre, String cantidad, String horaDia, String observaciones, float caloriasDieta) {
        this.idDieta = idDieta;
        this.idAlimento = idAlimento;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.horaDia = horaDia;
        this.observaciones = observaciones;
        this.caloriasDieta = caloriasDieta;
    }

    public Dieta(int idAlimento, String nombre, String cantidad, String horaDia, String observaciones, float caloriasDieta) {
        this.idAlimento = idAlimento;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.horaDia = horaDia;
        this.observaciones = observaciones;
        this.caloriasDieta = caloriasDieta;
    }

    public int getIdDieta() {
        return idDieta;
    }

    public void setIdDieta(int idDieta) {
        this.idDieta = idDieta;
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

    public String getNombreAlimento() {
        return nombreAlimento;
    }

    public void setNombreAlimento(String nombreAlimento) {
        this.nombreAlimento = nombreAlimento;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getHoraDia() {
        return horaDia;
    }

    public void setHoraDia(String horaDia) {
        this.horaDia = horaDia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public float getCaloriasDieta() {
        return caloriasDieta;
    }

    public void setCaloriasDieta(float caloriasDieta) {
        this.caloriasDieta = caloriasDieta;
    }

    @Override
    public String toString() {
        return nombre + " - " + caloriasDieta + " cal";
    }
    
    
    
    
}
