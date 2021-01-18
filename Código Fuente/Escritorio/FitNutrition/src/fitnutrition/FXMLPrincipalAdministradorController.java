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
public class FXMLPrincipalAdministradorController implements Initializable, NotificaCambios {

    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<Medico> tbMedico;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colApellido;
    @FXML
    private TableColumn<?, ?> colFecha;
    @FXML
    private TableColumn<?, ?> colGenero;
    @FXML
    private TableColumn<?, ?> colDomicilio;
    @FXML
    private TableColumn<?, ?> colNumPersonal;
    @FXML
    private TableColumn<?, ?> colNumCedula;
    @FXML
    private TableColumn<?, ?> colContrasena;
    @FXML
    private TableColumn<?, ?> colEstatus;

    private ObservableList<Medico> medicos;
    private NotificaCambios notificacion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.colApellido.setCellValueFactory(new PropertyValueFactory("apellidos"));
        this.colFecha.setCellValueFactory(new PropertyValueFactory("fecha_nacimiento"));
        this.colGenero.setCellValueFactory(new PropertyValueFactory("genero"));
        this.colDomicilio.setCellValueFactory(new PropertyValueFactory("domicilio"));
        this.colNumPersonal.setCellValueFactory(new PropertyValueFactory("num_personal"));
        this.colNumCedula.setCellValueFactory(new PropertyValueFactory("num_cedula"));
        this.colContrasena.setCellValueFactory(new PropertyValueFactory("contrasena"));
        this.colEstatus.setCellValueFactory(new PropertyValueFactory("status"));
        
        cargaElementosTabla();
    }
    
    private void cargaElementosTabla(){
        
        String url = Constantes.URL + "medicos/getAllMedicos";
        RespuestaWS resp = ConsumoWS.consumoWSGET(url);
        if(resp.getCodigo() == 200){
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            Type listaMedicos = new TypeToken<List<Medico>>(){}.getType();
            ArrayList<Medico> medicosBD = gson.fromJson(resp.getMensaje(), listaMedicos);
            medicos = FXCollections.observableArrayList(medicosBD);
            funcionBuscar();
        }else{
            muestraDialogoError("Error de conexión", "Lo sentimos, tenemos problemas con el servidor");
        }        
    }
    
    private void funcionBuscar(){
        if(medicos.size() > 0){
            FilteredList<pojos.Medico> filtroData = new FilteredList<>(medicos, p -> true);
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
                        if(busqueda.getNombre().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }else if(busqueda.getApellidos().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }else if(busqueda.getNum_personal().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }                      
                        return false;
                    });
                }
            });
            SortedList<pojos.Medico> sortedData = new SortedList<>(filtroData);
            sortedData.comparatorProperty().bind(tbMedico.comparatorProperty());
            tbMedico.setItems(sortedData);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fitnutrition/FXMLFormularioMedico.fxml"));
            //loader.setController(this);
            Parent root = loader.load();
            FXMLFormularioMedicoController controlador = loader.getController();
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
        int celdaSeleccionada = tbMedico.getSelectionModel().getSelectedIndex();
        if(celdaSeleccionada >= 0){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFormularioMedico.fxml"));
                Parent root = loader.load();

                FXMLFormularioMedicoController controlador = loader.getController();
                controlador.inicializaCampos(this, false, medicos.get(celdaSeleccionada));

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

    @FXML
    private void clicEliminar(ActionEvent event) {
        int celdaSeleccionada = tbMedico.getSelectionModel().getSelectedIndex();
        if(celdaSeleccionada >= 0){
            Medico medicoDelete = medicos.get(celdaSeleccionada);
            Alert alertConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirmacion.setTitle("Deshabilitar Registro");
            alertConfirmacion.setHeaderText(null);
            alertConfirmacion.setContentText("¿Estás seguro de deshabilitar al Medico " + 
                    medicoDelete.getNombre() +" de tus registros?");
            Optional<ButtonType> respDialogo = alertConfirmacion.showAndWait();
            if(respDialogo.get() == ButtonType.OK){                
                eliminaWSRegistro(medicoDelete.getIdMedico());
                //String PATH = "D:\\Documentos\\UV\\7moSemestre\\Integracion_Soluciones\\Imagenes\\"+pacienteDelete.getIdPaciente()+".png";
                //File img = new File(PATH);
                //if (img.delete()){
                //    System.out.println("La imagen ha sido borrada satisfactoriamente");
                //}else{
                //   System.out.println("La imagen no pudo ser borrada");                
                //}
            }
        }else{
            muestraDialogError("Selecciona registro", "Para dehabilitar un registro debes seleccionarlo de la tabla");
        }
    }
    
    private void eliminaWSRegistro(int idMedico){
        String parametros = "idMedico="+idMedico;
        String url = Constantes.URL+"medicos/eliminarMedico";
        RespuestaWS resp = ConsumoWS.consumoWSDISABLE(url, parametros);
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
                //tbAerolinea.getItems().clear();
                cargaElementosTabla();
            }
        }else{
            muestraDialogError("Error de conexión", "Lo sentimos no se puede conectar con el servidor");
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
    private void clicCerrarSesion(ActionEvent event) {
        try{
            Stage stage = (Stage) tfBuscar.getScene().getWindow();
            Scene sceneSesion = new Scene(FXMLLoader.load(getClass().getResource("FXMLLogin.fxml")));
            stage.setScene(sceneSesion);
            stage.show();
        }catch(IOException ex){
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
