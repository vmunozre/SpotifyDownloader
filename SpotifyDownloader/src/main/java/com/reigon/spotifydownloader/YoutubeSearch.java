/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reigon.spotifydownloader;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Reiner
 */
public class YoutubeSearch {

    List<Cancion> listaCanciones;
    /**
     * Global instance of the HTTP transport.
     */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /**
     * Global instance of the max number of videos we want returned (50 = upper
     * limit per page).
     */
    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

    /**
     * Global instance of Youtube object to make all API requests.
     */
    private static YouTube youtube;
    TextUI textui;

    public YoutubeSearch(TextUI t) {
        listaCanciones = new ArrayList<>();
        textui = t;
    }

    public List<Cancion> process(List<Cancion> canciones) {

        for (Cancion track : canciones) {
            try {
                /*
                 * The YouTube object is used to make all API requests. The last argument is required, but
                 * because we don't need anything initialized when the HttpRequest is initialized, we override
                 * the interface and provide a no-op function.
                 */
                youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                    public void initialize(HttpRequest request) throws IOException {
                    }
                }).setApplicationName("youtube-search-pruebas-spotify").build();

                // Get query term from user.
                String queryTerm = track.getQuery();

                YouTube.Search.List search = youtube.search().list("id,snippet");
                //No deberiamos poner el api key a pelo
                String apiKey = "AIzaSyCkQfDbMO7A7Dtq062uB6xtRRmWypDIrAw";
                search.setKey(apiKey);
                search.setQ(queryTerm);

                //Busqueda de solo de videos
                search.setType("video");

                search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
                search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
                SearchListResponse searchResponse = search.execute();

                List<SearchResult> searchResultList = searchResponse.getItems();

                if (!searchResultList.isEmpty()) {
                    //Aqui iria el codigo de una segunda búsqueda mas abierta
                   
                    /*List<SearchResult> filtered = searchResultList.stream()
                                                    .filter(vi -> vi.getSnippet().getTitle().contains("lyrics"))
                                                    .collect(Collectors.toList());*/
                   
                    
                    track.setUrl("https://www.youtube.com/watch?v=" + searchResultList.get(0).getId().getVideoId());
                    track.setVideoID(searchResultList.get(0).getId().getVideoId());    
                } else {
                    System.out.println("La pista " + track.getQuery() + " no se ha encontrado, buscando de forma mas general");
                    textui.printText("La pista " + track.getQuery() + " no se ha encontrado, buscando de forma mas general");
                    queryTerm = track.getEasyQuery();
                    search.setQ(queryTerm);
                    searchResponse = search.execute();
                    searchResultList = searchResponse.getItems();
                    
                    if (!searchResultList.isEmpty()) {
                        //Aqui iria el codigo de una segunda búsqueda mas abierta
                       
                        track.setUrl("https://www.youtube.com/watch?v=" + searchResultList.get(0).getId().getVideoId());
                        track.setVideoID(searchResultList.get(0).getId().getVideoId());
                        System.out.println("Encontrado! "+ track.getEasyQuery() +" Comprueba que es la cancion que buscabas!");
                        textui.printText("Encontrado! "+ track.getEasyQuery() +" Comprueba que es la cancion que buscabas!");
                    } else {
                        System.out.println("La pista " + track.getQuery() + " no se ha encontrado");
                        textui.printText("La pista " + track.getQuery() + " no se ha encontrado");
                    }
                }
            } catch (GoogleJsonResponseException e) {
                System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                        + e.getDetails().getMessage());
            } catch (IOException e) {
                System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
            } catch (Throwable t) {
                t.printStackTrace();
            }

        }

        return canciones;
    }
}
