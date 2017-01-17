/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reigon.spotifydownloader;

import java.io.File;
import java.io.IOException;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class Home {

    @RequestMapping(value = "/")
    @ResponseBody
    String getHome() throws IOException {
        
        String strReturn = "<html><head></head><body><h1>QUE TE JODAN CLAUDIA</h1></body></html>";
        return strReturn;
    }


}
