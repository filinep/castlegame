package mygame;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.bullet.BulletAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ScreenController {
    // MAIN STUFF:

    private Player myPlayer;
    private static BitmapText sysout;//debug to player screen
    // TESTQ3 STUFF:
    private BulletAppState bulletAppState;
    private MagicEffect theEffect;
    private GameLogic logic;
    private static Main instance = null;
    private Nifty nifty;

    public static Main get() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }

    private Main() {
    }

    public static void main(String[] args) {
        Main app = Main.get();
        app.start();
    }

    public void addMagicEffect(MagicEffect x) {
        if (theEffect == null) {
            theEffect = x;
        }
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
        //logic = new GameLogic("Models/castle02c00/castle02c00.j3o");

        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                inputManager,
                audioRenderer,
                guiViewPort);
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/nifty.xml", "start", this);

        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);

        inputManager.setCursorVisible(true);

    }

    public void bind(Nifty nifty, Screen screen) {
        System.out.println("bind( " + screen.getScreenId() + ")");
    }

    public void onStartScreen() {
        System.out.println("onStartScreen");
    }

    public void onEndScreen() {
        System.out.println("onEndScreen");
        // init game logic:
        logic = new GameLogic("Models/castle02c00/castle02c00.j3o");
        inputManager.setCursorVisible(false);
    }

    public void quit() {
        nifty.gotoScreen("end");
    }

    public PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (logic != null) {
            logic.update(tpf);
        }
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
