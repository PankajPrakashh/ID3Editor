/**
 * 
 */
package com.codeforwin.id3;

/**
 * This class defines all ID3 frames, list of genres, ID3 integer packing methods and other constants.
 * 
 * @author Pankaj Prakash
 * @version 0.9
 */
public class ID3 {
	
	private ID3() { }

	// ID3 character encodings
    public final static String ENCODING_ASCII       = "ISO-8859-1";
    public final static String ENCODING_UTF8        = "UTF-8";
    public final static String ENCODING_UTF16       = "UTF-16";
    public final static String ENCODING_UTF16BE     = "UTF-16BE";
    public final static String ENCODING_UTF16LE     = "UTF-16LE";
    
    public final static int    FLAG_UNSYNCRONIZATION= 0x80; // 10000000
    public final static int    FLAG_EXTENDED_HEADER = 0x40; // 01000000
    public final static int    FLAG_EXPERIMENTAL    = 0x20; // 00100000
    
    public final static String ID3_IDENTIFIER       = "ID3";
	
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
	public final static String[] ApicType = new String[] {
			"OTHER", 						"FILE_ICON",			"OTHER_FILE_ICON", 	"COVER_FRONT", 	
			"COVER_BACK",					"LEAFLET_PAGE",			"MEDIA",			"LEAD_ARTIST",
			"ARTIST",						"CONDUCTOR",			"BAND",				"COMPOSER",
			"LYRICISTS",					"RECORDING_LOCATION",	"DURING_RECORDING",	"DURING_PERFORMANCE", 	
			"MOVIE_VIDEO_SCREEN_CAPTURE",	"BRIGHT_COLOURED_FISH",	"ILLUSTRATION",		"BAND_ARTIST_LOGO",
			"PUBLISHER_STUDIO_LOGO" 
			};
	
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public byte[] packInteger(int value) {
		return null;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public int unpackInteger(byte[] bytes) {
		int value = (bytes[0] << 21) | (bytes[1] << 14) | (bytes[2] << 7) | bytes[3];
		
		return value;
	}
}
