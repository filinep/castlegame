/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;

/**
 *
 * @author filipe
 */
public class Scene extends Node {
    
    public Scene(String fname) {
        //set the name of the node
        super("Map");

        //load a scene from the assets and attach it to this node
        this.attachChild((Node) Main.get().getAssetManager().loadModel(fname));
        
        //add a sun
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        this.addLight(sun);
        
        //add another light
        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White.clone().multLocal(2));
        dl.setDirection(new Vector3f(-1, -1, -1).normalize());
        this.addLight(dl);

        //set the ambient light
        AmbientLight am = new AmbientLight();
        am.setColor(ColorRGBA.White.mult(2));
        this.addLight(am);
        
        //set shadows
        this.setShadowMode(ShadowMode.CastAndReceive);
        
        //scale the level
        this.setLocalScale(5f);
        
        // add a physics control, it will generate a MeshCollisionShape based on the gameLevel
        this.addControl(new RigidBodyControl(0));
    }
    
}
