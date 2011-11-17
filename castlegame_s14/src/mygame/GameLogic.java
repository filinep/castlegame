package mygame;

import mygame.weapon.Bullet;
import mygame.weapon.MagicEffect;
import mygame.enemy.Enemy;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
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
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import mygame.enemy.EnemySpawner;
import mygame.enemy.EnemyType;

/**
 *
 * @author scott
 */
public class GameLogic extends AbstractAppState {

    private Player player;
    private List<Enemy> enemies = new CopyOnWriteArrayList<Enemy>();
    private List<Bullet> bullets = new CopyOnWriteArrayList<Bullet>();
    private List<MagicEffect> effects = new CopyOnWriteArrayList<MagicEffect>();
    private List<GameEntity> other = new CopyOnWriteArrayList<GameEntity>();
    private String levelName;
    private Scene gameLevel;
    private HUD hud;
    private Main main = Main.get();
    private AudioNode audio_attack;
    private BulletAppState bulletAppState;

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
        gameLevel = new Scene(this, levelName);

        //create the player with the starting player position at "playerPos" from the scene
        Spatial startLoc = gameLevel.getChild("playerPos");
        Vector3f start = startLoc.getWorldTranslation();
        start.mult(20f);
        player = new Player(this, gameLevel.getChild("playerPos"));

        //setup hud
        hud = new HUD(this);
        main.getGuiNode().attachChild(hud);
        addOther(hud);

        //attach player and level to physics space
        getPhysicsSpace().addAll(gameLevel);
        getPhysicsSpace().add(player.getPhysicsControl());

        // get enemies from spawn points:
        spawnEnemies();

        //setup keys:
        setupKeys();
    }
    
    private void initAudio() {
        // gun shot sound is triggered by any weapon firing
        audio_attack = new AudioNode(main.getAssetManager(), "Sounds/hit-01.wav", false);
        audio_attack.setLooping(false);
        audio_attack.setVolume(2);
        
        main.getRootNode().attachChild(audio_attack);
    }
    
    public AudioNode getAudioAttack() {
        return audio_attack;
    }

    public PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

    public void spawnEnemies() {
        EnemySpawner skeletonSpawner = new EnemySpawner(this, 6, EnemyType.SKELETON);
        EnemySpawner impSpawner = new EnemySpawner(this, 4, EnemyType.IMP);
        EnemySpawner frogSpawner = new EnemySpawner(this, 8, EnemyType.FROG);
    }

    public void setupKeys() {
        InputManager inputManager = Main.get().getInputManager();
        inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Fire", new KeyTrigger(KeyInput.KEY_F),
                                        new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("WeaponNext", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true),
                                              new KeyTrigger(KeyInput.KEY_LBRACKET));
        inputManager.addMapping("WeaponPrev", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false),
                                              new KeyTrigger(KeyInput.KEY_RBRACKET));

        inputManager.addListener(player, "Lefts", "Rights", "Ups", "Downs");
        inputManager.addListener(player, "Space", "Fire", "WeaponNext", "WeaponPrev");
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
        
        for (GameEntity o : other) {
            o.update(tpf);
        }
        //hud.update(tpf);
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
            case Other:
                addOther((GameEntity) entity);
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
    
    public void addOther(GameEntity ent) {
        other.add(ent);
    }

    public Scene getGameLevel() {
        return gameLevel;
    }

    public Player getPlayer() {
        return player;
    }

    public HUD getHud() {
        return hud;
    }
    
    public Node getRootNode() {
        return main.getRootNode();
    }
    
    public AssetManager getAssetManager() {
        return main.getAssetManager();
    }
    
    public List<Enemy> getEnemies() {
        return enemies;
    }
    
    // BORN: ------------------
    // LIFE: ------------------
    // IMPACT: ----------------
    // DAMAGE: ----------------
    // DEATH: -----------------
}
