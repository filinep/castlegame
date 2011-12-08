package mygame.weapon;

import com.jme3.audio.AudioNode;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import mygame.GameEntity;
import mygame.GameLogic;

/**
 *
 * @author scott
 */
public class MagicEffect extends GameEntity {
    private static long DURATION = 2000;
    private long start;
    
    MagicEffect(GameLogic gl, Vector3f loc) {
        super(gl);

        type = TYPE.MagicEffect;
        setName("Magic Effect");
        setLocalTranslation(loc);

        ParticleEmitter fire =
                new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
        Material mat_red = new Material(game.getAssetManager(),
                "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", game.getAssetManager().loadTexture(
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

        this.attachChild(fire);

        ParticleEmitter debris =
                new ParticleEmitter("Debris", ParticleMesh.Type.Triangle, 10);
        Material debris_mat = new Material(game.getAssetManager(),
                "Common/MatDefs/Misc/Particle.j3md");
        debris_mat.setTexture("Texture", game.getAssetManager().loadTexture(
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
       
        this.attachChild(debris);

        debris.emitAllParticles();

        start = System.currentTimeMillis();
        
        birth();
        
        AudioNode audio = new AudioNode(game.getAssetManager(), "Sounds/magic.wav", false);
        audio.setLooping(false);
        audio.setVolume(2);
        this.attachChild(audio);
        audio.playInstance();
    }

    @Override
    public void update (float tpf) {
        if ((start + DURATION) < (System.currentTimeMillis())) {
            kill();
        }
    }
}
