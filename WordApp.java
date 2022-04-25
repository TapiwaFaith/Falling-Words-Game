package skeletonCodeAssgnmt2;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;


import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
//model is separate from the view.

public class WordApp {
//shared variables
	static int noWords=4;
	static int totalWords;

   	static int frameX=1000;
	static int frameY=600;
	static int yLimit=480;
	public static boolean exittt = false;
	public static AtomicInteger wordCount; //number of words on the screen per any given time
	public static AtomicInteger numberThreads; //number of threads running per any given time

	static WordDictionary dict = new WordDictionary(); //use default dictionary, to read from file eventually

	static WordRecord[] words;
	public static volatile boolean done;  //must be volatile
	static 	Score score = new Score();

	static WordPanel w;
	public static String input = "";
	static JLabel scr;
	static JLabel caught;// =new JLabel("Caught: " + score.getCaught() + "    ");
      	static JLabel missed;// =new JLabel("Missed:" + score.getMissed()+ "    ");
      	static JFrame frame;
      	static Thread threads;
      	/*
      	*@method this method sets up the GUI interface and puts all the necessary buttons to the GUI to allow for user interaction.
      	*
      	*/	
	public static void setupGUI(int frameX,int frameY,int yLimit) {
		// Frame init and dimensions
    	/*JFrame*/ frame = new JFrame("WordGame"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(frameX, frameY);
      JPanel g = new JPanel();
      g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
      g.setSize(frameX,frameY);
    	
		w = new WordPanel(words,yLimit);
		w.setSize(frameX,yLimit+100);
	   g.add(w); 
	    
      JPanel txt = new JPanel();
      txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS)); 
      /*JLabel*/ caught =new JLabel("Caught: " + score.getCaught() + "    ");
      /*JLabel*/ missed =new JLabel("Missed:" + score.getMissed()+ "    ");
      /*JLabel*/ scr =new JLabel("Score:" + score.getScore()+ "    ");    
      txt.add(caught);
	   txt.add(missed);
	   txt.add(scr);
    
	    //[snip]
  
	   final JTextField textEntry = new JTextField("",20);
	   textEntry.addActionListener(new ActionListener()
	   {
	      public void actionPerformed(ActionEvent evt) {
	         String text = textEntry.getText();
	          //[snip]
	          
	         textEntry.setText("");
	         textEntry.requestFocus();
	         input = text;
	      }
	   });
	   
	   txt.add(textEntry);
	   txt.setMaximumSize( txt.getPreferredSize() );
	   g.add(txt);
	    
	   JPanel b = new JPanel();
      b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS)); 
	   JButton startB = new JButton("Start");;
		
			// add the listener to the jbutton to handle the "pressed" event
		startB.addActionListener(new ActionListener()
		{
		   public void actionPerformed(ActionEvent e)
		   {
		      //[snip]
		      score.resetScore();
		      for(int i = 0; i < noWords; i++)
		      {
		      	words[i].resetPos();
		      	words[i].resetWord();
		      }
		      scr.setText("Score:"+score.getScore()+"    ");
		      caught.setText("Caught:"+score.getCaught()+"    ");
		      missed.setText("Missed:"+score.getMissed()+"    ");
		      g.repaint();
		      startGame();
		      textEntry.requestFocus();  //return focus to the text entry field
		   }
		});
		JButton endB = new JButton("End");;
			
				// add the listener to the jbutton to handle the "pressed" event
		endB.addActionListener(new ActionListener()
		{
		   public void actionPerformed(ActionEvent e)
		   {
		   	stop();
		   	score.resetScore();
		   	scr.setText("Score:"+score.getScore()+"    ");
		      caught.setText("Caught:"+score.getCaught()+"    ");
		      missed.setText("Missed:"+score.getMissed()+"    ");
		   	//g.repaint();
		   }
		});
		
		JButton quitB = new JButton("Quit");;
		quitB.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		
		b.add(startB);
		b.add(endB);
		b.add(quitB);
		
		g.add(b);
    	
      frame.setLocationRelativeTo(null);  // Center window on screen.
      frame.add(g); //add contents to window
      frame.setContentPane(g);     
       	//frame.pack();  // don't do this - packs it into small space
      frame.setVisible(true);
	}
	/*
	*@method this method takes in the text file to allow for population of the array which is going to be useful when creating the threads
	*
	*/
   public static String[] getDictFromFile(String filename) {
		String [] dictStr = null;
		try {
			Scanner dictReader = new Scanner(new FileInputStream(filename));
			int dictLength = dictReader.nextInt();
			//System.out.println("read '" + dictLength+"'");

			dictStr=new String[dictLength];
			for (int i=0;i<dictLength;i++) {
				dictStr[i]=new String(dictReader.next());
				//System.out.println(i+ " read '" + dictStr[i]+"'"); //for checking
			}
			dictReader.close();
		} catch (IOException e) {
	        System.err.println("Problem reading file " + filename + " default dictionary will be used");
	    }
		return dictStr;
	}
	
	/*
	*@method this function should start the game by creating the necessary threads
	*
	*/
	public static void startGame()
	{
		done = false;
		wordCount = new AtomicInteger(noWords);
		numberThreads = new AtomicInteger(noWords);
		
		for (int i = 0; i<noWords; i++)
		{
			threads = new Thread(new WordPanel(words,words[i], yLimit));
			threads.start();
		}
	}
	
	public static void stop()
	{
		done = true;
		for (int i = 0; i<noWords; i++)
		{
			words[i].resetWord();
		}
	}
	/*
	*@method this is the main method which runs all the programs
	*/
	public static void main(String[] args) {
    	
		//deal with command line arguments
		totalWords=Integer.parseInt(args[0]);  //total words to fall
		noWords=Integer.parseInt(args[1]); // total words falling at any point
		assert(totalWords>=noWords); // this could be done more neatly
		String[] tmpDict=getDictFromFile(args[2]); //file of words
		if (tmpDict!=null)
			dict= new WordDictionary(tmpDict);
		
		WordRecord.dict=dict; //set the class dictionary for the words.
		
		words = new WordRecord[noWords];  //shared array of current words
		
		//[snip]
		
		setupGUI(frameX, frameY, yLimit);  
    	//Start WordPanel thread - for redrawing animation

		int x_inc=(int)frameX/noWords;
	  	//initialize shared array of current words

		for (int i=0;i<noWords;i++) {
			words[i]=new WordRecord(dict.getNewWord(),i*x_inc,yLimit);
		}
	}
}
