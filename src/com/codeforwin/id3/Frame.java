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

import java.util.Arrays;
import static com.codeforwin.id3.ID3.*;


/**
 * <code>Frame</code> is the smallest block of data inside an ID3Metadata tag.
 * It contains the actual ID3 meta-data. It is identified by a unique frame ID.
 * 
 * @author Pankaj Prakash
 * @version 1.0
 */
public class Frame {
    public final static int FLAG_NONE                       = 0x00; // 00000000
    public final static int FLAG_TAG_ALTER_PRESERVATION     = 0x80; // 10000000
    public final static int FLAG_FILE_ALTER_PRESERVATION	= 0x40; // 01000000
    public final static int FLAG_READ_ONLY                  = 0x20; // 00100000
    public final static int FLAG_COMPRESSION                = 0x80; // 10000000
    public final static int FLAG_ENCRYPTION                 = 0x40; // 01000000
    public final static int FLAG_GROUP_IDENTITY             = 0x20; // 00100000
    
    
    /**
     * Unique identifier assigned to each frame.
     */
    protected final String frameID;
    
    /**
     * Current textual data encoding
     */
    protected String encoding;
    
    /**
     * Size of the data.
     */
    protected int size;
    
    /**
     * The real data associated with the frame.
     */
    protected byte[] data;
    
    /**
     * The current identifier is a valid standard identifier or not.
     */
    private final boolean standardIdentifier;
    
    /**
     * Flags set to the frame.
     */
    private int flag1, flag2;
    
    
    /**
     * Creates a new instance of <code>Frame</code> class with a valid unique
     * ID3 frame identifier. 
     * 
     * @param frameID String representing unique frame identifier.
     * @param size Size of the data
     * @param data Real data contained by the frame.
     */
    public Frame(String frameID, int size, byte[] data) {
        this.frameID = frameID;
        
        this.size = size;
        
        standardIdentifier = isValidIdentifier(frameID);
        
        /**
         * Sets the current encoding technique used for textual data if any
         */
        if(data.length >= 2 	&& (data[1] == (byte)0xff && data[2] == (byte)0xfe))
        	encoding = ENCODING_UTF16;
        else if(data.length >=2 && (data[1] == (byte)0xfe && data[2] == (byte)0xff))
        	encoding = ENCODING_UTF16BE;
        else if(data.length >=3 && (data[1] == (byte)0xef && data[2] == (byte)0xbb && data[3] == (byte)0xbf))
        	encoding = ENCODING_UTF8;
        else
        	encoding = ENCODING_ISO_8859_1;
        
        this.data = Arrays.copyOfRange(data, 0, data.length);
    }

    
    /**
     * Checks whether the specified identifier is a valid ID3v2.3 or ID3v2.4 
     * unique frame identifier or not. 
     * 
     * @param identifier Unique frame identifier which is to be checked.
     * @return Returns true if the given <code>identifier</code> is unique
     * frame identifier otherwise false.
     */
    public static boolean isValidIdentifier(String identifier) {
    	for (String id : STANDARD_FRAMES) {
            if (id.equals(identifier)) {
                return true;
            }
        }
        
        return false;
    }
    
    
    /**
     * Gets, the encoding byte used in the frame.
     * @return Byte containing the text encoding.
     */
    public byte getEncodingByte() {
    	byte encodingByte = 0x0;
    	
    	switch(encoding) {
    	case ENCODING_UTF16:
    		encodingByte = 1;
    		break;
    	case ENCODING_UTF16BE:
    		encodingByte = 2;
    		break;
    	case ENCODING_UTF8: 
    		encodingByte = 3;
    		break;
    	case ENCODING_ISO_8859_1:
    	default: 
    		encodingByte = 0;
    		break;
    	}
    	
    	return encodingByte;
    }
    
    
	/**
	 * Converts the frame information to binary format so that it can be written
	 * to the file.
	 * 
	 * @return Array of bytes representing the frame data
	 */
	public byte[] pack() {
        byte frameData[] = new byte[size + HEADER_SIZE];
        
        String frameID  = this.frameID;
        int frameSize   = this.size;
        int flag1       = this.flag1;
        int flag2       = this.flag2;
        
        // Copy frame ID to the binary frame data
        System.arraycopy(frameID.getBytes(), 0, frameData, 0, 4);
        
        // Copy size to binary frame data
        byte[] sizeArr 	= getBytes(frameSize);
        System.arraycopy(sizeArr, 0, frameData, 4, Integer.BYTES);
        
        // Copy first flag to binary frame data
        frameData[8] = (byte) flag1;
        
        // Copy the second flag to binary frame data
        frameData[9] = (byte) flag2;
        
        // Copy frame data
        System.arraycopy(data, 0, frameData, HEADER_SIZE, size);
        
        return frameData;
    }
	
	
    /**
     * Checks if the current frame is equal to the some other frame. Two frames 
     * are said to be identical if they have same frame identifier.
     * @param frame Frame to be checked.
     * @return True if current frame is equal to the frame.
     */
    public boolean equals(Frame frame) {
        boolean equal = false;
        
        if(frame != null) {
            equal = this.frameID.equals(frame.frameID);
        }
        
        return equal;
    }
    

    /**
     * Gets, the encoding of the current ID3 frame.
     * @return String containing the encoding description.
     */
    public String getEncoding() {
        return encoding;
    }
    
    
    /**
     * Sets, the encoding of the current ID3 frame.
     * @param encoding String containing the encoding description.
     */
    public void setEncoding(String encoding) {
    	this.encoding = encoding;
    }
    

    /**
     * Gets, the total size of the current ID3 frame data.
     * @return Integer containing actual size of the frame data.
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets, the unique frame ID associated with the current ID3 frame.
     * @return String containing unique frame ID.
     * @see isStandardIdentifier()
     */
    public String getFrameID() {
        return frameID;
    }

    /**
     * Gets, whether the current ID3v2.3 or ID3v2.4 frame is a standard ID3 frame 
     * or not. 
     * @return Returns true if it is standard ID3 frame.
     * @see getFrameID()
     */
    public boolean isStandardIdentifier() {
        return standardIdentifier;
    }

    /**
     * Sets, the two flag bytes of the current ID3 frame.
     * @param flag1 First flag byte of the ID3 frame.
     * @param flag2 Second flag byte of the ID3 frame.
     */
    public void setFlags(int flag1, int flag2) {
        this.flag1 = flag1;
        this.flag2 = flag2;
    }


    /**
     * Gets, the first flag byte.
     * @return An integer representing the status of first flag byte.
     */
    public int getFlag1() {
        return flag1;
    }    
    
    
    /**
     * Sets the value of first flag.
     * @param value Value to be set
     * @see FLAG_TAG_ALTER_PRESERVATION
     * @see FLAG_FILE_ALTER_PRESERVATION
     * @see FLAG_READ_ONLY
     */
    public void setFlag1(int value) {
    	flag1 = flag1 | value; 
    }
    

    /**
     * Gets, the second flag byte.
     * @return An integer representing the status of second flag byte.
     */
    public int getFlag2() {
        return flag2;
    }
    
    
    /**
     * Sets the value of second flag.
     * @param value Value to be set
     * @see FLAG_COMPRESSION
     * @see FLAG_ENCRYPTION
     * @see FLAG_GROUP_IDENTITY
     */
    public void setFlag2(int value) {
    	flag2 = flag2 | value;
    }
    
    
    /**
     * Gets, the raw bytes contained in the frame data. 
     * @return Returns an array of bytes containing original frame data.
     */
    public byte[] getData() {
        return data;
    }
}
