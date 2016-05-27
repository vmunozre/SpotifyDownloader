/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reigon.spotifydownloader;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;
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
    private final int numCancion;
    private final int numDisc;

    private List<String> artistas;
    private String url;
    private String videoID;

    public Cancion(String nombre, String album, int duracion, int numCancion, int numDisc) {
        this.nombre = nombre;
        this.album = album;
        this.duracion = duracion / 1000;   //SEGUNDOS
        this.artistas = new ArrayList<>();
        this.numCancion = numCancion;
        this.numDisc = numDisc;
        this.url = "";
        this.videoID = "";
    }

    public String getQuery() {
        String query = nombre + " ";
        if (!artistas.isEmpty()) {
            query += "\"" + artistas.get(0) + "\"";
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

    public boolean duracionAceptable(int duracionVideo) {
        return (((duracion - UMBRAL) <= duracionVideo) && (duracionVideo < (duracion + UMBRAL)));
    }

    //Segundos
    public String getPrimerArtista() {
        String res = "";
        res += artistas.get(0);

        return res;
    }

    public void addArtista(String artista) {
        artistas.add(artista);
    }

    public int getDuracion() {
        return duracion;
    }

    public int getNumCancion() {
        return numCancion;
    }

    public String getVideoID() {
        return videoID;
    }

    public int getNumDisc() {
        return numDisc;
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

    public void saveMetadata(String path, String filename) throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {

        Mp3File mp3file = new Mp3File(path + filename);
        ID3v1 id3v1Tag;
        if (mp3file.hasId3v1Tag()) {
            id3v1Tag = mp3file.getId3v1Tag();
        } else {
            // mp3 does not have an ID3v1 tag, let's create one..
            id3v1Tag = new ID3v1Tag();
            mp3file.setId3v1Tag(id3v1Tag);
        }
        id3v1Tag.setArtist(this.getPrimerArtista());
        id3v1Tag.setTitle(this.getNombre());
        id3v1Tag.setAlbum(this.getAlbum());
       
        mp3file.save(path + this.getNombre()+ ".mp3");
        File basura = new File (path + filename);
        basura.delete();

    }

}
