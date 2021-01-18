/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fitnutrition;

import com.google.gson.Gson;
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
import pojos.Consulta;
import pojos.Dieta;
import pojos.Mensaje;
import pojos.RespuestaWS;
import util.Constantes;
import util.ConsumoWS;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class FXMLConsultasController implements Initializable, NotificaCambios {

    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<Consulta> tbConsulta;
    @FXML
    private TableColumn<?, ?> colPaciente;
    @FXML
    private TableColumn<?, ?> colObservaciones;
    @FXML
    private TableColumn<?, ?> colPeso;
    @FXML
    private TableColumn<?, ?> colTalla;
    @FXML
    private TableColumn<?, ?> colIMC;
    @FXML
    private TableColumn<?, ?> colDieta;

    private NotificaCambios notificacion;
    private ObservableList<Consulta> consultas;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.colPaciente.setCellValueFactory(new PropertyValueFactory("usuario"));
        this.colObservaciones.setCellValueFactory(new PropertyValueFactory("observaciones"));
        this.colPeso.setCellValueFactory(new PropertyValueFactory("peso"));
        this.colTalla.setCellValueFactory(new PropertyValueFactory("talla"));
        this.colIMC.setCellValueFactory(new PropertyValueFactory("imc"));
        this.colDieta.setCellValueFactory(new PropertyValueFactory("nombre"));
        
        cargaElementosTabla();
    }    

    private void cargaElementosTabla(){
        
        String url = Constantes.URL + "consultas/getAllConsultas";
        RespuestaWS resp = ConsumoWS.consumoWSGET(url);
        if(resp.getCodigo() == 200){
            Gson gson = new Gson();
            Type listaConsultas = new TypeToken<List<Consulta>>(){}.getType();
            ArrayList<Consulta> consultasBD = gson.fromJson(resp.getMensaje(), listaConsultas);
            consultas = FXCollections.observableArrayList(consultasBD);
            funcionBuscar();
        }else{
            muestraDialogoError("Error de conexión", "Lo sentimos, tenemos problemas con el servidor");
        }        
    }
    
    private void funcionBuscar(){
        if(consultas.size() > 0){
            FilteredList<pojos.Consulta> filtroData = new FilteredList<>(consultas, p -> true);
            tfBuscar.textProperty().addListener(new ChangeListener<String>(){
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                    filtroData.setPredicate(busqueda ->{
                        //Si valor es vacio --> Borramos todo
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        //Convertir todo a minuscula para evitar problemas de busqueda
                        String IMC = busqueda.getImc()+"";
                        String lowerCaseFilter = newValue.toLowerCase();
                        if(busqueda.getNombre().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }else if(busqueda.getUsuario().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }else if(IMC.toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false;
                    });
                }
            });
            SortedList<pojos.Consulta> sortedData = new SortedList<>(filtroData);
            sortedData.comparatorProperty().bind(tbConsulta.comparatorProperty());
            tbConsulta.setItems(sortedData);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fitnutrition/FXMLFormularioConsulta.fxml"));
            //loader.setController(this);
            Parent root = loader.load();
            FXMLFormularioConsultaController controlador = loader.getController();
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
        int celdaSeleccionada = tbConsulta.getSelectionModel().getSelectedIndex();
        if(celdaSeleccionada >= 0){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFormularioConsulta.fxml"));
                Parent root = loader.load();

                FXMLFormularioConsultaController controlador = loader.getController();
                controlador.inicializaCampos(this, false, consultas.get(celdaSeleccionada));

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
        int celdaSeleccionada = tbConsulta.getSelectionModel().getSelectedIndex();
        if(celdaSeleccionada >= 0){
            Consulta consultaDelete = consultas.get(celdaSeleccionada);
            Alert alertConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirmacion.setTitle("Eliminar Registro");
            alertConfirmacion.setHeaderText(null);
            alertConfirmacion.setContentText("¿Estás seguro de eliminar la Consulta " + 
                    consultaDelete.getIdConsulta() +" de tus registros?");
            Optional<ButtonType> respDialogo = alertConfirmacion.showAndWait();
            if(respDialogo.get() == ButtonType.OK){                
                eliminaWSRegistro(consultaDelete.getIdConsulta());
            }
        }else{
            muestraDialogError("Selecciona registro", "Para eliminar un registro debes seleccionarlo de la tabla");
        }
    }
    
    private void eliminaWSRegistro(int idConsulta){
        String parametros = "idConsulta="+idConsulta;
        String url = Constantes.URL+"consultas/eliminarConsulta";
        RespuestaWS resp = ConsumoWS.consumoWSDELETE(url, parametros);
        if(resp.getCodigo() == 200){
            Gson gson = new Gson();
            Mensaje msj = gson.fromJson(resp.getMensaje(), Mensaje.class);
            if(msj.isError()){
                muestraDialogError("Error al eliminar", msj.getMensaje());
            }else{
                Alert alertError = new Alert(Alert.AlertType.INFORMATION);
                alertError.setTitle("Registro Eliminado");
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

    @FXML
    private void clicRegresar(ActionEvent event) {
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
