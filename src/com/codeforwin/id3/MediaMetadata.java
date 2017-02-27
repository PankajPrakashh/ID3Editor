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
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author Pankaj Prakash
 * @version 0.9
 */
public class MediaMetadata {
    
    /**
     * <b>APIC</b> tag - Attached picture with the media. 
     */
    private ImageFrame imageFrame;
    
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
    
    
    private File musicFile  = null;
    private ID3Metadata id3 = null;
    
    
    public MediaMetadata(File musicFile) throws IOException {
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

        Frame frames[] = null;
        
        try {
        	frames = id3.getAllFrames();
        } catch (IOException e) {
        	return;
        }

        // If the media contains metadata
        if (frames.length < 1) {
            return;
        }

        // Get all frame details
        for (Frame frame : frames) {
            switch (frame.getFrameID()) {
                case "APIC":
                    imageFrame = (ImageFrame)frame;
                    break;

                case "COMM":
    				CommentFrame commFrame = (CommentFrame) frame;

    				if (comment == null)
    					comment = commFrame.getComment();
    				else
    					comment += "\\\\" + commFrame.getComment();
                    break;

                case "POPM":
                    
                    break;

                case "TALB":
                    albumName = ((TextFrame)frame).getTextData();
                    break;

                case "TCOM":
                    composer = ((TextFrame)frame).getTextData();
                    break;

                case "TCON":
                    genre = ((TextFrame)frame).getTextData();
                    
                    if(genre.matches("\0\\(\\d\\)")) {
                        try {
                            genre = genre.replaceAll("[\0\\(\\)]", "");
                            int index = Integer.parseInt(genre);

                            genre = ID3.GENRES[index];
                        } catch (Exception e) {
                            System.out.println("com.codeforwin.id3.MediaMetadata.getAllMetadata()");
                        }
                    }
                    break;

                case "TCOP":
                    copyright = ((TextFrame)frame).getTextData();
                    break;

                case "TYER":
                	String yr = ((TextFrame)frame).getTextData();
                    try {
                        year = Integer.parseInt(yr);
                    } catch (NumberFormatException e) {
                        year = Integer.parseInt(yr.replace("\0", ""));
                    }
                    break;

                case "TDRL":
                    releaseTime = null;
                    break;

                case "TENC": //Media data encoded by.
                    encodedBy = ((TextFrame)frame).getTextData();
                    break;

                case "TEXT": //Lyricists/writer of the media.
                    lyricist = ((TextFrame)frame).getTextData();
                    break;

                case "TIT2": //Name of the song or content description.
                    songName = ((TextFrame)frame).getTextData();
                    break;

                case "TIT3": //Sub albumName of the song.
                    subTitle = ((TextFrame)frame).getTextData();
                    break;

                case "TLEN": //Length of the media in minutes.
                	String len = ((TextFrame)frame).getTextData();
                    try {
                        length = Integer.parseInt(len);
                    } catch (NumberFormatException e) {
                        length = Integer.parseInt(len.replace("\0", ""));
                    }
                    break;

                case "TMED": //Type of the media.
                    mediaType = ((TextFrame)frame).getTextData();
                    break;

                case "TOAL": //Original albumName of the media.
                    originalTitle = ((TextFrame)frame).getTextData();
                    break;

                case "TOFN": //Original name of the file.
                    originalFileName = ((TextFrame)frame).getTextData();
                    break;

                case "TOLY": //Original name of the lyricists.
                    originalLyricist = ((TextFrame)frame).getTextData();
                    break;

                case "TOPE": //Original name of the media artist.
                    originalArtist = ((TextFrame)frame).getTextData();
                    break;

                case "TDOR": //Original release year of the media.
                	String ory = ((TextFrame)frame).getTextData();
                    try {
                        originalReleaseYear = Integer.parseInt(ory);
                    } catch (NumberFormatException e) {
                        originalReleaseYear = Integer.parseInt(ory.replace("\0", ""));
                    }
                    break;

                case "TPE1": //Lead performers/Soloists of the song.
                    leadPerformer = ((TextFrame)frame).getTextData();
                    break;

                case "TPE2": //Band/Orchestra of the song.
                    albumArtists = ((TextFrame)frame).getTextData();
                    break;

                case "TPE3": //Conductor of the media song.
                    songConductor = ((TextFrame)frame).getTextData();
                    break;

                case "TPUB": //Publisher of the song.
                    publisher = ((TextFrame)frame).getTextData();
                    break;

                case "TRCK": //Track number of current song in album.
                	String trck = ((TextFrame)frame).getTextData();
                    try {
                        trackNumber = Integer.parseInt(trck);
                    } catch (NumberFormatException e) {
                        if(trck.contains("/"))
                            trck = trck.substring(0, trck.indexOf("/"));
                        
                        trck = trck.replace("\0", "");
                        
                        trackNumber = Integer.parseInt(trck);
                    }
                    break;

                case "TRSN": //Internet radio station name of the media song.
                    internetRadioStationName = ((TextFrame)frame).getTextData();
                    break;

                case "TRSO": //Internet radio station owner name of the media song.
                    internetRadioStationOwner = ((TextFrame)frame).getTextData();
                    break;

                case "UFID": //Unique file identifier of the media file.
                    ufid = ((TextFrame)frame).getTextData();
                    break;

                case "WCOM": //Commercial information of the media song.
                    commercialInformation = ((TextFrame)frame).getTextData();
                    break;

                case "WCOP": //Additional copyright information of media song.
                    copyrightInformation = ((TextFrame)frame).getTextData();
                    break;

                case "WOAF": //Official webpage of the audio file.
                    officialAudioWebpage = ((TextFrame)frame).getTextData();
                    break;

                case "WOAR": //Official webpage of the artist/performer.
                    officialArtistWebpage = ((TextFrame)frame).getTextData();
                    break;

                case "WOAS": //Official webpage of the audio source.
                    officialAudioSourceWebpage = ((TextFrame)frame).getTextData();
                    break;

                case "WORS": //Official homepage of the internet radio station homepage.
                    officialInternetRadioStationHomepage = ((TextFrame)frame).getTextData();
                    break;

                case "WPUB": //Official web page of the media song publisher.
                    officialPublisherWebpage = ((TextFrame)frame).getTextData();
                    break;
                    
                default:
                    System.out.println(frame.getFrameID());
                    break;
            } // End of switch
        } // End of for
    }


    public ImageFrame getImageFrame() {
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
}
