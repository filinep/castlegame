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
    
    private MagicEffect theEffect;
    private GameLogic logic;
    
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

        //setup physics state manager
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        //set camera rotation speed
        flyCam.setMoveSpeed(100);
        
        //set clipping distance
        cam.setFrustumFar(10000);
        
        // init game logic:
        logic = new GameLogic("Models/castle02c00/castle02c00.j3o");
 
    }
   

    public PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

    @Override
    public void simpleUpdate(float tpf) {
        logic.update(tpf);
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
