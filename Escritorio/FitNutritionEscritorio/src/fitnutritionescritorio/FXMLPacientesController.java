/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fitnutritionescritorio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import interfaz.NotificaCambios;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import pojos.Paciente;
import pojos.RespuestaWS;
import util.Constantes;
import util.ConsumoWS;

/**
 * FXML Controller class
 *
 * @author dany
 */
public class FXMLPacientesController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<Paciente> tbPacientes;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colApellidos;
    @FXML
    private TableColumn<?, ?> colTelefono;
    @FXML
    private TableColumn<?, ?> colEmail;
    @FXML
    private TableColumn<?, ?> colStatus;

    private ObservableList<Paciente> pacientes;
    private NotificaCambios notificacion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.colApellidos.setCellValueFactory(new PropertyValueFactory("apellidos"));
        this.colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        this.colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        this.colStatus.setCellValueFactory(new PropertyValueFactory("status"));

        cargaElementosTabla();
        //funcionBuscar();
    }

    @FXML
    public void clickNuevoPaciente() {
    }

    public void cargaElementosTabla() {
        String url = Constantes.URL + "pacientes/getAllPacientes";
        RespuestaWS resp = ConsumoWS.consumoWSGET(url);
        if (resp.getCodigo() == 200) {
            Gson gson = new Gson();
            Type tipoListaAerolineas = new TypeToken<List<Paciente>>() {
            }.getType();
            ArrayList<Paciente> aerolineasBD = gson.fromJson(resp.getMensaje(), tipoListaAerolineas);
            pacientes = FXCollections.observableArrayList(aerolineasBD);
            tbPacientes.setItems(pacientes);
        } else {
            muestraDialogError("Error de conexión", "Lo sentimos, tenemos problemas con el servidor");
        }
    }
    
    private void muestraDialogError(String titulo, String mensaje){
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setTitle(titulo);
        alertError.setHeaderText(null);
        alertError.setContentText(mensaje);
        alertError.showAndWait();            
    }

}
