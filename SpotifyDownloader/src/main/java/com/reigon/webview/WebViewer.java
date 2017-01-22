/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reigon.webview;

import java.io.File;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.boot.SpringApplication;

/**
 *
 * @author cimba
 */
public class WebViewer extends Application{
    private static Scene scene;
    private static Stage pStage;
    private static final int MINX = 800;    //bounds.getMinX()
    private static final int MINY = 600;    //bounds.getMaxY()
    public WebViewer(){
        super();
    }
    
    @Override public void start(Stage stage) {
        this.pStage = stage;
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        // create the scene
        stage.setTitle("Spotify Downloader");
        scene = new Scene(new Browser(),MINX,MINY, Color.web("#666970"));
        stage.setScene(scene);
        //scene.getStylesheets().add("webviewsample/BrowserToolbar.css");        
        stage.show();
        //stage.setMaximized(true);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
              System.exit(0);
          }
        }); 
        
    }
    public static void main(String[] args){
        launch(args);
    }
    public Stage getStage() {
        return pStage;
    }
    public String getDirChooser(){
        String strReturn = "";
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Seleccionar Carpeta Descarga");

        File selectedDirectory = chooser.showDialog(scene.getWindow());
        
        strReturn = selectedDirectory.getAbsolutePath();
        
        return strReturn;
    }
    
    
}
