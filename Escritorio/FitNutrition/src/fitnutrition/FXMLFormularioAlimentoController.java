/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fitnutrition;

import com.google.gson.Gson;
import interfaz.NotificaCambios;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojos.Alimento;
import pojos.Cita;
import pojos.Mensaje;
import pojos.RespuestaWS;
import util.Constantes;
import util.ConsumoWS;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class FXMLFormularioAlimentoController implements Initializable {

    @FXML
    private Label lbTitulo;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfCaloriasPorcion;
    @FXML
    private TextField tfPorcion;
    
    private NotificaCambios notificacion;
    private boolean isNuevo;
    private Alimento alimento;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializaCampos(NotificaCambios notificacion,boolean isNuevo,Alimento alimento){
        this.notificacion = notificacion;
        this.isNuevo = isNuevo;
        this.alimento = alimento;
        if(!isNuevo){
            lbTitulo.setText("Editar Alimento");
            cargaDatosEdicion();
        }
    }
    
    private void cargaDatosEdicion(){
        if(alimento != null){            
            tfNombre.setText(alimento.getNombre());
            tfCaloriasPorcion.setText(alimento.getCalorias_por_porcion()+"");
            tfPorcion.setText(alimento.getPorcion());
        }
    }

    @FXML
    private void clicEnviar(ActionEvent event) {
        if(isNuevo){            
            String url = Constantes.URL + "alimentos/registrarAlimento";
            String parametros = String.format("nombre=%s&calorias_por_porcion=%s&porcion=%s",
                    tfNombre.getText(), tfCaloriasPorcion.getText(), tfPorcion.getText());
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
            String url = Constantes.URL + "alimentos/actualizarAlimento";
            String parametros = String.format("idAlimento=%d&nombre=%s&calorias_por_porcion=%s&porcion=%s",
                    alimento.getIdAlimento(), tfNombre.getText(), tfCaloriasPorcion.getText(), tfPorcion.getText());
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
        Stage stage = (Stage) this.tfNombre.getScene().getWindow();
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
    
    private void muestraDialogoError(String titulo, String mensaje){
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setTitle(titulo);
        alertError.setHeaderText(null);
        alertError.setContentText(mensaje);
        alertError.showAndWait();            
    }
}
