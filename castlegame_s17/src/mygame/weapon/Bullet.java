package mygame.weapon;

import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import mygame.GameEntity;
import mygame.GameLogic;
import mygame.Main;

/**
 *
 * @author scott
 */
public class Bullet extends GameEntity {

    public enum BULLETTYPE {

        MELEE, RANGED, FIREBALL, HEALING, LIGHTING
    };
    
    private static int id = 0;
    private Geometry geometry = null;
    private BulletControl bulletControl = null;
    private float speed = 50f;
    private int damage = 0;
    private BULLETTYPE bulletType = BULLETTYPE.MELEE;

    Bullet(GameLogic gl, BULLETTYPE bt, Vector3f from, Vector3f to, boolean magic) {
        super(gl);
        type = TYPE.Bullet;
        bulletType = bt;
        AudioNode audio = null;
        int volumeFactor = 1;
        
        //TODO: change audio sound files below
        switch (bulletType) {
            case MELEE:
                init(
                        ColorRGBA.Red, (Mesh) (new Sphere(3, 4, 1)),
                        (CollisionShape) (new SphereCollisionShape(.2f)), 1);
                damage = 50;
                audio = new AudioNode(game.getAssetManager(), "Sounds/hit-01.wav", false);
                break;
            case RANGED:
                init(
                        ColorRGBA.Brown, new Cylinder(10, 5, .5f, 10f, true, false), new CylinderCollisionShape(new Vector3f(.25f, 5f, .25f)), 10);
                damage = 10;
                audio = new AudioNode(game.getAssetManager(), "Sounds/hit-01.wav", false);
                break;
            case FIREBALL:
                init(
                        ColorRGBA.Yellow, new Sphere(3, 4, 1), new SphereCollisionShape(.2f), 20);
                damage = 25;
                audio = new AudioNode(game.getAssetManager(), "Sounds/fireball.wav", false);
                volumeFactor = 4;
                break;
            default:
                ;
        }
        
        audio.setLooping(false);
        audio.setVolume(2 * volumeFactor);
        attachChild(audio);

        birth();
        move(from);
        
        Vector3f z = to.subtract(from).normalize();
        Vector3f x = new Vector3f(z.z, 0f, -z.x).normalize();
        Vector3f y = z.cross(x);

        bulletControl.setPhysicsRotation(new Quaternion().fromAxes(x, y, z));
        bulletControl.setPhysicsLocation(from);
        bulletControl.setLinearVelocity(z.mult(speed));
        
        audio.playInstance();
    }

    public final void init(ColorRGBA color, Mesh mesh, CollisionShape physicsShape, int range) {
        Material material = new Material(Main.get().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", Main.get().getAssetManager().loadTexture("Textures/circuit1.jpg"));
        material.setColor("Color", color);

        geometry = new Geometry("Bullet" + id++, mesh);
        geometry.setMaterial(material);

        bulletControl = new BulletControl(this, physicsShape, 1f, range);
        bulletControl.setGravity(Vector3f.ZERO);
        
        getPhysicsSpace().add(bulletControl);
        addControl(bulletControl);
        attachChild(geometry);
    }
    
    public int getDamage() {
        return damage;
    }

}
