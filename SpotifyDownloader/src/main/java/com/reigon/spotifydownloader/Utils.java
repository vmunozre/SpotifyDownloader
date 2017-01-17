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

import com.google.api.client.util.Charsets;
import com.google.common.io.CharStreams;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/*
 * @author Victor_Reiner_&_Gonzalo_Ruanes
 */
public class Utils {
    private static Object file;
    public static String cleanString(String in){
        return in.replace("|", "").replace("/", "").replace(":", "").replace("*", "").replace("?", "").replace("<", "").replace(">", "");
    }
    
    public static String readfile(String filepath) throws IOException{
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(filepath);
        String result = CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));
        return result;
    }
}
