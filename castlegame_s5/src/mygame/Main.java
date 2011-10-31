package mygame;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.bullet.BulletAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.system.AppSettings;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    // MAIN STUFF:
    private Player myPlayer;
    private static BitmapText sysout;//debug to player screen
    
    // TESTQ3 STUFF:
    private BulletAppState bulletAppState;
    private Scene gameLevel;
    private HUD hud;
    private MagicEffect theEffect;
    
    private static Main instance = null;
    
    public static Main get() {
        if(instance == null)
            instance = new Main();
        return instance;
    }
    
    private Main() {
    }

    public static void main(String[] args) {
        Main app = Main.get();
        app.start();
    }
    
    public void addMagicEffect(MagicEffect x) {
        if (theEffect == null)
            theEffect = x;
    }

    @Override
    public void simpleInitApp() {
        //setup shadows
        PssmShadowRenderer pssmRenderer = new PssmShadowRenderer(assetManager, 1024, 4);
        pssmRenderer.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        viewPort.addProcessor(pssmRenderer);
        
        //set background/sky color
        viewPort.setBackgroundColor(ColorRGBA.Cyan);
        
        //add random blue block
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("m_Color", ColorRGBA.Blue);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);
        
        //setup physics state manager
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        //set camera rotation speed
        flyCam.setMoveSpeed(100);
        
        //set clipping distance
        cam.setFrustumFar(10000);

        //initialize level/scene
        gameLevel = new Scene();
                
        //create the player with the starting player position at "playerPos" from the scene
        myPlayer = new Player(gameLevel.getChild("playerPos"));
        
        //setup hud
        hud = new HUD();
        guiNode.attachChild(hud);
        
        //setup key bindings for player actions
        setupKeys();

        //attach level to root node
        rootNode.attachChild(gameLevel);

        //attach player and level to physics space
        getPhysicsSpace().addAll(gameLevel);
        getPhysicsSpace().add(myPlayer.getPhysicsControl());
    }

    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

    @Override
    public void simpleUpdate(float tpf) {
        myPlayer.update(tpf);
        hud.update();
    }

    public Player getPlayer()
    {
        return myPlayer;
    }
    
    private void setupKeys() {
        inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Fire", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("WeaponNext", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping("WeaponPrev", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("WeaponFire", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(myPlayer, "Lefts", "Rights", "Ups", "Downs",
                "Space", "Fire", "WeaponNext", "WeaponPrev", "WeaponFire");
    }

    /** 
     * used for debuging 
     * @param str is called from place in code where you want to check something 
     */
    public void debug(String str) {
        sysout.setText(str);
    }

    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }

    public BitmapFont getGuiFont() {
        return guiFont;
    }

    public AppSettings getSettings() {
        return settings;
    }
   
}
