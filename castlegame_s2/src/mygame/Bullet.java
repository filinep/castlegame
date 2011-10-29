/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author filipe
 */
public enum Bullet {
    MELEE(ColorRGBA.Blue, new Sphere(3, 4, 1), (CollisionShape) (new SphereCollisionShape(.2f)), 1),
    RANGED(ColorRGBA.Brown, new Sphere(3, 4, 1), new SphereCollisionShape(.2f), 10),
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
    
    public void fire() {
        Geometry newBullet = geometry.clone(true);
        BulletControl newBulletControl = bulletControl.cloneForSpatial(newBullet);
        
        Main.get().getBulletAppState().getPhysicsSpace().add(newBulletControl);
        newBullet.addControl(newBulletControl);
        Main.get().getRootNode().attachChild(newBullet);

        Camera camera = Main.get().getCamera();
        newBulletControl.setGravity(Vector3f.ZERO);
        newBulletControl.setPhysicsLocation(camera.getLocation().add(camera.getDirection().mult(2f)));
        newBulletControl.setLinearVelocity(camera.getDirection().mult(50f));
    }   
}
