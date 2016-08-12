import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


public class MPlayerPanel extends JPanel {
	//initializes Buttons, Tables, Panels, and layout
	private JButton playButton, stopButton, exitButton, loadMp3Button, saveButton, openButton;
	private JTable table = null;
	
	MPlayerPanel() {
	
	this.setLayout(new BorderLayout());
	
	// buttonPanelTop contains the top row of buttons:
	// load mp3 files, save and open
	JPanel buttonPanelTop = new JPanel();
	buttonPanelTop.setLayout(new GridLayout(1,3));
	loadMp3Button = new JButton("Load mp3");
	saveButton = new JButton("Save Library");
	openButton = new JButton("Load Library");


	loadMp3Button.addActionListener(new MyButtonListener());
	saveButton.addActionListener(new MyButtonListener());
	openButton.addActionListener(new MyButtonListener());

	buttonPanelTop.add(loadMp3Button);
	buttonPanelTop.add(saveButton);
	buttonPanelTop.add(openButton);
	this.add(buttonPanelTop, BorderLayout.NORTH);
		
	
	// Bottom panel with panels: Play, Stop, Exit buttons
	JPanel buttonPanelBottom = new JPanel();
	buttonPanelBottom.setLayout(new GridLayout(1,3));
	playButton = new JButton("Play");
	stopButton = new JButton("Stop");
	exitButton = new JButton("Exit");
	


	playButton.addActionListener(new MyButtonListener());
	stopButton.addActionListener(new MyButtonListener());
	exitButton.addActionListener(new MyButtonListener());


	buttonPanelBottom.add(playButton);
	buttonPanelBottom.add(stopButton);
	buttonPanelBottom.add(exitButton);

	
	this.add(buttonPanelBottom, BorderLayout.SOUTH);	
}
	
	SongLibrary library = new SongLibrary();																		
	PlayerThread cThread;
	boolean playing=false;
	
	class MyButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// The function that does something whenever each
			// button is pressed
			if (e.getSource() == loadMp3Button) {
				library.clear();																				//clears the ArrayList				
				File dir = new File("C:/Users/India/Documents/CS112/workspace/Project4/music");					//initializes File, traverses and sorts
				library.travdir(dir); 
				library.sort();
				if(table == null){																			
					String[][] tableElems = new String[library.length()][3];
					String[] columnNames = {"Title", "Artist", "Album"};
					for(int i=0; i<library.length(); i++){
						tableElems[i][0] = library.getLibrary().get(i).getTitle();
						tableElems[i][1] = library.getLibrary().get(i).getArtist();
						tableElems[i][2] = library.getLibrary().get(i).getAlbum();
					}
					table = new JTable(tableElems, columnNames);
					JScrollPane scrollPane=new JScrollPane(table);
					add(scrollPane, BorderLayout.CENTER);
				}
				else{																							
					for(int i=0; i<library.length(); i++){														//prints song elements in the ArrayList to the table
						table.setValueAt(library.getLibrary().get(i).getTitle(), i, 0);							//keeps program from unnecessarily traversing the files
						table.setValueAt(library.getLibrary().get(i).getArtist(), i, 1);
						table.setValueAt(library.getLibrary().get(i).getAlbum(), i, 2);
					}
				}
				updateUI();
			}
			
			else if (e.getSource() == saveButton) {
				try {
					library.savetofile();																		//calls savetofile method when save button is pressed, handles IOException
				} catch (IOException io) {
					io.printStackTrace();
				}				
			}
			else if (e.getSource() == openButton) {
				File songtxt = new File("C:/Users/India/Documents/CS112/workspace/Project4/songs.txt");
				try {																							//loads and prints song items from the saved text file to the table
					library.clear();
					library.loadfromfile(songtxt);
					library.sort();

					if (table == null){
						String[][] tableElems = new String[library.length()][3];
						String[] columnNames = {"Title", "Artist","Album"};
						for(int i=0; i<library.length(); i++){
							tableElems[i][0] = library.getLibrary().get(i).getTitle();
							tableElems[i][1] = library.getLibrary().get(i).getArtist();
							tableElems[i][2] = library.getLibrary().get(i).getAlbum();
						}
						table = new JTable(tableElems,columnNames);
						JScrollPane scrollPane = new JScrollPane(table);
						add(scrollPane,BorderLayout.CENTER);
						
						updateUI();
					} 
					else if (table != null){
						for(int i = 0; i<library.length(); i++){												//sets song elements in ArrayList to the respective columns in the table 
							table.setValueAt(library.getLibrary().get(i).getTitle(),i,0);
							table.setValueAt(library.getLibrary().get(i).getArtist(),i,1);
							table.setValueAt(library.getLibrary().get(i).getAlbum(),i,2);
						}
					}						
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}				
			}
			else if (e.getSource() == playButton) {																//Plays the user selected song from the ArrayList
				if(table == null){																				//Prints message when the play button is pressed when the table is empty
					System.out.println("You're getting ahead of yourself, Yo");
				}
				else{
					if (playing == true){																		//if a song is already playing when the button is pressed, 
						cThread.stop();																		//stops it and plays the user selected song
					}
					int slctSong = table.getSelectedRow();
					String filename = library.getSong(slctSong).getPath();
					cThread = new PlayerThread(filename); 
					cThread.start();
					playing = true;
				}
			}
			
			else if (e.getSource() == stopButton) {																//stops the song that is being played when the button is pressed
				if (playing == true){
					cThread.stop();
				}
				else{																							//prints a message if there is no song playing
					System.out.println("There's nothing playing...");
				}
			}
			else if (e.getSource() == exitButton) {																//terminates the program when exit button is pressed											
				System.exit(0);
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame ("Mp3 player");
	      frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	      MPlayerPanel panel  = new MPlayerPanel();
	      panel.setPreferredSize(new Dimension(400,400));
	      frame.getContentPane().add (panel);
	     
	      
	      frame.pack();
	      frame.setVisible(true);
	}
	
	
}