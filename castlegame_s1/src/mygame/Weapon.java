/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author scott
 */
public class Weapon {
    public enum weapon{sword, bow};
    
    weapon type;
    String name = "";
    boolean inPossession = true;
    int ammo = 0;
    float linearVelocity;
    int counter;
    
    

    public Weapon(weapon w, String n, boolean p, int a, float lv, int c) {
        type = w;
        name = n;
        inPossession = p;
        ammo = a;
        linearVelocity = lv;
        counter = c;
    }

    public weapon getType() {
        return type;
    }

    public void setType(weapon type) {
        this.type = type;
    }
    
    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public boolean isInPossession() {
        return inPossession;
    }

    public void setInPossession(boolean inPossession) {
        this.inPossession = inPossession;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public float getLinearVelocity() {
        return linearVelocity;
    }

    public void setLinearVelocity(float linearVelocity) {
        this.linearVelocity = linearVelocity;
    }
    
    
    
    
}
