package mygame;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
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

    private CharacterControl physicsControl;
    private boolean left = false, right = false, up = false, down = false;
    private int activeWeapon;
    private List<Weapon> weapons = new ArrayList<Weapon>();
    private Vector3f walkDirection;
    private int health;
    
    public Player(GameLogic gl, Spatial startLoc) {
        super (gl);
        type = TYPE.Player;
        this.physicsControl = new CharacterControl(new CapsuleCollisionShape(1f, 5f), .5f);
        this.physicsControl.setJumpSpeed(20);
        this.physicsControl.setFallSpeed(30);
        this.physicsControl.setGravity(30);
        
        setName("Player");
        addControl(physicsControl);
        this.physicsControl.setPhysicsLocation(startLoc.getWorldTranslation());
        
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
        
        birth();
    }

    public void useWeapon() {
        Camera cam = Main.get().getCamera();
        Vector3f playerPos = physicsControl.getPhysicsLocation();
        Vector3f from = playerPos.add(cam.getDirection().normalize().mult(2f));
        Vector3f to = playerPos.add(cam.getDirection().normalize().mult(4f));

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

        if (binding.equals("Fire") && value) {
            useWeapon();
        }

        //change weapon with mouse wheel
        if (binding.equals("WeaponNext")) {
            activeWeapon++;
            if (activeWeapon >= Weapon.TOTALWEAPONS)
                activeWeapon = 0;
        } else if (binding.equals("WeaponPrev")) {
            activeWeapon--;
            if (activeWeapon < 0)
                activeWeapon = Weapon.TOTALWEAPONS-1;
            }
    }

    @Override
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
    }

    public int getHealth() {
        return health;
    }
    
    public Weapon getWeapon() {
        return weapons.get(activeWeapon);
    }
    
    @Override
    public void damage(int dhp) {
        health -= dhp;
        
        game.getHud().setPlayerDamaged();
        //TODO check for death
    }
}