/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reigon.spotifydownloader;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Victor_Reiner
 */
public class Cancion {

    private final String nombre;
    private final String album;
    private final int duracion;
    private final int UMBRAL = 40;
    
    private List<String> artistas;
    private String url;
    private String videoID;
    
    public Cancion(String nombre, String album, int duracion) {
        this.nombre = nombre;
        this.album = album;
        this.duracion = duracion/1000;   //SEGUNDOS
        this.artistas = new ArrayList<>();
        this.url = "";
        this.videoID = "";
    }

    public String getQuery() {
        String query = nombre + " " + album + " ";
        if (!artistas.isEmpty()) {
            query += artistas.get(0);
        }
    
        return query;
    }

    public String getEasyQuery() {
        String query = nombre + " ";
        if (!artistas.isEmpty()) {
            query += artistas.get(0);
        }

        return query;
    }
    public boolean duracionAceptable(int duracionVideo){
        return (((duracion-UMBRAL)<=duracionVideo) && (duracionVideo < (duracion+UMBRAL)));          
    }
    //Segundos
    

    public void addArtista(String artista) {
        artistas.add(artista);
    }

    public String getVideoID() {
        return videoID;
    }

    //GETTERS AND SETTERS
    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAlbum() {
        return album;
    }

    public List<String> getArtistas() {
        return artistas;
    }

    public void mostrarCancion() {
        System.out.println("------------ Cancion ------------");
        System.out.println("Nombre: " + this.nombre);
        System.out.println("Album: " + this.album);
        System.out.println("Duracion: " + this.duracion);
        System.out.print("Artistas: [");
        for (String artista : artistas) {
            System.out.print(artista + ", ");
        }
        System.out.println("]");
        System.out.println("URL: " + this.url);
        System.out.println("------------ Cancion ------------");
    }

}
