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
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojos.Cita;
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
public class FXMLCitasController implements Initializable, NotificaCambios {

    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<Cita> tbCita;
    @FXML
    private TableColumn<?, ?> colFecha;
    @FXML
    private TableColumn<?, ?> colHora;
    @FXML
    private TableColumn<?, ?> colPaciente;
    @FXML
    private TableColumn<?, ?> colObservaciones;

    private ObservableList<Cita> citas;
    private NotificaCambios notificacion;    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.colFecha.setCellValueFactory(new PropertyValueFactory("fecha"));
        this.colHora.setCellValueFactory(new PropertyValueFactory("hora"));
        this.colPaciente.setCellValueFactory(new PropertyValueFactory("usuario"));
        this.colObservaciones.setCellValueFactory(new PropertyValueFactory("observaciones"));
        
        cargaElementosTabla();
    }
    
    private void cargaElementosTabla(){
        
        String url = Constantes.URL + "citas/getAllCitas";
        RespuestaWS resp = ConsumoWS.consumoWSGET(url);
        if(resp.getCodigo() == 200){
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            Type listaCitas = new TypeToken<List<Cita>>(){}.getType();
            ArrayList<Cita> citasBD = gson.fromJson(resp.getMensaje(), listaCitas);
            citas = FXCollections.observableArrayList(citasBD);
            funcionBuscar();
        }else{
            muestraDialogoError("Error de conexión", "Lo sentimos, tenemos problemas con el servidor");
        }        
    }
    
    private void funcionBuscar(){
        if(citas.size() > 0){
            FilteredList<pojos.Cita> filtroData = new FilteredList<>(citas, p -> true);
            tfBuscar.textProperty().addListener(new ChangeListener<String>(){
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                    filtroData.setPredicate(busqueda ->{
                        //Si valor es vacio --> Borramos todo
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        //Convertir todo a minuscula para evitar problemas de busqueda
                        String lowerCaseFilter = newValue.toLowerCase();
                        if(busqueda.getFecha().toString().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }else if(busqueda.getUsuario().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }                      
                        return false;
                    });
                }
            });
            SortedList<pojos.Cita> sortedData = new SortedList<>(filtroData);
            sortedData.comparatorProperty().bind(tbCita.comparatorProperty());
            tbCita.setItems(sortedData);
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
    private void clicAgregar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fitnutrition/FXMLFormularioCita.fxml"));
            //loader.setController(this);
            Parent root = loader.load();
            FXMLFormularioCitaController controlador = loader.getController();
            controlador.inicializaCampos(this, true, null);
            Scene scFormulario = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scFormulario);
            stage.showAndWait();
            
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    @Override
    public void refrescarTabla(boolean isReload) {
        //Aqui se notifica el guardado o la actualización
        System.out.println("Valor de isReload es:" + isReload);
        cargaElementosTabla();
    }

    @FXML
    private void clicEditar(ActionEvent event) {
        int celdaSeleccionada = tbCita.getSelectionModel().getSelectedIndex();
        if(celdaSeleccionada >= 0){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFormularioCita.fxml"));
                Parent root = loader.load();

                FXMLFormularioCitaController controlador = loader.getController();
                controlador.inicializaCampos(this, false, citas.get(celdaSeleccionada));

                Scene scFormulario = new Scene(root);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scFormulario);
                stage.showAndWait();
            
            } catch (IOException ex) {
                System.out.println("Error al cargar la ventana");
            }
        }else{
            muestraDialogError("Selecciona registro", "Para editar un registro seleccionalo de la tabla.");
        }
    }
    
    private void muestraDialogError(String titulo, String mensaje){
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setTitle(titulo);
        alertError.setHeaderText(null);
        alertError.setContentText(mensaje);
        alertError.showAndWait();            
    }

    @FXML
    private void clicEliminar(ActionEvent event) {
        int celdaSeleccionada = tbCita.getSelectionModel().getSelectedIndex();
        if(celdaSeleccionada >= 0){
            Cita citaDelete = citas.get(celdaSeleccionada);
            Alert alertConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirmacion.setTitle("Eliminar cita");
            alertConfirmacion.setHeaderText(null);
            alertConfirmacion.setContentText("¿Estás seguro de la cita de " + 
                    citaDelete.getUsuario()+" de tus registros?");
            Optional<ButtonType> respDialogo = alertConfirmacion.showAndWait();
            if(respDialogo.get() == ButtonType.OK){                
                eliminaWSRegistro(citaDelete.getIdCita());
            }
        }else{
            muestraDialogError("Selecciona registro", "Para dehabilitar un registro debes seleccionarlo de la tabla");
        }
    }
    
    private void eliminaWSRegistro(int idCita){
        String parametros = "idCita="+idCita;
        String url = Constantes.URL+"citas/eliminarCita";
        RespuestaWS resp = ConsumoWS.consumoWSDELETE(url, parametros);
        if(resp.getCodigo() == 200){
            Gson gson = new Gson();
            Mensaje msj = gson.fromJson(resp.getMensaje(), Mensaje.class);
            if(msj.isError()){
                muestraDialogError("Error al eliminar", msj.getMensaje());
            }else{
                Alert alertError = new Alert(Alert.AlertType.INFORMATION);
                alertError.setTitle("Registro Deshabilitado");
                alertError.setHeaderText(null);
                alertError.setContentText(msj.getMensaje());
                alertError.showAndWait();
                cargaElementosTabla();
            }
        }else{
            muestraDialogError("Error de conexión", "Lo sentimos no se puede conectar con el servidor");
        }
    }

    @FXML
    private void clicPacientes(ActionEvent event) {
        try{
            Stage stage = (Stage) tfBuscar.getScene().getWindow();
            Scene scenePrincipalMedico = new Scene(FXMLLoader.load(getClass().getResource("FXMLPrincipalMedico.fxml")));
            stage.setScene(scenePrincipalMedico);
            stage.show(); 
        }catch(IOException ex){
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
