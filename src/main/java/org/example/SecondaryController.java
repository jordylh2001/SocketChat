package org.example;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SecondaryController extends Thread implements Initializable {

    public TextArea chat;
    public TextField mensajeTF;
    public Button envioBt;
    public Button editar;
    public ImageView avatar;
    public Label user_name;
    public Label label1;
    public Label label2;
    public Label label3;
    public CheckBox checkedit;
    public TextField name2;
    public TextField pass2;
    public TextField imag2;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private Image imagen;
    private List<File> files;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(!PrimaryController.image.equals("default")){
            InputStream stream= null;
            try {
                stream = new FileInputStream(PrimaryController.image);
            } catch (FileNotFoundException e) {
                PrimaryController prim=new PrimaryController();
                prim.Popup(e.getMessage());
            }
            imagen=new Image(stream);
            avatar.setImage(imagen);
        }else{
            InputStream stream= null;
            try {
                stream = new FileInputStream("src/main/imagenes/default.jpeg");
            } catch (FileNotFoundException e) {
                PrimaryController prim=new PrimaryController();
                prim.Popup(e.getMessage());
            }
            imagen=new Image(stream);
            avatar.setImage(imagen);
        }
        user_name.setText(PrimaryController.username);
        connectSocket();
        writer.println(PrimaryController.username+" has connected!");
        chat.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
    }

    private void connectSocket() {
        try {
            socket = new Socket("localhost", 8889);
            chat.appendText("Socket is connected with server!"+"\n");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            PrimaryController prim=new PrimaryController();
            prim.Popup(e.getMessage());
        }
    }

    @Override
    public void run() {
        App.stage.setHeight(484.0);
        App.stage.setWidth(1000.0);
        try {
            while (true) {
                String msg = reader.readLine();
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];
                System.out.println(cmd);
                StringBuilder fulmsg = new StringBuilder();
                for(int i = 1; i < tokens.length; i++) {
                    fulmsg.append(tokens[i]);
                }
                System.out.println(fulmsg);
                if (cmd.equalsIgnoreCase(PrimaryController.username + ":")) {
                    continue;
                } else if(fulmsg.toString().equalsIgnoreCase("bye")) {
                    chat.appendText(PrimaryController.username+" has disconnected!");
                    break;
                }
                chat.appendText(msg + "\n");
            }
            reader.close();
            writer.close();
            socket.close();

        } catch (Exception e) {
            PrimaryController prim=new PrimaryController();
            prim.Popup(e.getMessage());
        }
    }

    public void Enviar() {
        String msg = mensajeTF.getText();
        writer.println(PrimaryController.username + ": " + msg);
        chat.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        chat.appendText("Me: " + msg + "\n");
        mensajeTF.setText("");
        if(msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);
        }
    }

    public void habilitar(ActionEvent actionEvent) {
        if(checkedit.isSelected()){
            label1.setVisible(true);
            label2.setVisible(true);
            label3.setVisible(true);
            name2.setVisible(true);
            name2.setEditable(true);
            pass2.setVisible(true);
            pass2.setEditable(true);
            imag2.setVisible(true);
            imag2.setEditable(true);
            editar.setVisible(true);
            editar.setDisable(false);
            name2.setText(PrimaryController.username);
            pass2.setText(PrimaryController.contra);
            imag2.setText(PrimaryController.image);
        }else{
            label1.setVisible(false);
            label2.setVisible(false);
            label3.setVisible(false);
            name2.setVisible(false);
            name2.setEditable(false);
            pass2.setVisible(false);
            pass2.setEditable(false);
            imag2.setVisible(false);
            imag2.setEditable(false);
            editar.setVisible(false);
            editar.setDisable(true);
            name2.setText("");
            pass2.setText("");
            imag2.setText("");
        }
    }

    public void Editar(ActionEvent actionEvent) {
        String name=name2.getText();
        String pass=pass2.getText();
        String imagen2=imag2.getText();
        editarperfil(name,pass,imagen2);

    }

    private void editarperfil(String name, String pass, String imagen2) {
        try{
            String path="src/main/Users/"+PrimaryController.username+".txt";
            File file=new File(path);
            FileWriter escribir=new FileWriter(file);
            escribir.write("nickname: "+name+"\n");
            escribir.write("image: "+imagen2+"\n");
            escribir.write("password: "+pass+"\n");
            escribir.close();
            File oldfile= new File("src/main/Users/"+PrimaryController.username+".txt");
            File newFile= new File("src/main/Users/"+name+".txt");
            if(oldfile.renameTo(newFile)){
                System.out.println("Archivo renombrado");
            }else{
                System.out.println("No se pudo renombrar");
            }
            user_name.setText(name);
            PrimaryController.username=name;
            PrimaryController.image=imagen2;
            PrimaryController.contra=pass;
            if(!PrimaryController.image.equals("default")){
                InputStream stream= null;
                try {
                    stream = new FileInputStream(PrimaryController.image);
                } catch (FileNotFoundException e) {
                    PrimaryController prim=new PrimaryController();
                    prim.Popup(e.getMessage());
                }
                imagen=new Image(stream);
                avatar.setImage(imagen);
            }else{
                InputStream stream= null;
                try {
                    stream = new FileInputStream("src/main/imagenes/default.jpeg");
                } catch (FileNotFoundException e) {
                    PrimaryController prim=new PrimaryController();
                    prim.Popup(e.getMessage());
                }
                imagen=new Image(stream);
                avatar.setImage(imagen);
            }
        }catch(Exception e){
            PrimaryController prim=new PrimaryController();
            prim.Popup(e.getMessage());
        }
    }

    public void files(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Attach File");
        Stage stage = (Stage) App.stage.getScene().getWindow();
        List<File> list = fileChooser.showOpenMultipleDialog(stage);
        if (list != null){
            for (File file : list){
                System.out.println(file.toString());
            }
            this.files = list;
        }
        sendFiles(files);
    }

    private void sendFiles(List<File> files) throws IOException {
        /*
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        Socket sk = null;
        String elemento;
        try {
            while (true) {
                try {
                    // se envía el archivo
                    for(int i=0;i<files.size();i++){
                        File archivo = new File (String.valueOf(files.get(i)));
                        byte [] ab  = new byte [(int)archivo.length()]; //se crea un array de bytes
                        fis = new FileInputStream(archivo);
                        bis = new BufferedInputStream(fis);
                        bis.read(ab,0,ab.length);
                        os = sk.getOutputStream();
                        System.out.println("Enviando " + archivo + "(" + ab.length + " bytes)");
                        os.write(ab,0,ab.length);
                        os.flush();
                    }
                    System.out.println("Enviado.");
                } catch (FileNotFoundException e) {
                    PrimaryController prim=new PrimaryController();
                    prim.Popup(e.getMessage());
                } catch (IOException e) {
                    PrimaryController prim=new PrimaryController();
                    prim.Popup(e.getMessage());
                } finally {
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (sk!=null) sk.close();
                }
            }
        }
        finally {
            if (socket != null) socket.close();
        }
        */
    }
}