package mygame.enemy;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import mygame.GameLogic;
import mygame.Player;
import mygame.weapon.Weapon;

/**
 *
 * @author filipe
 */
public class Frog extends Enemy {
    private static int idTicker = 0;
    private static float RADIUS = .5f;
    private static float HEIGHT = 1f;
    
    public Frog(GameLogic gl, Vector3f startLoc) {
        super(gl);
        
        this.turnRate = FastMath.QUARTER_PI / 5f;
        this.walkingRange = 50f;
        this.firingRange = 5f;
        this.walkSpeed = .25f;
        this.shootRate = 1f;
        
        this.health = 15;
        this.enemyType = EnemyType.FROG;
        setName(enemyType.name().toLowerCase() + idTicker++);
        
        this.weapons.add(new Weapon(game, Weapon.WeaponType.MELEE));
        
        this.initModel("Models/frog.j3o", RADIUS, HEIGHT);
        this.physicsControl.setPhysicsLocation(startLoc);
        this.physicsControl.setGravity(100f);
    }
    
    @Override
    public void update(float tpf) {
        // TODO maybe change this to target
        Player player = game.getPlayer();
        
        shootUpdate(tpf);
        
        if (withinRange(walkingRange, player)) {
            turningTo(player.getLocation());
            
            if (withinRange(firingRange, player)) {
                stop();
                attack();
            } else {
                walkForward();
            }
            
            if (physicsControl.onGround()) {
                jump();
            }
        } else {
            stop();
        }
    }
}
