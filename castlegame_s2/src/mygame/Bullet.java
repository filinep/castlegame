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
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author filipe
 */
public enum Bullet {
    MELEE(ColorRGBA.Blue, new Sphere(3, 4, 1), (CollisionShape) (new SphereCollisionShape(.2f)), 1),
    RANGED(ColorRGBA.Brown, new Cylinder(3, 5, .5f, 3, true, false), new CylinderCollisionShape(new Vector3f(.25f, .25f, 1.5f)), 10),
    MAGIC(ColorRGBA.Yellow, new Sphere(3, 4, 1), new SphereCollisionShape(.2f), 5);
    
    private final Geometry geometry;
    private final BulletControl bulletControl;
    
    Bullet(ColorRGBA color, Mesh mesh, CollisionShape physicsShape, int range) {
        Material material = new Material(Main.get().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", Main.get().getAssetManager().loadTexture("Textures/circuit1.jpg"));
        material.setColor("Color", color);
        
        this.geometry = new Geometry("Bullet", mesh);
        this.geometry.setMaterial(material);
        
        this.bulletControl = new BulletControl(physicsShape, 1f, range);
    }
    
    public void fire(int name) {
        Geometry newBullet = geometry.clone(true);
        BulletControl newBulletControl = bulletControl.cloneForSpatial(newBullet);
        
        Main.get().getBulletAppState().getPhysicsSpace().add(newBulletControl);
        newBullet.setName("Bullet" + name);
        newBullet.addControl(newBulletControl);
        Main.get().getRootNode().attachChild(newBullet);

        Camera camera = Main.get().getCamera();
        newBulletControl.setGravity(Vector3f.ZERO);
        newBulletControl.setPhysicsRotation(camera.getRotation());
        newBulletControl.setPhysicsLocation(camera.getLocation().add(camera.getDirection().mult(2f)));
        newBulletControl.setLinearVelocity(camera.getDirection().mult(50f));
    }   
}
