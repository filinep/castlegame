/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author scott
 */
public class Enemy extends GameEntity {

    public enum ENEMYTYPE {

        FROG, SKELETON, IMP
    };
    
    private static int idTicker = 0;
    
    // taken from player:
    private EnemyControl physicsControl;
    private boolean left = false, right = false, up = false, down = false;
    private int activeWeapon;
    private List<Weapon> weapons = new ArrayList<Weapon>();
    private Vector3f walkDirection;
    private int health;
    private final int MAGICEFFECT_MAX = 10;
    private MagicEffect[] effect = new MagicEffect[MAGICEFFECT_MAX];
    private int currentEffect = -1;
    private int lightCounter = 0;
    
    // taken from bullet:
    private static int number = 0;
    private Geometry geometry = null;
    private EnemyControl enemyControl = null;
    private float speed = 50f;
    private int damage = 0;
    private ENEMYTYPE enemyType = ENEMYTYPE.SKELETON;

    public Enemy(GameLogic gl, ENEMYTYPE bt, Spatial startLoc) {
        super(gl);
        type = TYPE.Enemy;
        this.physicsControl = new EnemyControl(this, new CapsuleCollisionShape(1f, 5f), .01f);
        this.physicsControl.setJumpSpeed(20);
        this.physicsControl.setFallSpeed(30);
        this.physicsControl.setGravity(30);
        Vector3f loc = new Vector3f(30f, 30f, 30f);
        this.physicsControl.setPhysicsLocation(loc);
        //Camera cam = Main.get().getCamera();
        //float[] angles = {0f, -FastMath.HALF_PI, 0f};
        //cam.setRotation(new Quaternion(angles));

        this.walkDirection = new Vector3f();
        //weapons.add(new Weapon(game, Weapon.WeaponType.RANGED));
        //.add(new Weapon(game, Weapon.WeaponType.MELEE));
        //weapons.add(new Weapon(game, Weapon.WeaponType.FIREBALL));
        //weapons.add(new Weapon(game, Weapon.WeaponType.HEALING));
        //weapons.add(new Weapon(game, Weapon.WeaponType.LIGHTING));

        this.activeWeapon = 0;
        this.health = 100;

        // taken from bullet:
        switch (bt) {
            case FROG:
                initModel(
                        ColorRGBA.Green, (Mesh) (new Box(1f, 1f, 1f)),
                        1);
                break;
            case SKELETON:
                initModel(
                        ColorRGBA.White, (new Box(3f, 6f, 3f)), 10);
                break;
            case IMP:
                initModel(
                        ColorRGBA.Red, (new Box(1f, 1f, 1f)), 5);
                break;
            default:
                ;
        }
        
        //Vector3f loc = new Vector3f(30f, 30f, 30f);
        this.physicsControl.setPhysicsLocation(startLoc.getWorldTranslation());
        
        //Geometry newBullet = geometry.clone(true);
        //BulletControl newBulletControl = bulletControl.cloneForSpatial(this, newBullet);
        //Main.get().getBulletAppState().getPhysicsSpace().add(newBulletControl);
        //newBullet.setName("Skeleton" + idTicker++);
        //newBullet.addControl(newBulletControl);
        //Main.get().getRootNode().attachChild(newBullet);

        //newBulletControl.setGravity(Vector3f.ZERO);

        //Vector3f z = to.subtract(from).normalize();
        //Vector3f x = new Vector3f(z.z, 0f, -z.x).normalize();
        //Vector3f y = z.cross(x);

        //newBulletControl.setPhysicsRotation(new Quaternion().fromAxes(x, y, z));
        //newBulletControl.setPhysicsLocation(from);
        //newBulletControl.setLinearVelocity(z.mult(speed));

        birth();
    }
    
    public PhysicsSpace phys() {
        return Main.get().getBulletAppState().getPhysicsSpace();
    }

    public void initModel(ColorRGBA color, Mesh mesh, int range) {
        Material material = new Material(Main.get().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", Main.get().getAssetManager().loadTexture("Textures/circuit1.jpg"));
        material.setColor("Color", color);

        this.geometry = new Geometry("Skeleton" + idTicker++, mesh);
        this.geometry.setMaterial(material);
        
        geometry.addControl(physicsControl);
        
        phys().add(physicsControl);
        
        Main.get().getRootNode().attachChild(geometry);
    }

    public void useWeapon() {
        Camera camera = Main.get().getCamera();
        Vector3f from = camera.getLocation().add(camera.getDirection());
        Vector3f to = camera.getLocation().add(camera.getDirection().mult(2f));

        //weapons.get(activeWeapon).fire(from, to);
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
        if (binding.equals("WeaponNext") || binding.equals("WeaponNext2")) {
            activeWeapon++;
            if (activeWeapon >= Weapon.TOTALWEAPONS) {
                activeWeapon = 0;
            }
        } else if (binding.equals("WeaponPrev") || binding.equals("WeaponPrev2")) {
            activeWeapon--;
            if (activeWeapon < 0) {
                activeWeapon = Weapon.TOTALWEAPONS - 1;
            }
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
