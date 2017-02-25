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

import java.awt.Image;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * <code>Frame</code> is the smallest block of data inside an ID3Metadata tag.
 * It contains the actual ID3 meta-data. It is identified by a unique frame ID.
 * 
 * @author Pankaj Prakash
 * @version 1.0
 */
public class Frame {      
    /**
     * List of all valid ID3v2.3 and ID3v2.4 frame identifiers.
     */
    public final static String [] IDENTIFIERS = new String[] {
        "AENC", "APIC", "ASPI", "COMM", "COMR", "TSIZ", "ENCR", "EQUA", "EQU2",
        "ETCO", "GEOB", "GRID", "LINK", "MCDI", "MLLT", "OWNE", "PCNT", "POPM",
        "POSS", "PRIV", "RBUF", "RVAD", "RVA2", "RVRB", "SEEK", "SIGN", "SYLT",
        "SYTC", "TALB", "TBPM", "TCOM", "TCON", "TCOP", "TDEN", "TDLY", "TORY",
        "TDOR", "TDAT", "TDRC", "TRDA", "TIME", "TYER", "TDRL", "TDTG", "TENC",
        "TEXT", "TFLT", "IPLS", "TIPL", "TIT1", "TIT2", "TIT3", "TKEY", "TLAN",
        "TLEN", "TMCL", "TMED", "TMOO", "TOAL", "TOFN", "TOLY", "TOPE", "TOWN",
        "TPE1", "TPE2", "TPE3", "TPE4", "TPOS", "TPRO", "TPUB", "TRCK", "TRSN",
        "TRSO", "TSOA", "TSOP", "TSOT", "TSRC", "TSSE", "TSST", "TXXX", "UFID",
        "USER", "USLT", "WCOM", "WCOP", "WOAF", "WOAR", "WOAS", "WORS", "WPAY",
        "WPUB", "WXXX"
    };
    
    public final static int FLAG_NONE                       = 0x00; // 00000000
    public final static int FLAG_TAG_ALTER_PRESERVATION     = 0x80; // 10000000
    public final static int FLAG_FILE_ALTER_PRESERVATION    = 0x40; // 01000000
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
    protected final String encoding;
    
    /**
     * Size of the data.
     */
    protected int size;
    
    /**
     * The real data associated with the frame.
     */
    protected byte [] data;
    
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
        
        // Eliminate all null paddings in data
        int index = 0;
        
        this.size    = size;
        
        standardIdentifier = isValidIdentifier(frameID);
        
        /**
         * Sets the current encoding technique used for textual data if any
         */
        switch(data[0]) {
            case 0: 
                encoding = ID3Metadata.ENCODING_ASCII;
                break;
                
            case 1: 
                encoding = ID3Metadata.ENCODING_UTF16;
                index = 1;
                break;
                
            case 2: 
                encoding = ID3Metadata.ENCODING_UTF16BE;
                break;
                
            case 3:
                encoding = ID3Metadata.ENCODING_UTF8;
                break;
                
            default:
                encoding = ID3Metadata.ENCODING_ASCII;
        }
        
        this.data    = Arrays.copyOfRange(data, index, data.length-index);
        
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
        if(identifier.length() != 4)
            return false;
        
        for (String id : IDENTIFIERS) {
            if (id.equals(identifier)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets, the type of the data associated with the current data field of the
     * frame. 
     * @return String containing the type name of the data field.
     */
    public String getDataType() {
        if(frameID.startsWith("T")) 
            return String.class.getSimpleName();
        else if (frameID.equals("APIC"))
            return Image.class.getSimpleName();
        else if (frameID.startsWith("W")) 
            return URI.class.getSimpleName();
        else if (frameID.equals("POMP"))
            return String.class.getSimpleName();
        else 
            return Byte.class.getSimpleName();
    }
    
    
    /**
     * Gets, the raw bytes contained in the frame. 
     * @return An array of bytes containing original frame  data.
     */
    public byte[] getBytes() {
        return data;
    }
    
    /**
     * Gets, the <code>String</code> representation of the data associated with
     * the current frame.
     * @return ISO-8859-1, UTF-8 or UTF-16 representation of the data. Returns 
     * null if error in encoding.
     * @see getDataType()
     */
    public String getString() {
        String str = null;
        
        try {
            str = new String(data, 0, data.length, encoding);
        } catch (UnsupportedEncodingException e) { }
        
        return str;
    }
    
    /**
     * Gets, the 2-byte integer representation of the data associated with the
     * current frame.
     * @return short representation of the data.
     * @see getDataType()
     */
    public short getShort() {
        short s;
        
        ByteBuffer buff = ByteBuffer.wrap(data);
        s = buff.getShort();
        
        return s;
    }
    
    /**
     * Gets, the 4-byte integer representation of the data associated with the 
     * current frame.
     * @return int representation of the data.
     * @see getDataType()
     */
    public int getInt() {
        int i;
        
        ByteBuffer buff = ByteBuffer.wrap(data);
        i = buff.getInt();
        
        return i;
    }
    
    /**
     * Gets, the URI link contained in the data.
     * @return Instance of URI representing the link.
     * @see getDataType()
     */
    public URI getURL() {
        URI uri = null;
        
        String url = getString();
        
        try {
            uri = new URI(url);
        } catch(URISyntaxException e) { }
        
        return uri;
    }
    
    /**
     * If the current frame is of APIC type then it returns an instance of 
     * <code>FrameAPIC</code> otherwise returns null. 
     * @return Returns an instance of <code>FrameAPIC</code> if current frame is 
     * APIC type otherwise returns null.
     * @see FrameAPIC
     * @see getDataType()
     * @see getImageType()
     */
    public FrameAPIC getImageFrame() {
        if (frameID.equals("APIC"))
            return new FrameAPIC(size, data);
        else
            return null;
    }
    
    /**
     * If the current ID3 frame is valid APIC frame. Then this function returns 
     * the type of image associated with the Image.
     * @return Instance of ApicType specifying the type of image.
     * @see #getImageFrame()
     * @see ApicType
     */
    public ApicType getImageType() {
        ApicType type = null;
        
        if(frameID.equals("APIC")) {
            int descriptionIndex = 1;
            
            while(data[descriptionIndex] != 0) 
                descriptionIndex++;
            
            int imgType = data[descriptionIndex];
            
            try {
                type = ApicType.values()[imgType];
            } catch (ArrayIndexOutOfBoundsException e) {
                ArrayIndexOutOfBoundsException ex = new ArrayIndexOutOfBoundsException("Unkown image type found. It is not defined by the APIC standard.");
                
                throw ex;
            }
        }
        
        return type;
    }

    /**
     * Gets, the encoding of the current ID3 frame.
     * @return String containing the encoding description.
     */
    public String getEncoding() {
        return encoding;
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
     * Gets, the second flag byte.
     * @return An integer representing the status of second flag byte.
     */
    public int getFlag2() {
        return flag2;
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
}
