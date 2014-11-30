package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.controller.Game;

import java.awt.*;

/**
 * Created by wuyanzhe on 11/24/14.
 */
public class UFO extends Sprite {
    //level of UFOs
    private int nLevel;
    private final int R = 37;
    private int nSpin;
    private int nLife;

    public UFO(int nLevel){
        setnLife(4);
        setDim(Game.DIM);
        setColor(Color.pink);
        setCenter(new Point(Game.R.nextInt(Game.DIM.width),
                Game.R.nextInt(Game.DIM.height)));
        //the spin will be either plus or minus 0-9
        int nSpin = Game.R.nextInt(10);
        if(nSpin %2 ==0)
            nSpin = -nSpin;
        setSpin(nSpin);

        //random delta-x
        int nDX = 1 + Game.R.nextInt(5);
        if(nDX %2 ==0)
            nDX = -nDX;
        setDeltaX(nDX);

        //random delta-y
        int nDY = 1 + Game.R.nextInt(5);
        if(nDY %2 ==0)
            nDY = -nDY;
        setDeltaY(nDY);
        if(nLevel == 0){
            setColor(Color.pink);
        }
        else{
            setColor(Color.GREEN);
        }
        setRadius(R);
    }

    public void setSpin(int nSpin) {
        this.nSpin = nSpin;
    }

    public void expire(){
        if (getExpire() == 0)
            CommandCenter.movDebris.remove(this);
        else
            setExpire(getExpire() - 1);
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

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.pink);
        g.drawOval(getCenter().x - getRadius(),
                getCenter().y - getRadius(), getRadius() * 2,
                getRadius() * 2);
        g.fillOval(getCenter().x - getRadius(),
                getCenter().y - getRadius(), getRadius() * 2,
                getRadius() * 2);
    }

    public int getnLife() {
        return nLife;
    }

    public void setnLife(int nLife) {
        this.nLife = nLife;
    }
}
