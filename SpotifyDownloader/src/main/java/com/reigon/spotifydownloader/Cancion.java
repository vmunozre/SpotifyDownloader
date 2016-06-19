
/*
    Licencia:
    «Copyright 2016 ReiGon - Victor Reiner & Gonzalo Ruanes»

    This file is part of YouDownloadify.

    YouDownloadify is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    YouDownloadify is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.reigon.spotifydownloader;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * @author Victor_Reiner_&_Gonzalo_Ruanes
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

    //CONSTRUCTOR
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

    //FUNCIONES GENERALES
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
        ID3v2 id3v2Tag;
        if (mp3file.hasId3v2Tag()) {
          id3v2Tag = mp3file.getId3v2Tag();
        } else {
          // mp3 does not have an ID3v2 tag, let's create one..
          id3v2Tag = new ID3v24Tag();
          mp3file.setId3v2Tag(id3v2Tag);
        }
        
        if(this.getPrimerArtista().length() > 28){
            id3v2Tag.setArtist(this.getPrimerArtista().substring(0, 28));
        }else{
            id3v2Tag.setArtist(this.getPrimerArtista());
        }
        
        if(this.getNombre().length() > 28){
            id3v2Tag.setTitle(this.getNombre().substring(0, 28));
        }else{
            id3v2Tag.setTitle(this.getNombre());
        }
        
        if(this.getAlbum().length() > 28){
            id3v2Tag.setAlbum(this.getAlbum().substring(0, 28));
        }else{
            id3v2Tag.setAlbum(this.getAlbum());
        }
        

        mp3file.save(path + Utils.cleanString(this.getNombre()) + ".mp3");
        File basura = new File(path + filename);
        basura.delete();
            

    }

    //GETTERS AND SETTERS, ETC
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

}
