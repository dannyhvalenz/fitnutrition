/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fitnutrition;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import interfaz.NotificaCambios;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojos.Cita;
import pojos.Medico;
import pojos.Mensaje;
import pojos.Paciente;
import pojos.RespuestaWS;
import util.Constantes;
import util.ConsumoWS;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class FXMLFormularioCitaController implements Initializable {

    @FXML
    private Label lbTitulo;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private TextField tfHoras;
    @FXML
    private TextField tfMinutos;
    @FXML
    private ComboBox<Paciente> cbPacientesActivos;
    @FXML
    private TextArea taObservaciones;

    private NotificaCambios notificacion;
    private boolean isNuevo;
    private Paciente paciente;
    private Cita cita;
    private ObservableList<Paciente> pacientes;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargaElementosCombo();
    }    

    public void inicializaCampos(NotificaCambios notificacion,boolean isNuevo,Cita cita){
        this.notificacion = notificacion;
        this.isNuevo = isNuevo;
        this.cita = cita;
        if(!isNuevo){
            lbTitulo.setText("Editar Cita");
            cargaDatosEdicion();
        }
    }
    
    private void cargaDatosEdicion(){
        if(cita != null){            
            dpFecha.setValue(cita.getFecha().toLocalDate());
            String[] hora = cita.getHora().split(":");
            tfHoras.setText(hora[0]);
            tfMinutos.setText(hora[1]);
            int posCombo = getIndexListaPaciente(cita.getIdPaciente());
            cbPacientesActivos.getSelectionModel().select(posCombo);            
            taObservaciones.setText(cita.getObservaciones());            
        }
    }
    
    private int getIndexListaPaciente(int idPaciente){
        for(int i=0; i < pacientes.size(); i++){
            if(pacientes.get(i).getIdPaciente() == idPaciente){
                return i;
            }
        }
        return 0;
    }
    
    private void cargaElementosCombo(){        
        String url = Constantes.URL + "pacientes/getAllPacientesActivos";
        RespuestaWS resp = ConsumoWS.consumoWSGET(url);
        if(resp.getCodigo() == 200){
            Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
            Type pacienteLista = new TypeToken<ArrayList<Paciente>>(){}.getType();
            System.out.println(pacienteLista);
            System.out.println(resp.getMensaje());
            ArrayList<Paciente> tipoBD = gson.fromJson(resp.getMensaje(), pacienteLista);
            System.out.println("CONTENIDO DE tipoBD" + tipoBD.get(0));
            pacientes = FXCollections.observableArrayList(tipoBD);
            //System.out.println(medicos.size());
            cbPacientesActivos.setItems(pacientes);
        }else{
            muestraDialogoError("Error de conexión", "Lo sentimos, tenemos problemas con el servidor");
        }        
    }
    
    private void muestraDialogoError(String titulo, String mensaje){
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setTitle(titulo);
        alertError.setHeaderText(null);
        alertError.setContentText(mensaje);
        alertError.showAndWait();            
    }
    
    @FXML
    private void clicEnviar(ActionEvent event) {
        int seleccionP = pacientes.get(cbPacientesActivos.getSelectionModel().getSelectedIndex()).getIdPaciente();
        String hora = tfHoras.getText() + ":" + tfMinutos.getText() + ":00";
        if(isNuevo){            
            String url = Constantes.URL + "citas/registrarCita";
            String parametros = String.format("fecha=%s&hora=%s&idPaciente=%d&observaciones=%s",
                    dpFecha.getValue(), hora, seleccionP, taObservaciones.getText());
            RespuestaWS respServicio = ConsumoWS.consumoWSPOST(url, parametros);
            if(respServicio.getCodigo() == 200){
                Gson gson = new Gson();
                Mensaje msj = gson.fromJson(respServicio.getMensaje(), Mensaje.class);                   
                if(msj.isError()){
                    muestraDialogoError("El registro no pudo ser realizado", msj.getMensaje());
                }else{
                    cerrarScena();                       
                }
            }else{
                muestraDialogoError("Error de conexión", "Lo sentimos, tenemos problemas con el servidor");
            }
        }else{
            String url = Constantes.URL + "citas/actualizarCita";
            String parametros = String.format("idCita=%d&fecha=%s&hora=%s&idPaciente=%d&observaciones=%s",
                    cita.getIdCita(), dpFecha.getValue(), hora, seleccionP, taObservaciones.getText());
            RespuestaWS respServicio = ConsumoWS.consumoWSPUT(url, parametros);
            if(respServicio.getCodigo() == 200){
                Gson gson = new Gson();
                Mensaje msj = gson.fromJson(respServicio.getMensaje(), Mensaje.class);
                if(msj.isError()){
                    muestraDialogoError("Error al editar", msj.getMensaje());
                }else{
                    muestraDialogoInfo("Registro editado", msj.getMensaje());                    
                    cerrarScena();
                }
            }else{
               muestraDialogoError("Error de conexión", "Lo sentimos, tenemos problemas para conectar con el servidor"); 
            }
         
        }
    }
    
    private void cerrarScena(){
        Stage stage = (Stage) this.tfHoras.getScene().getWindow();
        stage.close();
        
        notificacion.refrescarTabla(true);
    }
    
    private void muestraDialogoInfo(String titulo, String mensaje){
        Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
        alertInfo.setTitle(titulo);
        alertInfo.setHeaderText(null);
        alertInfo.setContentText(mensaje);
        alertInfo.showAndWait();
    }
}
