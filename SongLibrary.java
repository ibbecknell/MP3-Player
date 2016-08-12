import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;


public class SongLibrary{															// Initialization of Library class, ArrayList, and methods 
	ArrayList<Song> library = new ArrayList <Song>();								
																					//constructor
	public SongLibrary(){
	}
	public ArrayList<Song> getLibrary(){											//getter
		return library;
	}
	public void setLibrary(ArrayList<Song> library){								//setter
		this.library = library;
	}
	public Song getSong(int i){														//returns the song info at user specified index
		return library.get(i);
	}
	public void clear(){															// clears the ArrayList 
		library.clear();
	}
	public int length(){															// length method. Acts as the size method of ArrayList.
		return library.size();
	}
	public void addSong(Song song){													// adds song object to the ArrayList.
		library.add(song);
	}
	public void sort(){																// sort method. Sorts the library ArrayList alphabetically through titles. Uses insertion sort.
			for (int i = 1; i < library.size(); i++){
				Song temp = library.get(i);
				int j;
				for (j=i-1; j >= 0 && temp.compareTo(library.get(j))<0; j--){
					library.remove(j+1);
					library.add(j+1, library.get(j));
				}
				library.remove(j+1);
				library.add((j+1), temp);
			}
		}
	public void savetofile() throws IOException{									// Writes song information stored in the ArrayList to text file "songs.txt"
		String songtext = "songs.txt";
		String arrlength = "" + library.size() + "";
		FileWriter fw = new FileWriter(songtext);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter outfile= new PrintWriter(bw);
		
		outfile.println(arrlength);
		for (int l=0; l<library.size();l++){
			outfile.println(library.get(l).getTitle());
			outfile.println(library.get(l).getArtist());
			outfile.println(library.get(l).getAlbum());
			outfile.println(library.get(l).getPath());
			outfile.println();
		}
		bw.close();
		System.out.println("Library Saved to 'songs.txt'");
	}
	public void loadfromfile(File textfile) throws FileNotFoundException{			// Scans a text file line by line, creates new song objects, and adds them to the ArrayList 
		ArrayList<Song> lsongs = new ArrayList <Song>();
		Scanner filescan = new Scanner(textfile);
		int songcount = filescan.nextInt();
		for(int i = 0; i < songcount; i++){
			if(filescan.nextLine()!=" "){
				String title = filescan.nextLine();
				String artist = filescan.nextLine();
				String album = filescan.nextLine();
				String path = filescan.nextLine();
				Song nsong = new Song(title, artist, album, path);
				lsongs.add(nsong);
				this.library = lsongs;
			}
		}
		filescan.close();
	}	

	public void travdir(File dir){													//traverses the files to get info of mp3 files, creates new song objects from tags and adds them to the ArrayList
		if(dir.isFile()){
			String filename = dir.getName(); 
			if(filename.length() > 4) { 
				String extension = filename.substring(filename.length()-3, filename.length()); 
				if(extension.contains("mp3")) {
					AudioFile f;
					try {
						f = AudioFileIO.read(dir);
						Tag tag = f.getTag();
						String title = tag.getFirst(FieldKey.TITLE);
						String artist = tag.getFirst(FieldKey.ARTIST);
						String album = tag.getFirst(FieldKey.ALBUM);
						String path = dir.getAbsolutePath();
						Song newsong = new Song(title, artist, album,path);
						library.add(newsong);
					} catch (CannotReadException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (TagException e) {
						e.printStackTrace();
					} catch (ReadOnlyFileException e) {
						e.printStackTrace();
					} catch (InvalidAudioFrameException e) {
						e.printStackTrace();
					}
				}
			}
			return;
		}
		//directory
		File [] array = dir.listFiles(); 
			for(File temp : array) { 
				travdir(temp); 
			}
			return;	
	}
}

