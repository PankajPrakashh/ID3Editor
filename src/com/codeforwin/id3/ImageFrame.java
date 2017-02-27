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
import java.util.Arrays;
import static com.codeforwin.id3.ID3.*;

/**
 * <code>ImageFrame</code> class represents an image frame of a valid ID3 tag.
 * 
 * @author Pankaj Prakash
 * @version 0.9
 * @see Frame
 */
public class ImageFrame extends Frame {
    
    /**
     * Mime type of the image.
     */
    private String mimeType;
    
    /**
     * Image type used in the current APIC frame.
     */
    private String albumArtType;
    
    /**
     * Description of image.
     */
    private String albumArtDescription;
    
    /**
     * Raw bytes containing image.
     */
    private byte[] imageData;
    
    /**
     * Image associated with the current APIC frame.
     */
    @SuppressWarnings("unused")
	private Image image;
    
    
    /**
     * Creates a new instance of <code>FrameApic</code>. Use the ImageFrame.newInstance() method if you want to add 
     * a new APIC tag to the ID3 metadata.
     * @param size Total size of the frame excluding header.
     * @param data Data contained in the frame.
     * @param parseFrameData False if it is new ImageFrame otherwise true.
     */
    public ImageFrame(int size, byte[] data, boolean parseFrameData) {
    	super("APIC", size, data);
    	
    	if(parseFrameData)
    		unpack();
    }
    
    
    /**
     * Creates a new instance of ImageFrame. Use this method if you want to add a new APIC frame to the ID3 metadata.
     * @param encoding Encoding in which image details are encoded
     * @param mimeType Mime type of the image
     * @param imageType Album art type
     * @param imageDesc Short image description
     * @param imageBinaryData Binary data of image
     * @return Instance of ImageFrame
     */
	public static ImageFrame newInstance(String encoding, String mimeType, String imageType, String imageDesc,
			byte[] imageBinaryData) {
		
		ImageFrame frame = null;

		/**
		 * Gets the encoding byte
		 */
		byte encodingByte = 0;
		switch (encoding) {
		case ENCODING_ISO_8859_1:
			encodingByte = 0;
			break;
		case ENCODING_UTF16:
			encodingByte = 1;
			break;
		case ENCODING_UTF16BE:
			encodingByte = 2;
			break;
		case ENCODING_UTF8:
			encodingByte = 3;
			break;
		}
		
		byte[] 	mimeTypeByte 	= getBytes(mimeType, encoding);
		byte	imageTypeByte	= 0;
		for(int i=0; i<ALBUM_ART_TYPE.length; i++) {
			if(ALBUM_ART_TYPE[i].equals(imageType)) {
				imageTypeByte = (byte) i;
				break;
			}
		}
		byte[] 	imageDescByte	= getBytes(imageDesc, encoding);
		
		
		int frameSize			= mimeTypeByte.length + imageDescByte.length + 3;
		byte[] frameData = new byte[frameSize];

		
		/**
		 * Copy the encoding byte
		 */
		int copyIndex = 0;
		frameData[copyIndex] = encodingByte;
		copyIndex += 1;
		
		/**
		 * Copy the mime type to frame data
		 */
		System.arraycopy(mimeTypeByte, 0, frameData, copyIndex, mimeTypeByte.length);
		copyIndex += mimeTypeByte.length;
		frameData[copyIndex++] = 0;
		
		/**
		 * Copy the image type information data
		 */
		frameData[copyIndex++] = imageTypeByte;
		
		/**
		 * Copy the image description information data
		 */
		System.arraycopy(imageDescByte, 0, frameData, copyIndex, imageDescByte.length);
		copyIndex += imageDescByte.length;
		frameData[copyIndex++] = 0;
		
		/**
		 * Copy the original image binary data
		 */
		System.arraycopy(imageBinaryData, 0, frameData, copyIndex, imageBinaryData.length);
    	
		frame = new ImageFrame(frameSize, frameData, false);
		
    	return frame;
    }
    
    
    /**
     * Evaluates the current APIC frame and extract various data from frame.
     */
    private void unpack() {
        /**
         * Image inside a APIC data is stored in below format. 
         * Text encoding $xx (Already processed by the super class)
         * MIME type text-string $00 
         * Picture type $xx 
         * Description text string according to encoding $00 (00) 
         * Picture data binary image data
         */
        int index = 1; // Not 0 as first byte is already processed by super()
        
        // Move till the mime type text ends
        while(data[index++] != 0);
        
        mimeType = getString(data, 1, index - 1, encoding);
        
        // Next byte contains the image type
        int picType = data[index];
        albumArtType = ALBUM_ART_TYPE[picType];
        
        /**
         * Read the image description
         */
        int descStartIndex = index + 1;
        
        // Move till the description ends
        while(data[index++] != 0);
        
        int descEndIndex = index - descStartIndex;
        
        albumArtDescription = getString(data, descStartIndex, descEndIndex, encoding);
        
        // The real image data
        imageData = new byte[data.length - index];
        imageData = Arrays.copyOfRange(data, index, data.length);
    }
    

	/**
	 * Converts the frame information to binary format so that it can be written
	 * to the file.
	 * 
	 * @return Array of bytes representing the frame data
	 */
    @Override
    public byte[] pack() {
        /**
         * Image inside a APIC data is stored in below format. 
         * Text encoding $xx (Already processed by the super class)
         * MIME type text-string $00 
         * Picture type $xx 
         * Description text string according to encoding $00 (00) 
         * Picture data binary image data
         */
    	
		/**
		 * Gets the encoding byte
		 */
		byte encodingByte = 0;
		switch (encoding) {
		case ENCODING_ISO_8859_1:
			encodingByte = 0;
			break;
		case ENCODING_UTF16:
			encodingByte = 1;
			break;
		case ENCODING_UTF16BE:
			encodingByte = 2;
			break;
		case ENCODING_UTF8:
			encodingByte = 3;
			break;
		}
		
		byte[] 	mimeTypeByte 	= getBytes(mimeType, encoding);
		byte	imageTypeByte	= 0;
		for (int i = 0; i < ALBUM_ART_TYPE.length; i++) {
			if (ALBUM_ART_TYPE[i].equals(albumArtType)) {
				imageTypeByte = (byte) i;
				break;
			}
		}
		byte[] 	imageDescByte	= getBytes(albumArtDescription, encoding);
		
		
		/**
		 * Image frame size = text_encoding_byte(1) + mime_type_length + picture_type_length + description_type_length 
		 * 					+ image_binary_data_length + extra_padding
		 */
		final int PADDING = 1;
		int frameSize = 1 + mimeTypeByte.length + PADDING + 1 + imageDescByte.length + PADDING + imageData.length;
		data = new byte[frameSize];

		
		/**
		 * Copy the encoding byte
		 */
		int copyIndex 	= 0;
		data[copyIndex] = encodingByte;
		copyIndex += 1;
		
		/**
		 * Copy the mime type to frame data
		 */
		System.arraycopy(mimeTypeByte, 0, data, copyIndex, mimeTypeByte.length);
		copyIndex += mimeTypeByte.length;
		data[copyIndex++] = 0;
		
		/**
		 * Copy the image type information data
		 */
		data[copyIndex++] = imageTypeByte;
		
		/**
		 * Copy the image description information data
		 */
		System.arraycopy(imageDescByte, 0, data, copyIndex, imageDescByte.length);
		copyIndex += imageDescByte.length;
		data[copyIndex++] = 0;
		
		/**
		 * Copy the original image binary data
		 */
		System.arraycopy(imageData, 0, data, copyIndex, imageData.length);
		
		size = data.length;
    	
    	return super.pack();
    }
    
    
    /**
     * Gets, the mime type of the image. The mime type represented by a series 
     * of string. 
     * Example - image/png
     * @return String specifying the mime type of the image.
     */
    public String getMimeType() {
        return mimeType;
    }
    
    
    /**
     * Sets, MIME type of the image. The MIME type is given by a series of string
     * in specified format. 
     * Example - image/png
     * @param mimeType MIME type of the image
     */
    public void setMimeType(String mimeType) {
    	this.mimeType = mimeType;
    }
    

    /**
     * Gets, the type of the picture associated with the current APIC tag.
     * @return String specifying the current APIC type.
     */
    public String getAlbumArtType() {
        return albumArtType;
    }
    
    
    /**
     * Sets, the type of the album art.
     * @param albumArtType Type of the album art
     * @see ID3.ALBUM_ART_TYPE
     */
    public void setAlbumArtType(String albumArtType) {
    	this.albumArtType = albumArtType;
    }
    

    /**
     * Gets, the picture description if any.
     * @return Description of album are if any otherwise null.
     */
    public String getAlbumArtDescription() {
        return albumArtDescription;
    }
    
    
    /**
     * Sets, the album art description if any.
     * @param description Description of album art
     */
    public void setAlbumArtDescription(String description) {
    	this.albumArtDescription = description;
    }

    
    /**
     * Gets, the raw bytes containing the image.
     * @return Array of bytes containing image.
     */
    public byte[] getImageData() {
        return imageData;
    }
    
    
    /**
     * Gets, the Image representation of the raw image bytes.
     * @return Instance of Image specifying the APIC image.
     * @see getBufferedImage()
     */
    public Image getAlbumArt() {
    	return getImage(imageData);
    }
    
    
    /**
     * Sets, the new image of the album art.
     * @param image Image to be set.
     */
    public void setAlbumArt(Image image) {
    	this.image 		= image;
    	this.imageData 	= getBytes(image);
    }
}
