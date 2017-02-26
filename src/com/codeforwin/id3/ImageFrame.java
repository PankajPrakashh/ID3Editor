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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import javax.imageio.ImageIO;

/**
 * <code>FrameAPIC</code> class represents an image frame of a valid ID3 tag.
 * 
 * @author Pankaj Prakash
 * @version 1.0
 * @see Frame
 */
public class FrameAPIC extends Frame {
    
    /**
     * Mime type of the image.
     */
    private String mimeType;
    
    /**
     * Image type used in the current APIC frame.
     */
    private ApicType pictureType;
    
    /**
     * Description of image.
     */
    private String pictureDescription;
    
    /**
     * Raw bytes containing image.
     */
    private byte[] imageData;
    
    /**
     * Image associated with the current APIC frame.
     */
    private Image image;
    
    /**
     * Extension of the image.
     */
    private String imageFormat;
    
    
    /**
     * Creates a new instance of <code>FrameApic</code>.
     * 
     * @param size Total size of the frame excluding header.
     * @param data Data contained in the frame.
     */
    public FrameAPIC(int size, byte[] data) {
        super("APIC", size, data);
        
        processFrame();
    }
    
    /**
     * Evaluates the current APIC frame and extract various data from frame.
     */
    @SuppressWarnings("empty-statement")
    private void processFrame() {
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
        
        try {
            mimeType = new String(data, 1, index-1, encoding);
        } catch(UnsupportedEncodingException ex) {
            System.out.println("com.codeforwin.id3.FrameAPIC.processFrame()");
        }
        
        // Next byte contains the image type
        //index++;
        int picType = data[index];
        pictureType = ApicType.values()[picType];
        
        int descriptionStartIndex = index + 1;
        
        // Move till the description ends
        while(data[index++] != 0);
        
        try {
            pictureDescription = new String(data, descriptionStartIndex, index - descriptionStartIndex, encoding);
        } catch (UnsupportedEncodingException ex) {
            System.out.println("com.codeforwin.id3.FrameAPIC.processFrame()");
        }
        
        // The real image data
        imageData = new byte[data.length - index];
        imageData = Arrays.copyOfRange(data, index, data.length);
    }

    /**
     * Gets, the mime type of the image. The mime type represented by a series 
     * of string in given format - image/png etc.
     * @return String specifying the mime type of the image.
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Gets, the type of the picture associated with the current APIC tag.
     * @return Instance of ApicType specifying the current APIC type.
     * @see ApicType
     */
    public ApicType getPictureType() {
        return pictureType;
    }

    /**
     * Gets, the picture description if any.
     * @return String containing picture description if any otherwise null.
     */
    public String getPictureDescription() {
        return pictureDescription;
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
    public Image getImage() {
        BufferedImage img = getBufferedImage();
        
        if(img == null)
            return null;
        else
            return (Image)img;
    }
    
    /**
     * Gets, an instance of <code>BufferedImage</code> that represents the current
     * APIC frame image.
     * @return An instance of <code>BufferedImage</code>.
     * @see getImage()
     */
    public BufferedImage getBufferedImage() {
        BufferedImage img = null;
        
        try( ByteArrayInputStream stream = new ByteArrayInputStream(this.imageData)){
            img  = ImageIO.read(stream);
        } catch (IOException ex) {
            System.out.println("com.codeforwin.id3.FrameAPIC.getBufferedImage()");
        }
        
        return img;
    }
}
