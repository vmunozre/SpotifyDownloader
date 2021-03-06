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
package com.reigon.spotifydownloader.DownloadMP3;

import java.io.File;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.reigon.spotifydownloader.Interface;
import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;

/*
 * @author Victor_Reiner_&_Gonzalo_Ruanes
 */
public class DownloadRequest implements Callable<File> {

	/**
	 * The logger for this class
	 */
	private static final Logger logger = LogManager.getLogger(DownloadRequest.class);

	/**
	 * Base URL for all the youtube-mp3 API calls
	 */
	public final String YOUTUBE_MP3 = "http://www.youtube-mp3.org";
	
	public static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";

	/**
	 * The Google JSON parser instance
	 */
	private static final Gson gson = new Gson();

	/**
	 * The ID of the video the user wants to download
	 */
	private final String videoId;

	private final String nombreCancion;

	private final String path;

	Interface textui;

	/**
	 * 
	 * @param videoId
	 * @param path
	 * @param nombreCancion
	 */
	public DownloadRequest(String videoId, String path, String nombreCancion, Interface t) {
		this.videoId = videoId;
		this.path = path;
		this.nombreCancion = nombreCancion;
		this.textui = t;
	}

	@Override
	public File call() throws Exception {
		logger.info("looking up video with id: {}", videoId);

		/*
		 * First we have to create the lookup request consisting of the video id, the
		 * current timestamp, a random hash generated by the Youtube-MP3 Javascript and
		 * some filler variables
		 */
		String url = this.YOUTUBE_URL + this.videoId;
		System.out.println("Trying to download video with url: " + url);
		
		YoutubeDLRequest request = new YoutubeDLRequest(url, path);
		request.setOption("ignore-errors");		// --ignore-errors
		request.setOption("extract-audio");
		request.setOption("audio-format","mp3");
		request.setOption("output", "%(id)s.%(ext)s");	// --output "%(id)s"
		request.setOption("retries", 10);		// --retries 10

		// Make request and return response
		YoutubeDLResponse response = YoutubeDL.execute(request);
		
		textui.printText("Descargada: " + this.nombreCancion);
		
		File song = new File (this.path + this.videoId + ".mp3");
		System.out.println(song.exists());
		File newname = new File(this.path + this.nombreCancion);
		Files.move(song,newname);
		return newname;
		
		

		// Response
		//String stdOut = response.getOut(); // Executable output
        //System.out.println("Downloaded: " + stdOut);
	}

}