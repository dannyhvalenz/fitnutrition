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
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import pojos.Consulta;
import pojos.Dieta;
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
public class FXMLFormularioConsultaController implements Initializable {

    
    private NotificaCambios notificacion;
    private boolean isNuevo;
    private Consulta consulta;
    
    private ObservableList<Paciente> pacientes;
    private ObservableList<Dieta> dietas;
    @FXML
    private Label lbTitulo;
    @FXML
    private ComboBox<Paciente> cbPaciente;
    @FXML
    private TextArea taObservaciones;
    @FXML
    private TextField tfPeso;
    @FXML
    private TextField tfTalla;
    @FXML
    private TextField tfIMC;
    @FXML
    private ComboBox<Dieta> cbDieta;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargaElementosCombo();
    }
    
    public void inicializaCampos(NotificaCambios notificacion,boolean isNuevo, Consulta consulta){
        this.notificacion = notificacion;
        this.isNuevo = isNuevo;
        this.consulta = consulta;
        if(!isNuevo){
            lbTitulo.setText("Editar dieta");
            cargaDatosEdicion();
        }
    }
    
    private void cargaDatosEdicion(){
        if(consulta != null){        
            int pos = getIndexListaPaciente(consulta.getIdPaciente());
            cbPaciente.getSelectionModel().select(pos);
            taObservaciones.setText(consulta.getObservaciones());
            tfPeso.setText(consulta.getPeso()+"");
            tfTalla.setText(consulta.getTalla());
            tfIMC.setText(consulta.getImc()+"");
            pos = getIndexListaDieta(consulta.getIdDieta());
            cbDieta.getSelectionModel().select(pos);
            System.out.println("idPaciente: " + consulta.getIdPaciente() + "idDieta: " + consulta.getIdDieta());
        }
    }
    
    private int getIndexListaPaciente(int idPaciente){
        int i=0;
        for(i=0; i < pacientes.size(); i++){
            if(pacientes.get(i).getIdPaciente() == idPaciente){
                return i;
            }
        }
        return i;
    }
    
    private int getIndexListaDieta(int idDieta){
        int i=0;
        for(i=0; i < dietas.size(); i++){
            if(dietas.get(i).getIdDieta() == idDieta){
                return i;
            }
        }
        return i;
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
            cbPaciente.setItems(pacientes);
        }else{
            muestraDialogoError("Error de conexión", "Lo sentimos, tenemos problemas con el servidor");
        }
        
        url = Constantes.URL + "dietas/getAllDietas";
        resp = ConsumoWS.consumoWSGET(url);
        if(resp.getCodigo() == 200){
            Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
            Type dietaLista = new TypeToken<ArrayList<Dieta>>(){}.getType();
            System.out.println(dietaLista);
            System.out.println(resp.getMensaje());
            ArrayList<Dieta> tipoBD = gson.fromJson(resp.getMensaje(), dietaLista);
            System.out.println("CONTENIDO DE tipoBD" + tipoBD.get(0));
            dietas = FXCollections.observableArrayList(tipoBD);
            //System.out.println(medicos.size());
            cbDieta.setItems(dietas);
        }else{
            muestraDialogoError("Error de conexión", "Lo sentimos, tenemos problemas con el servidor");
        }
    }
    
    private void cerrarScena(){
        Stage stage = (Stage) this.taObservaciones.getScene().getWindow();
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

    @FXML
    private void clicEnviar(ActionEvent event) {
        int seleccionP = pacientes.get(cbPaciente.getSelectionModel().getSelectedIndex()).getIdPaciente();
        int seleccionD = dietas.get(cbDieta.getSelectionModel().getSelectedIndex()).getIdDieta();
        if(isNuevo){            
            String url = Constantes.URL + "consultas/registrarConsulta";
            String parametros = String.format("idPaciente=%d&observaciones=%s&peso=%f&talla=%s&IMC=%f&idDieta=%d",
                    seleccionP, taObservaciones.getText(), Float.valueOf(tfPeso.getText()), tfTalla.getText(), Float.valueOf(tfIMC.getText()), seleccionD);
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
            String url = Constantes.URL + "consultas/actualizarConsulta";
            String parametros = String.format("idConsulta=%d&idPaciente=%d&observaciones=%s&peso=%f&talla=%s&IMC=%f&idDieta=%d",
                    consulta.getIdConsulta(), seleccionP, taObservaciones.getText(), Float.valueOf(tfPeso.getText()), tfTalla.getText(), Float.valueOf(tfIMC.getText()), seleccionD);
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

    @FXML
    private void calcularIMC1(KeyEvent event) {
        String cPeso="";
        //System.out.println(tfPeso.getText());
        char[] cad = tfPeso.getText().toCharArray();
        if(cad[2] == '.'){                       
            for(int i=0; i<5; i++){               
               cPeso += cad[i];
            }            
        }else if(cad[3]=='.'){            
            for(int i=0; i<6; i++){
                cPeso += cad[i];
            }            
        }
        if(cPeso.length()>=5 || cPeso.length() >=6){
            calcularIMC2(cPeso);
        }
    }
    
    @FXML
    private void calcularIMC2(String cPeso){
        float peso, estatura, estatu2, IMC;
        peso = Float.valueOf(cPeso);
        //System.out.print("Valor convertido a float: " + peso);
        Paciente paci = cbPaciente.getSelectionModel().getSelectedItem();
        System.out.println("paci: " + paci.getEstatura());
        estatura = Float.valueOf(paci.getEstatura())/100;
        System.out.println("estatura: "+estatura);
        estatu2 = estatura*estatura;
        IMC = peso / estatu2;
        System.out.println("peso: "+peso +" - estatu2: " + estatu2);
        tfIMC.setText(IMC+"");
    }
}
