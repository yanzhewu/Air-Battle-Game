package edu.uchicago.cs.java.finalproject.controller;

import sun.audio.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.Clip;

import edu.uchicago.cs.java.finalproject.game.model.*;
import edu.uchicago.cs.java.finalproject.game.view.*;
import edu.uchicago.cs.java.finalproject.sounds.Sound;

// ===============================================
// == This Game class is the CONTROLLER
// ===============================================

public class Game implements Runnable, KeyListener {

	// ===============================================
	// FIELDS
	// ===============================================

	public static final Dimension DIM = new Dimension(1100, 700); //the dimension of the game.
	private GamePanel gmpPanel;
	public static Random R = new Random();
	public final static int ANI_DELAY = 45; // milliseconds between screen
											// updates (animation)
	private Thread thrAnim;
	private int nLevel = 1;
	private int nTick = 0;
	private ArrayList<Tuple> tupMarkForRemovals;
	private ArrayList<Tuple> tupMarkForAdds;
	private boolean bMuted = true;
	private boolean keepFire = false;

	private final int PAUSE = 80, // p key
			QUIT = 81, // q key
			LEFT = 37, // rotate left; left arrow
			RIGHT = 39, // rotate right; right arrow
			UP = 38, // thrust; up arrow
            DOWN = 40,//opposite thrust; down arrow
			START = 83, // s key
			FIRE = 32, // space key
			MUTE = 77, // m-key mute

	// for possible future use
	// HYPER = 68, 					// d key
	   SHIELD = 65, 				// a key arrow
	// NUM_ENTER = 10, 				// hyp
	 SPECIAL = 70; 					// fire special weapon;  F key

	private Clip clpThrust;
	private Clip clpMusicBackground;
    private Clip clpMusicUFO;

	private static final int SPAWN_NEW_SHIP_FLOATER = 1200;
    private static final int SPAWN_SHIELD_FLOATER = 800;



	// ===============================================
	// ==CONSTRUCTOR
	// ===============================================

	public Game() {

		gmpPanel = new GamePanel(DIM);
		gmpPanel.addKeyListener(this);

		clpThrust = Sound.clipForLoopFactory("whitenoise.wav");
		clpMusicBackground = Sound.clipForLoopFactory("music-background.wav");
        clpMusicUFO = Sound.clipForLoopFactory("alert.wav");

	}

	// ===============================================
	// ==METHODS
	// ===============================================

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() { // uses the Event dispatch thread from Java 5 (refactored)
					public void run() {
						try {
							Game game = new Game(); // construct itself
							game.fireUpAnimThread();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void fireUpAnimThread() { // called initially
		if (thrAnim == null) {
			thrAnim = new Thread(this); // pass the thread a runnable object (this)
            thrAnim.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {

                }
            });
			thrAnim.start();
		}
	}

	// implements runnable - must have run method
	public void run() {

		// lower this thread's priority; let the "main" aka 'Event Dispatch'
		// thread do what it needs to do first
		thrAnim.setPriority(Thread.MIN_PRIORITY);

		// and get the current time
		long lStartTime = System.currentTimeMillis();

		// this thread animates the scene
		while (Thread.currentThread() == thrAnim) {
			tick();
			spawnNewShipFloater();
            spawnShieldFloater();
			gmpPanel.update(gmpPanel.getGraphics()); // update takes the graphics context we must 
														// surround the sleep() in a try/catch block
														// this simply controls delay time between 
														// the frames of the animation

			//this might be a good place to check for collisions
			checkCollisions();
			//this might be a god place to check if the level is clear (no more foes)
			//if the level is clear then spawn some big asteroids -- the number of asteroids 
			//should increase with the level. 
			checkNewLevel();
            checkDebris();
            UFOfire();
            MissileFly();
            keepFire();
			try {
				// The total amount of time is guaranteed to be at least ANI_DELAY long.  If processing (update) 
				// between frames takes longer than ANI_DELAY, then the difference between lStartTime - 
				// System.currentTimeMillis() will be negative, then zero will be the sleep time
				lStartTime += ANI_DELAY;
				Thread.sleep(Math.max(0,
						lStartTime - System.currentTimeMillis()));
			} catch (InterruptedException e) {
				// just skip this frame -- no big deal
				continue;
			}
		} // end while
	} // end run

    public void keepFire(){
        if(keepFire){
            if(getTick() % 2 == 0){
                CommandCenter.movFriends.add(new Bullet(CommandCenter.getFalcon()));
                //Sound.playSound("laser.wav");
                Sound.playSound("Fire.wav");
            }
        }
    }

    public void MissileFly(){
        for(Movable movable : CommandCenter.movFriends){
            if(movable instanceof  Missile){
                movable.move();
            }
        }
    }

    private void checkDebris(){

    }

    private void UFOfire(){
        for(Movable movFoe : CommandCenter.movFoes){
            if(movFoe instanceof UFO){
                if(getTick()%15 == 0){
                    CommandCenter.movFoes.add(new Bullet((UFO)movFoe));
                }
                }
            }
        }


	private void checkCollisions() {

		
		//@formatter:off
		//for each friend in movFriends
			//for each foe in movFoes
				//if the distance between the two centers is less than the sum of their radii
					//mark it for removal
		
		//for each mark-for-removal
			//remove it
		//for each mark-for-add
			//add it
		//@formatter:on
		
		//we use this ArrayList to keep pairs of movMovables/movTarget for either
		//removal or insertion into our arrayLists later on
		tupMarkForRemovals = new ArrayList<Tuple>();
		tupMarkForAdds = new ArrayList<Tuple>();

		Point pntFriendCenter, pntFoeCenter;
		int nFriendRadiux, nFoeRadiux;

		for (Movable movFriend : CommandCenter.movFriends) {
			for (Movable movFoe : CommandCenter.movFoes) {

				pntFriendCenter = movFriend.getCenter();
				pntFoeCenter = movFoe.getCenter();
				nFriendRadiux = movFriend.getRadius();
				nFoeRadiux = movFoe.getRadius();

				//detect collision
				if (pntFriendCenter.distance(pntFoeCenter) < (nFriendRadiux + nFoeRadiux)) {

					//falcon
					if ((movFriend instanceof Falcon) ){
						if (!CommandCenter.getFalcon().getProtected() && !CommandCenter.getFalcon().getBShield()){
							tupMarkForRemovals.add(new Tuple(CommandCenter.movFriends, movFriend));
							CommandCenter.spawnFalcon(false);
							killFoe(movFoe);
                            //Sound.playSound("Slab.wav");
						}
                        else{
                            CommandCenter.setScore(CommandCenter.getScore() + 1);
                            killFoe(movFoe);
                            //Sound.playSound("Slab.wav");
                        }
					}
					//not the falcon
					else {
                        CommandCenter.setScore(CommandCenter.getScore()+1);
						tupMarkForRemovals.add(new Tuple(CommandCenter.movFriends, movFriend));
						killFoe(movFoe);
                        //Sound.playSound("Slab.wav");
					}//end else 

					//explode/remove foe
					
					
				
				}//end if 
			}//end inner for
		}//end outer for


		//check for collisions between falcon and floaters
		if (CommandCenter.getFalcon() != null){
			Point pntFalCenter = CommandCenter.getFalcon().getCenter();
			int nFalRadiux = CommandCenter.getFalcon().getRadius();
			Point pntFloaterCenter;
			int nFloaterRadiux;
			
			for (Movable movFloater : CommandCenter.movFloaters) {
				pntFloaterCenter = movFloater.getCenter();
				nFloaterRadiux = movFloater.getRadius();
	
				//detect collision
				if (pntFalCenter.distance(pntFloaterCenter) < (nFalRadiux + nFloaterRadiux)) {
                    if(movFloater instanceof NewShipFloater){
                        CommandCenter.setNumFalcons(CommandCenter.getNumFalcons() + 1);
                    }
                    if(movFloater instanceof  ShieldFloater){
                        CommandCenter.getFalcon().ShieldOn();
                    }
                    tupMarkForRemovals.add(new Tuple(CommandCenter.movFloaters, movFloater));
					Sound.playSound("pacman_eatghost.wav");
				}
		      //end if
			}//end inner for
		}//end if not null
		
		//remove these objects from their appropriate ArrayLists
		//this happens after the above iterations are done
		for (Tuple tup : tupMarkForRemovals) 
			tup.removeMovable();
		
		//add these objects to their appropriate ArrayLists
		//this happens after the above iterations are done
		for (Tuple tup : tupMarkForAdds) 
			tup.addMovable();

		//call garbage collection
		System.gc();
		
	}//end meth

	private void killFoe(Movable movFoe) {
		
		if (movFoe instanceof Asteroid){

			//we know this is an Asteroid, so we can cast without threat of ClassCastException
			Asteroid astExploded = (Asteroid)movFoe;
			//big asteroid 
			if(astExploded.getSize() == 0){
				//spawn two medium Asteroids
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
			} 
			//medium size aseroid exploded
			else{
                if(astExploded.getSize() == 1){
				//spawn three small Asteroids
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
                }
                else{
                    int num = astExploded.getXcoords().length;
                    for(int i=0;i<num-1;i++) {
                        tupMarkForAdds.add(new Tuple(CommandCenter.movDebris, new Asteroid(astExploded, new Point(astExploded.getXcoord(i), astExploded.getYcoord(i)), new Point(astExploded.getXcoord(i+1), astExploded.getYcoord(i+1)))));
                    }
                    tupMarkForAdds.add(new Tuple(CommandCenter.movDebris, new Asteroid(astExploded, new Point(astExploded.getXcoord(num-2), astExploded.getYcoord(num-2)), new Point(astExploded.getXcoord(num-1), astExploded.getYcoord(num-1)))));
                }
            }
			//remove the original Foe	
			tupMarkForRemovals.add(new Tuple(CommandCenter.movFoes, movFoe));
		    Sound.playSound("Slap.wav");
			
		} 
		//not an asteroid
        else{
            if(movFoe instanceof UFO){
                if(((UFO) movFoe).getnLife() > 1){
                    ((UFO) movFoe).setnLife(((UFO) movFoe).getnLife() - 1);
                }
                else{
                    if(((UFO) movFoe).getnLife() == 1){
                        tupMarkForRemovals.add(new Tuple(CommandCenter.movFoes,movFoe));
                        Sound.playSound("Bomb.wav");
                        if(getTick() % 3 == 0){
                            CommandCenter.movFloaters.add(new ShieldFloater(movFoe));
                        }
                        else{
                            CommandCenter.movFloaters.add(new NewShipFloater(movFoe));
                        }
                    }
                }
            }
            else {
                //remove the original Foe
                tupMarkForRemovals.add(new Tuple(CommandCenter.movFoes, movFoe));
            }
        }

		
		
		

		
		
		
		
	}

	//some methods for timing events in the game,
	//such as the appearance of UFOs, floaters (power-ups), etc. 
	public void tick() {
		if (nTick == Integer.MAX_VALUE)
			nTick = 0;
		else
			nTick++;
	}

	public int getTick() {
		return nTick;
	}

	private void spawnNewShipFloater() {
		//make the appearance of power-up dependent upon ticks and levels
		//the higher the level the more frequent the appearance
		if (nTick % (SPAWN_NEW_SHIP_FLOATER - nLevel * 300) == 0) {
			CommandCenter.movFloaters.add(new NewShipFloater());
		}
	}

    private void spawnShieldFloater(){
        if(nTick % (SPAWN_SHIELD_FLOATER - nLevel *300) == 0){
            CommandCenter.movFloaters.add(new ShieldFloater());
        }
    }



	// Called when user presses 's'
	private void startGame() {
		CommandCenter.clearAll();
		CommandCenter.initGame();
		CommandCenter.setLevel(0);
		CommandCenter.setPlaying(true);
		CommandCenter.setPaused(false);
		//if (!bMuted)
		   // clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
	}

	//this method spawns new asteroids
	private void spawnAsteroids(int nNum) {
		for (int nC = 0; nC < nNum; nC++) {
			//Asteroids with size of zero are big
			CommandCenter.movFoes.add(new Asteroid(0));
		}
	}

    private void spawnUFOs(int nNum){
        for (int nC = 0; nC < nNum; nC++) {
            //Asteroids with size of zero are big
            UFO ufo = new UFO(0);
            CommandCenter.movFoes.add(ufo);
            //CommandCenter.movFoes.add(new Bullet(ufo));
        }
    }

    private void spawnMissiles(int nNum){
        int num = CommandCenter.getMovFoes().size();
        for(int nC = 0; nC < nNum; nC++){
            if(num >= 1){
            Missile missile = new Missile(CommandCenter.getMovFoes().get(num-1),CommandCenter.getFalcon());
            CommandCenter.movFriends.add(missile);
                num--;
            }
        }
    }
	
	
	private boolean isLevelClear(){
		//if there are no more Asteroids on the screen
		
		boolean bAsteroidFree = true;
		for (Movable movFoe : CommandCenter.movFoes) {
			if (movFoe instanceof Asteroid){
				bAsteroidFree = false;
				break;
			}
		}
		
		return bAsteroidFree;

		
	}
	
	private void checkNewLevel(){
		if (isLevelClear() ){
			if (CommandCenter.getFalcon() !=null){
				CommandCenter.getFalcon().setProtected(true);
            }
			spawnAsteroids(CommandCenter.getLevel());
			CommandCenter.setLevel(CommandCenter.getLevel() + 1);
            if(CommandCenter.getLevel() == 1){
                spawnAsteroids(2);
            }else{
            if(CommandCenter.getLevel() == 2){
                Sound.playSound("alert.wav");
                spawnUFOs(CommandCenter.getLevel() - 1);
            }
            else{
                spawnUFOs(2);
            }
            }

		}
	}


	
	
	

	// Varargs for stopping looping-music-clips
	private static void stopLoopingSounds(Clip... clpClips) {
		for (Clip clp : clpClips) {
			clp.stop();
		}
	}

	// ===============================================
	// KEYLISTENER METHODS
	// ===============================================

	@Override
	public void keyPressed(KeyEvent e) {
		Falcon fal = CommandCenter.getFalcon();
		int nKey = e.getKeyCode();
		// System.out.println(nKey);

		if (nKey == START && !CommandCenter.isPlaying())
			startGame();

		if (fal != null) {

			switch (nKey) {
                case FIRE:
                    //CommandCenter.movFriends.add(new Bullet(fal));
                    //Sound.playSound("laser.wav");
                    Sound.playSound("Fire.wav");
                    keepFire = true;
                    break;

                case PAUSE:
				CommandCenter.setPaused(!CommandCenter.isPaused());
				if (CommandCenter.isPaused())
					stopLoopingSounds(clpMusicBackground, clpThrust);
				else
					clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
				break;

                case QUIT:
				System.exit(0);
				break;

                case UP:
				fal.thrustOn();
				if (!CommandCenter.isPaused())
					clpThrust.loop(Clip.LOOP_CONTINUOUSLY);
				break;

                case DOWN:
                fal.thrustOppositeOn();
                if (!CommandCenter.isPaused())
                    clpThrust.loop(Clip.LOOP_CONTINUOUSLY);
                break;

                case LEFT:
				fal.rotateLeft();
				break;

                case RIGHT:
				fal.rotateRight();
				break;

			// possible future use
			// case KILL:

                case SHIELD:
                fal.ShieldOn();
                break;
			// case NUM_ENTER:


                default:
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Falcon fal = CommandCenter.getFalcon();
		int nKey = e.getKeyCode();
        System.out.println(nKey);

		if (fal != null) {
			switch (nKey) {
			case FIRE:
//				CommandCenter.movFriends.add(new Bullet(fal));
//				//Sound.playSound("laser.wav");
//                Sound.playSound("Fire.wav");
                keepFire = false;
				break;
				
			//special is a special weapon, current it just fires the cruise missile. 
			case SPECIAL:
//                int num = CommandCenter.getFalcon().getXcoords().length;
//                for(int i=0;i<num-1;i++) {
//                    CommandCenter.movFriends.add(new Cruise(fal, new Point(fal.getXcoord(i), fal.getYcoord(i)), new Point(fal.getXcoord(i+1), fal.getYcoord(i+1))));
//                }
				//CommandCenter.movFriends.add(new Cruise(fal));
                spawnMissiles(3);
                Sound.playSound("Missile.wav");
//				Sound.playSound("laser.wav");
				break;
				
			case LEFT:
				fal.stopRotating();
				break;
			case RIGHT:
				fal.stopRotating();
				break;
			case UP:
				fal.thrustOff();
				clpThrust.stop();
				break;
            case DOWN:
                fal.thrustOppositeOff();
                clpThrust.stop();
                break;
			case MUTE:
				if (!bMuted){
					stopLoopingSounds(clpMusicBackground);
					bMuted = !bMuted;
				} 
				else {
					clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
					bMuted = !bMuted;
				}
				break;
				
				
			default:
				break;
			}
		}
	}

	@Override
	// Just need it b/c of KeyListener implementation
	public void keyTyped(KeyEvent e) {
	}
	

	
}

// ===============================================
// ==A tuple takes a reference to an ArrayList and a reference to a Movable
//This class is used in the collision detection method, to avoid mutating the array list while we are iterating
// it has two public methods that either remove or add the movable from the appropriate ArrayList 
// ===============================================

class Tuple{
	//this can be any one of several CopyOnWriteArrayList<Movable>
	private CopyOnWriteArrayList<Movable> movMovs;
	//this is the target movable object to remove
	private Movable movTarget;
	
	public Tuple(CopyOnWriteArrayList<Movable> movMovs, Movable movTarget) {
		this.movMovs = movMovs;
		this.movTarget = movTarget;
	}
	
	public void removeMovable(){
		movMovs.remove(movTarget);
	}
	
	public void addMovable(){
		movMovs.add(movTarget);
	}

}