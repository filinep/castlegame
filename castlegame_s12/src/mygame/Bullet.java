/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

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

/**
 *
 * @author scott
 */
public class Bullet extends GameEntity {

    public enum BULLETTYPE {

        MELEE, RANGED, FIREBALL, HEALING, LIGHTING
    };
    private static int number = 0;
    private Geometry geometry = null;
    private BulletControl bulletControl = null;
    private float speed = 50f;
    private int damage = 0;
    private BULLETTYPE bulletType = BULLETTYPE.MELEE;

    Bullet(GameLogic gl, BULLETTYPE bt, int name, Vector3f from, Vector3f to, boolean magic) {
        super(gl);
        type = TYPE.Bullet;
        switch (bt) {
            case MELEE:
                init(
                        ColorRGBA.Red, (Mesh) (new Sphere(3, 4, 1)),
                        (CollisionShape) (new SphereCollisionShape(.2f)), 1);
                break;
            case RANGED:
                init(
                        ColorRGBA.Brown, new Cylinder(10, 5, .5f, 10f, true, false), new CylinderCollisionShape(new Vector3f(.25f, 5f, .25f)), 10);
                break;
            case FIREBALL:
                init(
                        ColorRGBA.Yellow, new Sphere(3, 4, 1), new SphereCollisionShape(.2f), 5);
                break;
            case HEALING:
                init(
                        ColorRGBA.Blue, new Sphere(8, 8, 3), new SphereCollisionShape(.2f), 3);
                break;
            case LIGHTING:
                init(
                        ColorRGBA.Green, new Sphere(8, 8, 3), new SphereCollisionShape(.2f), 3);
                break;
            default:
                ;
        }
        Geometry newBullet = geometry.clone(true);
        BulletControl newBulletControl = bulletControl.cloneForSpatial(this, newBullet);
        Main.get().getBulletAppState().getPhysicsSpace().add(newBulletControl);
        newBullet.setName("Bullet");
        newBullet.addControl(newBulletControl);
        Main.get().getRootNode().attachChild(newBullet);

        newBulletControl.setGravity(Vector3f.ZERO);

        Vector3f z = to.subtract(from).normalize();
        Vector3f x = new Vector3f(z.z, 0f, -z.x).normalize();
        Vector3f y = z.cross(x);

        newBulletControl.setPhysicsRotation(new Quaternion().fromAxes(x, y, z));
        newBulletControl.setPhysicsLocation(from);
        newBulletControl.setLinearVelocity(z.mult(speed));
        
        birth();
    }

    Bullet(GameLogic gl, ColorRGBA color, Mesh mesh, CollisionShape physicsShape, int range) {
        super(gl);
        init(color, mesh, physicsShape, range);
    }

    Bullet(ColorRGBA color, Mesh mesh, CollisionShape physicsShape, int range) {
        super(null);
        init(color, mesh, physicsShape, range);
    }

    public void init(ColorRGBA color, Mesh mesh, CollisionShape physicsShape, int range) {
        Material material = new Material(Main.get().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", Main.get().getAssetManager().loadTexture("Textures/circuit1.jpg"));
        material.setColor("Color", color);

        this.geometry = new Geometry("Bullet", mesh);
        this.geometry.setMaterial(material);

        this.bulletControl = new BulletControl(this, physicsShape, 1f, range);
    }

}
