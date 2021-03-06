package edu.uchicago.cs.java.finalproject.game.model;


import java.awt.*;
import java.util.Arrays;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import edu.uchicago.cs.java.finalproject.controller.Game;

public class Asteroid extends Sprite {

	
	private int nSpin;
	private Color color;
	//radius of a large asteroid
	private final int RAD = 100;
    private Point[] pntCoords; //an array of points used to draw polygon
    private int[] nXCoords;
    private int[] nYCoords;
	//nSize determines if the Asteroid is Large (0), Medium (1), or Small (2)
	//when you explode a Large asteroid, you should spawn 2 or 3 medium asteroids
	//same for medium asteroid, you should spawn small asteroids
	//small asteroids get blasted into debris
	public Asteroid(int nSize){
		
		//call Sprite constructor
		super();
		
		
		//the spin will be either plus or minus 0-9
		int nSpin = Game.R.nextInt(10);
		if(nSpin %2 ==0)
			nSpin = -nSpin;
		setSpin(nSpin);
			
		//random delta-x
		int nDX = Game.R.nextInt(10);
		if(nDX %2 ==0)
			nDX = -nDX;
		setDeltaX(nDX);
		
		//random delta-y
		int nDY = Game.R.nextInt(10);
		if(nDY %2 ==0)
			nDY = -nDY;
		setDeltaY(nDY);
			
		assignRandomShape();
		
		//an nSize of zero is a big asteroid
		//a nSize of 1 or 2 is med or small asteroid respectively
		if (nSize == 0)
			setRadius(RAD);
		else
			setRadius(RAD/(nSize * 2));
		

	}

	public Asteroid(Asteroid astExploded,Point X,Point Y){
        super();
        setDim(Game.DIM);
        setColor(Color.yellow);
        setCenter(new Point(Game.R.nextInt(Game.DIM.width),
                Game.R.nextInt(Game.DIM.height)));
        int nSpin = Game.R.nextInt(10);
        if (nSpin % 2 == 0)
            nSpin = -nSpin;
        setSpin(nSpin);

        int x = (int)(astExploded.getCenter().getX());
        int y = (int)(astExploded.getCenter().getY());

        int nDX = (int)(astExploded.getCenter().getX()-(X.getX()+Y.getX())/2);
        setDeltaX(nDX);

        //random delta-y
        int nDY = (int)(astExploded.getCenter().getY()-(X.getY()+Y.getY())/2);
        setDeltaY(nDY);
        setExpire(12);
        assignRandomShape();
        setFadeValue(0);
        //an nSize of zero is a big asteroid
        //a nSize of 1 or 2 is med or small asteroid respectively

        setRadius(RAD / (9));

        setCenter(new Point((int)(x+(X.getX()+Y.getX())/2)/2,(int)(y+(X.getY()+Y.getY())/2)/2));
    }

    public void expire(){
        if (getExpire() == 0)
            CommandCenter.movDebris.remove(this);
        else
            setExpire(getExpire() - 1);
    }
	
	public Asteroid(Asteroid astExploded){
	

		//call Sprite constructor
		super();
		
		int  nSizeNew =	astExploded.getSize() + 1;

            //the spin will be either plus or minus 0-9
            int nSpin = Game.R.nextInt(10);
            if (nSpin % 2 == 0)
                nSpin = -nSpin;
            setSpin(nSpin);

            //random delta-x
            int nDX = Game.R.nextInt(10 + nSizeNew * 2);
            if (nDX % 2 == 0)
                nDX = -nDX;
            setDeltaX(nDX);

            //random delta-y
            int nDY = Game.R.nextInt(10 + nSizeNew * 2);
            if (nDY % 2 == 0)
                nDY = -nDY;
            setDeltaY(nDY);

            assignRandomShape();

            //an nSize of zero is a big asteroid
            //a nSize of 1 or 2 is med or small asteroid respectively

            setRadius(RAD / (nSizeNew * 2));
            setCenter(astExploded.getCenter());

	}
	
	public int getSize(){
		
		int nReturn = 0;
		
		switch (getRadius()) {
			case 100:
				nReturn= 0;
				break;
			case 50:
				nReturn= 1;
				break;
			case 25:
				nReturn= 2;
				break;
            default:
                nReturn= 3;
                break;
		}
		return nReturn;
		
	}

	//overridden
	public void move(){
		super.move();
		
		//an asteroid spins, so you need to adjust the orientation at each move()
		setOrientation(getOrientation() + getSpin());
		
	}

	public int getSpin() {
		return this.nSpin;
	}
	

	public void setSpin(int nSpin) {
		this.nSpin = nSpin;
	}
	
	//this is for an asteroid only
	  public void assignRandomShape ()
	  {
	    int nSide = Game.R.nextInt( 7 ) + 7;
	    int nSidesTemp = nSide;

	    int[] nSides = new int[nSide];
	    for ( int nC = 0; nC < nSides.length; nC++ )
	    {
	      int n = nC * 48 / nSides.length - 4 + Game.R.nextInt( 8 );
	      if ( n >= 48 || n < 0 )
	      {
	        n = 0;
	        nSidesTemp--;
	      }
	      nSides[nC] = n;
	    }

	    Arrays.sort( nSides );

	    double[]  dDegrees = new double[nSidesTemp];
	    for ( int nC = 0; nC <dDegrees.length; nC++ )
	    {
	    	dDegrees[nC] = nSides[nC] * Math.PI / 24 + Math.PI / 2;
	    }
	   setDegrees( dDegrees);
	   
		double[] dLengths = new double[dDegrees.length];
			for (int nC = 0; nC < dDegrees.length; nC++) {
				if(nC %3 == 0)
				    dLengths[nC] = 1 - Game.R.nextInt(40)/100.0;
				else
					dLengths[nC] = 1;
			}
		setLengths(dLengths);

	  }


    public void draw(Graphics g) {
        if(this.getSize() == 3){
            if (getFadeValue() == 255) {
                color = Color.BLUE;
                setColor(color);
                //g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
            } else {
                color = new Color(255/12*this.getExpire(), 0, 255 - 255/24*this.getExpire());
                setColor(color);
                //g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
            }}
        super.draw(g);
        //fill this polygon (with whatever color it has)
        if(this.getSize() == 3){
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
            g.setColor(Color.BLACK);
            g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
        }
        //now draw a white border
        else{
            g.setColor(new Color(153,76,28));
        g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
            g.setColor(new Color(153,76,28));
            g.fillPolygon(getXcoords(),getYcoords(),dDegrees.length);
        }

    }



}
