/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reigon.spotifydownloader;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradientBuilder;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;

/**
 *
 * @author Victor
 */
public class Interface extends Application {

    private String url;
    private String path;

    private TextField turl;
    private TextField tpath;

    public Interface() {

    }

    @Override
    public void start(Stage primaryStage) {

        //Grid
        GridPane root = new GridPane();
        root.setPadding(new Insets(12));
        root.setStyle("-fx-background-color:#8bc34a;");
        root.setHgap(2);
        root.setVgap(2);
        root.setAlignment(Pos.CENTER);

        //Titulo
        Text titulo = new Text("YouDownloadify");
        titulo.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 48));
        root.add(titulo, 1, 0);
        root.setHalignment(titulo, HPos.CENTER);

        //Labels
        Label info1 = new Label("Introduzca la URL de la PlayList");
        Label info2 = new Label("Para obtener ayuda pulse el botón ['?']");
        root.add(info1, 1, 3);
        root.setHalignment(info1, HPos.CENTER);
        root.add(info2, 1, 4);
        root.setHalignment(info2, HPos.CENTER);

        //Input url
        turl = new TextField();
        turl.setPrefWidth(300);
        turl.setPrefHeight(40);
        turl.setPromptText("Introduzca el URL de la PlayList aqui...");
        turl.setFocusTraversable(true);

        root.add(turl, 1, 5);
        root.setHalignment(turl, HPos.CENTER);

        //Selección Carpeta
        tpath = new TextField();
        tpath.setPrefWidth(200);
        tpath.setPrefHeight(40);
        tpath.setPromptText("Ruta de descarga...");
        tpath.setFocusTraversable(true);

        root.add(tpath, 1, 7);
        root.setHalignment(tpath, HPos.CENTER);

        Button bselecarpeta = new Button();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream("icfolder.png");
        Image icfolder = new Image(is);
        bselecarpeta.setGraphic(new ImageView(icfolder));
        root.add(bselecarpeta, 2, 7);
        root.setHalignment(bselecarpeta, HPos.CENTER);
        DirectoryChooser dirChooser = new DirectoryChooser();

        bselecarpeta.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Path: " + path);
                String localPath = dirChooser.showDialog(primaryStage).getAbsolutePath() + File.separator;

                tpath.setText(localPath);
                System.out.println("Path: " + localPath);
            }
        });

        //Descargar
        Button bdescargar = new Button("Descargar");
        bdescargar.getStyleClass().add("button1");
        root.add(bdescargar, 1, 9);
        GridPane.setHalignment(bdescargar, HPos.CENTER);

        bdescargar.setOnMouseClicked((MouseEvent event) -> {
            String localUrl = turl.getText();
            String localPath = tpath.getText();
            if (isValidUrl(localUrl)) {
                url = localUrl;
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Error URL PlayList");
                alert.setHeaderText(null);
                alert.setContentText("La url introducida no está bien formada, por favor"
                        + " intentelo de nuevo o acuda a ['?'] para buscar ayuda.");
                alert.showAndWait();
            }
            File pathCheck = new File(localPath);
            if(pathCheck.isDirectory()){
                path = localPath;
            }else{
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Error Ruta Carpeta");
                alert.setHeaderText(null);
                alert.setContentText("La rúta de la carpeta introducida no es correcta o no existe.");
                alert.showAndWait();
            }
            if(isValidUrl(localUrl)&&pathCheck.isDirectory()){
                primaryStage.close();
                Downloader d = new Downloader(localUrl,path);
                d.start();
                
            }
        });

        //Info
        Label info3 = new Label("Copyright (C) 2016 Victor_Reiner & Gonzalo Ruanes");
        root.add(info3, 1, 12);
        GridPane.setHalignment(info3, HPos.CENTER);

        //Boton Ayuda
        Button bayuda = new Button();
        ClassLoader c2 = Thread.currentThread().getContextClassLoader();
        InputStream is2 = c2.getResourceAsStream("ichelp.png");
        Image ichelp = new Image(is2);
        bayuda.setGraphic(new ImageView(ichelp));
        root.add(bayuda, 2, 12);
        root.setHalignment(bayuda, HPos.RIGHT);

        bayuda.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Inciar panel
                Stage modalStage = new Stage();
                GridPane helpPane = new GridPane();
                helpPane.setPadding(new Insets(12));
                helpPane.setStyle("-fx-background-color:#8bc34a;");

                //Titulo
                Text tituloHelp = new Text("Ayuda");
                tituloHelp.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 48));
                helpPane.add(tituloHelp, 1, 0);
                helpPane.setHalignment(tituloHelp, HPos.CENTER);

                //Info
                Label infoh1 = new Label("Versión: 1 - Oficial");
                Label infoh2 = new Label("Copyright (C) 2016 Victor_Reiner & Gonzalo Ruanes");
                Label infoh3 = new Label("");
                Label infoh4 = new Label("SpotifyDownloader es un proyecto realizado por Victor Reiner y Gonzalo Ruanes\r\n"
                        + "con objetivo didáctico de aprender a usar las APIs de Youtube y Spotify.\r\n"
                        + "Por ello no nos hacemos responsables de su uso indebido.");
                Label infoh5 = new Label("¿Tienes dudas de como funciona la aplicación?");
                Label infoh6 = new Label("Visita nuestra guía:");
                Label infoh7 = new Label("http://vmunozre.github.io/SpotifyDownloader");
                Hyperlink link = new Hyperlink();
                link.setText("http://vmunozre.github.io/SpotifyDownloader");

                helpPane.add(infoh1, 1, 1);
                helpPane.add(infoh2, 1, 2);
                helpPane.add(infoh3, 1, 3);
                helpPane.add(infoh4, 1, 4);

                helpPane.add(infoh5, 1, 6);
                helpPane.add(infoh6, 1, 7);
                helpPane.add(link, 1, 8);
                helpPane.setHalignment(infoh1, HPos.CENTER);
                helpPane.setHalignment(infoh2, HPos.CENTER);
                helpPane.setHalignment(infoh3, HPos.CENTER);
                helpPane.setHalignment(infoh4, HPos.CENTER);
                helpPane.setHalignment(infoh5, HPos.CENTER);
                helpPane.setHalignment(infoh6, HPos.CENTER);
                helpPane.setHalignment(link, HPos.CENTER);

                link.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        HostServicesDelegate hostServices = HostServicesFactory.getInstance(new Application() {
                            @Override
                            public void start(Stage primaryStage) throws Exception {

                            }
                        });
                        hostServices.showDocument("http://vmunozre.github.io/SpotifyDownloader");
                    }

                });
                //Creacion de la escena
                Scene modalScene = new Scene(helpPane);
                modalStage.setScene(modalScene);
                modalStage.initModality(Modality.APPLICATION_MODAL);
                modalStage.showAndWait();
            }
        });

        //Escena
        Scene scene = new Scene(root);
        ClassLoader c3 = Thread.currentThread().getContextClassLoader();
        InputStream is3 = c3.getResourceAsStream("style.css");
        scene.getStylesheets().add(c3.getResource("style.css").toString());
        primaryStage.setTitle("YouDownloadify");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public boolean isValidUrl(String input) {
        //A valid url would be
        //https://open.spotify.com/user/reiner13/playlist/2plTFnZFDDIhyhGIGy377e
        boolean valid = false;
        String[] partes = input.split("/");
        if (partes.length != 7) {
            return false;
        }
        if ("open.spotify.com".equals(partes[2]) && "user".equals(partes[3]) && "playlist".equals(partes[5])) {
            valid = true;
        }
        return valid;
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return path;
    }


    public void iniciar() {
        Application.launch();
    }

}
