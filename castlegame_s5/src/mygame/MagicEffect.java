/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author scott
 */
public class MagicEffect {
    long start;
    Node theEffect;
    boolean done = false;
    
    MagicEffect(Vector3f loc)
    {
        Node mbulletnode = new Node("Magic Effect");
        mbulletnode.setLocalTranslation(loc);
        theEffect = mbulletnode;
        ParticleEmitter fire =
                new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
        Material mat_red = new Material(Main.get().getAssetManager(),
                "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", Main.get().getAssetManager().loadTexture(
                "Textures/debris.png"));
        fire.setMaterial(mat_red);
        fire.setImagesX(2);
        fire.setImagesY(2); // 2x2 texture animation
        fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));   // red
        fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
        fire.setStartSize(5f);
        fire.setEndSize(0.1f);
        fire.setGravity(0, 0, 0);
        fire.setLowLife(1f);
        fire.setHighLife(3f);
        fire.getParticleInfluencer().setVelocityVariation(0.3f);

        mbulletnode.attachChild(fire);

        ParticleEmitter debris =
                new ParticleEmitter("Debris", ParticleMesh.Type.Triangle, 10);
        Material debris_mat = new Material(Main.get().getAssetManager(),
                "Common/MatDefs/Misc/Particle.j3md");
        debris_mat.setTexture("Texture", Main.get().getAssetManager().loadTexture(
                "Textures/debris.png"));
        debris.setMaterial(debris_mat);
        debris.setImagesX(3);
        debris.setImagesY(3); // 3x3 texture animation
        debris.setRotateSpeed(4);
        debris.setSelectRandomImage(true);
        debris.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 4, 0));
        debris.setStartColor(ColorRGBA.White);
        debris.setGravity(0, 6, 0);
        debris.getParticleInfluencer().setVelocityVariation(.60f);
        mbulletnode.attachChild(debris);

        debris.emitAllParticles();

        Main.get().getRootNode().attachChild(mbulletnode);
        start = System.currentTimeMillis();
    }
    
    
    public void update ()
            {
    if ((start + 2000) < (System.currentTimeMillis()))
        if (theEffect != null)
        {
            Main.get().getRootNode().detachChild(theEffect);
        }
    }
}
