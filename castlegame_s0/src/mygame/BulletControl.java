/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 *
 * @author scott
 */
public class BulletControl extends RigidBodyControl
        implements PhysicsCollisionListener {

    public boolean collidedWithMap = false;

    public BulletControl(CollisionShape shape, float mass, BulletAppState bulletAppState) {
        super(shape, mass);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }

    public void prePhysicsTick(PhysicsSpace space, float f) {
        // apply state changes ...
    }

    public void physicsTick(PhysicsSpace space, float f) {
        // poll game state ...
    }

    public void collision(PhysicsCollisionEvent event) {
        Spatial a = event.getNodeA();
        Spatial b = event.getNodeB();
        if (a != null) {
            if (a.getName().equals("Map")) {
                // node = (Node)event.getNodeA();
                /** ... do something with the node ... */
                collidedWithMap = true;
            } else if (b != null) {
                if (b.getName().equals("Map")) {
                    //Node node = (Node)event.getNodeB();
                    /** ... do something with the node ... */
                    collidedWithMap = true;
                }
            }
        }
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        return super.cloneForSpatial(spatial);
    }

    @Override
    protected void createCollisionShape() {
        super.createCollisionShape();
    }

    @Override
    public PhysicsSpace getPhysicsSpace() {
        return super.getPhysicsSpace();
    }

    @Override
    public boolean isApplyPhysicsLocal() {
        return super.isApplyPhysicsLocal();
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public boolean isKinematicSpatial() {
        return super.isKinematicSpatial();
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
        super.render(rm, vp);
    }

    @Override
    public void setApplyPhysicsLocal(boolean applyPhysicsLocal) {
        super.setApplyPhysicsLocal(applyPhysicsLocal);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public void setKinematicSpatial(boolean kinematicSpatial) {
        super.setKinematicSpatial(kinematicSpatial);
    }

    @Override
    public void setPhysicsSpace(PhysicsSpace space) {
        super.setPhysicsSpace(space);
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
    }
}
