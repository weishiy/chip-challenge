## High level design

The project uses MVC (Model-View-Controller) pattern, where Domain is model, Renderer is view and Application is
controller. Communication from domain (model) to renderer (view)/application (controller) is through Observer
pattern, where domain is observable and renderer (view)/application (controller) are observers.

The project uses java.awt.Timer to trigger ticks. In each tick, domain (model) get updated (with/without player/enemy
inputs), and it notifies renderer (view)/application (controller) about its internal state changes through various game
events. There are different timers, sitting in different modules and used to
drive different workflows: gameplay, replay and fuzz testing.

Putting it together, here is how the system works under different workflows:

**Gameplay**

<img alt="gameplay.png" src="images/gameplay.png" width="640">

A Timer fires ticks every 1.0/Game.FRAME_RATE seconds:

1. In each tick Application polls user/enemy inputs and sends them to Recorder for recording
2. Application sends the inputs to GameEngine
3. GameEngine updates Domain using those inputs
4. Domain fires GameEvent(s) when its internal state changes. Renderer render(repaint) itself as needed when it receives
   those events.
5. GameEngine updates itself as needed when it receives those events.

**Replay**

<img alt="replay.png" src="images/replay.png" width="640" >

A Timer fires ticks every 1.0/Game.FRAME_RATE (adjustable) seconds:

1. In each tick Replayer gets user/enemy inputs from historical records and send them to GameEngine
2. GameEngine updates Domain using those inputs
3. Domain fires GameEvent(s) when its internal state changes. Renderer render(repaint) itself as needed when it receives
   those events.
4. GameEngine updates itself as needed when it receives those events.

**Fuzz Testing**

<img alt="fuzz_test.png" src="images/fuzz_test.png" width="640" >

A Timer fires ticks:

1. In each tick Fuzz generates a random player input and sends it to GameEngine
2. GameEngine updates Domain using those inputs
3. Domain fires GameEvent(s) when its internal state changes. Renderer render(repaint) itself as needed when it receives
   those events.
4. GameEngine updates itself as needed when it receives those events.

**GUI**

Here're the layers of GUI:

<img alt="gui.png" src="images/gui.png" width="640" >

* **Application GUI**. Created by Application module. The main window, which is normally a JFrame, of the application.
* **HUB (glass pane)**. Created by Application module. A JPanel (or other alternatives) that's laid on top of the game 
  window. This pane can be used to place move control buttons, player inventory, game stats (level no, chips left, 
  timer, etc) and messages (Paused, Replay Ended, etc)   
* **Game window**. Created by Renderer module. The game window used to render the game.    
 


### Domain

Domain is where most of the game models seat.

It has the following entities:

```
Entity                -> Root class for all entities in the game. 
    Game              -> Presents a game. Contains methods to update the game's interal state and notify other module 
                         about those state changes.  
    Level             -> Presents a level (or maze/board). Contains what's on the board: tiles (excluding free tiles), 
                         enemies, player, etc  
    Tile              -> Parent class for all tiles
        KeyTile       -> Presents a key tile
        ChipTile      -> Presents a chip tile
        DoorTile      -> Presents a door tile
        ... 
    Character         -> Parent class for Player and Enemy
        Player        -> Presents a player
        Enemy         -> Presents an enemy
    Key               -> Presents a key, which is used by KeyTile and Player
    Chip              -> Presents a chip, which is used by ChipTile and Player
```

Among them:

```
Tile:
    boolean isEnterable(Player player)  -> Decides whether player can enter this tile
    void onEnter(Player player)         -> Called when player enter the tile. Different tile has different behavour. 
                                           KeyTile.onEnter(Player) add the key to player's inventory, remove itself from
                                           the level (board), and fire a ChipConsumedEvent
    void onExit(Player player)          -> Called when player exit the tile
```

Domain fires follow game events:

```
GameEvent                   -> Parent class for all events
    -- level (board) changed events --
    PlayerMovedEvent        -> Fires when position of the player changed
    EnemyMovedEvent         -> Fires when position of an enemy changed
    KeyPickedUpEvent        -> Fires when a key is picked up by a player
    KeyConsumedEvent        -> Fires when a key is consumed (to unlock a door)
    ChipPickedUpEvent       -> Fires when a chip is picked up by a player
    DockUnlockedEvent       -> Fires when a door is unlocked
    ExitLockUnlockedEvent   -> Fires when the exit lock is unlocked
    InfoFieldPressedEvent   -> Fires when player entered a info field 
    InfoFieldReleasedEvent  -> Fires when player exited the info field
    -- game status changed events
    GameOverEvent
        PlayerWonEvent      -> Fires when player reaches the exit
        PlayerDiedEvent     -> Fires when player is caught by an enemy
        TimeoutEvent        -> Fires when countdown reaches 0
    -- timer events --
    TickEvent               -> Fires at the end of every tick
    CountDownEvent          -> Fires every second (PS: countdown decrease 1)
    TimeoutEvent            -> Fires when level timed out
```

### Application

Application is responsible for putting the system together. It renders application UI, handles menu events (keystrokes),
initializes gameplays/replays and instantiates Persistence, Game, Renderer and Recorder to help with the processes, and
schedule updates(ticks) for the gameplay.

Application can implement a GameEngine interface. Then it, Recorder.Replayer and Fuzz can use it to update the game for
gameplay/replay/fuzz testing respectively.

```
GameEngine                    -> Represents a game engine. The most important methods in this interface are update(*) 
                                 methods below which update the game (domain) with provided/auto-generated inputs. These
                                 update(*) methods can be used by application itself, recorder or fuzz to "tick" the 
                                 gameplay/replay (replay is also a gameplay, it, just instead of polling user inputs, 
                                 retrieves inputs from historical records) 
    void onStart();           -> Called when gameplay is about to start
    boolean isGameOver();     -> Returns whether current game is over
    int getTickNo();          -> Returns current tick no.
    void update(Vector2D playerMovement);
                              -> Updates the game (domain) with provided player input (the only allowed input is an 
                                 one-tile-movement, i.e. one of Vector2D.LEFT, Vector2D.UP, Vector2D.RIGHT, 
                                 Vector2D.DOWN) and auto enemies inputs (Game engine needs to poll each enemy for input 
                                 via enemy.nextInput)
    void update(Vector2D playerMovement, Map<Enemy, Vector2D> enemyMovement);
                              -> Updates the game (domain) with provided player and enemy inputs
    void onDestroy();         -> Called after gameplay is ended
    void bindInputWithAction(KeyStroke keyStroke, Consumer<ActionEvent> callback);
                              -> Binds provided keystore with provided callback. When replaying, recorder needs to use 
                                 this methods to register its controls.  
    void unbindInputWithAction(KeyStroke keyStroke);
                              -> Unbinds provided keystore.
    Container getGlassPane(); -> Returns the glass pane. The game engine should layer a transparent (glass) pane on top 
                                 of the game window. This pane can be helpful in many cases, e.g. when game is paused, 
                                 grey out the game window and display "Paused".     
```

Here is how Application code can look like:

Gameplay

```
public class Gameplay implements GameEventListener {
    private Persistence persistence;
    private Game game;
    private GameEngine gameEngine;
    private Recorder recorder;
    private Timer timer;
    
    public Gameplay(int levelNo) {
        persistence = new PersistenceImpl();
        game = persistence.loadGame(levelNo);
        gameEngine = new GameEngineImpl(game);
        recorder = new RecorderImpl(persistence, game);
       
        timer = new Timer(1000 / Game.FRAME_RATE, e -> update());
        
        gameEngine.onStart();
        recorder.onStart();
        game.addListener(this);
        bindKeyStrokes();
        
        timer.start();
    }
    
    private void update() {
        var enemyMovementMap = game.getLevel()
                                   .getEnemies()
                                   .stream()
                                   .collect(Collectors.toMap(e -> e, Enemy::nextMove));
        recorder.update(playerMovement, enemyMovementMap);
        gameEngine.update(playerMovement, enemyMovementMap);
        playerMovement = Vector2D.ZERO;
    }
    
    
    private void bindKeyStrokes() {
        gameEngine.bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
                e -> playerMovement = Vector2D.LEFT);
        gameEngine.bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
                e -> playerMovement = Vector2D.UP);
        gameEngine.bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
                e -> playerMovement = Vector2D.RIGHT);
        gameEngine.bindInputWithAction(
                KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
                e -> playerMovement = Vector2D.DOWN);
    }

    private void unbindKeyStrokes() {
        gameEngine.unbindInputWithAction(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
        gameEngine.unbindInputWithAction(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
        gameEngine.unbindInputWithAction(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
        gameEngine.unbindInputWithAction(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
    }
    
    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent instanceof GameOverEvent g) {
            unbindKeyStrokes();
        
            timer.stop();
            game.removeListener(this);
            recorder.onDestroy();
            gameEngine.onDestroy();
        }
    }
}
```

Replay

```
public class Replay implements GameEventListener {
    private Game game;
    private GameEngine gameEngine;
    private Recorder recorder;
    private Timer timer;
    
    public Replay(File file) {
        var playback = (new PersistenceImpl()).loadPlayback(file);
        game = playback.getSince();
        gameEngine = new GameEngineImpl(game);
        replayer = new ReplayerImpl(gameEngine, playback);  // ReplayerImpl is class of Recorder module. 
        
        gameEngine.onStart();
        // replayer has an internal timer which updates gameEngine afterwards.
        replayer.onStart();
        game.addListener(this);
    }
    
    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent instanceof GameOverEvent g) {
            game.removeListener(this);
            replayer.onDestroy();
            gameEngine.onDestroy();
        }
    }
}
```

It also needs to implement ApplicationDebugger for Fuzz to create a new game in debug mode and get the engine.

```
ApplicationDebugger           -> Application itself should implement this interface. During testing, Fuzz will create
                                 the application using application's constructor and then starts a new game in debug 
                                 mode.
    GameEngine newGameInDebugMode(int levelNo);
                              -> Starts a new game at provided level in debug mode. In debug mode, application does not
                                 schedule game updates, i.e. does not "tick". 
```

### Renderer

Renderer is responsible to render the game window. (PS: Application will render the outer application UI.)

It needs to:

* Extends java.awt.Component so that application can attach it to the main JFrame instance.
* Overwrite java.awt.Component.setEnabled(boolean) and subscribe/unsubscribe game events in that method.
    * A reference to current Game instance will be provided through constructor.
    * IMPORTANT Do not subscribe game events in the constructor immediately. Do it in setEnabled(boolean). Application
      will call later when time fits.
* Use the game events received to decide when to repaint itself by itself.

### Recorder

Recorder is responsible for:

* Record player and enemies inputs.
* Drive a replay process using an interface of Application. PS: Refer to replay workflow on the top of the page.

Here are the models it may need:

```
Playback
    Game since                            -> A snapshot of Game object when recording starts.
    List<Moment> moments                  -> A list of Moment (see below) that occured during the gameplay.
    int endTickNo                         -> The tick no when recording stops. When replaying, record ends the replay 
                                             when reaching this.
Moment                                    -> Represents a tick when there is either a player input or an enemy input
    int tickNo                            -> The tick when the input(s) happened
    Vector2D playerMovement               -> Player's input (the only allowed input is an one-tile-movement, one of 
                                             Vector2D.LEFT, Vector2D.UP, Vector2D.RIGHT, Vector2D.DOWN)
    Map<Enemy, Vector2D> enemyMovementMap -> Each enemies' input (the only allowed input is an one-tile-movement, one of
                                             Vector2D.LEFT, Vector2D.UP, Vector2D.RIGHT, Vector2D.DOWN)
```

Recorder needs to implement below two interfaces:

```
Recorder                  -> Represents a recorder which record player/enemy inputs. The current game instance will be
                             passed by application in constructor.
    void onStart()        -> Called by application when a gameplay is about to start. 
    void update(Vector2D playerMovement, Map<Enemy, Vector2D> enemyMovementMap)
                          -> Called by application in every tick.
    void onDestroy()      -> Called by application after gameplay is ended. In this method Recorder needs to persist the 
                             Playback object. Recorder needs to decide by itself where (under which folder and with
                             what filename) to store the playback. 
Replayer                  -> Represents a replayer which drives the replaying process. A GameEngine instance will be 
                             passed by application in constructor. Replayer can then use this GameEngine instance to 
                             manuplate the game.
    void onStart();       -> Called by application when a replay is about to start. Relayer should take control from here onwards.
    void onDestroy();     -> Called by application when the replay needs to exit. (E.g when application receives a new
                             game request from user.) 
```

### Persistence

Persistence is responsible for serialize java objects into json texts and saving them in disk. And vise visa. Note when
deserialize java objects from json texts, the relationship between objects should also be recovered. E.g. a Game
object can have a Level object as a member. This relationship needs also persisted and recovered.

It needs to implement below interface:

```
interface Persistence:
    List<Integer> loadAllLevelNos()                 - Returns all available level nos in a list
    Game loadGame(int levelNo)                      - Creates/compose a new Game object at provided level, return it
    Game loadGame(File save)                        - Load a previous saved Game object (in json format), deserialize it
                                                      back to a Game object, and return the latter  
    void saveGame(File save, Game game)             - Serialize the Game object into a json text and save the text to 
                                                      disk
    Playback loadPlayback(File save)                - Load a previous saved Playback object (in json format), 
                                                      deserialize it back to a Playback object, and return the latter
    void savePlayback(File save, Playback playback) - Serialize the Playback object into a json text and save the text
                                                      to disk
```

### Fuzz

There are two interfaces provided in Application for Fuzz to work. At the start, fuzz needs to create an application
using its constructor. The application will implement ApplicationDebugger from which fuzz can start a new game at a
provided level in debug mode (In debug mode, application does not schedule game updates, i.e. does not "tick") using
ApplicationDebugger.newGameInDebugMode(int levelNo). Fuzz then can use the returned GameEngine object to control the
game. (Have a look of the specification of GameEngine interface above in Application to see what's available. )


**UML**

<img alt="uml.png" src="uml.png" width="640">
