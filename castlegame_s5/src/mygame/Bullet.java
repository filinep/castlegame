/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author filipe
 */
public enum Bullet {

    MELEE(ColorRGBA.Red, new Sphere(3, 4, 1), (CollisionShape) (new SphereCollisionShape(.2f)), 1),
    RANGED(ColorRGBA.Brown, new Cylinder(10, 5, .5f, 10f, true, false), new CylinderCollisionShape(new Vector3f(.25f, 5f, .25f)), 10),
    FIREBALL(ColorRGBA.Yellow, new Sphere(3, 4, 1), new SphereCollisionShape(.2f), 5),
    HEALING(ColorRGBA.Blue, new Sphere(8, 8, 3), new SphereCollisionShape(.2f), 3),
    LIGHTING(ColorRGBA.Green, new Sphere(8, 8, 3), new SphereCollisionShape(.2f), 3);
    private final Geometry geometry;
    private final BulletControl bulletControl;
    private final float speed = 50f;
    private final int damage = 0;

    Bullet(ColorRGBA color, Mesh mesh, CollisionShape physicsShape, int range) {
        Material material = new Material(Main.get().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", Main.get().getAssetManager().loadTexture("Textures/circuit1.jpg"));
        material.setColor("Color", color);

        this.geometry = new Geometry("Bullet", mesh);
        this.geometry.setMaterial(material);

        this.bulletControl = new BulletControl(physicsShape, 1f, range);
    }

    public void fire(int name, Vector3f from, Vector3f to, boolean magic) {
        Geometry newBullet = geometry.clone(true);
        BulletControl newBulletControl = bulletControl.cloneForSpatial(newBullet);
        Main.get().getBulletAppState().getPhysicsSpace().add(newBulletControl);
        //newBullet.setName("Bullet" + name);
        newBullet.addControl(newBulletControl);
        Main.get().getRootNode().attachChild(newBullet);

        newBulletControl.setGravity(Vector3f.ZERO);

        Vector3f z = to.subtract(from).normalize();
        Vector3f x = new Vector3f(z.z, 0f, -z.x).normalize();
        Vector3f y = z.cross(x);

        newBulletControl.setPhysicsRotation(new Quaternion().fromAxes(x, y, z));
        newBulletControl.setPhysicsLocation(from);
        newBulletControl.setLinearVelocity(z.mult(speed));
    }
    
    public void magic(Vector3f loc)
    {
        Main.get().getPlayer().addMagicEffect(loc);
    }
}
