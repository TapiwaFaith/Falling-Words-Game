package skeletonCodeAssgnmt2;

public class Score
{
	private int missedWords;
	private int caughtWords;
	private int gameScore;
	
	Score()
	{
		missedWords=0;
		caughtWords=0;
		gameScore=0;
	}
		
	// all getters and setters must be synchronized
	/*
	*@returns the missedWords counter
	*/
	public synchronized int getMissed()
	{
		return missedWords;
	}
	
	/*
	*@returns the caughtWords counter
	*/
	public synchronized int getCaught()
	{
		return caughtWords;
	}
	
	/*
	*@returns the total of the missedWords + caughtWords
	*/
	public synchronized int getTotal()
	{
		return (missedWords+caughtWords);
	}
	
	/*
	*@returns the gameScore counter
	*/
	public synchronized int getScore()
	{
		return gameScore;
	}
	
	/*
	*@method that increments the missedWords counter
	*/
	public synchronized void missedWord()
	{
		missedWords++;
	}

	/*
	*@method that increments the missedWords counter as well as the gameScore
	*/
	public synchronized void caughtWord(int length)
	{
		caughtWords++;
		gameScore+=length;
	}

	/*
	*@method that resets the caughtWords, missedWords and gameScore counter
	*/
	public synchronized void resetScore()
	{
		caughtWords=0;
		missedWords=0;
		gameScore=0;
	}
}
