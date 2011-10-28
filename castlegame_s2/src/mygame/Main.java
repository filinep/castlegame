package mygame;

import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.bullet.BulletAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsCharacter;
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
import com.jme3.scene.shape.Sphere;
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
    private Spatial playerPos;
    private static BitmapText sysout;//dubug to player screen
    // TESTQ3 STUFF:
    private BulletAppState bulletAppState;
    private Node gameLevel;
    private PhysicsCharacter player;
    private Vector3f walkDirection = new Vector3f();
    private static boolean useHttp = false;
    private boolean left = false, right = false, up = false, down = false;
    // SHOOTING STUFF:
    Sphere bulletsphere;
    Node bulletnode;
    Geometry geombullet;
    BulletControl bulletcontrol;
    boolean bulletfired;
    int bulletcounter;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        pssmRenderer = new PssmShadowRenderer(assetManager, 1024, 4);
        pssmRenderer.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        viewPort.addProcessor(pssmRenderer);

        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        Geometry geom = new Geometry("Box", b);
        geom.updateModelBound();

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("m_Color", ColorRGBA.Blue);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);

        theRootNode = rootNode;

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
        playerPos = gameLevel.getChild("playerPos");
        floor = (Node) gameLevel.getChild("grass");
        tower = (Node) gameLevel.getChild("tower");

        gameLevel.setShadowMode(ShadowMode.CastAndReceive);
        //attach the two meshes to placeholder 
        //placeholder.attachChild(floor);
        //placeholder.attachChild(tower);

        /* 
         * get the position of playerPos and print it's x value to the screen using the 
         * debug function defined in Main.  
         */
        Vector3f playervector = playerPos.getLocalTranslation();
        String str = "playervectorEmpty x, y, z is ";
        str += Float.toString(playervector.x) + ", ";
        str += Float.toString(playervector.y) + ", ";
        str += Float.toString(playervector.z);
        System.err.println(str);

        //a light 
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);


        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        flyCam.setMoveSpeed(100);
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

        player = new PhysicsCharacter(new CapsuleCollisionShape(1f, 5f), .01f);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(30);

        playerPos = gameLevel.getChild("playerPos");
        Vector3f startLoc = playerPos.getLocalTranslation();
        //startLoc.y -= 5f;
        //startLoc.mult(10f);
        player.setPhysicsLocation(startLoc);

        rootNode.attachChild(gameLevel);

        getPhysicsSpace().addAll(gameLevel);
        getPhysicsSpace().add(player);
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
        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());

        if (bulletfired) {
            if ((bulletcounter-- <= 0) || bulletcontrol.collidedWithMap) {
                rootNode.detachChild(geombullet);
                getPhysicsSpace().remove(bulletcontrol);
                bulletfired = false;
            }
        }
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
            player.jump();
        }
        if (binding.equals("Fire")) {
            if (value) {
                if (!bulletfired) {
                    SphereCollisionShape sphereShape =
                            new SphereCollisionShape(.2f);
                    bulletsphere = new Sphere(3, 4, 1);
                    //pbullet = new PhysicsCharacter(new SphereCollisionShape(.5f), 0f);
                    //bulletnode = new Node();
                    geombullet = new Geometry("Bullet", bulletsphere);
                    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                    mat.setTexture("ColorMap", assetManager.loadTexture("Textures/circuit1.jpg"));
                    geombullet.setMaterial(mat);
                    //bulletnode.attachChild(geombullet);
                    bulletcontrol = new BulletControl(sphereShape, 1f, bulletAppState);
                    getPhysicsSpace().add(bulletcontrol);
                    geombullet.addControl(bulletcontrol);
                    rootNode.attachChild(geombullet);
                    //bulletcontrol.setKinematic(true);
                    bulletcontrol.setGravity(Vector3f.ZERO);
                    bulletcontrol.setPhysicsLocation(cam.getLocation().add(cam.getDirection().mult(2f)));
                    bulletcontrol.setLinearVelocity(cam.getDirection().mult(50f));
                    bulletfired = true;
                    bulletcounter = 50;
                }



            }
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    //getter methods 
    public Vector3f getPlayerPos() {
        return playerPos.getLocalTranslation();
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
}
