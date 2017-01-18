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
    private Semaphore sem2;
    private Semaphore sem3;
    private int totalsongs;
    private int downloaded;
    private ArrayList<String> currentDownloads;
    
  
    
    public DownloadStatusObject(){
        this.stageStatus = "Spotify";
        this.messageBuffer = new ArrayList();
        sem = new Semaphore(1);
        sem2 = new Semaphore(1);
        sem3 = new Semaphore(1);
        this.currentDownloads = new ArrayList();
        
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

    public void addcurrentdownload(String id) throws InterruptedException{
        sem2.acquire();
        this.currentDownloads.add(id);
        sem2.release();
    }
    
    public void removecurrentdownload(String id) throws InterruptedException{
        sem2.acquire();
        this.currentDownloads.remove(id);
        sem2.release();
    }
    
    public int getTotalsongs() {
        return totalsongs;
    }

    public void setTotalsongs(int totalsongs) {
        this.totalsongs = totalsongs;
    }

    public int getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(int downloaded) {
        this.downloaded = downloaded;
    }

    public ArrayList<String> getCurrentDownloadsCopy() throws InterruptedException {
        sem2.acquire();
        ArrayList<String> aux = (ArrayList<String>) currentDownloads.clone();
        sem2.release();
        return aux;
    }

    
    
    
}
