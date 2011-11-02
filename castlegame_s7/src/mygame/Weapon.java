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

/**
 *
 * @author filipe
 */
public class Weapon extends GameEntity {

    public static final int TOTALWEAPONS = 5;

    public enum WeaponType {

        MELEE, RANGED, FIREBALL, HEALING, LIGHTING
    };
    private final int INFINITY = -1; //Used for weapons with unlimited ammo
    String name;
    private Bullet.BULLETTYPE bullet;
    boolean isMagic = false;
    private int currentAmmo; //Number of bullets left
    private int maxBulletsOnScreen = 100;
    public static GameLogic game;

    Weapon(GameLogic gl, WeaponType wtype) {
        super(gl);
        switch (wtype) {
            case MELEE:
                init("MELEE", Bullet.BULLETTYPE.MELEE, -1, false);
                break;
            case RANGED:
                init("RANGED", Bullet.BULLETTYPE.RANGED, 50, false);
                break;
            case FIREBALL:
                init("FIREBALL", Bullet.BULLETTYPE.FIREBALL, 50, false);
                break;
            case HEALING:
                init("HEALING", Bullet.BULLETTYPE.HEALING, 10, true);
                break;
            case LIGHTING:
                init("LIGHTING", Bullet.BULLETTYPE.LIGHTING, 10, true);
                break;

            default:
                ;
        }
        //fire (from, to);
    }

    public void init(String n, Bullet.BULLETTYPE b, int ammo, boolean magic) {
        this.name = n;
        this.bullet = b;
        this.currentAmmo = ammo;
        this.isMagic = magic;
    }

    public void fire(Vector3f from, Vector3f to) {
        if (isMagic) {
            MagicEffect me = new MagicEffect(game, from);
        } else {
            boolean canFire = false;
            //This determines if there are any bullets currently on the screen

            for (int i = 0; i < maxBulletsOnScreen; ++i) {
                if (Main.get().getRootNode().getChild("Bullet" + i) == null) {
                    canFire = true;
                }
            }

            if ((currentAmmo == INFINITY || currentAmmo > 0) && canFire) {
                Bullet b = new Bullet(game, bullet, currentAmmo % maxBulletsOnScreen,
                        from, to, isMagic);

                if (currentAmmo != INFINITY) {
                    currentAmmo--;
                }
            }
        }
    }

    public void incrementAmmo(int obtained) {
        currentAmmo += obtained;
    }

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public void runHealingEffect(Vector3f from) {
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
        fire.setLocalTranslation(from);
        Main.get().getRootNode().attachChild(fire);

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
        Main.get().getRootNode().attachChild(debris);
        debris.setLocalTranslation(from);
        debris.emitAllParticles();
    }

    public String getName() {
        return name;
    }
}
