package mygame.enemy;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import mygame.GameEntity;
import mygame.GameLogic;
import mygame.Main;
import mygame.weapon.Weapon;

/**
 *
 * @author scott
 */
public abstract class Enemy extends GameEntity {

    protected CharacterControl physicsControl;
    protected int activeWeapon;
    protected List<Weapon> weapons = new ArrayList<Weapon>();
    protected Vector3f walkDirection, viewDirection;
    protected int health;
    protected Spatial model = null;
    protected EnemyType enemyType;
    protected float shootDelay;
    protected float turnRate;
    protected float firingRange;
    protected float walkingRange;
    protected float rangeToPlayer;
    protected float walkSpeed;
    protected float shootRate;

    public static Enemy createEnemy(GameLogic gl, Vector3f startLoc, EnemyType type) {
        switch (type) {
            case IMP:
                return new Imp(gl, startLoc);
            case SKELETON:
                return new Skeleton(gl, startLoc);
            case FROG:
                return new Frog(gl, startLoc);
            default:
                return new Skeleton(gl, startLoc);
        }
    }

    public Enemy(GameLogic gl) {
        super(gl);

        this.type = TYPE.Enemy;

        this.walkDirection = new Vector3f();
        this.activeWeapon = 0;
        this.shootDelay = 2f;

        birth();
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }

    public final void initModel(String modelName, float radius, float height) {
        //TODO: hopefully material is already on model

        /*Material material = new Material(Main.get().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", Main.get().getAssetManager().loadTexture("Textures/circuit1.jpg"));
        material.setColor("Color", color);*/

        //TODO: since bullet collision is dont in BulletControl we might not need EnemyControl
        //physicsControl = new EnemyControl(this, new CapsuleCollisionShape(radius, height), .01f);
        physicsControl = new CharacterControl(new CapsuleCollisionShape(radius, height), .01f);
        physicsControl.setJumpSpeed(20);
        physicsControl.setFallSpeed(30);
        physicsControl.setGravity(30);

        model = Main.get().getAssetManager().loadModel(modelName);
        addControl(physicsControl);

        //TODO: the level should have enough lighting
        PointLight light = new PointLight();
        light.setColor(ColorRGBA.White);
        light.setRadius(20f);

        model.addLight(light);
        model.setShadowMode(ShadowMode.CastAndReceive);

        getPhysicsSpace().add(physicsControl);
        attachChild(model);
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

    @Override
    public void update(float tpf) {
        //TODO factor this out to AIProfiles or something

        //get player position and distance to player
        Vector3f playerPos = game.getPlayer().getPhysicsControl().getPhysicsLocation();
        float distToPlayer = playerPos.distance(physicsControl.getPhysicsLocation());
        walkDirection.set(0f, 0f, 0f);

        //countdown till enemy can shoot again
        if (shootDelay > 0f) {
            shootDelay -= tpf;
        }

        //TODO: change these distances (100f and 200f) to use the range of the weapon
        if (distToPlayer < 25f) {
            //only shoot after at least one second since last shot
            //TODO: change 1f for different delays
            if (shootDelay < 0f) {
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

    public void shootUpdate(float tpf) {
        if (shootDelay > 0f) {
            shootDelay -= tpf;
        }
    }
    
    public boolean withinRange(float distance, GameEntity entity) {
        CharacterControl control = entity.getControl(CharacterControl.class);
        float dist = control.getPhysicsLocation().distanceSquared(physicsControl.getPhysicsLocation());
        
        if (dist <= distance * distance) {
            return true;
        }
        
        return false;
    }

    public void walkForward() {
        walkDirection = viewDirection = physicsControl.getViewDirection().normalize().mult(walkSpeed);
        physicsControl.setWalkDirection(walkDirection);
    }

    public void stop() {
        walkDirection.set(0f, 0f, 0f);
        physicsControl.setWalkDirection(walkDirection);
    }

    public void turnLeft() {
        viewDirection = physicsControl.getViewDirection();
        Vector3f temp = viewDirection.normalize();
        Quaternion turn = new Quaternion();
        turn.fromAngleAxis(turnRate, Vector3f.UNIT_Y);
        temp = turn.mult(temp);
        physicsControl.setViewDirection(temp);
    }

    public void turnRight() {
        viewDirection = physicsControl.getViewDirection();
        Vector3f temp = viewDirection.normalize();
        Quaternion turn = new Quaternion();
        turn.fromAngleAxis(-turnRate, Vector3f.UNIT_Y);
        temp = turn.mult(temp);
        physicsControl.setViewDirection(temp);
    }

    public void turningTo(Vector3f target) {
        Quaternion diff1 = new Quaternion();
        Quaternion diff2 = new Quaternion();
        Quaternion diff3 = new Quaternion();
        
        Vector3f newOrient = target.subtract(physicsControl.getPhysicsLocation());
        Vector3f curOrient = physicsControl.getViewDirection();
        
        diff1.lookAt(newOrient, Vector3f.UNIT_Y);
        diff2.lookAt(curOrient, Vector3f.UNIT_Y);
        diff3 = diff2.subtract(diff1);
        
        float ydiff = diff3.getY();
        
        if (FastMath.abs(ydiff) > turnRate) {
            if (ydiff < 0) {
                turnRight();
            } else {
                turnLeft();
            }
        } else {
            physicsControl.setViewDirection(newOrient);
        }
    }

    public void jump() {
        physicsControl.jump();
    }

    public void attack() {
        if (shootDelay < 0f) {
            useWeapon();
            shootDelay = shootRate;
        }
    }

    public int getHealth() {
        return health;
    }

    public Weapon getWeapon() {
        return weapons.get(activeWeapon);
    }

    @Override
    public void damage(int dhp) {
        super.damage(dhp);

        health -= dhp;

        if (health <= 0) {
            kill();
        }
    }

    @Override
    public void kill() {
        super.kill();

    }
}
