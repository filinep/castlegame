package mygame.enemy;

import com.jme3.math.Vector3f;
import mygame.GameLogic;
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
        
        this.health = 115;
        this.enemyType = EnemyType.IMP;
        setName(enemyType.name().toLowerCase() + idTicker++);
        
        this.weapons.add(new Weapon(game, Weapon.WeaponType.MELEE));
        
        this.initModel("Models/imptrial1.j3o", RADIUS, HEIGHT);
        this.physicsControl.setPhysicsLocation(startLoc);
    }

    @Override
    public void update(float tpf) {
        
    }
}
