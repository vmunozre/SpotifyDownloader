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
    private List<String> artistas;
    private String url;
    public Cancion(String nombre, String album){
        this.nombre = nombre;
        this.album = album;
        this.artistas = new ArrayList<>();
        this.url = "";
    }
    
    public String getQuery(){
        String query = nombre + " " + album + " ";
        for(String artista : artistas){
            query += artista + " ";
        }
        return query;
    }
    
    public void addArtista(String artista){
        artistas.add(artista);
    }
    
    //GETTERS AND SETTERS
    
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
    
    public void mostrarCancion(){
        System.out.println("------------ Cancion ------------");
        System.out.println("Nombre: " + this.nombre);
        System.out.println("Album: " + this.album);
        System.out.print("Artistas: [");
        for(String artista : artistas){
            System.out.print(artista + ", ");
        }
        System.out.println("]");
        System.out.println("URL: " + this.url);
        System.out.println("------------ Cancion ------------");
    }
    
}