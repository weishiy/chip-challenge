/**
 * The application module provides a Graphical User Interface implemented using swing through which the player can see
 * the maze and interact with it through the following keystrokes:
 *
 * CTRL-X - exit the game, the current game state will be lost, the next time the game is started, it will resume from
 *          the last unfinished level
 * CTRL-S - exit the game, saves the game state, game will resume next time the application will be started
 * CTRL-R - resume a saved game -- this will pop up a file selector to select a saved game to be loaded
 * CTRL-1 - start a new game at level 1
 * CTRL-2 - start a new game at level 2
 * SPACE  - pause the game and display a “game is paused” dialog
 * ESC    - close the “game is paused” dialog and resume the game
 * UP, DOWN, LEFT, RIGHT ARROWS -- move Chap within the maze
 *
 * The application window should display the time left to play, the current level, keys collected, and the number of
 * treasures that still need to be collected.  It should also offer buttons and menu items to pause and exit the game,
 * to save the game state and to resume a saved game, and to display a help page with game rules.
 *
 * Note that the actual drawing of the maze is not the responsibility of the application module, but of the rendering
 * module.
 *
 * This module also manages a countdown -- each level has a maximum time associated with it (level 1 -- 1 min), and once
 * the countdown reaches zero, the game terminates with a message informing the user, and then resetting the game to
 * replay the current level.
 *
 * The application package must include an executable class nz.ac.wgtn.swen225.lc.app.Main which starts the game.
 *
 * Graphical user interfaces are notoriously difficult to test, so no unit tests are expected for this package. Testing
 * is to be done manually.
 */
package nz.ac.wgtn.swen225.lc.app;