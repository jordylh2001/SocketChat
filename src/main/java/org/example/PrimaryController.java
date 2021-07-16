package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PrimaryController{

    public TextField passTF;
    public TextField nameTF;
    public Button crearBT;
    public Button iniciarTF;
    public static String username,contra,image;

    public void Crear(ActionEvent actionEvent) {
        String name=String.valueOf(nameTF.getText());
        String pass=String.valueOf(passTF.getText());
        nameTF.setText("");
        passTF.setText("");
        if(!Checar(name)){
            Crearuser(name,pass);
        }else{
            Popup("El usuario ya existe");
        }
    }

    @FXML
    public void Popup(String message) {
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Atencion");
            alert.setContentText(message);
            alert.setResizable(true);
            alert.showAndWait();
    }

    public void Crearuser(String name, String pass) {
        try{
            String ruta="src/main/Users/"+name+".txt";
            ruta=ruta.replaceAll(" ","");
            File file=new File(ruta);
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fw=new FileWriter(file);
            BufferedWriter bw=new BufferedWriter(fw);
            bw.write("nickname: "+name+"\n");
            bw.write("image: default"+"\n");
            bw.write("password: "+pass+"\n");
            bw.close();
        }catch(Exception e){
            Popup(e.getMessage());
        }
    }

    public boolean Checar(String name) {
        boolean estado=false;
        try{
            String ruta="src/main/Users/"+name+".txt";
            File file =new File(ruta);
            if(file.exists()){
                estado=true;
            }else{
                estado=false;
            }
        }catch(Exception e){
            Popup(e.getMessage());
        }
        return estado;
    }



    public void Iniciar(ActionEvent actionEvent) throws IOException {
        String name=String.valueOf(nameTF.getText());
        String pass=String.valueOf(passTF.getText());
        username=name;
        nameTF.setText("");
        passTF.setText("");
        if(Checar(name)){
            if(IniciarSesion(name, pass)){
                App.setRoot("secondary");
            }
        }else{
            Popup("Verifique los datos del usuario");
        }
    }

    public boolean IniciarSesion(String name, String pass) {
        try{
            String cadena;

            String[] split;
            int cont=1;
            FileReader f = new FileReader("src/main/Users/"+name+".txt");
            BufferedReader b=new BufferedReader(f);
            while((cadena=b.readLine())!=null){
                split=cadena.split(" ");
                if(cont==1){
                    username=split[1];
                    username=username.replaceAll(" ","");
                }if(cont==2){
                    image=split[1];
                    image=image.replaceAll(" ","");
                }if(cont==3){
                    contra=split[1];
                    contra=contra.replaceAll(" ","");
                }
                cont++;
            }
            if(username.equals(name) && contra.equals(pass)){
                b.close();
                return true;
            }else{
                b.close();
                return false;
            }
        }catch(Exception e){
            Popup(e.getMessage());
        }
        return false;
    }

}
