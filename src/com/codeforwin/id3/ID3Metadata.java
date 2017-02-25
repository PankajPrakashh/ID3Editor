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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;


/**
 * <code>ID3Metadata</code> class represents a valid ID3 media meta-data format. 
 * 
 * @author Pankaj Prakash
 * @version 1.0
 */
public class ID3Metadata {
    public final static String ENCODING_ASCII       = "ISO-8859-1";
    public final static String ENCODING_UTF8        = "UTF-8";
    public final static String ENCODING_UTF16       = "UTF-16";
    public final static String ENCODING_UTF16BE     = "UTF-16BE";
    public final static String ENCODING_UTF16LE     = "UTF-16LE";
    
    public final static int    FLAG_UNSYNCRONIZATION= 0x80; // 10000000
    public final static int    FLAG_EXTENDED_HEADER = 0x40; // 01000000
    public final static int    FLAG_EXPERIMENTAL    = 0x20; // 00100000
    
    public final static String ID3_IDENTIFIER       = "ID3";
    
    /**
     * Media file whose meta-data is to be fetched.
     */
    private final File file;
    
    /**
     * Major version of the ID3 tag.
     */
    private int majorVersion;
    /**
     * Minor version of the ID3 tag.
     */
    private int minorVersion;
    /**
     * Size of entire ID3 metadata.
     */
    private int size;
    /**
     * ID3 flags.
     */
    private int flag;
    
    /**
     * True if un-synchronization flag is set.
     */
    private boolean unsynchronizationSet;
    /**
     * True if the tag is followed by extended header tag.
     */
    private boolean extendedHeaderAdded;
    /**
     * True if the current tag is experimental tag.
     */
    private boolean experimentalTag;
    
    /**
     * True if the file contains ID3 tag.
     */
    private boolean validID3Tag;
    
    /**
     * List of all frames in the current ID3 tag.
     */
    private ArrayList<Frame> frames;
    
    
    /**
     * Initializes a new instance of <code>ID3Metadata</code> with default values.
     * 
     * @param file Media File to which the current ID3Metadata is associated with.
     */
    public ID3Metadata(File file) {
        this.file                   = file;
        this.validID3Tag            = false;
        
        this.unsynchronizationSet   = false;
        this.experimentalTag        = false;
        this.extendedHeaderAdded    = false;
        this.flag                   = 0;
        this.majorVersion           = 3;
        this.minorVersion           = 0;
        this.size                   = 0;
        
        this.frames                 = new ArrayList<>();
    }
    
    /**
     * Use <code>parseMedia()</code> method to get an instance of ID3Metadata. 
     * It parses the media file for a valid ID3 tag. If the media file contains
     * a valid ID3 tag then the function <code>isValidID3Tag()</code> returns 
     * true.
     * <br>
     * This function reads the first 10 bytes of the file. If the media file
     * contains valid ID3 tag then, it initializes a new instance of ID3Metadata
     * with the header information stored in the first 10 bytes of the ID3 header.
     * 
     * @param file File which is to be parsed for valid ID3 meta-data.
     * @return An instance of <code>ID3Metadata</code> if the file contains valid
     * ID3 tag otherwise returns null.
     * @see isValidID3Tag()
     */
    public static ID3Metadata parseMedia(File file) {
        ID3Metadata id3 = null;

        byte headerInfo[] = new byte[10];

        String identifier = "";

        try (FileInputStream stream = new FileInputStream(file)) {
            // Read the first 10 header info from the media file
            stream.read(headerInfo, 0, headerInfo.length);

            // Get the first three bytes
            identifier = new String(headerInfo, 0, 3, ENCODING_ASCII);
        } catch (IOException ex) {
            System.out.println("com.codeforwin.id3.ID3Metadata.parseMedia() - " + ex.getMessage());
            return null;
        }
        
        /**
         * If current tag is valid ID3 tag read its header information.
         */
        if (identifier.equals(ID3_IDENTIFIER)) {
            id3 = new ID3Metadata(file);
            
            id3.validID3Tag = true;
            
            // 4th byte contains the major version information
            id3.majorVersion = headerInfo[3];

            // 5th byte contains the minor version information
            id3.minorVersion = headerInfo[4];

            // 6th byte contains the flag informations
            int flag = headerInfo[5];
            id3.flag = headerInfo[5];

            // Set various flag informations
            id3.unsynchronizationSet= ((flag & FLAG_UNSYNCRONIZATION) == FLAG_UNSYNCRONIZATION);
            id3.extendedHeaderAdded = ((flag & FLAG_EXTENDED_HEADER ) == FLAG_EXTENDED_HEADER);
            id3.experimentalTag     = ((flag & FLAG_EXPERIMENTAL    ) == FLAG_EXPERIMENTAL);

            /**
             * Reads the size of the id3 tag Size is defined by 4 bytes. The
             * last 4 bytes of an id3 header contains the size of the total tag.
             * The first bit of each bit is set to 0 and is ignored. Hence the
             * total bits used is 28 bits. Sizes must be calculated accordingly.
             */
            id3.size = (headerInfo[6] << 21) | (headerInfo[7] << 14) | (headerInfo[8] << 7) | headerInfo[9];
            
            // Read extended header if extended header flag is set
            if(id3.extendedHeaderAdded)
                id3.readExtendedHeader();
        }
        
        return id3;
    }
    
    /**
     * Gets, all frames associated with the current ID3Metadata.
     * @return An array of Frames associated with the current frame.
     */
    public Frame[] getAllFrames() {
        Frame[] allFrames;
        
        /** 
         * If list of frames have already been parsed.
         */
        if(frames.size() >= 1) {
            allFrames = new Frame[frames.size()];
            allFrames = frames.toArray(allFrames);
            
            return allFrames;
        }
        
        try (FileInputStream stream = new FileInputStream(file)) {

            /* 11 byte of the ID3 metadata */
            int index = 10;
            
            // Skip the first 10 header bytes of the ID3 metadata
            stream.skip(10);

            while (index < size) {
                /**
                 * Each ID3 frame contains a 10 byte header.
                 * First 4 bytes represents unique ID of the frame.
                 * Next  4 bytes represents the size of frame data.
                 * Last  2 bytes represents the flags used in the frame.
                 */
                byte[] header   = new byte[10];
                
                // Read header information from the stream
                stream.read(header, 0, header.length);
                
                // Skip the extra padding 
                if(header[0] == 0) {
                    index += 10;
                    continue;
                }
                
                String headerID = new String(header, 0, 4, ENCODING_ASCII);
                int dataSize    = ByteBuffer.wrap(header, 4, 4).getInt();
                int flag1       = header[8];
                int flag2       = header[9];
                
                byte[] data     = new byte[dataSize];
                
                // Move the current reader index to data
                index += 10;
                
                // Read the data of current frame
                stream.read(data, 0, data.length);
                
                // Move the current reader index to next frame
                index += dataSize;
                
                /**
                 * Create a new instance of Frame, add with the frames list
                 */
                Frame newFrame = new Frame(headerID, dataSize, data);
                newFrame.setFlags(flag1, flag2);
                frames.add(newFrame);
                
            }
            
        } catch (IndexOutOfBoundsException | IOException ex) {
            System.out.println("com.codeforwin.id3.ID3Metadata.getAllFrames()");
        }
        
        // Convert the list of frames to array type.
        allFrames = new Frame[frames.size()];
        allFrames = frames.toArray(allFrames);
        
        return allFrames;
    }
    
    /**
     * Adds a new Frame to the ID3 tag. 
     * @param frame Frame to be added.
     */
    public void addFrame(Frame frame) {
        // Check null frames
        if(frame != null) {
            frames.add(frame);
        }
    }
    
    /**
     * Adds a list of Frames to the ID3 tag.
     * @param frames Array of frames to be added.
     */
    public void addFrames(Frame [] frames) {
        
        // Check null
        if(frames != null) {
            for(Frame frame : frames) {
                if(frame != null) {
                    this.frames.add(frame);
                }
            }
        }
    }
    
    /**
     * Saves the new updated ID3 meta data.
     */
    public void saveID3Metadata() {
        // Calculate the total size of frame
        int framesSize = 0;
        for(Frame frame : frames) {
            // Frame data size + header size
            framesSize += frame.getSize() + 10;
        }
        
        /**
         * Create a valid ID3 header tag
         */
        byte frameData[] = new byte[framesSize + 10];
        
        System.arraycopy(ID3_IDENTIFIER.getBytes(), 0, frameData, 0, 3);
        frameData[3] = (byte) majorVersion;
        frameData[4] = (byte) minorVersion;
        frameData[5] = (byte) flag;
        //(headerInfo[6] << 21) | (headerInfo[7] << 14) | (headerInfo[8] << 7) | headerInfo[9];
        int sevenBitMask = 0x7f;
        
        /**
         * Copy all frame objects to binary frame data
         */
        int index = 10;
        for(Frame frame : frames) {
            byte[] data = transformFrameToBytes(frame);
            System.arraycopy(data, 0, frameData, index, data.length);
            
            index += data.length;
        }
        
        int size = index - 10;
        frameData[6] = (byte) ((size >>> 21 ) & sevenBitMask);
        frameData[7] = (byte) ((size >>> 14 ) & sevenBitMask);
        frameData[8] = (byte) ((size >>> 7 ) & sevenBitMask);
        frameData[9] = (byte) ((size) & sevenBitMask);
        
        /**
         * Read original media data.
         */
        byte mediaData[] = null;
        try(FileInputStream stream = new FileInputStream(file)) {
            int mediaSize   = (int) file.length();
            int indexToRead = 0;
            
            // If it contains id3 metadata then skip it
            if(validID3Tag) {
                indexToRead = this.size + 10;
                stream.skip(indexToRead);
                mediaSize -= indexToRead;
            }
            
            mediaData = new byte[mediaSize];
            
            stream.read(mediaData, 0, mediaData.length);
        } catch(IOException e) {
            System.out.println("com.codeforwin.id3.ID3Metadata.saveID3Metadata() READ - " + e.getMessage());
        }
        
        /**
         * Final write ID3 tag with media data.
         */
        try (FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(frameData);
            
            if (mediaData != null)
                stream.write(mediaData);
        } catch (IOException e) {
            System.out.println("com.codeforwin.id3.ID3Metadata.saveID3Metadata() WRITE");
        }
    }
    
    /**
     * Replaces an ID3 frame with another ID3 frame.
     * @param toReplace Frame which will be deleted and replaced by other.
     * @param replaceWith Frame which will be added by replacing toReplace.
     */
    public void replaceFrame(Frame toReplace, Frame replaceWith) {
        // Null checks
        if(toReplace != null && replaceWith != null) {
            frames.remove(toReplace);
            frames.add(replaceWith);
        }
    }
    
    /**
     * Replaces a ID3 frame with another frame. In case there exists more than 
     * one frame with same frameID. It removes all occurrences of the frameID
     * and adds a single frame.
     * @param frameID Unique identifier of the frame to the replaced.
     * @param frame Frame to be added in place of replaced frame.
     * @see addFrame(Frame frame)
     */
    public void replaceFrame(String frameID, Frame frame) {
        // Null checks 
        if(frame != null ) {
            ArrayList<Frame> framesToRemove = new ArrayList<>();
            
            for(Frame f : frames) {
                if(f.getFrameID().equals(frameID)) {
                    framesToRemove.add(f);
                }
            }
            
            if(framesToRemove.size() >= 1)
                frames.removeAll(framesToRemove);
            
            // Add the new frame
            frames.add(frame);
        }
    }
    
    /**
     * Checks whether the current ID3 tag contains a frame or not. A frame 
     * uniqueness is determined by its frame identifier.
     * @param frame Frame to be checked.
     * @return True if the current ID3 tag contains the given frame otherwise
     * false.
     */
    public boolean contains(Frame frame) {
        boolean exists = false;
        
        // If frames have not been parsed
        getAllFrames();
        
        // Check for valid ID3 tag
        if(frames != null) {
            for(Frame f : frames) {
                if(f.equals(frame)) {
                    exists = true;
                    break;
                }
            }
        }
        
        return exists;
    }

    
    /**
     * Gets, the major version associated with the ID3 tag. If the current 
     * <code>ID3Metadata</code> is not valid. Then this function returns 0.
     * 
     * @return Integer specifying the major version of ID3 tag.
     * @see isValidID3Tag()
     */
    public int getMajorVersion() {
        return majorVersion;
    }

    /**
     * Gets, the minor version associated with the ID3 tag. If the current
     * <code>ID3Metadata</code> is not valid. Then this function returns 0.
     * 
     * @return Integer specifying the minor version of ID3 tag.
     */
    public int getMinorVersion() {
        return minorVersion;
    }

    /**
     * Gets, the total size of the ID3 meta-data in the media file. It is the 
     * actual size of the tag excluding the current header tag. For every valid
     * <code>ID3Metadata</code> size must be greater than 0.
     * 
     * @return Integer containing the size of the ID3 tag. Returns 0 if the current
     * <code>ID3Metadata</code> is not valid ID3 tag.
     * @see isValidID3Tag()
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns true if the current media file contains a valid ID3 metadata tag.
     * @return True if media contains ID3 tag otherwise false.
     */
    public boolean isValidID3Tag() {
        return validID3Tag;
    }

    /**
     * Gets, the 7-bit flag status of ID3 valid header flag.  
     * @return Returns true if unsynchronization flag is set otherwise false.
     * @see setFlag(int flags)
     */
    public boolean isUnsynchronizationSet() {
        return unsynchronizationSet;
    }

    /**
     * Gets, the 6-bit flag status of ID3 valid header flag.
     * @return Returns true if an additional extended header is added with header
     * otherwise false.
     * @see setFlag(int flags)
     */
    public boolean isExtendedHeaderAdded() {
        return extendedHeaderAdded;
    }

    /**
     * Gets, the 5-bit flag status of ID3 valid header flag.
     * @return Returns true if experimental tag flag bit is set otherwise false.
     * @see setFlag(int flags)
     */
    public boolean isExperimentalTag() {
        return experimentalTag;
    }

    /**
     * Sets, the flag bits. Set various flag values from one of the three allowed
     * flag values. <code>FLAG_UNSYNCRONIZATION</code>, <code>FLAG_EXTENDED_HEADER</code>, 
     * <code>FLAG_EXPERIMENTAL</code>. 
     * @param flag Integer value specifying the flag.
     */
    public void setFlag(int flag) {
        this.flag = this.flag | flag;
    }
    
    /**
     * Reads, the extended header information if exists.
     */
    private void readExtendedHeader() {
        
    }
    
    /**
     * Converts the frame information to binary format so that it can be 
     * written to the file.
     * @param frame Frame to be transformed to bytes.
     * @return Array of bytes representing the frame data
     */
    private byte[] transformFrameToBytes(Frame frame) {
        byte frameData[] = new byte[frame.getSize() + 10];
        
        String frameID  = frame.getFrameID();
        int frameSize   = frame.getSize();
        int flag1       = frame.getFlag1();
        int flag2       = frame.getFlag2();
        
        // Copy frame ID to the binary frame data
        System.arraycopy(frameID.getBytes(), 0, frameData, 0, 4);
        
        // Copy size to binary frame data
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(frameSize);
        System.arraycopy(buffer.array(), 0, frameData, 4, 4);
        
        // Copy first flag to binary frame data
        frameData[8] = (byte) flag1;
        
        // Copy the second flag to binary frame data
        frameData[9] = (byte) flag2;
        
        // Copy frame data
        System.arraycopy(frame.data, 0, frameData, 10, frame.size);
        
        return frameData;
    }
}
