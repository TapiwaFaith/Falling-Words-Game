package skeletonCodeAssgnmt2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JPanel;

public class WordPanel extends JPanel implements Runnable {
		public static volatile boolean done;
		private WordRecord[] words;
		private int noWords;
		private int maxY;
		private WordRecord currentWord;
		
		/*
		*@method this is the method that fills in the panel with colour
		*
		*/
		public void paintComponent(Graphics g) {
		    int width = getWidth();
		    int height = getHeight();
		    g.clearRect(0,0,width,height);
		    g.setColor(Color.red);
		    g.fillRect(0,maxY-10,width,height);

		    g.setColor(Color.black);
		    g.setFont(new Font("Helvetica", Font.PLAIN, 26));
		   //draw the words
		   //animation must be added 
		    for (int i=0;i<noWords;i++){	    	
		    	//g.drawString(words[i].getWord(),words[i].getX(),words[i].getY());	
		    	g.drawString(words[i].getWord(),words[i].getX(),words[i].getY()+20);  //y-offset for skeleton so that you can see the words	
		    }
		    if (WordApp.score.getTotal() == WordApp.totalWords)
		    {
		    	WordApp.done = true;
		    	//g.clearRect(0,0,width,height);
		    	//g.setColor(Color.black);
		    	g.fillRect(200,50,650,400);
			g.setColor(Color.red);
			g.setFont(new Font("MV_Boli",Font.PLAIN,45));
			g.drawString("GAME OVER!",325,100);
			g.drawString("Missed "+WordApp.score.getMissed()+" words!",325,150);
			g.drawString("Caught "+WordApp.score.getCaught()+" words!",325,200);
			g.drawString("Score "+WordApp.score.getScore(),325,250);
			g.drawString("Press Start to start a new game!",325,300);
			//WordApp.startGame();
        				//print the start message
        				//call the start method
		    }
		    /*if (WordApp.exittt = true)
		    {
		    	super.paintComponent(g);
		    }*/
				repaint();	   
		  }
		/*
		*@constructor
		*/
		WordPanel(WordRecord[] words, int maxY) {
			this.words=words; //will this work?
			noWords = words.length;
			done=false;
			this.maxY=maxY;		
		}
		
		/*
		*@constructor
		*/
		WordPanel(WordRecord[] words, WordRecord currentWord, int maxY)
		{
			this.words=words; //will this work?
			this.currentWord = currentWord;
			noWords = words.length;
			done=false;
			this.maxY=maxY;
		}
		/*
		*@method this is the method that does the animation
		*/
		public void run() {
			//System.out.println("Faith");
			//add in code to animate this
			
			
			while(this.currentWord.wordThreadNotActive() == false && WordApp.done == false)
			{
				this.currentWord.drop(10);
				//check input is the same as the word. Check if the word is past the redzone. Check if the total words(missed and caught) falling have reached the limit.
				if (this.currentWord.matchWord(WordApp.input))
				{
					//System.out.println("Faith");
					WordApp.score.caughtWord(WordApp.input.length());
					WordApp.caught.setText("Caught: " + WordApp.score.getCaught() + "    ");
					WordApp.scr.setText("Score:" + WordApp.score.getScore()+ "    ");
					//System.out.println(WordApp.score.getCaught());
				}
				if (this.currentWord.dropped())
				{
					this.currentWord.resetPos();
					this.currentWord.resetWord();
					WordApp.score.missedWord();
					WordApp.missed.setText("Missed:" + WordApp.score.getMissed()+ "    ");
				}
				//System.out.println(this.currentWord);
				   				
				try
				{
					Thread.sleep(currentWord.getSpeed());
				}
				//drops to the maximum level
				catch(Exception e)
				{
					e.printStackTrace();
				}
				repaint();
				//drop the word till it touches the red
			}
		}

	}


