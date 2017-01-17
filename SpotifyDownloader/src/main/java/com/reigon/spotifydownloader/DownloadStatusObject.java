/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reigon.spotifydownloader;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Gonzalo
 */
public class DownloadStatusObject {
    private String stageStatus;
    private ArrayList<String> messageBuffer;
    private Semaphore sem;
    
  
    
    public DownloadStatusObject(){
        this.stageStatus = "Spotify";
        this.messageBuffer = new ArrayList();
        sem = new Semaphore(1);
    }
    
    public ArrayList<String> retrievebuffer() throws InterruptedException{
        
        sem.acquire();
        ArrayList<String> aux = (ArrayList<String>) this.messageBuffer.clone();
        this.messageBuffer.clear();
        sem.release();
        return aux;
    }
    
    public void addmessage(String msg) throws InterruptedException{
        sem.acquire();
        this.messageBuffer.add(msg);
        sem.release();
    }
    
    public String getStageStatus() {
        return stageStatus;
    }

    public void setStageStatus(String stageStatus) {
        this.stageStatus = stageStatus;
    }
    
    public void clearbuffer() throws InterruptedException{
        sem.acquire();
        this.messageBuffer.clear();
        sem.release();
    }

}
