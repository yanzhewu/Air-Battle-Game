package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;
import java.util.ArrayList;

import com.sun.javafx.geom.transform.BaseTransform;
import edu.uchicago.cs.java.finalproject.controller.Game;


public class Bullet extends Sprite {

	  private final double FIRE_POWER = 35.0;
      private final double SLOW_FIRE_POWER = 5.0;
	  private int bad;
	
public Bullet(Falcon fal){
		
		super();
		
		bad = 0;
		//defined the points on a cartesean grid
		ArrayList<Point> pntCs = new ArrayList<Point>();
		
//		pntCs.add(new Point(0,3)); //top point
//
//		pntCs.add(new Point(1,-1));
//		pntCs.add(new Point(0,-2));
//		pntCs.add(new Point(-1,-1));
//
//		assignPolarPoints(pntCs);

		//a bullet expires after 20 frames
	    setExpire( 20 );
	    setRadius(8);
	    setColor(Color.GREEN);

	    //everything is relative to the falcon ship that fired the bullet
	    setDeltaX( fal.getDeltaX() +
	               Math.cos( Math.toRadians( fal.getOrientation() ) ) * FIRE_POWER );
	    setDeltaY( fal.getDeltaY() +
	               Math.sin( Math.toRadians( fal.getOrientation() ) ) * FIRE_POWER );
	    setCenter( fal.getCenter() );

	    //set the bullet orientation to the falcon (ship) orientation
	    setOrientation(fal.getOrientation());


	}

    public Bullet(UFO ufo){
        bad = 1;
        setDim(Game.DIM);
        setColor(Color.RED);
        setCenter(new Point(Game.R.nextInt(Game.DIM.width),
                Game.R.nextInt(Game.DIM.height)));
        //defined the points on a cartesean grid
        ArrayList<Point> pntCs = new ArrayList<Point>();

        pntCs.add(new Point(0,3)); //top point
        pntCs.add(new Point(1,2));
        pntCs.add(new Point(2,2));
        pntCs.add(new Point(2,1));
        pntCs.add(new Point(3,0));
        pntCs.add(new Point(2,-1));
        pntCs.add(new Point(2,-1));
        pntCs.add(new Point(1,-2));
        pntCs.add(new Point(0,-3));

        pntCs.add(new Point(-1,-2)); //top point
        pntCs.add(new Point(-2,-2));
        pntCs.add(new Point(-2,-1));
        pntCs.add(new Point(-3,0));
        pntCs.add(new Point(-2,1));
        pntCs.add(new Point(-2,2));
        pntCs.add(new Point(-1,2));

        assignPolarPoints(pntCs);

        //a bullet expires after 20 frames
        setExpire(60);
        setRadius(9);
        setColor(new Color(255,0,127));

        //everything is relative to the falcon ship that fired the bullet
        setDeltaX( ufo.getDeltaX() +
                Math.cos( Math.toRadians( ufo.getOrientation() ) ) * SLOW_FIRE_POWER );
        setDeltaY( ufo.getDeltaY() +
                Math.sin( Math.toRadians( ufo.getOrientation() ) ) * SLOW_FIRE_POWER );
        setCenter( ufo.getCenter() );

        //set the bullet orientation to the falcon (ship) orientation
        setOrientation(ufo.getOrientation());
    }

    //override the expire method - once an object expires, then remove it from the arrayList. 
	public void expire(){
 		if (getExpire() == 0){
 			CommandCenter.movFriends.remove(this);
            CommandCenter.movFoes.remove(this);}
		 else 
			setExpire(getExpire() - 1);
	}

    @Override
    public void draw(Graphics g) {
        if(bad == 0){
        g.setColor(new Color(204,153,255));
        g.drawOval(getCenter().x - getRadius(),
                getCenter().y - getRadius(), getRadius() * 2,
                getRadius() * 2);
        g.fillOval(getCenter().x - getRadius(),
                getCenter().y - getRadius(), getRadius() * 2,
                getRadius() * 2);
        }
        else{
            super.draw(g);
            setColor(new Color(255,0,127));
            g.fillPolygon(getXcoords(),getYcoords(), dDegrees.length);
        }
    }

}
