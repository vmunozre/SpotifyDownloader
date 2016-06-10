/*
    Licencia:
    «Copyright 2016 ReiGon - Victor Reiner & Gonzalo Ruanes»

    This file is part of SpotifyDownloader.

    SpotifyDownloader is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SpotifyDownloader is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.reigon.spotifydownloader;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.reigon.spotifydownloader.DownloadMP3.DownloadRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/*
 * @author Victor_Reiner_&_Gonzalo_Ruanes
 */
public class Main {

    public static void main(String[] args) {
        
        Interface IU = new Interface();
        IU.iniciar();
        /*
        ArrayList<Cancion> failedsongs = new ArrayList<Cancion>();
        
        //Input URL playlist
        UrlInputDialog input = new UrlInputDialog(new javax.swing.JFrame(), true);
        input.setVisible(true);
        String url = input.getUrl();
        
        //Selecctor de carpeta
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.showOpenDialog(null);
        String path = fileChooser.getSelectedFile().getAbsolutePath() + File.separator;
        System.out.println("Path: " + path);
        
                
        //Interfaz de progreso
        TextUI textui = new TextUI();
        textui.setVisible(true);
        textui.printText("Recuperando lista de Spotify");
        
        //Sacamos datos de la playlist de spotify usando su API
        SpotifyProcessor spoti = new SpotifyProcessor(textui);
        
        
        try {
            spoti.process(url);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        textui.clearText();
        
        //Buscamos en Youtube las canciones
        textui.printText("Buscando Canciones");
        YoutubeSearch yout = new YoutubeSearch(textui);
        List<Cancion> canciones;
        canciones = yout.process(spoti.getListaCanciones()); 
        
        //Procedemos a descargar las canciones
        ExecutorService service = Executors.newFixedThreadPool(5);
        textui.clearText();
        textui.printText("Descargando Canciones");
        //Comento para pruebas
        for (Cancion cancion : canciones) {

            try {
                if (!cancion.getVideoID().isEmpty()) {
                    String nombreArchivo = (cancion.getNombre() + " - " + cancion.getArtistas().get(0)).replace("|", "").replace("/", "").replace(":", "").replace("*", "").replace("?", "").replace("<", "").replace(">", "");
                    //Comprobamos que no exista
                    File src = new File(path + cancion.getNombre() + ".mp3");
                    if (!src.exists()) {
                        service.submit(new DownloadRequest(cancion.getVideoID(), path, nombreArchivo, textui)).get();
                        try {
                            // the file we are going to modify
                            cancion.saveMetadata(path , nombreArchivo);
                        } catch (UnsupportedTagException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvalidDataException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (NotSupportedException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        System.out.println("REPETIDA!");
                        textui.printText("La canción "+ cancion.getNombre() +" Está repetida");
                    }

                } 

            } catch (InterruptedException | ExecutionException ex) {

                System.out.println("El video " + cancion.getUrl() + " No ha podido descargarse, por favor bajalo manualmente");
                textui.printText("La canción " + cancion.getNombre() + " No ha podido descargarse, por favor, descargala manualmente");
                failedsongs.add(cancion);
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        service.shutdown();
        textui.clearText();
        textui.printText("Estas son las canciones que no se han podido descargar automáticamente");
        textui.printText("puedes bajartelas manualmente utilizando el link en youtube-mp3.org");
        textui.printText("Canciones fallidas: " + failedsongs.size());
        FileWriter fi = null;

        PrintWriter pw = null;
        
        //Guardamos en un fichero error_log.txt las canciones que han fallado.
        try {
            fi = new FileWriter(path + "__error_log.txt");
            pw = new PrintWriter(fi);
            pw.println("Canciones fallidas: " + failedsongs.size());
            pw.println();
            for (Cancion s : failedsongs) {
                textui.printText("Titulo de la canción: " + s.getNombre() + " - " + s.getArtistas().get(0));
                textui.printText("Link de descarga: " + s.getUrl());

                pw.println("Titulo de la canción: " + s.getNombre() + " - " + s.getArtistas().get(0));
                pw.println("Link de descarga: " + s.getUrl());
                pw.println("---------------------------------------------------------------");
                pw.println();

            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fi.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        textui.printText("Se ha terminado de descargar ------ Ya puede cerrar la aplicación ");
        Scanner in = new Scanner(System.in);
        String end = in.nextLine();
        System.exit(0);*/
    }
}
