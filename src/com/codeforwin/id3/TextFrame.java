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

/**
 * <code>TextFrame</code> represents all valid textual content frames. 
 * @author Pankaj Prakash
 * @version 0.9
 * @see Frame
 */
public class TextFrame extends Frame {

	private String textData;
	
	public TextFrame(String frameID, int size, byte[] data) {
		super(frameID, size, data);
	}

	public TextFrame(String frameID, String data) {
		this(frameID, data, ENCODING_ASCII);
	}
	
	public TextFrame(String frameID, String data, String encoding) {
		super(frameID, getBytes(data, encoding).length, getBytes(data, encoding));
		
		this.encoding = encoding;
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
}
