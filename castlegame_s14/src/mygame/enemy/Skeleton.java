package mygame.enemy;

import com.jme3.math.Vector3f;
import mygame.GameLogic;
import mygame.weapon.Weapon;

/**
 *
 * @author filipe
 */
public class Skeleton extends Enemy {
    private static int idTicker = 0;
    private static float RADIUS = 1f;
    private static float HEIGHT = 5f;
    
    public Skeleton(GameLogic gl, Vector3f startLoc) {
        super(gl);
        
        this.health = 75;
        this.enemyType = EnemyType.SKELETON;
        setName(enemyType.name().toLowerCase() + idTicker++);
        
        this.weapons.add(new Weapon(game, Weapon.WeaponType.MELEE));
        
        this.initModel("Models/skele11/skele11.j3o", RADIUS, HEIGHT);
        this.physicsControl.setPhysicsLocation(startLoc);
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
    }
}
