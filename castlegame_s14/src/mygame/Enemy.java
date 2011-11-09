/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
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
    
    private EnemyControl physicsControl;
    private int activeWeapon;
    private List<Weapon> weapons = new ArrayList<Weapon>();
    private Vector3f walkDirection;
    private int health;

    private Spatial model = null;
    private Geometry geometry = null;
    private ENEMYTYPE enemyType;
    private float shootDelay;

    public Enemy(GameLogic gl, ENEMYTYPE enemyType, Spatial startLoc) {
        super(gl);
        
        this.type = TYPE.Enemy;
        
        this.physicsControl = new EnemyControl(this, new CapsuleCollisionShape(1f, 5f), .01f);
        this.physicsControl.setJumpSpeed(20);
        this.physicsControl.setFallSpeed(30);
        this.physicsControl.setGravity(30);

        this.walkDirection = new Vector3f();
        this.activeWeapon = 0;
        this.health = 100;
        this.shootDelay = 2f;

        switch (enemyType) {
            case FROG:
                initModel(ColorRGBA.Green, new Box(1f, 1f, 1f), 1);
                enemyType = ENEMYTYPE.FROG;
                break;
            case SKELETON:
                initModel(ColorRGBA.White, new Box(3f, 6f, 3f), 10);
                enemyType = ENEMYTYPE.SKELETON;
                weapons.add(new Weapon(game, Weapon.WeaponType.MELEE));
                break;
            case IMP:
                initModel(ColorRGBA.Red, new Box(1f, 1f, 1f), 5);
                enemyType = ENEMYTYPE.IMP;
                weapons.add(new Weapon(game, Weapon.WeaponType.MELEE));
                break;
            default:
                ;
        }
        
        this.physicsControl.setPhysicsLocation(startLoc.getWorldTranslation());

        birth();
    }
    
    public PhysicsSpace phys() {
        return Main.get().getBulletAppState().getPhysicsSpace();
    }

    public ENEMYTYPE getEnemyType() {
        return enemyType;
    }

    public void initModel(ColorRGBA color, Mesh mesh, int range) {
        Material material = new Material(Main.get().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", Main.get().getAssetManager().loadTexture("Textures/circuit1.jpg"));
        material.setColor("Color", color);

        this.geometry = new Geometry("Skeleton" + idTicker++, mesh);
        this.geometry.setMaterial(material);
        
        model = Main.get().getAssetManager().loadModel("Models/skele11/skele11.j3o");
        model.addControl(physicsControl);
        
        PointLight light = new PointLight();
        light.setColor(ColorRGBA.White);
        light.setRadius(20f);
        model.addLight(light);
        model.setShadowMode(ShadowMode.CastAndReceive);
        
        phys().add(physicsControl);
        
        Main.get().getRootNode().attachChild(model);
    }

    public void useWeapon() {
        Vector3f playerPos = game.getPlayer().getPhysicsControl().getPhysicsLocation();
        Vector3f from = physicsControl.getPhysicsLocation().add(playerPos.subtract(physicsControl.getPhysicsLocation()).normalize().mult(20f));
        Vector3f to = game.getPlayer().getPhysicsControl().getPhysicsLocation();

        weapons.get(activeWeapon).fire(from, to);
    }

    public PhysicsCharacter getPhysicsControl() {
        return physicsControl;
    }

    public void update(float tpf) {
        //get player position and distance to player
        Vector3f playerPos = game.getPlayer().getPhysicsControl().getPhysicsLocation();
        float distToPlayer = playerPos.distance(physicsControl.getPhysicsLocation());
        walkDirection.set(0f, 0f, 0f);
   
        //countdown till enemy can shoot again
        if(shootDelay > 0f) {
            shootDelay -= tpf;
        }
        
        //TODO: change these distances (100f and 200f) to use the range of the weapon
        if (distToPlayer < 25f) {
            //only shoot after at least one second since last shot
            //TODO: change 1f for different delays
            if(shootDelay < 0f) {
                useWeapon();
                shootDelay = 1f;
            }
            
            //make enemy face player
            physicsControl.setViewDirection(playerPos.subtract(physicsControl.getPhysicsLocation()));
        } else if (distToPlayer < 200f) {
            //if player in *sight* (read, near) but out of range then set move direction
            //TODO: change enemy speed by multiplying normalized vector with move speed
            //default move speed is one, which is pretty fast
            walkDirection.set(playerPos.subtract(physicsControl.getPhysicsLocation()).normalize());
        }
        
        //make enemy move
        physicsControl.setWalkDirection(walkDirection);
    }

    public int getHealth() {
        return health;
    }

    public Weapon getWeapon() {
        return weapons.get(activeWeapon);
    }
}
