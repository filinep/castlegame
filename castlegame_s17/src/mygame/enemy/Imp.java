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
public class Imp extends Enemy {
    private static int idTicker = 0;
    private static float RADIUS = 1f;
    private static float HEIGHT = 1f;
    
    public Imp(GameLogic gl, Vector3f startLoc) {
        super(gl);
        
        this.turnRate = FastMath.QUARTER_PI / 5f;
        this.walkingRange = 100f;
        this.firingRange = 30f;
        this.walkSpeed = .5f;
        this.shootRate = 1f;
        
        this.health = 115;
        this.enemyType = EnemyType.IMP;
        setName(enemyType.name().toLowerCase() + idTicker++);
        
        this.weapons.add(new Weapon(game, Weapon.WeaponType.FIREBALL));
        
        this.initModel("Models/imptrial1.j3o", RADIUS, HEIGHT);
        this.physicsControl.setPhysicsLocation(startLoc);
        physicsControl.setGravity(0f);
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
        } else {
            stop();
        }
    }
}
