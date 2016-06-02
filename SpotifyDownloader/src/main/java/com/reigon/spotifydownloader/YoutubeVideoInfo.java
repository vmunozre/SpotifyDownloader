/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reigon.spotifydownloader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import static com.reigon.spotifydownloader.DownloadMP3.DownloadRequest.YOUTUBE_MP3;
import com.reigon.spotifydownloader.DownloadMP3.URLBuilder;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Reiner
 */
public class YoutubeVideoInfo {

    public static final String YOUTUBE_MP3 = "https://www.googleapis.com";

    /**
     * The Google JSON parser instance
     */
    private static final Gson gson = new Gson();

    public YoutubeVideoInfo() {

    }

    public int getLongitudVideo(String videoId, String apiKey) {
        //https://www.googleapis.com/youtube/v3/videos?id=5hzgS9s-tE8&key=YOUR_API_KEY&part=snippet,contentDetails,statistics,status
        URLBuilder lookup_builder = new URLBuilder("/youtube/v3/videos").addParameter("id", videoId).addParameter("key", apiKey).addParameter("part", "contentDetails");

        /*
		 * Send the request and read the JSON file
         */
        StringBuilder json_builder = new StringBuilder();
        try (Scanner scanner = new Scanner(lookup_builder.build(YOUTUBE_MP3).openStream())) {
            while (scanner.hasNext()) {
                json_builder.append(scanner.nextLine());
            }
        } catch (Exception ex) {
            System.out.println("Error al sacar Longitud de youtube");
        }

        /*
		 * Parse the JSON to a hashmap
         */
        Map<String, Object> response = gson.fromJson(json_builder.toString(), new TypeToken<Map<String, Object>>() {
        }.getType());
        ArrayList aux = (ArrayList) response.get("items");
        Map<String, Object> aux2 = (Map) aux.get(0);
        Map<String, Object> aux3 = (Map) aux2.get("contentDetails");
        //System.out.println(this.getDuration(aux3.get("duration").toString()));

        return (int) getDuration(aux3.get("duration").toString());
    }

    public long getDuration(String t) {
        String time = t.substring(2);
        long duration = 0L;
        Object[][] indexs = new Object[][]{{"H", 3600}, {"M", 60}, {"S", 1}};
        for (int i = 0; i < indexs.length; i++) {
            int index = time.indexOf((String) indexs[i][0]);
            if (index != -1) {
                String value = time.substring(0, index);
                duration += Integer.parseInt(value) * (int) indexs[i][1] * 1000;
                time = time.substring(value.length() + 1);
            }
        }
        return duration / 1000;
    }
}