package mygame.weapon;

import com.jme3.math.Vector3f;
import mygame.GameEntity;
import mygame.GameLogic;

/**
 *
 * @author filipe
 */
public class Weapon extends GameEntity {

    public static final int TOTALWEAPONS = 5;

    public enum WeaponType {

        MELEE, RANGED, FIREBALL, HEALING, LIGHTING
    };
    
    private final int INFINITY = -1; //Used for weapons with unlimited ammo
    private Bullet.BULLETTYPE bullet;
    boolean isMagic = false;
    private int currentAmmo; //Number of bullets left

    public Weapon(GameLogic gl, WeaponType wtype) {
        super(gl);
        type = TYPE.Item;
        
        switch (wtype) {
            case MELEE:
                init("MELEE", Bullet.BULLETTYPE.MELEE, -1, false);
                break;
            case RANGED:
                init("RANGED", Bullet.BULLETTYPE.RANGED, 50, false);
                break;
            case FIREBALL:
                init("FIREBALL", Bullet.BULLETTYPE.FIREBALL, 50, false);
                break;
            case HEALING:
                init("HEALING", Bullet.BULLETTYPE.HEALING, 10, true);
                break;
            case LIGHTING:
                init("LIGHTING", Bullet.BULLETTYPE.LIGHTING, 10, true);
                break;

            default:
                ;
        }
        
        birth();
    }

    public final void init(String n, Bullet.BULLETTYPE b, int ammo, boolean magic) {
        setName(n);
        this.bullet = b;
        this.currentAmmo = ammo;
        this.isMagic = magic;
    }

    public void fire(Vector3f from, Vector3f to) {
        if (currentAmmo == INFINITY || currentAmmo > 0) {
            if (isMagic) {
                MagicEffect me = new MagicEffect(game, from);
            } else {
                Bullet b = new Bullet(game, bullet, from, to, isMagic);
            }

            if (currentAmmo != INFINITY) {
                currentAmmo--;
            }
        }
    }

    public void incrementAmmo(int obtained) {
        currentAmmo += obtained;
    }

    public int getCurrentAmmo() {
        return currentAmmo;
    }
    
    public boolean isMagic() {
        return isMagic;
    }
}
