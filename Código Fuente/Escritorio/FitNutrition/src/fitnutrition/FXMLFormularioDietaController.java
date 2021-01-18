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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojos.Alimento;
import pojos.Dieta;
import pojos.Medico;
import pojos.Mensaje;
import pojos.RespuestaWS;
import util.Constantes;
import util.ConsumoWS;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class FXMLFormularioDietaController implements Initializable {

    @FXML
    private Label lbTitulo;
    @FXML
    private TextField tfNombre;
    @FXML
    private ComboBox<Alimento> cbAlimentos;    
    @FXML
    private TextField tfCantidad;
    @FXML
    private TextField tfHoraDia;
    @FXML
    private TextField tfCaloriasDieta;
    @FXML
    private TextArea taObservaciones;

    private NotificaCambios notificacion;
    private boolean isNuevo;
    private Dieta dieta;
    
    private ObservableList<Alimento> alimentos;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargaElementosCombo();
    }    

    public void inicializaCampos(NotificaCambios notificacion,boolean isNuevo, Dieta dieta){
        this.notificacion = notificacion;
        this.isNuevo = isNuevo;
        this.dieta = dieta;
        if(!isNuevo){
            lbTitulo.setText("Editar dieta");
            cargaDatosEdicion();
        }
    }
    
    private void cargaDatosEdicion(){
        if(dieta != null){            
            tfNombre.setText(dieta.getNombre());
            int posCombo = getIndexListaAlimento(dieta.getIdAlimento());
            cbAlimentos.getSelectionModel().select(posCombo);
            tfCantidad.setText(dieta.getCantidad());
            tfHoraDia.setText(dieta.getHoraDia());
            tfCaloriasDieta.setText(dieta.getCaloriasDieta()+"");
            taObservaciones.setText(dieta.getObservaciones());
        }
    }
 
    private void cargaElementosCombo(){
        
        String url = Constantes.URL + "alimentos/getAllAlimentos";
        RespuestaWS resp = ConsumoWS.consumoWSGET(url);
        if(resp.getCodigo() == 200){
            Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
            Type alimentoLista = new TypeToken<ArrayList<Alimento>>(){}.getType();
            System.out.println(alimentoLista);
            System.out.println(resp.getMensaje());
            ArrayList<Alimento> tipoBD = gson.fromJson(resp.getMensaje(), alimentoLista);
            System.out.println("CONTENIDO DE tipoBD" + tipoBD.get(0));
            alimentos = FXCollections.observableArrayList(tipoBD);
            //System.out.println(medicos.size());
            cbAlimentos.setItems(alimentos);
        }else{
            muestraDialogoError("Error de conexión", "Lo sentimos, tenemos problemas con el servidor");
        }        
    }
    
    private int getIndexListaAlimento(int idAlimento){
        for(int i=0; i < alimentos.size(); i++){
            if(alimentos.get(i).getIdAlimento() == idAlimento){
                return i;
            }
        }
        return 0;
    }
 
    @FXML
    private void clicEnviar(ActionEvent event) {
        int seleccionD = alimentos.get(cbAlimentos.getSelectionModel().getSelectedIndex()).getIdAlimento();
        if(isNuevo){            
            String url = Constantes.URL + "dietas/registrarDieta";
            String parametros = String.format("nombre=%s&idAlimento=%d&cantidad=%s&horaDia=%s&caloriasDieta=%f&observaciones=%s",
                    tfNombre.getText(), seleccionD, tfCantidad.getText(), tfHoraDia.getText(), Float.valueOf(tfCaloriasDieta.getText()), taObservaciones.getText());
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
            String url = Constantes.URL + "dietas/actualizarDieta";
            String parametros = String.format("idDieta=%d&nombre=%s&idAlimento=%d&cantidad=%s&horaDia=%s&caloriasDieta=%f&observaciones=%s",
                    dieta.getIdDieta(), tfNombre.getText(), seleccionD, tfCantidad.getText(), tfHoraDia.getText(), Float.valueOf(tfCaloriasDieta.getText()), taObservaciones.getText());
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
