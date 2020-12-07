package fitnutritionescritorio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import jfxtras.scene.control.agenda.Agenda;
import pojos.Cita;
import pojos.RespuestaWS;
import util.Constantes;
import util.ConsumoWS;

public class FXMLCitasController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXHamburger btnDrawer;

    @FXML
    private Agenda agenda;

    @FXML
    private JFXDrawer drawer;

    private List<Cita> citas;

    // Recurso Interfaz de Agenda
    private final Map<String, Agenda.AppointmentGroup> mapaDeGrupos = new TreeMap<String, Agenda.AppointmentGroup>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rootPane.setOpacity(0);
        fadeInTransition();
        drawer.setResizeContent(true);
        drawer.setOverLayVisible(false);
        drawer.setResizableOnDrag(true);
        prepararAgenda();
        cargarCitas();
    }

    @FXML
    public void agregarCita() {
        try {
            Stage stage = (Stage) agenda.getScene().getWindow();
            Scene scenePrincipal = new Scene(FXMLLoader.load(getClass().getResource("/view/FXMLGestionarCita.fxml")));
            stage.setScene(scenePrincipal);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLInicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void clickHamburger(ActionEvent event) {
        try {
            VBox box = FXMLLoader.load(getClass().getResource("/view/FXMLDrawer.fxml"));
            for (Node node : box.getChildren()) {
                node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    if (node.getAccessibleText() != null) {
                        switch (node.getAccessibleText()) {
                            case "Citas":
                                drawer.close();
                                drawer.setMouseTransparent(true);
                                break;
                            case "Consultas":
                                //cargarExperiencias();
                                break;
                            case "Pacientes":
                                //cargarExperiencias();
                                break;
                            case "Dietas":
                                //cargarExperiencias();
                                break;
                        }
                    }
                });
            }
            drawer.setSidePane(box);
            drawer.setEffect(new DropShadow());
            drawer.open();
            drawer.setMouseTransparent(false);
        } catch (IOException e) {

        }
    }

    /**
     * Configuración inicial de la Agenda
     */
    public void prepararAgenda() {
        for (Agenda.AppointmentGroup grupo : agenda.appointmentGroups()) {
            mapaDeGrupos.put(grupo.getDescription(), grupo);
        }
        agenda.setActionCallback((Agenda.Appointment cita) -> {
            mostrarDatosCita(cita);
            return null;
        });
        agenda.setEditAppointmentCallback((Agenda.Appointment clase) -> null);
        agenda.setAllowDragging(false);
        agenda.setAllowResize(false);
    }

    public void cargarCitas() {
        agenda.appointments().clear();
        String url = Constantes.URL + "citas/getAllCitas";
        RespuestaWS resp = ConsumoWS.consumoWSGET(url);
        if (resp.getCodigo() == 200) {
            Gson gson = new Gson();
            Type listaCitas = new TypeToken<List<Cita>>() {
            }.getType();
            ArrayList<Cita> citasBD = gson.fromJson(resp.getMensaje(), listaCitas);
            citas = FXCollections.observableArrayList(citasBD);
            if (!citas.isEmpty()) {
                int i, cont = citas.get(0).getIdCita(), sum = 1;
                for (Cita c : citas) {
                    i = c.getIdCita();
                    if (cont != i) {
                        cont = c.getIdCita();
                        sum++;
                    }
                    c.setAppointmentGroup(mapaDeGrupos.get("group" + (sum < 10 ? "0" : "") + sum));
                }
                agenda.appointments().addAll(citas);
            }
        } else {
            muestraDialogError("Error de conexión", "Lo sentimos, tenemos problemas con el servidor");
        }
    }

    /**
     * Se genera un JFXDialog con la interfaz y los datos de una Clase vinculada
     * a un Appointment seleccionado en la Agenda
     *
     * @param appointment Appointment seleccionado como respuesta a un click en
     * la Agenda
     */
    public void mostrarDatosCita(Agenda.Appointment appointment) {
        Cita cita = buscarCita(appointment);
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(appointment.getSummary()));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/FXMLDatosClase.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(FXMLCitasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        FXMLGestionarCitaController display = loader.getController();
        //display.montarDatos(cita);
        VBox p = loader.getRoot();
        content.setBody(p);
        //JFXDialog dialog = new JFXDialog(rootPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton aceptar = new JFXButton("ACEPTAR");

//        display.getButtonEliminar().setOnAction((ActionEvent e) -> {
//            eliminarClase();
//            dialog.close();
//        });
//
//        display.getButtonEditar().setOnAction((ActionEvent e) -> {
//            modificarClase(clase);
//        });
//
//        aceptar.setOnAction((ActionEvent e) -> {
//            dialog.close();
//        });
//
//        content.setActions(aceptar);
//        dialog.show();
    }
    
    public Cita buscarCita(Agenda.Appointment appointment) {
    for (Cita cita : citas) {
      if (cita.getDescription() == appointment.getDescription() && cita.getStartLocalDateTime() == appointment.getStartLocalDateTime()
          && cita.getLocation() == appointment.getLocation() && cita.getSummary() == appointment.getSummary()
          && cita.getEndLocalDateTime() == appointment.getEndLocalDateTime()) {
        return cita;
      }
    }
    return null;
  }

    private void muestraDialogError(String titulo, String mensaje) {
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setTitle(titulo);
        alertError.setHeaderText(null);
        alertError.setContentText(mensaje);
        alertError.showAndWait();
    }

    // Animaciones
    /**
     * Animación de entrada para la escena
     */
    public void fadeInTransition() {
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(300));
        transition.setNode(rootPane);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.play();
    }

    /**
     * Animación de salida para la escena
     */
    public void fadeOutTransition() {
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(300));
        transition.setNode(rootPane);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.play();
    }

}
