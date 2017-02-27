/*
 * Copyright (C) 2017 Pankaj Prakash
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.codeforwin.test;

import com.codeforwin.id3.Frame;
import com.codeforwin.id3.ID3Metadata;
import com.codeforwin.id3.ImageFrame;
import com.codeforwin.id3.MediaMetadata;
import com.codeforwin.id3.TextFrame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Pankaj Prakash
 */
public class Test {
    
    public static void main(String[] args) {
        
        MediaMetadata id3;
        
        try {
        	id3 = new MediaMetadata(new File("src\\com\\codeforwin\\test\\data\\2.mp3"));
        } catch (IOException e) {
        	System.out.println(e);
        	return;
        }
        
        System.out.println("Album name : "+id3.getAlbumName());
        System.out.println("Album artist: " + id3.getAlbumArtists());
        System.out.println("Comments : " + id3.getComment());
        System.out.println("Commercial information : " + id3.getCommercialInformation());
        System.out.println("Composer : " + id3.getComposer());
        System.out.println("Genre : "+id3.getGenre());
        System.out.println("Copyright : "+id3.getCopyright());
        System.out.println("Copyright Information : " + id3.getCopyrightInformation());
        System.out.println("Encoded by : " + id3.getEncodedBy());
        ImageFrame frame = id3.getImageFrame();
        System.out.println("Image Type : " + id3.getImageFrame().getAlbumArtType());
        System.out.println("Internet Radio Station : " + id3.getInternetRadioStationName());
        System.out.println("Internet Radio Station Owner : " + id3.getInternetRadioStationOwner());
        System.out.println("Lead Performer : " + id3.getLeadPerformer());
        System.out.println("Length : " + id3.getLength());
        System.out.println("Lyricists : " + id3.getLyricist());
        System.out.println("Media Type : " + id3.getMediaType());
        System.out.println("Official Artists Webpage : " + id3.getOfficialArtistWebpage());
        System.out.println("Official Audio Source Webpage : " + id3.getOfficialAudioSourceWebpage());
        System.out.println("Official Audio Webpage : " + id3.getOfficialAudioWebpage());
        System.out.println("Official Internet Radio Station Homepage : " + id3.getOfficialInternetRadioStationHomepage());
        System.out.println("Official Publisher Webpage : " + id3.getOfficialPublisherWebpage());
        System.out.println("Original Artists : " + id3.getOriginalArtist());
        System.out.println("Original File Name : " + id3.getOriginalFileName());
        System.out.println("Original Lyrists : " + id3.getOriginalLyricist());
        System.out.println("Original Release Year : " + id3.getOriginalReleaseYear());
        System.out.println("Original Title : " + id3.getOriginalTitle());
        System.out.println("Popularity Meter : " + id3.getPopularity());
        System.out.println("Publisher : " + id3.getPublisher());
        System.out.println("Recording Date : " + id3.getRecordingDate());
        System.out.println("Recording Time : " + id3.getRecordingTime());
        System.out.println("Release Time : " + id3.getReleaseTime());
        System.out.println("Song conductor : " + id3.getSongConductor());
        System.out.println("Song name : " + id3.getSongName());
        System.out.println("Sub title : " + id3.getSubTitle());
        System.out.println("Track number : " + id3.getTrackNumber());
        System.out.println("UFID : " + id3.getUfid());
        System.out.println("Year : " + id3.getYear());
    }
    
    public static void main1(String[] args) throws IOException {
        //7,170,840 bytes
        int i=11;
        if(i==1){
            check();
            return;
        }
        ID3Metadata meta = ID3Metadata.parseMedia(new File("test\\data\\[Songs.PK] Bhaag Milkha Bhaag - 03 - Mera Yaar.mp3"));
        
        System.out.println(meta.getMajorVersion());
        System.out.println(meta.getMinorVersion());
        System.out.println(meta.getSize() + " bytes");
        
        for(Frame frame : meta.getAllFrames()) {
            if(frame.getFrameID().equals("APIC"))
                System.out.println(frame.getFrameID() + " -- " + frame.getSize());
            else
                System.out.println(frame.getFrameID() + " -- " + frame.getSize() + " -- " +((TextFrame)frame).getTextData());
        }
        
        meta.pack();
    }
    
    public static void check() {
        try (FileInputStream reader = new FileInputStream("D:\\Music\\Bhaag Milkha Bhaag\\[Songs.PK] Bhaag Milkha Bhaag - 03 - Mera Yaar.mp3");
                FileInputStream stream = new FileInputStream("test\\data\\[Songs.PK] Bhaag Milkha Bhaag - 03 - Mera Yaar.mp3")) {

            byte data1[] = new byte[30];
            byte data2[] = new byte[30];
            
            reader.read(data1, 0, data1.length);
            stream.read(data2, 0, data2.length);
            
            for(int i=0; i<data1.length; i++) {
                System.out.println(data1[i] + "  " + data2[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
	private static void saveAlbumImage(BufferedImage image) {
        String fileName = "test\\data\\albumImage.jpg";
        File file = new File(fileName);
        try {
            RenderedImage img = image;
            boolean written = ImageIO.write(img, "jpg", file);  // ignore returned boolean

            System.out.println("SUCCESS " + written);
        } catch (IOException e) {
            System.out.println("Write error for " + file.getPath()
                    + ": " + e.getMessage());
        }
    }


    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
}
