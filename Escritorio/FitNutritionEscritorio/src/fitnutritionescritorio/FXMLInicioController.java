/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fitnutritionescritorio;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojos.Mensaje;
import pojos.RespuestaWS;
import util.Constantes;
import util.ConsumoWS;

public class FXMLInicioController implements Initializable {
    
    @FXML
    private Button button;
    @FXML
    private TextField tUsuario;
    @FXML
    private PasswordField tPassword;
    @FXML
    private Label lbErrorUsuario;
    @FXML
    private Label lbErrorPassword;
    
    @FXML
    private void clicLogin(ActionEvent event) throws IOException {
        lbErrorUsuario.setText("");
        lbErrorPassword.setText("");
        String usuario = tUsuario.getText();
        String password = tPassword.getText();
        boolean isValido = true;
        
        if(usuario.isEmpty()){
            lbErrorUsuario.setText("Campo Usuario Requerido");
            isValido = false;
        }
        
        if(password.isEmpty()){
            lbErrorPassword.setText("Campo Contraseña Requerido");
            isValido = false;
        }
        
        if(isValido){
            try{
                String parametros = String.format("usuario=%s&contrasena=%s", usuario, password);
                String url = Constantes.URL + "sesion/login";
                RespuestaWS respServicio = ConsumoWS.consumoWSPOST(url, parametros);
                if(respServicio.getCodigo() == 200){
                    Gson gson = new Gson();
                    Mensaje msj = gson.fromJson(respServicio.getMensaje(), Mensaje.class);
                    if(msj.isError()){                        
                        muestraDialogError("Usuario no encontrado", msj.getMensaje());
                    }else{
                        muestraDialogError("Usuario encontrado", msj.getMensaje());
                        //irPrincipal();
                    }
                }else{
                    muestraDialogError("Error de conexión", "Lo sentimos, tenemos problemas con el servidor");
                }
                
               
            }catch(Exception ex){
                Logger.getLogger(FXMLInicioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }
    
    private void irPrincipal(){
        try{
            Stage stage = (Stage) tUsuario.getScene().getWindow();
            Scene scenePrincipal = new Scene(FXMLLoader.load(getClass().getResource("FXMLPrincipal.fxml")));
            stage.setScene(scenePrincipal);
            stage.show(); 
        }catch(IOException ex){
            Logger.getLogger(FXMLInicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void muestraDialogError(String titulo, String mensaje){
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setTitle(titulo);
        alertError.setHeaderText(null);
        alertError.setContentText(mensaje);
        alertError.showAndWait();            
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }      
    
}
