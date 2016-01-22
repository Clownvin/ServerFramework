package com.git.cs309.mmoserver.characters;

public class Character {
	// Things to add:
	/*
	 * 
	 */
	//TODO
	//decide what defult x and y positions are (im thinking far off map
	
	//for anything that can be his/move might include trees. objects top class
	//right now no was to res creatures
	
	//base functions
	//generateing function
	//set hp
	//kill
	// remove hp
	//give hp
	//get position
	//set position 
	//get name
	//set name
	//set id
	//get id
	//get speed
	//set speed
	//get isdead
	//set isdead
	
	
	//bellow 
	//implemented down
	//set xp (eXicution Points)
	// remove xp (eXicution Points)
	//attacks
	
	protected String Name;
	protected int HitPoints;
	protected int ID;
	protected boolean isDead; //true is dead
	protected int Speed;
	protected int xPosition;
	protected int yPosition;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	//generating;
	public Character()
	{
		Name = null;
		HitPoints = -1;
		ID = -1;
		isDead = false;
		Speed = 0;
		xPosition = 0;//defult position is off map
		yPosition = 0;  //defult position is off map
	}
	
	public Character(String inComingName ,int inComingHitPoints, int inComingID , boolean inComingisDead, int inComingSpeed, int inComingxPosition , int inComingyPosition)
	{
		Name = inComingName;
		HitPoints = inComingHitPoints;
		ID = inComingID;
		isDead = inComingisDead;
		Speed = inComingSpeed;
		xPosition = inComingxPosition;
		yPosition = inComingyPosition;
	}
	
	
	
	public int getHP(int x)
	{
		return HitPoints;
	}
	
	public void setHP(int x)
	{
		HitPoints =x;
		if(HitPoints ==0)
		{
			isDead =true;
		}
		else
		{
			isDead =false;
		}
	}
	
	public void giveHp(int x)
	{
		HitPoints =HitPoints + x;
		if (HitPoints ==0)
		{
			isDead =true;
		}
		else
		{
			isDead =false;
		}
	}
	
	public void kill()
	{
		isDead =true;
	}
	
	public boolean getIsDead()
	{
		return isDead;
	}
	
	public void setIsDead(boolean x)
	{
		isDead=x;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public void setID(int x)
	{
		ID =x;
	}
	
	public String getName()
	{
		return Name;
	}
	
	public void setName(String x)
	{
		Name =x;
	}
	
	public int getSpeed()
	{
		return Speed;
	}
	
	public void setSpeed(int x)
	{
		Speed =x;
	}
	
	public int getXPosition()
	{
		return xPosition;
	}
	
	public int getYPosition()
	{
		return yPosition;
	}
	
	public void setPosition(int x, int y)
	{
		xPosition = x;
		yPosition = y;
	}
	
}
