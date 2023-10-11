package nz.ac.wgtn.swen225.lc.fuzz;

import nz.ac.wgtn.swen225.lc.app.Application;
import nz.ac.wgtn.swen225.lc.app.GameEngine;
import nz.ac.wgtn.swen225.lc.utils.Vector2D;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.time.Duration.ofMinutes;

/**
 * The class containing the fuzz tests for levels 1
 * and 2 for the "Chips Challenge" game.
 *
 * @author Oliver Berry
 * Student ID: 300474410
 */
public class FuzzTests {

    private static final Random RANDOM = new Random();

    private GameEngine engine;
    private Timer timer;

    private Vector2D pos;



    private Set<Vector2D> hasBeen;

    /**
     * Runs the fuzz test for level 1, if the game ends normally or it runs for 1 minute, nothing happens.
     * If an error occurs while running, it makes an issue on the github with a title of the error and a description
     * that is the cause of the error assigned to the user in charge of the package where the error was found.
     */

    @Test
    public void test1() {
        // Starts the game in debug mode.
        SwingUtilities.invokeLater(() -> {
            Application app = new Application();
            engine = app.newGameInDebugMode(1);
        });

        // nextTick() is and needs to be running in the UI thread, a different thread of this. It will need this
        // CompletableFuture object to notify test1 whether the game has completed or there is an exception.
        var future = new CompletableFuture<Void>();

        hasBeen = new HashSet<Vector2D>();

        Assertions.assertTimeoutPreemptively(ofMinutes(1), () -> {
            // java.awt.Timer fires in the UI thread.
            timer = new Timer(100, e -> {
                try {
                    // update1() happens in UI thread
                    nextTick(future);
                } catch (RuntimeException ex) {
                    // notify test1() thread that there is an exception
                    future.completeExceptionally(ex);
                }
            });
            // wait 1 second for application to fully start up
            timer.setInitialDelay(1000);
            // start testing
            timer.start();

            try {
                // wait for testing to complete or error out
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                // prints a stacktrace
                System.out.println("test1 met an exception");
                e.printStackTrace();

                // a bit of setup to find who to assign the issue to
                int assignee_id = -1;

                // gets the package of the class which caused the error
                String pName = e.getClass().getPackageName();

                // checks which package it is and assigns the relevant user id
                if (pName.contains("app")){
                    // assign to Shuja
                    assignee_id = 2730;
                } else if (pName.contains("domain")){
                    // assign to Shiyan
                    assignee_id = 2952;
                } else if (pName.contains("renderer")){
                    // assign to Jeremy
                    assignee_id = 3306;
                } else if (pName.contains("recorder")){
                    // assign to Sajja
                    assignee_id = 3291;
                } else if (pName.contains("persistency")){
                    // assign to Brett
                    assignee_id = 3287;
                }

                // checks if an ID was assigned, if it was makes an issue assigned to that id
                // uses the error.toString as the title and the cause as the description
                if (assignee_id!=-1) {
                    GitLabIntegration.reportIssueWithAssign(e.toString(), e.getCause().toString(), ""+assignee_id);

                    // otherwise just raises an issue without assignment
                } else {
                    System.out.println(
                            "Error when trying to assign to module, raising as issue without assignment instead.");
                    GitLabIntegration.reportIssueNoAssign(e.toString(), e.getCause().toString());
                }
            }
        });
    }


    /**
     * Runs the fuzz test for level 2, if the game ends normally or it runs for 1 minute, nothing happens.
     * If an error occurs while running, it makes an issue on the github with a title of the error and a description
     * that is the cause of the error assigned to the user in charge of the package where the error was found.
     */
    @Test
    public void test2() {
        // Starts the game in debug mode.
        SwingUtilities.invokeLater(() -> {
            Application app = new Application();
            engine = app.newGameInDebugMode(2);
        });

        // nextTick() is and needs to be running in the UI thread, a different thread of this. It will need this
        // CompletableFuture object to notify test1 whether the game has completed or there is an exception.
        var future = new CompletableFuture<Void>();


        hasBeen = new HashSet<Vector2D>();

        Assertions.assertTimeoutPreemptively(ofMinutes(1), () -> {
            // java.awt.Timer fires in the UI thread.
            timer = new Timer(100, e -> {
                try {
                    // update1() happens in UI thread
                    nextTick(future);
                } catch (RuntimeException ex) {
                    // notify test1() thread that there is an exception
                    future.completeExceptionally(ex);
                }
            });
            // wait 1 second for application to fully start up
            timer.setInitialDelay(1000);
            // start testing
            timer.start();

            try {
                // wait for testing to complete or error out
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                // prints a stacktrace
                System.out.println("test2 met an exception");
                e.printStackTrace();

                // a bit of setup to find who to assign the issue to
                int assignee_id = -1;

                // gets the package of the class which caused the error
                String pName = e.getClass().getPackageName();

                // checks which package it is and assigns the relevant user id
                if (pName.contains("app")){
                    // assign to Shuja
                    assignee_id = 2730;
                } else if (pName.contains("domain")){
                    // assign to Shiyan
                    assignee_id = 2952;
                } else if (pName.contains("renderer")){
                    // assign to Jeremy
                    assignee_id = 3306;
                } else if (pName.contains("recorder")){
                    // assign to Sajja
                    assignee_id = 3291;
                } else if (pName.contains("persistency")){
                    // assign to Brett
                    assignee_id = 3287;
                }

                // checks if an ID was assigned, if it was makes an issue assigned to that id
                // uses the error.toString as the title and the cause as the description
                if (assignee_id!=-1) {
                    GitLabIntegration.reportIssueWithAssign(e.toString(), e.getCause().toString(), ""+assignee_id);

                    // otherwise just raises an issue without assignment
                } else {
                    System.out.println(
                            "Error when trying to assign to module, raising as issue without assignment instead.");
                    GitLabIntegration.reportIssueNoAssign(e.toString(), e.getCause().toString());
                }
            }
        });
    }


    /**
     * This method is used to progress the game by the tests above. It also is where
     * the semi-intelligent random movement is handled. It does this by keeping track of the places
     * that the player has attempted to move to. Every tick it will check all the adjacent positions which could be
     * moved to. Any position which hasn't been moved to before will be put into a list and a random position from
     * these will be selected and moved to. If the list is empty because the player has already attempted to move to
     * all adjacent positions, then any of the adjacent positions will be chosen at random.
     */

    private void nextTick(CompletableFuture<Void> future) {


        if (engine.isGameOver()) {
            timer.stop();
            future.complete(null);
            return;
        }


        /*
        This only works if I have a way to access the player position, but if I am allowed
        to read that field then I am able to make the movement of the test smarter by making it
        prefer to avoid places which it has already been (It may require a manual implementation
         of an equals method in Vector2D). The code would be as follows:
        */

        pos = engine.getPlayerPos();
        // checks if the player has already tried to go to the square, if not adds it
        // to the randomMoveList as a possible move.
        List<Integer>randomMoveList = new ArrayList<Integer>();
        if(!hasBeen.contains(pos.add(Vector2D.LEFT))){
            randomMoveList.add(0);
        }
        if(!hasBeen.contains(pos.add(Vector2D.RIGHT))){
            randomMoveList.add(1);
        }
        if(!hasBeen.contains(pos.add(Vector2D.UP))){
            randomMoveList.add(2);
        }
        if(!hasBeen.contains(pos.add(Vector2D.DOWN))){
            randomMoveList.add(3);
        }

        // makes the move completely random if all adjacent squares have been moved to
        if (randomMoveList.isEmpty()){
            randomMoveList.add(0);
            randomMoveList.add(1);
            randomMoveList.add(2);
            randomMoveList.add(3);
        }

        //gets a random integer for the switch from the list of moves
        int randomMove = randomMoveList.get(RANDOM.nextInt(0,randomMoveList.size()));
        //moves the player
        switch (randomMove) {
            case 0 -> {engine.update(Vector2D.LEFT);
                hasBeen.add(pos.add(Vector2D.LEFT));}
            case 1 -> {engine.update(Vector2D.RIGHT);
                hasBeen.add(pos.add(Vector2D.RIGHT));}
            case 2 -> {engine.update(Vector2D.UP);
                hasBeen.add(pos.add(Vector2D.UP));}
            case 3 -> {engine.update(Vector2D.DOWN);
                hasBeen.add(pos.add(Vector2D.DOWN));}
            default -> {
                // do nothing
            }
        /*
        Note that in the switch cases above, the Vector2D is added to the hasBeen list whether or not
        the player successfully moves to that position. This is so the fuzz tester won't repeatedly run
        into a wall, particularly when all of the other squares have been covered.
         */

        }
    }

}
