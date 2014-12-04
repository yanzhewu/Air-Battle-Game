package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.uchicago.cs.java.finalproject.controller.Game;
import edu.uchicago.cs.java.finalproject.sounds.Sound;


public class Falcon extends Sprite {

	// ==============================================================
	// FIELDS 
	// ==============================================================
	
	private final double THRUST = .7;


	final int DEGREE_STEP = 9;
	
	private boolean bShield = false;
    private boolean bFlameOpposite = false;
	private boolean bFlame = false;
	private boolean bProtected; //for fade in and out

    private boolean bThrustingOpposite = false;
	private boolean bThrusting = false;
	private boolean bTurningRight = false;
	private boolean bTurningLeft = false;
	private boolean bMove = false;
	private int nShield;
			
	private final double[] FLAME = { 23 * Math.PI / 24 + Math.PI / 2,
			Math.PI + Math.PI / 2, 25 * Math.PI / 24 + Math.PI / 2 };

	private int[] nXFlames = new int[FLAME.length];
	private int[] nYFlames = new int[FLAME.length];

	private Point[] pntFlames = new Point[FLAME.length];

	
	// ==============================================================
	// CONSTRUCTOR 
	// ==============================================================
	
	public Falcon() {
		super();

		ArrayList<Point> pntCs = new ArrayList<Point>();
		
		// top of ship
		pntCs.add(new Point(0, 18)); 
		
		//right points
		pntCs.add(new Point(3, 3)); 
		pntCs.add(new Point(12, 0)); 
		pntCs.add(new Point(13, -2)); 
		pntCs.add(new Point(13, -4)); 
		pntCs.add(new Point(11, -2)); 
		pntCs.add(new Point(4, -3)); 
		pntCs.add(new Point(2, -10)); 
		pntCs.add(new Point(4, -12)); 
		pntCs.add(new Point(2, -13)); 

		//left points
		pntCs.add(new Point(-2, -13)); 
		pntCs.add(new Point(-4, -12));
		pntCs.add(new Point(-2, -10)); 
		pntCs.add(new Point(-4, -3)); 
		pntCs.add(new Point(-11, -2));
		pntCs.add(new Point(-13, -4));
		pntCs.add(new Point(-13, -2)); 
		pntCs.add(new Point(-12, 0)); 
		pntCs.add(new Point(-3, 3)); 
		

		assignPolarPoints(pntCs);

		setColor(Color.white);
		
		//put falcon in the middle.
		setCenter(new Point(Game.DIM.width / 2, Game.DIM.height / 2));
		
		//with random orientation
		setOrientation(Game.R.nextInt(360));
		
		//this is the size of the falcon
		setRadius(35);

		//these are falcon specific
		setProtected(true);
		setFadeValue(0);
	}
	
	
	// ==============================================================
	// METHODS 
	// ==============================================================

	public void move() {

        //if((bThrusting || bThrustingOpposite) && (bTurningRight || bTurningLeft) ){
		//super.move();
        Point pnt = getCenter();
        double dX = pnt.x + getDeltaX();
        double dY = pnt.y + getDeltaY();

        //this just keeps the sprite inside the bounds of the frame
        if (pnt.x > getDim().width) {
            setCenter(new Point(1, pnt.y));

        } else if (pnt.x < 0) {
            setCenter(new Point(getDim().width - 1, pnt.y));
        } else if (pnt.y > getDim().height) {
            setCenter(new Point(pnt.x, 1));

        } else if (pnt.y < 0) {
            setCenter(new Point(pnt.x, getDim().height - 1));
        } else {

            setCenter(new Point((int) dX, (int) dY));
        }



        if (bThrustingOpposite){
            bFlameOpposite = true;
            double dAdjustX = Math.cos(Math.toRadians(getOrientation()))
                    * THRUST;
            double dAdjustY = Math.sin(Math.toRadians(getOrientation()))
                    * THRUST;
            //setDeltaX(getDeltaX() - dAdjustX);
            //setDeltaY(getDeltaY() - dAdjustY);
            setDeltaX(- dAdjustX*20);
            setDeltaY(- dAdjustY*20);
        }
		if (bThrusting) {
			bFlame = true;
			double dAdjustX = Math.cos(Math.toRadians(getOrientation()))
					* THRUST;
			double dAdjustY = Math.sin(Math.toRadians(getOrientation()))
					* THRUST;
  			//setDeltaX(getDeltaX() + dAdjustX);
  			//setDeltaY(getDeltaY() + dAdjustY);
            setDeltaX( dAdjustX*20);
            setDeltaY( dAdjustY*20);
		}
            if (bTurningLeft) {
                if (getOrientation() <= 0 && bTurningLeft) {
                    setOrientation(360);
                }
                if(bThrustingOpposite){
                    setOrientation(getOrientation() + DEGREE_STEP);
                }
                else{
                setOrientation(getOrientation() - DEGREE_STEP);}
            }
            if (bTurningRight) {
                if (getOrientation() >= 360 && bTurningRight) {
                    setOrientation(0);
                }
                if(bThrustingOpposite){
                    setOrientation(getOrientation() - DEGREE_STEP);
                }
                else{
                setOrientation(getOrientation() + DEGREE_STEP);}
            }
       // }

	} //end move

	public void rotateLeft() {
		bTurningLeft = true;
        if(bThrusting){
        setOrientation(getOrientation() + DEGREE_STEP);}
        else{
            setOrientation(getOrientation() - DEGREE_STEP);
        }
        setDeltaX(0);
        setDeltaY(0);
	}

	public void rotateRight() {
		bTurningRight = true;
        if(bThrusting){
            setOrientation(getOrientation() - DEGREE_STEP);}
        else{
            setOrientation(getOrientation() + DEGREE_STEP);
        }
        //setOrientation(getOrientation() + DEGREE_STEP);
        setDeltaX(0);
        setDeltaY(0);
	}

	public void stopRotating() {
		bTurningRight = false;
		bTurningLeft = false;
	}

    public void thrustOppositeOn() {bThrustingOpposite = true;bMove = true;}

    public void thrustOppositeOff() {
        bThrustingOpposite = false;
        bFlameOpposite = false;
        setDeltaX(0);
        setDeltaY(0);
    }

	public void thrustOn() {
		bThrusting = true;

	}

	public void thrustOff() {
		bThrusting = false;
		bFlame = false;
        setDeltaX(0);
        setDeltaY(0);
	}

	private int adjustColor(int nCol, int nAdj) {
		if (nCol - nAdj <= 0) {
			return 0;
		} else {
			return nCol - nAdj;
		}
	}

	public void draw(Graphics g) {
        Color colShip;

		//does the fading at the beginning or after hyperspace
//		Color colShip;
		if (getFadeValue() == 255) {
			colShip = Color.white;
		} else {
			colShip = new Color(adjustColor(getFadeValue(), 200), adjustColor(
					getFadeValue(), 175), getFadeValue());
		}

		//shield on
		if (bShield && nShield > 0) {

			setShield(getShield() - 1);

			g.setColor(Color.GREEN);
			g.drawOval(getCenter().x - getRadius(),
					getCenter().y - getRadius(), getRadius() * 2,
					getRadius() * 2);

		} //end if shield

        if (bShield == true && nShield > 0){
            colShip = new Color(178,255,102);

        }else{
            if(nShield == 0){
                bShield = false;
                colShip = Color.WHITE;
                //Sound.playSound("ShieldOff.wav");
            }
        }
        nShield--;

		//thrusting
		if (bFlame) {
			g.setColor(colShip);
			//the flame
			for (int nC = 0; nC < FLAME.length; nC++) {
				if (nC % 2 != 0) //odd
				{
					pntFlames[nC] = new Point((int) (getCenter().x + 2
							* getRadius()
							* Math.sin(Math.toRadians(getOrientation())
									+ FLAME[nC])), (int) (getCenter().y - 2
							* getRadius()
							* Math.cos(Math.toRadians(getOrientation())
									+ FLAME[nC])));

				} else //even
				{
					pntFlames[nC] = new Point((int) (getCenter().x + getRadius()
							* 1.1
							* Math.sin(Math.toRadians(getOrientation())
									+ FLAME[nC])),
							(int) (getCenter().y - getRadius()
									* 1.1
									* Math.cos(Math.toRadians(getOrientation())
											+ FLAME[nC])));

				} //end even/odd else

			} //end for loop

			for (int nC = 0; nC < FLAME.length; nC++) {
				nXFlames[nC] = pntFlames[nC].x;
				nYFlames[nC] = pntFlames[nC].y;

			} //end assign flame points

			//g.setColor( Color.white );
			g.fillPolygon(nXFlames, nYFlames, FLAME.length);

		} //end if flame


        if (bFlameOpposite) {
            g.setColor(colShip);
            //the flame
            for (int nC = 0; nC < FLAME.length; nC++) {
                if (nC % 2 != 0) //odd
                {
                    pntFlames[nC] = new Point((int) (getCenter().x - 2
                            * getRadius()
                            * Math.sin(Math.toRadians(getOrientation())
                            + FLAME[nC])), (int) (getCenter().y + 2
                            * getRadius()
                            * Math.cos(Math.toRadians(getOrientation())
                            + FLAME[nC])));

                } else //even
                {
                    pntFlames[nC] = new Point((int) (getCenter().x - getRadius()
                            * 1.1
                            * Math.sin(Math.toRadians(getOrientation())
                            + FLAME[nC])),
                            (int) (getCenter().y + getRadius()
                                    * 1.1
                                    * Math.cos(Math.toRadians(getOrientation())
                                    + FLAME[nC])));

                } //end even/odd else

            } //end for loop

            for (int nC = 0; nC < FLAME.length; nC++) {
                nXFlames[nC] = pntFlames[nC].x;
                nYFlames[nC] = pntFlames[nC].y;

            } //end assign flame points

            //g.setColor( Color.white );
            g.fillPolygon(nXFlames, nYFlames, FLAME.length);

        } //end if flame


        drawShipWithColor(g, colShip);


	} //end draw()

	public void drawShipWithColor(Graphics g, Color col) {
		super.draw(g);
		g.setColor(col);
		g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
        if(bShield){
            g.setColor(new Color(178,255,102));
            g.fillPolygon(getXcoords(),getYcoords(),dDegrees.length);
        }
        else{
            g.setColor(new Color(255,255,0));
            g.fillPolygon(getXcoords(),getYcoords(),dDegrees.length);
        }
	}

	public void fadeInOut() {
		if (getProtected()) {
			setFadeValue(getFadeValue() + 3);
		}
		if (getFadeValue() == 255) {
			setProtected(false);
		}
	}
	
	public void setProtected(boolean bParam) {
		if (bParam) {
			setFadeValue(0);
		}
		bProtected = bParam;
	}

	public void setProtected(boolean bParam, int n) {
		if (bParam && n % 3 == 0) {
			setFadeValue(n);
		} else if (bParam) {
			setFadeValue(0);
		}
		bProtected = bParam;
	}

    public void setColor(Graphics g,Color color){
        g.setColor(color);
    }

	public boolean getProtected() {return bProtected;}
	public void setShield(int n) {nShield = n;;}
	public int getShield() {return nShield;}	
	public void ShieldOn(){bShield = true;setShield(500);}
    public void ShieldOff(){bShield = false;setProtected(false);}
    public boolean getBShield(){return bShield;};
} //end class