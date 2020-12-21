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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojos.Alimento;
import pojos.Cita;
import pojos.RespuestaWS;
import util.Constantes;
import util.ConsumoWS;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class FXMLAlimentosController implements Initializable, NotificaCambios {

    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<Alimento> tbAlimento;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colCaloriasPorcion;
    @FXML
    private TableColumn<?, ?> colPorcion;
    
    private ObservableList<Alimento> alimentos;
    private NotificaCambios notificacion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.colCaloriasPorcion.setCellValueFactory(new PropertyValueFactory("calorias_por_porcion"));
        this.colPorcion.setCellValueFactory(new PropertyValueFactory("porcion"));      
        
        cargaElementosTabla();
    }    
    
    private void cargaElementosTabla(){
        
        String url = Constantes.URL + "alimentos/getAllAlimentos";
        RespuestaWS resp = ConsumoWS.consumoWSGET(url);
        if(resp.getCodigo() == 200){
            Gson gson = new Gson();
            Type listaAlimentos = new TypeToken<List<Alimento>>(){}.getType();
            ArrayList<Alimento> citasBD = gson.fromJson(resp.getMensaje(), listaAlimentos);
            alimentos = FXCollections.observableArrayList(citasBD);
            funcionBuscar();
        }else{
            muestraDialogoError("Error de conexión", "Lo sentimos, tenemos problemas con el servidor");
        }        
    }
    
    private void funcionBuscar(){
        if(alimentos.size() > 0){
            FilteredList<pojos.Alimento> filtroData = new FilteredList<>(alimentos, p -> true);
            tfBuscar.textProperty().addListener(new ChangeListener<String>(){
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                    filtroData.setPredicate(busqueda ->{
                        //Si valor es vacio --> Borramos todo
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        String calpor = busqueda.getCalorias_por_porcion() + "";
                        //Convertir todo a minuscula para evitar problemas de busqueda
                        String lowerCaseFilter = newValue.toLowerCase();
                        if(busqueda.getNombre().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }else if(calpor.toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }                      
                        return false;
                    });
                }
            });
            SortedList<pojos.Alimento> sortedData = new SortedList<>(filtroData);
            sortedData.comparatorProperty().bind(tbAlimento.comparatorProperty());
            tbAlimento.setItems(sortedData);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fitnutrition/FXMLFormularioAlimento.fxml"));
            //loader.setController(this);
            Parent root = loader.load();
            FXMLFormularioAlimentoController controlador = loader.getController();
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
        int celdaSeleccionada = tbAlimento.getSelectionModel().getSelectedIndex();
        if(celdaSeleccionada >= 0){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFormularioAlimento.fxml"));
                Parent root = loader.load();

                FXMLFormularioAlimentoController controlador = loader.getController();
                controlador.inicializaCampos(this, false, alimentos.get(celdaSeleccionada));

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
