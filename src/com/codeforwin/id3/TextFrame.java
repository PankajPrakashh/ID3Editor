/**
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

import static com.codeforwin.id3.ID3.*;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * TextFrame represents all valid textual content frames. 
 * @author Pankaj Prakash
 * @version 0.9
 * @see Frame
 */
public class TextFrame extends Frame {

	/**
	 * Encoding used by the Text frames
	 */
	@SuppressWarnings("unused")
	private final static String[] TEXT_ENCODINGS = new String[] {
		ENCODING_ISO_8859_1,
		ENCODING_UTF16,
		ENCODING_UTF16BE,
		ENCODING_UTF8
	};
	
	/**
	 * String terminators in various encoding
	 */
	private final static byte[][] TERMINATORS 	= new byte[][] {
		{0}, 
		{0},
		{0, 0},
		{0}
	};
	
	
	private String textData;
	
	
	
	public TextFrame(String frameID, int size, byte[] data) {
		super(frameID, size, data);
		
		textData = getString(data, encoding);
	}

	public TextFrame(String frameID, String data) {
		this(frameID, data, ENCODING_ISO_8859_1);
	}
	
	public TextFrame(String frameID, String data, String encoding) {
		super(frameID, getBytes(data, encoding).length, getBytes(data, encoding));
		
		this.encoding = encoding;
	}
	
	/**
	 * Gets, the actual text data in given encoding after stripping out BOM characters and terminators if exists. 
	 * @return String containing the actual textual data.
	 */
	@SuppressWarnings("unused")
	private String getActualText() {	
		String actualText = getString(data, encoding);
		
		int leadingCharsToRemove = 0;
		if (data.length >= 2 		&& ((data[1] == (byte)0xfe && data[2] == (byte)0xff) || (data[1] == (byte)0xff && data[2] == (byte)0xfe))) {
			leadingCharsToRemove = 2;
		} else if (data.length >= 3 && (data[1]  == (byte)0xef && data[2] == (byte)0xbb && data[3] == (byte)0xbf)) {
			leadingCharsToRemove = 3;
		}
		int trailingCharsToRemove = 0;
		
		byte textEncoding = getEncodingByte();
		byte[] terminator = TERMINATORS[textEncoding];
		if (data.length - leadingCharsToRemove >= terminator.length) {
			
			boolean haveTerminator = true;
			for (int i = 0; i < terminator.length; i++) {
				if (data[data.length - terminator.length + i] != terminator[i]) {
					haveTerminator = false;
					break;
				}
			}
			
			if (haveTerminator) trailingCharsToRemove = terminator.length;
		}
		
		if (leadingCharsToRemove + trailingCharsToRemove > 0) {
			int newLength = data.length - leadingCharsToRemove - trailingCharsToRemove + 2;
			byte[] newValue = new byte[newLength];
			if (newLength > 0) {
				System.arraycopy(data, 1 , newValue, 0, newValue.length);
			}
			
			try {
				actualText = bytesToString(newValue, encoding);//getString(newValue, encoding);
			} catch (CharacterCodingException e) {
				actualText = getString(newValue, encoding);
			}
		}
		
		return actualText;
	}
	
	
	/**
	 * Converts array of byte to String.
	 * @param bytes Array of byte to be converted
	 * @param textEncoding Encoding of the string
	 * @return Converted string
	 * @throws CharacterCodingException
	 */
	private static String bytesToString(byte[] bytes, String textEncoding) throws CharacterCodingException {
		CharBuffer cbuf = bytesToCharBuffer(bytes, textEncoding);
		String s = cbuf.toString();
		int length = s.indexOf(0);
		if (length == -1)
			return s;
		return s.substring(0, length);
	}

	
	/**
	 * Converts bytes to CharBuffer.
	 * @param bytes Array of byte to be converted
	 * @param textEncoding Encoding of the string
	 * @return Instance of CharBuffer
	 * @throws CharacterCodingException
	 */
	private static CharBuffer bytesToCharBuffer(byte[] bytes, String textEncoding) throws CharacterCodingException {
		Charset charset = Charset.forName(textEncoding);
		CharsetDecoder decoder = charset.newDecoder();
		return decoder.decode(ByteBuffer.wrap(bytes));
	}
	
	
	@Override
	public byte[] pack() {
		
		return super.pack();
	}
	

	/**
	 * @return the textData
	 */
	public String getTextData() {
		return textData;
	}

	
	/**
	 * @param textData the textData to set
	 */
	public void setTextData(String textData) {
		this.textData 	= textData;
		this.data		= getBytes(textData, encoding);
	}
	
	
	
	@Override
	public void setEncoding(String encoding) {
		// TODO Auto-generated method stub
		super.setEncoding(encoding);
	}
	
	
	@Override
	public String toString() {
		return textData;
	}
}
