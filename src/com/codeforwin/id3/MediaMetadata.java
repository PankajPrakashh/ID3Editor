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
package com.codeforwin.id3;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 *
 * @author Pankaj Prakash
 */
public class MediaMetadata {
    
    /// <editor-fold defaultstate="collapsed" desc="Global variable declarations">
    
    /**
     * <b>APIC</b> tag - Attached picture with the media. 
     */
    private FrameAPIC imageFrame;
    
    /**
     * <b>COMM</b> tag - Comments associated with the media.
     */
    private String comment;
    
    /**
     * <b>POPM</b> tag - The popularity meter frame.
     */
    private short popularity;
    
    /**
     * <b>TALB</b> tag - Album name.
     */
    private String albumName;
    
    /**
     * <b>TCOM</b> tag - Composer of the media.
     */
    private String composer;
    
    /**
     * <b>TCON</b> tag - Content type of the media.
     */
    private String genre;
    
    /**
     * <b>TCOP</b> tag - Copyright message of the media.
     */
    private String copyright;
    
    /**
     * <b>TDRC</b> tag - Date and time of the recording.
     */
    private Date recordingTime;
    
    /**
     * <b>TDRL</b> tag - Date and time of the release.
     */
    private Date releaseTime;
    
    /**
     * <b>TENC</b> tag - Media data encoded by.
     */
    private String encodedBy;
    
    /**
     * <b>TEXT</b> tag - Lyricists/writer of the media.
     */
    private String lyricist;
    
    /**
     * <b>TIT2</b> tag - Name of the song or content description.
     */
    private String songName;
    
    /**
     * <b>TIT3</b> tag - Sub albumName of the song.
     */
    private String subTitle;
    
    /**
     * <b>TLEN</b> tag - Length of the media in minutes.
     */
    private int length;
    
    /**
     * <b>TMED</b> tag - Type of the media.
     */
    private String mediaType;
    
    /**
     * <b>TOAL</b> tag - Original albumName of the media.
     */
    private String originalTitle;
    
    /**
     * <b>TOFN</b> tag - Original name of the file.
     */
    private String originalFileName;
    
    /**
     * <b>TOLY</b> tag - Original name of the lyricists.
     */
    private String originalLyricist;
    
    /**
     * <b>TOPE</b> tag - Original name of the media artist.
     */
    private String originalArtist;
    
    /**
     * <b>TDOR</b> tag - Original release year of the media.
     */
    private int originalReleaseYear;
    
    /**
     * <b>TPE1</b> tag - Lead performers/Soloists of the song.
     */
    private String leadPerformer;
    
    /**
     * <b>TPE2</b> tag - Band/Orchestra of the song.
     */
    private String albumArtists;
    
    /**
     * <b>TPE3</b> tag - Conductor of the media song.
     */
    private String songConductor;
    
    /**
     * <b>TPUB</b> tag - Publisher of the song.
     */
    private String publisher;
    
    /**
     * <b>TRCK</b> tag - Track number of current song in album.
     */
    private int trackNumber;
    
    /**
     * <b>TDRC</b> tag - Recording date of the song.
     */
    private Date recordingDate;
    
    /**
     * <b>TRSN</b> tag - Internet radio station name of the media song.
     */
    private String internetRadioStationName;
    
    /**
     * <b>TRSO</b> tag - Internet radio station owner name of the media song.
     */
    private String internetRadioStationOwner;
    
    /**
     * <b>TYER</b> tag - Year of the media song.
     */
    private int year;
    
    /**
     * <b>UFID</b> tag - Unique file identifier of the media file.
     */
    private String ufid;
    
    /**
     * <b>WCOM</b> tag - Commercial information of the media song.
     */
    private String commercialInformation;
    
    /**
     * <b>WCOP</b> tag - Additional copyright information of media song.
     */
    private String copyrightInformation;
    
    /**
     * <b>WOAF</b> tag - Official webpage of the audio file.
     */
    private String officialAudioWebpage;
    
    /**
     * <b>WOAR</b> tag - Official webpage of the artist/performer.
     */
    private String officialArtistWebpage;
    
    /**
     * <b>WOAS</b> tag - Official webpage of the audio source.
     */
    private String officialAudioSourceWebpage;
    
    /**
     * <b>WORS</b> tag - Official homepage of the internet radio station homepage.
     */
    private String officialInternetRadioStationHomepage;
    
    /**
     * <b>WPUB</b> tag - Official web page of the media song publisher.
     */
    private String officialPublisherWebpage;
    
    
    /// </editor-fold>
    
    
    private File musicFile  = null;
    private ID3Metadata id3 = null;
    
    
    
    public MediaMetadata(File musicFile) {
        this.musicFile = musicFile;
        
        id3 = ID3Metadata.parseMedia(musicFile);
        if(id3 == null) 
            id3 = new ID3Metadata(musicFile);
        
        getAllMetadata();
    }
    
    
    
    /**
     * If the current media file contains mp3 metadata the get all frame data.
     * If the media file does not contains mp3 metadata then it creates a 
     * ID3 frame and initializes all metadata fields to null.
     */
    private void getAllMetadata() {
        if (id3 == null) {
            id3 = new ID3Metadata(musicFile);
        }

        Frame frames[] = id3.getAllFrames();

        // If the media contains metadata
        if (frames.length < 1) {
            return;
        }

        // Get all frame details
        for (Frame frame : frames) {
            switch (frame.getFrameID()) {
                case "APIC":
                    imageFrame = frame.getImageFrame();
                    break;

                case "COMM":
                    /**
                     * Text encoding           $xx
                     * Language                $xx xx xx
                     * Short content descrip.  text string according to encoding $00 (00)
                     * The actual text         full text string according to encoding
                     */
                    byte[] data = frame.getBytes();
                    
                    String language = "";
                    try {
                        language = new String(data, 1, 3, frame.encoding);
                        
                        String comm = new String(data, 4, data.length - 4, frame.encoding);
                        
                        if(comment == null)
                            comment = comm;
                        else
                            comment += "\\\\" + comm.trim();
                        
                    } catch(UnsupportedEncodingException e) {
                        System.out.println("com.codeforwin.id3.MediaMetadata.getAllMetadata()");
                    }
                    
                    break;

                case "POPM":
                    
                    break;

                case "TALB":
                    albumName = frame.getString();
                    break;

                case "TCOM":
                    composer = frame.getString();
                    break;

                case "TCON":
                    genre = frame.getString();
                    
                    if(genre.matches("\0\\(\\d\\)")) {
                        try {
                            genre = genre.replaceAll("[\0\\(\\)]", "");
                            int index = Integer.parseInt(genre);

                            genre = GenreList.GENRES[index];
                        } catch (Exception e) {
                            System.out.println("com.codeforwin.id3.MediaMetadata.getAllMetadata()");
                        }
                    }
                    break;

                case "TCOP":
                    copyright = frame.getString();
                    break;

                case "TYER":
                    try {
                        year = Integer.parseInt(frame.getString());
                    } catch (NumberFormatException e) {
                        year = Integer.parseInt(frame.getString().replace("\0", ""));
                    }
                    break;

                case "TDRL":
                    releaseTime = null;
                    break;

                case "TENC": //Media data encoded by.
                    encodedBy = frame.getString();
                    break;

                case "TEXT": //Lyricists/writer of the media.
                    lyricist = frame.getString();
                    break;

                case "TIT2": //Name of the song or content description.
                    songName = frame.getString();
                    break;

                case "TIT3": //Sub albumName of the song.
                    subTitle = frame.getString();
                    break;

                case "TLEN": //Length of the media in minutes.
                    System.out.println("com.codeforwin.id3.MediaMetadata.getAllMetadata()");
                    length = Integer.parseInt(frame.getString());
                    break;

                case "TMED": //Type of the media.
                    mediaType = frame.getString();
                    break;

                case "TOAL": //Original albumName of the media.
                    originalTitle = frame.getString();
                    break;

                case "TOFN": //Original name of the file.
                    originalFileName = frame.getString();
                    break;

                case "TOLY": //Original name of the lyricists.
                    originalLyricist = frame.getString();
                    break;

                case "TOPE": //Original name of the media artist.
                    originalArtist = frame.getString();
                    break;

                case "TDOR": //Original release year of the media.
                    originalReleaseYear = frame.getInt();
                    break;

                case "TPE1": //Lead performers/Soloists of the song.
                    leadPerformer = frame.getString();
                    break;

                case "TPE2": //Band/Orchestra of the song.
                    albumArtists = frame.getString();
                    break;

                case "TPE3": //Conductor of the media song.
                    songConductor = frame.getString();
                    break;

                case "TPUB": //Publisher of the song.
                    publisher = frame.getString();
                    break;

                case "TRCK": //Track number of current song in album.
                    try {
                        trackNumber = Integer.parseInt(frame.getString());
                    } catch (NumberFormatException e) {
                        String dataString = frame.getString();
                        if(dataString.contains("/"))
                            dataString = dataString.substring(0, dataString.indexOf("/"));
                        
                        dataString = dataString.replace("\0", "");
                        
                        trackNumber = Integer.parseInt(dataString);
                    }
                    break;

                case "TRSN": //Internet radio station name of the media song.
                    internetRadioStationName = frame.getString();
                    break;

                case "TRSO": //Internet radio station owner name of the media song.
                    internetRadioStationOwner = frame.getString();
                    break;

                case "UFID": //Unique file identifier of the media file.
                    ufid = frame.getString();
                    break;

                case "WCOM": //Commercial information of the media song.
                    commercialInformation = frame.getString();
                    break;

                case "WCOP": //Additional copyright information of media song.
                    copyrightInformation = frame.getString();
                    break;

                case "WOAF": //Official webpage of the audio file.
                    officialAudioWebpage = frame.getString();
                    break;

                case "WOAR": //Official webpage of the artist/performer.
                    officialArtistWebpage = frame.getString();
                    break;

                case "WOAS": //Official webpage of the audio source.
                    officialAudioSourceWebpage = frame.getString();
                    break;

                case "WORS": //Official homepage of the internet radio station homepage.
                    officialInternetRadioStationHomepage = frame.getString();
                    break;

                case "WPUB": //Official web page of the media song publisher.
                    officialPublisherWebpage = frame.getString();
                    break;
                    
                default:
                    System.out.println(frame.getFrameID());
                    break;
            } // End of switch
        } // End of for
    }
    
    
    /// <editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public FrameAPIC getImageFrame() {
        return imageFrame;
    }

    public String getComment() {
        return comment;
    }

    public String getAlbumArtists() {
        return albumArtists;
    }

    public String getCommercialInformation() {
        return commercialInformation;
    }

    public String getComposer() {
        return composer;
    }

    public String getGenre() {
        return genre;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getCopyrightInformation() {
        return copyrightInformation;
    }

    public String getEncodedBy() {
        return encodedBy;
    }

    public String getInternetRadioStationName() {
        return internetRadioStationName;
    }

    public String getInternetRadioStationOwner() {
        return internetRadioStationOwner;
    }

    public String getLeadPerformer() {
        return leadPerformer;
    }

    public int getLength() {
        return length;
    }

    public String getLyricist() {
        return lyricist;
    }

    public String getMediaType() {
        return mediaType;
    }

    public File getMusicFile() {
        return musicFile;
    }

    public String getOfficialArtistWebpage() {
        return officialArtistWebpage;
    }

    public String getOfficialAudioSourceWebpage() {
        return officialAudioSourceWebpage;
    }

    public String getOfficialAudioWebpage() {
        return officialAudioWebpage;
    }

    public String getOfficialInternetRadioStationHomepage() {
        return officialInternetRadioStationHomepage;
    }

    public String getOfficialPublisherWebpage() {
        return officialPublisherWebpage;
    }

    public String getOriginalArtist() {
        return originalArtist;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getOriginalLyricist() {
        return originalLyricist;
    }

    public int getOriginalReleaseYear() {
        return originalReleaseYear;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public short getPopularity() {
        return popularity;
    }

    public String getPublisher() {
        return publisher;
    }

    public Date getRecordingDate() {
        return recordingDate;
    }

    public Date getRecordingTime() {
        return recordingTime;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public String getSongConductor() {
        return songConductor;
    }

    public String getSongName() {
        return songName;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getAlbumName() {
        return albumName;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public String getUfid() {
        return ufid;
    }

    public int getYear() {
        return year;
    }
    
    /// </editor-fold>
}
