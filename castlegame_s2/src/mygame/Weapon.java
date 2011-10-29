/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author filipe
 */
public enum Weapon {    
    MELEE(Bullet.MELEE, -1),
    RANGED(Bullet.RANGED, 50),
    MAGIC(Bullet.MAGIC, 50);
    
    private final Bullet bullet;
    private final int INFINITY = -1;
    private int currentAmmo;
    private int maxBulletsOnScreen;
    
    Weapon(Bullet bullet, int ammo) {
        this.bullet = bullet;
        this.currentAmmo = ammo;
    }
    
    public void fire() {
        if(currentAmmo == INFINITY || currentAmmo-- > 0) {
            bullet.fire();
        }
    }
    
    public void incrementAmmo(int obtained) {
        currentAmmo += obtained;
    }
}
