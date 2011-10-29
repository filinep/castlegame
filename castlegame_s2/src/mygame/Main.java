package mygame;

import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.bullet.BulletAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.shadow.PssmShadowRenderer;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener {

    // SHADOW STUFF:
    PssmShadowRenderer pssmRenderer;
    // MAIN STUFF:
    public Node theRootNode;
    private Node placeholder, scene, floor, tower;
    private Player myPlayer;
    private static BitmapText sysout;//dubug to player screen
    // TESTQ3 STUFF:
    private BulletAppState bulletAppState;
    private Node gameLevel;
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;

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

    @Override
    public void simpleInitApp() {
        pssmRenderer = new PssmShadowRenderer(assetManager, 1024, 4);
        pssmRenderer.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        viewPort.addProcessor(pssmRenderer);
        
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("m_Color", ColorRGBA.Blue);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        flyCam.setMoveSpeed(100);

        //sceneLoader = new SceneLoader();
        //sceneLoader.init(this);

        //app = a;

        //rootNode = rn;

        placeholder = new Node("placeholder");
        placeholder.setLocalTranslation(new Vector3f(0.0f, -2.5f, 0.0f));

        rootNode.attachChild(placeholder);


        /* 
         * the j3o file is loaded into the Node scene. We then dig into scene  
         * to grab its children, the empty playerPos-node, and the two 
         * meshes. Note the children have the same names as those found in 
         * the Scene Explorer 
         */

        gameLevel = (Node) getAssetManager().loadModel(
                "Scenes/aScene/basic.j3o");
        gameLevel.setName("Map");
        floor = (Node) gameLevel.getChild("grass");
        tower = (Node) gameLevel.getChild("tower");
        
        gameLevel.setShadowMode(ShadowMode.CastAndReceive);
        //attach the two meshes to placeholder 
        //placeholder.attachChild(floor);
        //placeholder.attachChild(tower);
        
        /**
         * Create the player with the starting player position at "playerPos" 
         * from the scene
         */
        myPlayer = new Player(gameLevel.getChild("playerPos"));

        /* 
         * get the position of playerPos and print it's x value to the screen using the 
         * debug function defined in Main.  
         */
        Vector3f playervector = myPlayer.getPosition().getLocalTranslation();
        String str = "playervectorEmpty x, y, z is ";
        str += Float.toString(playervector.x) + ", ";
        str += Float.toString(playervector.y) + ", ";
        str += Float.toString(playervector.z);
        System.err.println(str);

        //a light 
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);

        setupKeys();

        this.cam.setFrustumFar(10000);

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White.clone().multLocal(2));
        dl.setDirection(new Vector3f(-1, -1, -1).normalize());
        rootNode.addLight(dl);

        AmbientLight am = new AmbientLight();
        am.setColor(ColorRGBA.White.mult(2));
        rootNode.addLight(am);

        //gameLevel = scene;
        // create the geometry and attach it
        //MaterialList matList = (MaterialList) assetManager.loadAsset("Scene.material");
        //OgreMeshKey key = new OgreMeshKey("main.meshxml", matList);
        //gameLevel = (Node) assetManager.loadAsset(key);
        gameLevel.setLocalScale(5f);

        // add a physics control, it will generate a MeshCollisionShape based on the gameLevel
        gameLevel.addControl(new RigidBodyControl(0));

        rootNode.attachChild(gameLevel);

        getPhysicsSpace().addAll(gameLevel);
        getPhysicsSpace().add(myPlayer.getPhysicsControl());
    }

    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

    @Override
    public void simpleUpdate(float tpf) {
        Vector3f camDir = cam.getDirection().clone().multLocal(0.6f);
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.4f);
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
        //player.setWalkDirection(walkDirection);
        myPlayer.getPhysicsControl().setWalkDirection(walkDirection);
        cam.setLocation(myPlayer.getPhysicsControl().getPhysicsLocation());
    }

    private void setupKeys() {
        inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Fire", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addListener(this, "Lefts");
        inputManager.addListener(this, "Rights");
        inputManager.addListener(this, "Ups");
        inputManager.addListener(this, "Downs");
        inputManager.addListener(this, "Space");
        inputManager.addListener(this, "Fire");
    }

    public void onAction(String binding, boolean value, float tpf) {

        if (binding.equals("Lefts")) {
            if (value) {
                left = true;
            } else {
                left = false;
            }
        } else if (binding.equals("Rights")) {
            if (value) {
                right = true;
            } else {
                right = false;
            }
        } else if (binding.equals("Ups")) {
            if (value) {
                up = true;
            } else {
                up = false;
            }
        } else if (binding.equals("Downs")) {
            if (value) {
                down = true;
            } else {
                down = false;
            }
        } else if (binding.equals("Space")) {
            myPlayer.getPhysicsControl().jump();
        }
        
        if (binding.equals("Fire")) {
            if (value) {
                myPlayer.useWeapon();
            }
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    public Vector3f getBoxPos() {
        return tower.getLocalTranslation();
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
}
