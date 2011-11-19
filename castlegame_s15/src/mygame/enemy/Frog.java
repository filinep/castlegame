package mygame.enemy;

import com.jme3.math.Vector3f;
import mygame.GameLogic;
import mygame.weapon.Weapon;

/**
 *
 * @author filipe
 */
public class Frog extends Enemy {
    private static int idTicker = 0;
    private static float RADIUS = 1f;
    private static float HEIGHT = 5f;
    
    public Frog(GameLogic gl, Vector3f startLoc) {
        super(gl);
        
        this.health = 15;
        this.enemyType = EnemyType.FROG;
        setName(enemyType.name().toLowerCase() + idTicker++);
        
        this.weapons.add(new Weapon(game, Weapon.WeaponType.MELEE));
        
        this.initModel("Models/frog.j3o", RADIUS, HEIGHT);
        this.physicsControl.setPhysicsLocation(startLoc);
    }
    
    @Override
    public void update(float tpf) {
        
    }
}
