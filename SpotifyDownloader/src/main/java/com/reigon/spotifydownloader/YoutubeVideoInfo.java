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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reigon.spotifydownloader.DownloadMP3.URLBuilder;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/*
 * @author Victor_Reiner_&_Gonzalo_Ruanes
 */
public class YoutubeVideoInfo {

    public static final String YOUTUBE_MP3 = "https://www.googleapis.com";

    /**
     * The Google JSON parser instance
     */
    private static final Gson gson = new Gson();

    public YoutubeVideoInfo() {

    }
    //Sacamos la duración de un video
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
        

        return (int) getDuration(aux3.get("duration").toString());
    }
    //Parseamos la duracion a segundos
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
