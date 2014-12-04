package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.controller.Game;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by wuyanzhe on 12/3/14.
 */
public class CircleWeaponFloater extends Sprite{

    private int nSpin;

    public CircleWeaponFloater() {

        super();

        ArrayList<Point> pntCs = new ArrayList<Point>();
        pntCs.add(new Point(5, 5));
        pntCs.add(new Point(4,0));
        pntCs.add(new Point(5, -5));
        pntCs.add(new Point(0,-4));
        pntCs.add(new Point(-5, -5));
        pntCs.add(new Point(-4,0));
        pntCs.add(new Point(-5, 5));
        pntCs.add(new Point(0,4));

        assignPolarPoints(pntCs);


        setExpire(250);
        setRadius(50);
        setColor(Color.ORANGE);


        int nX = Game.R.nextInt(10);
        int nY = Game.R.nextInt(10);
        int nS = Game.R.nextInt(5);

        //set random DeltaX
        if (nX % 2 == 0)
            setDeltaX(nX);
        else
            setDeltaX(-nX);

        //set rnadom DeltaY
        if (nY % 2 == 0)
            setDeltaX(nY);
        else
            setDeltaX(-nY);

        //set random spin
        if (nS % 2 == 0)
            setSpin(nS);
        else
            setSpin(-nS);

        //random point on the screen
        setCenter(new Point(Game.R.nextInt(Game.DIM.width),
                Game.R.nextInt(Game.DIM.height)));

        //random orientation
        setOrientation(Game.R.nextInt(360));

    }

    public CircleWeaponFloater(Movable movable) {

        //super();

        ArrayList<Point> pntCs = new ArrayList<Point>();
        pntCs.add(new Point(5, 5));
        pntCs.add(new Point(4,0));
        pntCs.add(new Point(5, -5));
        pntCs.add(new Point(0,-4));
        pntCs.add(new Point(-5, -5));
        pntCs.add(new Point(-4,0));
        pntCs.add(new Point(-5, 5));
        pntCs.add(new Point(0,4));

        assignPolarPoints(pntCs);

        setExpire(250);
        setRadius(50);
        setColor(Color.ORANGE);


        int nX = Game.R.nextInt(10);
        int nY = Game.R.nextInt(10);
        int nS = Game.R.nextInt(5);

        //set random DeltaX
        if (nX % 2 == 0)
            setDeltaX(nX);
        else
            setDeltaX(-nX);

        //set rnadom DeltaY
        if (nY % 2 == 0)
            setDeltaX(nY);
        else
            setDeltaX(-nY);

        //set random spin
        if (nS % 2 == 0)
            setSpin(nS);
        else
            setSpin(-nS);

        //random point on the screen
        setCenter(movable.getCenter());

        //random orientation
        setOrientation(Game.R.nextInt(360));

    }

    public void move() {
        super.move();

        setOrientation(getOrientation() + getSpin());

    }

    public int getSpin() {
        return this.nSpin;
    }

    public void setSpin(int nSpin) {
        this.nSpin = nSpin;
    }

    //override the expire method - once an object expires, then remove it from the arrayList.
    @Override
    public void expire() {
        if (getExpire() == 0)
            CommandCenter.movFloaters.remove(this);
        else
            setExpire(getExpire() - 1);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.setColor(new Color(255,153,204));
        g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
        //super.draw(g);

        //g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
//        g.drawOval(getCenter().x - getRadius(),
//                getCenter().y - getRadius(), getRadius() * 2,
//                getRadius() * 2);
        //fill this polygon (with whatever color it has)
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        //now draw a white border
//        g.fillOval(getCenter().x - getRadius(),
//                getCenter().y - getRadius(), getRadius() * 2,
//                getRadius() * 2);
        //g.setColor(Color.WHITE);
        //g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
    }
}
