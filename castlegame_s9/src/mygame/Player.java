/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author filipe
 */
public class Player extends GameEntity implements ActionListener {

    private PhysicsCharacter physicsControl;
    private boolean left = false, right = false, up = false, down = false;
    private int activeWeapon;
    private List<Weapon> weapons = new ArrayList<Weapon>();
    private Vector3f walkDirection;
    private int health;
    private final int MAGICEFFECT_MAX = 10;
    private MagicEffect[] effect = new MagicEffect[MAGICEFFECT_MAX];
    private int currentEffect = -1;
    
    private int lightCounter = 0;
    
    public Player(GameLogic gl, Spatial startLoc) {
        super (gl);
        type = TYPE.Player;
        this.physicsControl = new PhysicsCharacter(new CapsuleCollisionShape(1f, 5f), .01f);
        this.physicsControl.setJumpSpeed(20);
        this.physicsControl.setFallSpeed(30);
        this.physicsControl.setGravity(30);
        this.physicsControl.setPhysicsLocation(startLoc.getWorldTranslation());
        Camera cam = Main.get().getCamera();
        float[] angles = {0f, FastMath.HALF_PI, 0f};
        cam.setRotation(new Quaternion(angles));

        this.walkDirection = new Vector3f();
        weapons.add(new Weapon(game, Weapon.WeaponType.RANGED));
        weapons.add(new Weapon(game, Weapon.WeaponType.MELEE));
        weapons.add(new Weapon(game, Weapon.WeaponType.FIREBALL));
        weapons.add(new Weapon(game, Weapon.WeaponType.HEALING));
        weapons.add(new Weapon(game, Weapon.WeaponType.LIGHTING));
        
        this.activeWeapon = 0;
        this.health = 100;
    }

    public void useWeapon() {
        Camera camera = Main.get().getCamera();
        Vector3f from = camera.getLocation().add(camera.getDirection());
        Vector3f to = camera.getLocation().add(camera.getDirection().mult(2f));

        weapons.get(activeWeapon).fire(from, to);
    }

    public PhysicsCharacter getPhysicsControl() {
        return physicsControl;
    }

    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Lefts")) {
            left = value;
        } else if (binding.equals("Rights")) {
            right = value;
        } else if (binding.equals("Ups")) {
            up = value;
        } else if (binding.equals("Downs")) {
            down = value;
        } else if (binding.equals("Space")) {
            physicsControl.jump();
        }

        if (binding.equals("WeaponFire") && value) {
            useWeapon();
        }

        //change weapon with mouse wheel
        if (binding.equals("WeaponNext")||binding.equals("WeaponNext2")) {
            activeWeapon++;
            if (activeWeapon >= Weapon.TOTALWEAPONS)
                activeWeapon = 0;
        } else if (binding.equals("WeaponPrev")||binding.equals("WeaponPrev2")) {
            activeWeapon--;
            if (activeWeapon < 0)
                activeWeapon = Weapon.TOTALWEAPONS-1;
            }
    }

    public void update(float tpf) {
        Camera camera = Main.get().getCamera();
        Vector3f camDir = camera.getDirection().clone().multLocal(.6f);
        Vector3f camLeft = camera.getLeft().clone().multLocal(.4f);
        walkDirection.set(0, 0, 0);

        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }

        physicsControl.setWalkDirection(walkDirection);
        camera.setLocation(physicsControl.getPhysicsLocation());

        for (int ix = 0; ix < MAGICEFFECT_MAX; ix++) {
            if (effect[ix] != null) {
                effect[ix].update();
                if (effect[ix].done) {
                    effect[ix] = null;
                }
            }

        }
       
    }

    public int getHealth() {
        return health;
    }
    
    public Weapon getWeapon() {
        return weapons.get(activeWeapon);
    }
}
