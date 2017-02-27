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

import static com.codeforwin.id3.ID3.*;

/**
 * Comment frame structure
 * -----------------------
 * Text encoding $xx 
 * Language 	 $xx xx xx 
 * Short content message text string according to encoding $00 (00) 
 * The actual text full text string according to encoding
 *
 * @author Pankaj Prakash
 * @version 0.9
 * @see Frame
 */
public class CommentFrame extends Frame {
	
	/**
	 * Language of the comment.
	 */
	private String language;
	
	/**
	 * Short content message description text.
	 */
	private String description;
	
	/**
	 * The actual comment full text.
	 */
	private String comment;
	
	

	/**
	 * @param frameID
	 * @param size
	 * @param data
	 */
	public CommentFrame(String frameID, int size, byte[] data) {
		super(frameID, size, data);

		unpack();
	}


	/**
	 * Unpacks the comment frame and fetches the data.
	 */
	private void unpack() {
		language = getString(data, 1, 3, encoding);
		
		int terminatorIndex = 4;
		while(terminatorIndex < size && data[terminatorIndex++] != 0);
		terminatorIndex--;
		
		description = getString(data, 4, terminatorIndex - 4, encoding);
		
		int terminatorLen = (description.isEmpty()) ? 0 : description.length();
		
		comment	 	= getString(data, terminatorIndex, data.length - 4 - terminatorLen, encoding);
	}
	
	
	@Override
	public byte[] pack() {
		byte[] lang = getBytes(language, ENCODING_ISO_8859_1);
		byte[] desc = getBytes(description, encoding);
		byte[] comm = getBytes(comment, encoding);
		
		byte encodingByte = getEncodingByte();
		
		int newLength = lang.length + desc.length + comm.length + 1;
		
		data = new byte[newLength];
		
		int index = 0;
		data[index++] = encodingByte;
		System.arraycopy(lang, 0, data, index, lang.length);
		index += lang.length;
		
		System.arraycopy(desc, 0, data, index, desc.length);
		index += desc.length;
		
		System.arraycopy(comm, 0, data, index, comm.length);
		
		size = newLength;
		
		return super.pack();
	}
	

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}



	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}



	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}



	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}



	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}



	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

}
