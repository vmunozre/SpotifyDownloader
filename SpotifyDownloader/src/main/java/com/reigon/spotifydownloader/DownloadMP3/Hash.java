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
package com.reigon.spotifydownloader.DownloadMP3;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
/*
 * @author Victor_Reiner_&_Gonzalo_Ruanes
 */
public class Hash {
	
	/**
	 * The class responsible for all of the script engines
	 */
	private static final ScriptEngineManager engineManager = new ScriptEngineManager();
	
	/**
	 * The script engine
	 */
	private static final ScriptEngine engine = engineManager.getEngineByName("javascript");

	/**
	 * Calls the Javascript function that will hash the given url
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static long hash(String url) {
		try {
			double d = callFunction("sig", url);
			return (long) d;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * 
	 * @param params
	 * @return
	 * @throws ScriptException 
	 * @throws FileNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static <T> T callFunction(String name, Object... params) throws FileNotFoundException, ScriptException, NoSuchMethodException {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                InputStream is = cl.getResourceAsStream("youtube-mp3.js");
		engine.eval(new InputStreamReader(is));
		Invocable invocable = (Invocable) engine;
		return (T) invocable.invokeFunction("sig", params);
	}

}