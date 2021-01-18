package pojos;

public class Consulta {
    private int idConsulta, idPaciente, idDieta;
    private float peso, imc;
    private String observaciones, talla, usuario, nombre;

    public Consulta() {
    }

    public Consulta(int idConsulta, int idPaciente, int idDieta, float peso, float imc, String observaciones, String talla, String usuario, String nombre) {
        this.idConsulta = idConsulta;
        this.idPaciente = idPaciente;
        this.idDieta = idDieta;
        this.peso = peso;
        this.imc = imc;
        this.observaciones = observaciones;
        this.talla = talla;
        this.usuario = usuario;
        this.nombre = nombre;
    }
    
    

    public Consulta(int idConsulta, int idPaciente, int idDieta, float peso, float imc, String observaciones, String talla) {
        this.idConsulta = idConsulta;
        this.idPaciente = idPaciente;
        this.idDieta = idDieta;
        this.peso = peso;
        this.imc = imc;
        this.observaciones = observaciones;
        this.talla = talla;
    }

    public Consulta(int idPaciente, int idDieta, float peso, float imc, String observaciones, String talla) {
        this.idPaciente = idPaciente;
        this.idDieta = idDieta;
        this.peso = peso;
        this.imc = imc;
        this.observaciones = observaciones;
        this.talla = talla;
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdDieta() {
        return idDieta;
    }

    public void setIdDieta(int idDieta) {
        this.idDieta = idDieta;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getImc() {
        return imc;
    }

    public void setImc(float imc) {
        this.imc = imc;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
    
    
}
