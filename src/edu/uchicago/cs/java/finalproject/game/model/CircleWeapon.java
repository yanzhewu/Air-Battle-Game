package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.sounds.Sound;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by wuyanzhe on 12/1/14.
 */
public class CircleWeapon extends Sprite{
    private int circle = 360;
    private ArrayList<Bullet> list;

    public CircleWeapon(){
        Sound.playSound("laser.wav");

        Bullet bullet1 = new Bullet(CommandCenter.getFalcon());
        bullet1.setDeltaX( 20);
        bullet1.setDeltaY( 20);
        bullet1.setCenter( CommandCenter.getFalcon().getCenter() );
        bullet1.setExpire(15);
        CommandCenter.movFriends.add(bullet1);

        Bullet bullet2 = new Bullet(CommandCenter.getFalcon());
        bullet2.setDeltaX( 0);
        bullet2.setDeltaY( 33);
        bullet2.setCenter( CommandCenter.getFalcon().getCenter() );
        bullet2.setExpire(15);
        CommandCenter.movFriends.add(bullet2);

        Bullet bullet3 = new Bullet(CommandCenter.getFalcon());
        bullet3.setDeltaX( 33);
        bullet3.setDeltaY( 0);
        bullet3.setCenter( CommandCenter.getFalcon().getCenter() );
        bullet3.setExpire(15);
        CommandCenter.movFriends.add(bullet3);


        Bullet bullet4 = new Bullet(CommandCenter.getFalcon());
        bullet4.setDeltaX( -33);
        bullet4.setDeltaY( 0);
        bullet4.setCenter( CommandCenter.getFalcon().getCenter() );
        bullet4.setExpire(15);
        CommandCenter.movFriends.add(bullet4);

        Bullet bullet5 = new Bullet(CommandCenter.getFalcon());
        bullet5.setDeltaX( 0);
        bullet5.setDeltaY( -33);
        bullet5.setCenter( CommandCenter.getFalcon().getCenter() );
        bullet5.setExpire(15);
        CommandCenter.movFriends.add(bullet5);

        Bullet bullet6 = new Bullet(CommandCenter.getFalcon());
        bullet6.setDeltaX( -20);
        bullet6.setDeltaY( 20);
        bullet6.setCenter( CommandCenter.getFalcon().getCenter() );
        bullet6.setExpire(15);
        CommandCenter.movFriends.add(bullet6);

        Bullet bullet7 = new Bullet(CommandCenter.getFalcon());
        bullet7.setDeltaX( 20);
        bullet7.setDeltaY( -20);
        bullet7.setCenter( CommandCenter.getFalcon().getCenter() );
        bullet7.setExpire(15);
        CommandCenter.movFriends.add(bullet7);

        Bullet bullet8 = new Bullet(CommandCenter.getFalcon());
        bullet8.setDeltaX( -20);
        bullet8.setDeltaY( -20);
        bullet8.setCenter( CommandCenter.getFalcon().getCenter() );
        bullet8.setExpire(15);
        CommandCenter.movFriends.add(bullet8);

    }

    public void expire(){
        if (getExpire() == 0){
            CommandCenter.movFriends.remove(this);
            CommandCenter.movFoes.remove(this);}
        else
            setExpire(getExpire() - 1);
    }

    public ArrayList<Bullet> getList() {
        return list;
    }
}
