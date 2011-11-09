/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author scott
 */
public class GameLogic extends AbstractAppState {

    Player player;
    List<Enemy> enemies = new CopyOnWriteArrayList<Enemy>();
    List<Bullet> bullets = new CopyOnWriteArrayList<Bullet>();
    List<MagicEffect> effects = new CopyOnWriteArrayList<MagicEffect>();
    String levelName;
    Scene gameLevel;
    private HUD hud;
    Main main = Main.get();
    AudioNode audio_attack;
    private BulletAppState bulletAppState;

    private void initAudio() {
        /* gun shot sound is to be triggered by a mouse click. */
        audio_attack = new AudioNode(main.getAssetManager(), "Sounds/hit-01.wav", false);
        audio_attack.setLooping(false);
        audio_attack.setVolume(2);
        Main.get().getRootNode().attachChild(audio_attack);
    }
    
    AudioNode get_audio_attack()
    {
        return audio_attack;
    }

    public GameLogic(String fname) {
        levelName = fname;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        initAudio();
        
        //setup physics state manager
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        //initialize level/scene
        gameLevel = new Scene(levelName);

        //create the player with the starting player position at "playerPos" from the scene
        Spatial startLoc = gameLevel.getChild("playerPos");
        Vector3f start = startLoc.getWorldTranslation();
        start.mult(20f);
        player = new Player(this, gameLevel.getChild("playerPos"));

        //setup hud
        hud = new HUD(this);
        main.getGuiNode().attachChild(hud);

        //attach level to root node
        ((SimpleApplication) app).getRootNode().attachChild(gameLevel);

        //attach player and level to physics space
        getPhysicsSpace().addAll(gameLevel);
        getPhysicsSpace().add(player.getPhysicsControl());

        // get enemies from spawn points:
        spawnEnemies();

        //setup keys:
        setupKeys();
    }

    public PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

    public void spawnEnemies() {
        GameLogic game = this;
        // check for skeletons:
        String skeleton = "skeleton";
        for (int ix = 0; ix < 100; ix++) {
            String search = skeleton + ix;
            //System.out.println(search);
            Spatial item = gameLevel.getChild(search);
            if (item != null) {
                System.out.println(search);
                Enemy e = new Enemy(game, Enemy.ENEMYTYPE.SKELETON, item);
            }
        }
    }

    public void setupKeys() {
        InputManager inputManager = Main.get().getInputManager();
        inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Fire", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("WeaponNext2", new KeyTrigger(KeyInput.KEY_LBRACKET));
        inputManager.addMapping("WeaponPrev2", new KeyTrigger(KeyInput.KEY_RBRACKET));
        inputManager.addMapping("WeaponNext", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping("WeaponPrev", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("WeaponFire", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        inputManager.addListener(player, "Lefts", "Rights", "Ups", "Downs");
        inputManager.addListener(player, "Space", "Fire", "WeaponNext");
        inputManager.addListener(player, "WeaponPrev", "WeaponFire",
                "WeaponNext2", "WeaponPrev2");
    }

    @Override
    public void update(float tpf) {
        player.update(tpf);

        for (MagicEffect m : effects) {
            m.update(tpf);
        }

        for (Enemy e : enemies) {
            e.update(tpf);
        }

        hud.update();
    }

    public void remove(GameEntity entity) {
        switch (entity.type) {
            case Player:
                removePlayer();
                break;
            case Enemy:
                removeEnemy((Enemy) entity);
                break;
            case Bullet:
                removeBullet((Bullet) entity);
                break;
            case MagicEffect:
                removeEffect((MagicEffect) entity);
                break;
            default:
                ;
        }
    }

    public void removePlayer() {
    }

    public void removeEnemy(Enemy enem) {
        enemies.remove(enem);
    }

    public void removeBullet(Bullet bull) {
        bullets.remove(bull);
    }

    public void removeEffect(MagicEffect eff) {
        effects.remove(eff);
    }

    public void add(GameEntity entity) {
        switch (entity.type) {
            case Player:
                break; // nothing here
            case Enemy:
                addEnemy((Enemy) entity);
                break;
            case Bullet:
                addBullet((Bullet) entity);
                break;
            case MagicEffect:
                addEffect((MagicEffect) entity);
                break;
            default:
                ;
        }
    }

    public void addEnemy(Enemy enem) {
        enemies.add(enem);
    }

    public void addBullet(Bullet bull) {
        bullets.add(bull);
    }

    public void addEffect(MagicEffect eff) {
        effects.add(eff);
    }

    public Scene getGameLevel() {
        return gameLevel;
    }

    public Player getPlayer() {
        return player;
    }
    // BORN: ------------------
    // LIFE: ------------------
    // IMPACT: ----------------
    // DAMAGE: ----------------
    // DEATH: -----------------
}
