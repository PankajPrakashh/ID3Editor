/**
 * 
 */
package com.codeforwin.id3;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

/**
 * This class defines all ID3 frames, list of genres, ID3 integer packing methods and other constants.
 * 
 * @author Pankaj Prakash
 * @version 0.9
 */
public class ID3 {
	
	private ID3() { }

	// ID3 character encodings
    public final static String 	ENCODING_ISO_8859_1		= "ISO-8859-1";
    public final static String 	ENCODING_UTF8        	= "UTF-8";
    public final static String 	ENCODING_UTF16       	= "UTF-16";
    public final static String 	ENCODING_UTF16BE     	= "UTF-16BE";
    public final static String 	ENCODING_UTF16LE     	= "UTF-16LE";
    
    public final static int    	FLAG_UNSYNCRONIZATION	= 0x80; // 10000000
    public final static int    	FLAG_EXTENDED_HEADER 	= 0x40; // 01000000
    public final static int    	FLAG_EXPERIMENTAL    	= 0x20; // 00100000
    
    public final static String 	ID3_TAG_IDENTIFIER		= "ID3";
    
    public final static int		SEVEN_BIT_MASK			= 0x7f; 
    
    public final static int		HEADER_SIZE				= 0xa; // 10
	
    
	public final static String[] STANDARD_FRAMES = new String[] { 
			"AENC", "APIC", "ASPI", "COMM", "COMR", "TSIZ", "ENCR", "EQUA", "EQU2", "ETCO", "GEOB", "GRID", "LINK", 
			"MCDI", "MLLT", "OWNE", "PCNT", "POPM", "POSS", "PRIV", "RBUF", "RVAD", "RVA2", "RVRB", "SEEK", "SIGN", 
			"SYLT", "SYTC", "TALB", "TBPM", "TCOM", "TCON", "TCOP", "TDEN", "TDLY", "TORY", "TDOR", "TDAT", "TDRC", 
			"TRDA", "TIME", "TYER", "TDRL", "TDTG", "TENC", "TEXT", "TFLT", "IPLS", "TIPL", "TIT1", "TIT2", "TIT3", 
			"TKEY", "TLAN", "TLEN", "TMCL", "TMED", "TMOO", "TOAL", "TOFN", "TOLY", "TOPE", "TOWN", "TPE1", "TPE2", 
			"TPE3", "TPE4", "TPOS", "TPRO", "TPUB", "TRCK", "TRSN", "TRSO", "TSOA", "TSOP", "TSOT", "TSRC", "TSSE", 
			"TSST", "TXXX", "UFID", "USER", "USLT", "WCOM", "WCOP", "WOAF", "WOAR", "WOAS", "WORS", "WPAY", "WPUB", 
			"WXXX" };
    
	/**
	 * List of all default Genres
	 */
	public static final String[] GENRES = new String[] { 
			"Blues", 			"Classic Rock", 		"Country",			"Dance",			"Disco",			
			"Funk",				"Grunge",				"Hip-Hop",			"Jazz",				"Metal",
			"New Age",			"Oldies",				"Other",			"Pop",				"R&B",
			"Rap",				"Reggae",				"Rock",				"Techno",			"Industrial",
			"Alternative",		"Ska",					"Death Metal",		"Pranks",			"Soundtrack",
			"Euro-Techno",		"Ambient", 				"Trip-Hop", 		"Vocal", 			"Jazz+Funk", 
			"Fusion", 			"Trance", 				"Classical", 		"Instrumental",		"Acid",
			"House",			"Game",					"Sound Clip",		"Gospel",			"Noise",
			"Alternative Rock",	"Bass", 				"Punk",				"Space",			"Meditative",
			"Instrumental Pop",	"Instrumental Rock",	"Ethnic",			"Gothic", 			"Darkwave", 
			"Techno-Industrial","Electronic",			"Pop-Folk", 		"Eurodance",		"Dream", 
			"Southern Rock", 	"Comedy", 				"Cult", 			"Gangsta", 			"Top 40", 
			"Christian Rap",	"Pop/Funk", 			"Jungle", 			"Native US", 		"Cabaret", 
			"New Wave", 		"Psychedelic", 			"Rave", 			"Showtunes", 		"Trailer",
			"Lo-Fi", 			"Tribal", 				"Acid Punk", 		"Acid Jazz", 		"Polka", 
			"Retro", 			"Musical", 				"Rock & Roll", 		"Hard Rock",		"Folk", 
			"Folk-Rock", 		"National Folk", 		"Swing", 			"Fast Fusion", 		"Bebob", 
			"Latin", 			"Revival", 				"Celtic",			"Bluegrass", 		"Avantgarde", 
			"Gothic Rock", 		"Progressive Rock", 	"Psychedelic Rock",	"Symphonic Rock",	"Slow Rock", 
			"Big Band",			"Chorus", 				"Easy Listening", 	"Acoustic", 		"Humour", 
			"Speech", 			"Chanson", 				"Opera",			"Chamber Music", 	"Sonata", 
			"Symphony", 		"Booty Bass", 			"Primus", 			"Porn Groove", 		"Satire", 
			"Slow Jam", 		"Club",					"Tango", 			"Samba", 			"Folklore", 
			"Ballad", 			"Power Ballad", 		"Rhythmic Soul", 	"Freestyle", 		"Duet", 
			"Punk Rock",		"Drum Solo", 			"A Cappella", 		"Euro-House", 		"Dance Hall", 
			"Goa", 				"Drum & Bass", 			"Club-House", 		"Hardcore",			"Terror", 
			"Indie", 			"BritPop", 				"Negerpunk", 		"Polsk Punk", 		"Beat", 
			"Christian Gangsta","Heavy Metal",			"Black Metal", 		"Crossover", 		"Contemporary Christian",
			"Christian Rock", 	"Merengue", 			"Salsa", 			"Thrash Metal",		"Anime", 
			"JPop", 			"SynthPop" 
			};
    
	/**
	 * Types of images stored in APIC frame
	 */
	public final static String[] ALBUM_ART_TYPE = new String[] {
			"OTHER", 						"FILE_ICON",			"OTHER_FILE_ICON", 	"COVER_FRONT", 	
			"COVER_BACK",					"LEAFLET_PAGE",			"MEDIA",			"LEAD_ARTIST",
			"ARTIST",						"CONDUCTOR",			"BAND",				"COMPOSER",
			"LYRICISTS",					"RECORDING_LOCATION",	"DURING_RECORDING",	"DURING_PERFORMANCE", 	
			"MOVIE_VIDEO_SCREEN_CAPTURE",	"BRIGHT_COLOURED_FISH",	"ILLUSTRATION",		"BAND_ARTIST_LOGO",
			"PUBLISHER_STUDIO_LOGO" 
			};
	
	
	/**
	 * Converts an integer to four bytes array using ID3 size packing specification. 
	 * Size : 4 * %0xxxxxxx
	 * 
	 * @param value Integer to be converted.
	 * @return Returns four byte array.
	 */
	public static byte[] packInteger(int value) {
		byte[] frameData = new byte[4];
		
		frameData[0] = (byte) ((value >>> 21 ) & SEVEN_BIT_MASK);
        frameData[1] = (byte) ((value >>> 14 ) & SEVEN_BIT_MASK);
        frameData[2] = (byte) ((value >>> 7  ) & SEVEN_BIT_MASK);
        frameData[3] = (byte) ((value		 ) & SEVEN_BIT_MASK);
		
        return frameData;
	}
	
	/**
	 * Convert four byte array to integer using ID3 size packing specification.
	 * @param value Array of bytes of size four
	 * @return Returns the converted integer.
	 */
	public static int unpackInteger(byte[] bytes) {
		int value = (bytes[0] << 21) | (bytes[1] << 14) | (bytes[2] << 7) | bytes[3];
		
		return value;
	}
	
	
	/**
	 * Converts a 4 byte sized array to integer. 
	 * @param bytes byte array of size 4.
	 * @return Returns an integer representation of bytes
	 */
	public static int getInteger(byte[] bytes) {
		if(bytes.length != 4)
			throw new IllegalArgumentException("Bytes array must be of length 4.");
		
		return ByteBuffer.wrap(bytes, 0, bytes.length).getInt();
	}
	
	
	/**
	 * Converts bytes array to 4 byte integer format.
	 * @param bytes Array of bytes to be converted.
	 * @param startIndex Starting index from where the integer bytes starts.
	 * @return Returns an 4 byte integer.
	 */
	public static int getInteger(byte[] bytes, int startIndex) {
		if(bytes.length - startIndex < 4)
			throw new IllegalArgumentException("Bytes array must be atleast 4 byte long.");
		
		return ByteBuffer.wrap(bytes, startIndex, 4).getInt();
	}
	
	
	/**
	 * Converts the given byte array to string using specified encoding.
	 * @param bytes Array of bytes containing string.
	 * @param encoding Encoding in which string needs to be decoded.
	 * @return Returns the converted string. In case encoding is not correct
	 * returns null.
	 */
	public static String getString(byte[] bytes, String encoding) {
		try {
			return new String(bytes, 0, bytes.length, encoding);
		} catch (UnsupportedEncodingException ex) {
			return null;
		}
	}
	
	
	/**
	 * Converts the given byte array to string using specified encoding.
	 * @param bytes Array of byte containing string.
	 * @param start Starting position of the string in byte array.
	 * @param length Length of string in byte array.
	 * @param encoding Encoding in which string needs to be decoded.
	 * @return Returns the converted string. In case encoding is not correct
	 * returns null.
	 */
	public static String getString(byte[] bytes, int start, int length, String encoding) {
		try {
			return new String(bytes, start, length, encoding);
		} catch (UnsupportedEncodingException ex) {
			return null;
		}
	}
	
	
	/**
	 * Converts the given byte array to Image.
	 * @param imageData Array of byte containing image binary data
	 * @return Returns the Image on success otherwise returns null.
	 */
	public static Image getImage(byte[] imageData) {
        BufferedImage img = null;
        
        try(ByteArrayInputStream stream = new ByteArrayInputStream(imageData)) {
            img  = ImageIO.read(stream);
        } catch (IOException e) {
        	return null;
		}
        
        return ((Image)img);
	}
	
	
	/**
	 * Converts string to bytes.
	 * @param str String to be converted to bytes
	 * @param encoding Encoding in which string is to be encoded.
	 * @return Bytes representation of string.
	 */
	public static byte[] getBytes(String str, String encoding) {
		try {
			return str.getBytes(encoding);
		} catch (UnsupportedEncodingException ex) {
			return str.getBytes();
		}
	}
	
	
	/**
	 * Converts integer to bytes.
	 * @param number Integer to be converted to bytes
	 * @return Bytes representation of integer
	 */
	public static byte[] getBytes(int number) {
		ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
		buffer.putInt(number);
		
		return buffer.array();
	}
	
	
	/**
	 * Converts short to bytes.
	 * @param number Short to be converted to bytes
	 * @return Bytes representation of short
	 */
	public static byte[] getBytes(short number) {
		ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
		buffer.putShort(number);
		
		return buffer.array();
	}
	
	
	/**
	 * Converts Image to bytes.
	 * @param image Image to be converted to bytes
	 * @return Bytes representation of Image
	 */
	public static byte[] getBytes(Image image) {
        BufferedImage img = (BufferedImage) image;
        
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        try {
			ImageIO.write(img, "jpg", boas);
		} catch (IOException e) {
			return null;
		}
        
        return boas.toByteArray();
	}
}
