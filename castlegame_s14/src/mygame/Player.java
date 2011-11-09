/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author filipe
 */
public class Player extends GameEntity implements ActionListener {

    private CharacterControl physicsControl;
    private boolean left = false, right = false, up = false, down = false;
    private int activeWeapon;
    private List<Weapon> weapons = new ArrayList<Weapon>();
    private Vector3f walkDirection;
    private Node playerNode;
    private int health;
    private final int MAGICEFFECT_MAX = 10;
    private MagicEffect[] effect = new MagicEffect[MAGICEFFECT_MAX];
    private int currentEffect = -1;
    
    private int lightCounter = 0;
    
    public Player(GameLogic gl, Spatial startLoc) {
        super (gl);
        type = TYPE.Player;
        this.physicsControl = new CharacterControl(new CapsuleCollisionShape(1f, 5f), .5f);
        this.physicsControl.setJumpSpeed(20);
        this.physicsControl.setFallSpeed(30);
        this.physicsControl.setGravity(30);
        
        playerNode = new Node("Player");
        playerNode.addControl(physicsControl);
        this.physicsControl.setPhysicsLocation(startLoc.getWorldTranslation());
        Main.get().getRootNode().attachChild(playerNode);
        
        
        Camera cam = Main.get().getCamera();
        float[] angles = {0f, FastMath.HALF_PI, 0f};
        cam.setRotation(new Quaternion(angles));

        this.walkDirection = new Vector3f();
        
        this.weapons.add(new Weapon(game, Weapon.WeaponType.RANGED));
        this.weapons.add(new Weapon(game, Weapon.WeaponType.MELEE));
        this.weapons.add(new Weapon(game, Weapon.WeaponType.FIREBALL));
        this.weapons.add(new Weapon(game, Weapon.WeaponType.HEALING));
        this.weapons.add(new Weapon(game, Weapon.WeaponType.LIGHTING));
        
        this.activeWeapon = 0;
        this.health = 100;
    }

    public void useWeapon() {
        Camera cam = Main.get().getCamera();
        Vector3f playerPos = physicsControl.getPhysicsLocation();
        Vector3f from = physicsControl.getPhysicsLocation().add(cam.getDirection().normalize().mult(10f));
        Vector3f to = physicsControl.getPhysicsLocation().add(cam.getDirection().normalize().mult(40f));

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

        /*for (int ix = 0; ix < MAGICEFFECT_MAX; ix++) {
            if (effect[ix] != null) {
                effect[ix].update();
                if (effect[ix].done) {
                    effect[ix] = null;
                }
            }

        }*/
       
    }

    public int getHealth() {
        return health;
    }
    
    public Weapon getWeapon() {
        return weapons.get(activeWeapon);
    }
}
