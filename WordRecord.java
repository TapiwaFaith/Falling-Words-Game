package skeletonCodeAssgnmt2;

public class WordRecord {
	private String text;
	private  int x;
	private int y;
	private int maxY;
	private boolean dropped;
	
	private int fallingSpeed;
	private static int maxWait=1500;
	private static int minWait=100;

	public static WordDictionary dict;
	
	private boolean wordThreadStopped;
	
	/*
	*@constructor
	*/
	WordRecord() {
		text="";
		x=0;
		y=0;	
		maxY=300;
		dropped=false;
		wordThreadStopped=false;
		fallingSpeed=(int)(Math.random() * (maxWait-minWait)+minWait); 
	}
	
	/*
	*@constructor
	*/
	WordRecord(String text) {
		this();
		this.text=text;
	}
	
	/*
	*@constructor
	*/
	WordRecord(String text,int x, int maxY) {
		this(text);
		this.x=x;
		this.maxY=maxY;
	}
	
// all getters and setters must be synchronized
	/*
	*@returns the wordThreadStopped
	*/
	public synchronized boolean wordThreadNotActive()
	{
		return wordThreadStopped;
	}
	
	/*
	*@returns the y value if the object has been dropped or not
	*/
	public synchronized  void setY(int y) {
		if (y>maxY) {
			y=maxY;
			dropped=true;
		}
		this.y=y;
	}
	
	/*
	*@returns x value
	*/
	public synchronized  void setX(int x) {
		this.x=x;
	}
	
	/*
	*@returns the set Word/text
	*/
	public synchronized  void setWord(String text) {
		this.text=text;
	}
	
	
	/*
	*@returns the text
	*/
	public synchronized  String getWord() {
		return text;
	}
	
	/*
	*@returns the x value
	*/
	public synchronized  int getX() {
		return x;
	}	
	
	/*
	*@returns the y value
	*/
	public synchronized  int getY() {
		return y;
	}
	
	/*
	*@returns the fallingSpeed
	*/
	public synchronized  int getSpeed() {
		return fallingSpeed;
	}
	
	/*
	*@returns the x and y positions of the word
	*/
	public synchronized void setPos(int x, int y) {
		setY(y);
		setX(x);
	}
	
	/*
	*@returns the resetPos
	*/
	public synchronized void resetPos() {
		setY(0);
	}
	
	/*
	*@returns the resetWord position
	*/
	public synchronized void resetWord() {
		resetPos();
		text=dict.getNewWord();
		dropped=false;
		fallingSpeed=(int)(Math.random() * (maxWait-minWait)+minWait); 
		//System.out.println(getWord() + " falling speed = " + getSpeed());
		//the first lines only happens when the maximum number of words hasn't been reached. need an if else statement that resetword increases the counter if not reached the mnax number of words
	}
	
	/*
	*@returns if the words match or not
	*/
	public synchronized boolean matchWord(String typedText) {
		//System.out.println("Matching against: "+text);
		if (typedText.equals(this.text)) {
			resetWord();
			return true;
		}
		else
			return false;
	}
	
	
	/*
	*@returns how much the word has been dropped
	*/
	public synchronized  void drop(int inc) {
		setY(y+inc);
	}
	
	/*
	*@returns if the word has been dropped or not
	*/
	public synchronized  boolean dropped() {
		return dropped;
	}

}
