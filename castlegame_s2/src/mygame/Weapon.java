/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;

/**
 *
 * @author filipe
 */
public enum Weapon {    
    MELEE(Bullet.MELEE, -1),
    RANGED(Bullet.RANGED, 50),
    MAGIC(Bullet.MAGIC, 50);
    
    private final Bullet bullet; //The prototype bullet that gets cloned
    private final int INFINITY = -1; //Used for weapons with unlimited ammo
    private int currentAmmo; //Number of bullets left
    private int maxBulletsOnScreen = 2; //Still need to find a way to limit this
    
    Weapon(Bullet bullet, int ammo) {
        this.bullet = bullet;
        this.currentAmmo = ammo;
    }
    
    public void fire(Vector3f from, Vector3f to) {
        boolean canFire = false;
        for(int i = 0; i < maxBulletsOnScreen; ++i) {
            if(Main.get().getRootNode().getChild("Bullet" + i) == null) {
                canFire = true;
            }
        }
        
        if((currentAmmo == INFINITY || currentAmmo > 0) && canFire) {
            bullet.fire(currentAmmo % maxBulletsOnScreen, from, to);
            
            if(currentAmmo != INFINITY)
                currentAmmo--;
        }
    }
    
    public void incrementAmmo(int obtained) {
        currentAmmo += obtained;
    }
}
