
public class Song {

	String path;
	String title;
	String artist;
	String album;
	/**
	 * @param path
	 * @param title
	 * @param artist
	 * @param album
	 */
	
	//constructor
	public Song(String title, String artist, String album,String path) {
		super();
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.path = path;
	}
	//getters and setters
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	//compare to method for sort()
	public int compareTo(Song nsong) {		
		String t1 = this.title;
		String t2 = ((Song)nsong).getTitle();
		
		return t1.compareTo(t2);
	}
	//toString 
	@Override
	public String toString() {
		return "title: " + title + ", artist: " + artist
				+ ", album: " + album +", path: "+path;
	}
	

	
}