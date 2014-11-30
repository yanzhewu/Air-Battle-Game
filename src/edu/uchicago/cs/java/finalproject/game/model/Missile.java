package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.controller.Game;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by wuyanzhe on 11/29/14.
 */
public class Missile extends Sprite {
    private final double FIRE_POWER = 8.0;
    private Movable target;
    private Point lastPosition;

    public Missile(Movable target,Falcon falcon){

        setDim(Game.DIM);
        setColor(Color.ORANGE);
        setCenter(falcon.getCenter());


        //defined the points on a cartesean grid
        ArrayList<Point> pntCs = new ArrayList<Point>();

        pntCs.add(new Point(0,30)); //top point

        pntCs.add(new Point(10,20));
        pntCs.add(new Point(10,0));
        pntCs.add(new Point(0,-30));
        pntCs.add(new Point(-10,0));
        pntCs.add(new Point(-10,20));



        assignPolarPoints(pntCs);

        //a bullet expires after 20 frames
        setExpire( 50 );
        setRadius(20);
        setColor(Color.ORANGE);

        this.target = target;
        //everything is relative to the falcon ship that fired the bullet
//        setDeltaX( fal.getDeltaX() +
//                Math.cos( Math.toRadians( fal.getOrientation() ) ) * FIRE_POWER );
//        setDeltaY( fal.getDeltaY() +
//                Math.sin( Math.toRadians( fal.getOrientation() ) ) * FIRE_POWER );
//        setCenter( fal.getCenter() );

        //set the bullet orientation to the falcon (ship) orientation
        //setOrientation(fal.getOrientation());
        this.lastPosition = new Point(0,0);
    }

    public void move() {
        Point pnt = getCenter();
        Point destination = target.getCenter();

        setDeltaX(Math.cos(Math.toRadians(getOrientation()))*FIRE_POWER);
        setDeltaY(Math.sin(Math.toRadians(getOrientation()))*FIRE_POWER);

        double dX = pnt.x + getDeltaX();
        double dY = pnt.y + getDeltaY();

        if (pnt.x < destination.x && pnt.y > destination.y){
            double orientation =
                    Math.toDegrees(Math.atan((pnt.y-destination.y)/(destination.x-pnt.x)));
            setOrientation(360 - (int)orientation);
        }
        else if (pnt.x < destination.x && pnt.y < destination.y){
            double orientation =
                    Math.toDegrees(Math.atan((pnt.y-destination.y)/(pnt.x-destination.x)));
            setOrientation((int)orientation);
        }
        else if (pnt.x > destination.x && pnt.y < destination.y){
            double orientation =
                    Math.toDegrees(Math.atan((destination.y-pnt.y)/(pnt.x-destination.x)));
            setOrientation(180 - (int)orientation);
        }
        else if (pnt.x != destination.x) {
            double orientation =
                    Math.toDegrees(Math.atan((pnt.y-destination.y)/(pnt.x-destination.x)));
            setOrientation(180 + (int)orientation);
        };
        setCenter(new Point((int) dX, (int) dY));
    }

    public Movable getTarget() {
        return target;
    }

    public void setTarget(Movable target) {
        this.target = target;
    }

    public void expire(){
        if (getExpire() == 0){
            CommandCenter.movFriends.remove(this);
            CommandCenter.movFoes.remove(this);}
        else
            setExpire(getExpire() - 1);
    }

    @Override
    public void draw(Graphics g) {

        super.draw(g);
        //fill this polygon (with whatever color it has)
        g.setColor(Color.ORANGE);
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        //now draw a white border

        g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
    }
}
