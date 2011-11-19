/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

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
    private int maxBulletsOnScreen = 100;

    Weapon(GameLogic gl, WeaponType wtype) {
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

    public void init(String n, Bullet.BULLETTYPE b, int ammo, boolean magic) {
        setName(n);
        this.bullet = b;
        this.currentAmmo = ammo;
        this.isMagic = magic;
    }

    public void fire(Vector3f from, Vector3f to) {
        if (isMagic) {
            MagicEffect me = new MagicEffect(game, from);
        } else {
            game.get_audio_attack().setLocalTranslation(from);
            game.get_audio_attack().play();
            
            boolean canFire = false;
            //This determines if there are any bullets currently on the screen

            //TODO: might not need this
            for (int i = 0; i < maxBulletsOnScreen; ++i) {
                if (Main.get().getRootNode().getChild("Bullet" + i) == null) {
                    canFire = true;
                }
            }

            if ((currentAmmo == INFINITY || currentAmmo > 0) && canFire) {
                Bullet b = new Bullet(game, bullet, currentAmmo % maxBulletsOnScreen,
                        from, to, isMagic);

                if (currentAmmo != INFINITY) {
                    currentAmmo--;
                }
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
