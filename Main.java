import java.net.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.scene.image.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.io.*;
import java.lang.*;
import javafx.application.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import java.net.*;
import javafx.geometry.*;


public class Main extends Application
{
   FlowPane fp;
   Canvas theCanvas = new Canvas(600,600);
   ArrayList<Mine> mineList = new ArrayList<Mine>();
   float playerx = 300;
   float playery = 300;
   float forceX = 0;
   float forceY = 0;
   boolean collision = false;
   int highScore;
   Player thePlayer;
   Mine mine;

   public void start(Stage stage)
   {
      // Initialize the flowpane and graphicsContext
      fp = new FlowPane();
      fp.getChildren().add(theCanvas);
      gc = theCanvas.getGraphicsContext2D();
      
      // Initialize thePlayer
      thePlayer = new Player(playerx, playery);      
      // Initialize Keylisteners and animationhandler
      fp.setOnKeyPressed(new KeyListenerDown());
      fp.setOnKeyReleased(new KeyListenerUp());
      AnimationHandler animationHandler = new AnimationHandler();
      
      // Set and show the scene/stage
      Scene scene = new Scene(fp, 600, 600);
      stage.setScene(scene);
      stage.setTitle("Project :)");
      stage.show();
      
      // Start animationHandler and request focus to the flowPane
      animationHandler.start();
      fp.requestFocus();
   } 
   
   GraphicsContext gc;
   
   Image background = new Image("stars.png");
   Image overlay = new Image("starsoverlay.png");
   Random backgroundRand = new Random();
// Method to draw the background
   public void drawBackground(float playerx, float playery, GraphicsContext gc)
   {
     //re-scale player position to make the background move slower. 
      playerx*=.1;
      playery*=.1;
   
      //figuring out the tile's position.
      float x = (playerx) / 400;
      float y = (playery) / 400;
      
      int xi = (int) x;
      int yi = (int) y;
      
     //draw a certain amount of the tiled images
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(background,-playerx+i*400,-playery+j*400);
         }
      }
      
     //below repeats with an overlay image
      playerx*=2f;
      playery*=2f;
   
      x = (playerx) / 400;
      y = (playery) / 400;
      
      xi = (int) x;
      yi = (int) y;
      
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(overlay,-playerx+i*400,-playery+j*400);
         }
      }      
   }
   
// KeyListener for when user presses down on a key
   boolean left, right, down, up, noKeyPressed;
   public class KeyListenerDown implements EventHandler<KeyEvent>  
   {
      public void handle(KeyEvent event) 
      { 
         if (event.getCode() == KeyCode.A) // if user presses A set left to true
         {
            left = true;
            noKeyPressed = false; 
         }
         if (event.getCode() == KeyCode.W)  // if user presses W set up to true
         {
            up = true;
            noKeyPressed = false;
         }
         if (event.getCode() == KeyCode.S)  // if user presses S set down to true
         {
            down = true;
            noKeyPressed = false;
         }
         if (event.getCode() == KeyCode.D)  // if user presses D set right to true
         {
            right = true;
            noKeyPressed = false;
         }
      }
   }
   
// Keylistener for when user lifts up a key
   public class KeyListenerUp implements EventHandler<KeyEvent>  
   {
      public void handle(KeyEvent event) 
      { 
         if (event.getCode() == KeyCode.A) // if user realeases A set left to false
         {
            left = false;
         }
         if (event.getCode() == KeyCode.W) // if user realeases W set left to up to false
         {
            up = false;
         }
         if (event.getCode() == KeyCode.S)  // if user realeases S set down to false
         {
            down = false;
         }
         if (event.getCode() == KeyCode.D)  // if user realeases D set right to false
         {
            right = false;
         }
         if (!left && !right && !up && !down) { // if all keys are false set noKeyPressed to true
            noKeyPressed = true;
         }
      }
   }
   
   
   // Store previous grid positions
   int previousGridX = 0;
   int previousGridY = 0;
   
   
// Animation handler
   public class AnimationHandler extends AnimationTimer {
      float backgroundX = 300; // Initial background position
      float backgroundY = 300;
   
      public void handle(long currentTimeInNanoSeconds) {
         gc.clearRect(0, 0, 600, 600); // Clear the canvas
         
         if(thePlayer.checkLife())
         {
         
            thePlayer.act(up, down, left, right, noKeyPressed); // Change force
            forceX = thePlayer.getForceX(); // Get X force
            forceY = thePlayer.getForceY(); // Get Y force
                
         // Update background position based on the forceX and forceY variables
            backgroundX += forceX;
            backgroundY += forceY;
         
         }
         else // if player dies set forces to 0
         {
            forceX = 0;
            forceY = 0;
         }
        // Draw the background with the updated position
         drawBackground(backgroundX, backgroundY, gc); 
         
        // Draw the player at 300,300
         thePlayer.draw(playerx, playery, gc, true);
         
         // Get player position      
         int posX = (int) Math.round(backgroundX);
         int posY = (int) Math.round(backgroundY);
      
         // Set player position
         thePlayer.setX(posX);
         thePlayer.setY(posY);
         
         // Get grid position of the player
         int cgridx = (((int)thePlayer.getX())/100);
         int cgridy = (((int)thePlayer.getY())/100);
        
         // Get the score by calculating players distance from the origin
         int scoreX = posX - 300; // subtract starting position
         int scoreY = posY - 300;
         double scoreDouble = (Math.sqrt(scoreX *scoreX +  scoreY * scoreY));
         int score = (int)scoreDouble; // Convert score to integer to remove decimals
         
         
         // Update and display score and highscore
         gc.setFill(Color.WHITE);
         gc.fillText("Score is: " + score, 20, 20);
         gc.fillText("High Score is: " + highScore, 20, 50);
         
         // Display mines
         displayMines(backgroundX, backgroundY);
         checkCollision(); // check if user hits a mine
         setHighScore(score); // call highscore method
         
         // Create mines when entering new grid
         if (cgridx != previousGridX || cgridy != previousGridY) {
            previousGridX = cgridx;
            previousGridY = cgridy;
            generateMines(cgridx, cgridy);
         }
      }
   }
   
   // Initialize random
   Random rand = new Random();
   int mineCount = 0; 
   
// Method to generate mines around the player
   public void generateMines(int cgridx, int cgridy)
   {                
      int randCordX;
      int randCordY;
      int mineCheck;
      double distanceToOrigin;
      int n;
      int i;
      int mineX;
      int mineY;
      int x;
      int y;
      
// Create mines to the right of the player
      x=5; // Initialize x grid
      for(y=-4; y<6; y++)// loop through y's grid positions
      {
         // Calculate each grids distance to origin
         distanceToOrigin = (Math.sqrt((((cgridx + x)*100)-(300))*(((cgridx + x)*100)-(300)) +  (((cgridy + y)*100-(300))*((cgridy + y)*100-(300)))));
         n = (int) (distanceToOrigin) /1000; // Get n (number of possible mines per grid)

         for(i=0; i<n; i++) // create up to n mines
         {
            mineCheck = rand.nextInt(0, 100);
            if (mineCheck < 30) // 30% chance to create each mine
            {
               // get random coordinates for each mine
               randCordX = rand.nextInt(0, 100);
               randCordY = rand.nextInt(0, 100);
                        
               // Calculate positons for the mines
               mineX = (cgridx * 100) + (x * 100) - randCordX;
               mineY = ((cgridy*100) + (y*-100) + randCordY);
                        
               // Create a new mine
               mine = new Mine(mineX, mineY);
               mine.randColor(); //  set starting color for the mine
                                      
               mineList.add(mine); // Add the new mine to the arraylist
            }
         }
      }
            
// Create mines to the left of the player
      x=-5; // Initialize x grid
      for(y=-4; y<6; y++) // loop through y's grid positions
      {
         // Calculate each grids distance to origin
         distanceToOrigin = (Math.sqrt((((cgridx + x)*100)-(300))*(((cgridx + x)*100)-(300)) +  (((cgridy + y)*100-(300))*((cgridy + y)*100-(300)))));
         n = (int) (distanceToOrigin) /1000; // Get n (number of possible mines per grid)
            
         for(i=0; i<n; i++) // create up to n mines
         {
            mineCheck = rand.nextInt(0, 100);
            if (mineCheck < 30) // 30% chance to create each mine
            {
               // get random coordinates for each mine
               randCordX = rand.nextInt(0, 100);
               randCordY = rand.nextInt(0, 100);
                     
               // Calculate positons for the mines
               mineX = (cgridx * 100) + (x * 100) - randCordX;
               mineY = ((cgridy*100) + (y*-100) + randCordY);
                  
               // Create a new mine      
               mine = new Mine(mineX, mineY);
               mine.randColor(); //  set starting color for the mine
                                       
               mineList.add(mine); // Add the new mine to the arraylist
            }
         }
      }
      
// Create mines below the player
      y=5; // Initialize y grid
      for(x=-4; x<6; x++) // loop through x's grid positions
      {
         // Calculate each grids distance to origin
         distanceToOrigin = (Math.sqrt((((cgridx + x)*100)-(300))*(((cgridx + x)*100)-(300)) +  (((cgridy + y)*100-(300))*((cgridy + y)*100-(300)))));
         n = (int) (distanceToOrigin) /1000; // Get n (number of possible mines per grid)
        
            for(i=0; i<n; i++) // create up to n mines
            {
               mineCheck = rand.nextInt(0, 100); 
               if (mineCheck < 30) // 30% chance to create each mine
               {
                  // get random coordinates for each mine
                  randCordX = rand.nextInt(0, 100);
                  randCordY = rand.nextInt(0, 100);
                  
                  // Calculate positons for the mines
                  mineX = (cgridx * 100) + (x * 100) - randCordX;
                  mineY = ((cgridy * 100) + ((y)*100) - randCordY);
                       
                  // Create a new mine                              
                  mine = new Mine(mineX, mineY);
                  mine.randColor(); //  set starting color for the mine
                  
                  mineList.add(mine); // Add the new mine to the arraylist
               }
            }
      }
             
// Create mines above the player
      y=-5; // Initialize y grid
      for(x=-4; x<6; x++) // loop through x's grid positions
      {
         // Calculate each grids distance to origin
         distanceToOrigin = (Math.sqrt((((cgridx + x)*100)-(300))*(((cgridx + x)*100)-(300)) +  (((cgridy + y)*100-(300))*((cgridy + y)*100-(300)))));
         n = (int) (distanceToOrigin) /1000; // Get n (number of possible mines per grid)
                     
         for(i=0; i<n; i++) // create up to n mines
         {
            mineCheck = rand.nextInt(0, 100);
            if (mineCheck < 30) // 30% chance to create each mine
            {
               // get random coordinates for each mine
               randCordX = rand.nextInt(0, 100);
               randCordY = rand.nextInt(0, 100);
                     
               // Calculate positons for the mines
               mineX = (cgridx * 100) + (x * 100) - randCordX;
               mineY = ((cgridy*100) + (y*100) - randCordY);
                     
               // Create a new mine  
               mine = new Mine(mineX, mineY);
               mine.randColor(); //  set starting color for the mine
                        
               mineList.add(mine); // Add the new mine to the arraylist
            //          System.out.println(mine.getX() + "  |-----|  " + mine.getY());
            }
         }
      }   
    }
   
// Method to display the mines
   public void displayMines(float backgroundX, float backgroundY) {
      for (int i = 0; i < mineList.size(); i++) { // Loop through all the mines
         Mine mine = mineList.get(i);
         // Alternate mines between red and white
         mine.opacity();
         // Draw the mines
         mine.draw(backgroundX, backgroundY, gc, false);
         // If mine is 800 px away remove it
         if (Math.abs(thePlayer.distance(mineList.get(i))) > 800)
         {         
            mineList.remove(i);
         }
      }
   }
   
// Check if player hits a mine
   public void checkCollision()
   {
      for(int i=0; i<mineList.size(); i++) // loop through all the mines
      {
         Mine mine = mineList.get(i);
         
         // get center coordiantes of player and mine
         double playerCenterX = thePlayer.getX() + 13.5;
         double playerCenterY = thePlayer.getY() + 13.5;
         double mineCenterX = mine.getX() + 6;
         double mineCenterY = mine.getY() + 6;
         
         // calculate distance between player and mine
         double distance = (Math.sqrt((mineCenterX-(playerCenterX))*(mineCenterX-(playerCenterX)) +  (mineCenterY-(playerCenterY))*(mineCenterY-(playerCenterY))));
      
         if(distance<=20 && distance >= -20) // if mine and player are within 20px collision = true
         {
            mineList.remove(i); // remove the offending mine
            thePlayer.remove(); // remove the player
            collision = true; // set collision to true
         }
      }     
   }

// Method to check/set highscore
   public void setHighScore(int score) {
      File scoreFile = new File("score.txt"); // assign the scoreFile to score.txt
      try {
         if (!scoreFile.exists()) { // Check if score.txt exists
            scoreFile.createNewFile(); // Create the file if it doesn't exist
         }
      
        // Read the current highscore from the file
         Scanner scan = new Scanner(scoreFile);
         if (scan.hasNextInt()) {
            highScore = scan.nextInt();
         }
         scan.close(); // close the file
      
        // Check if the current score is higher than the highscore saved
         if (score > highScore) {
            highScore = score; // update high score to current score
         
            // Write the new high score to the file
            PrintWriter pw = new PrintWriter(scoreFile);
            pw.print(highScore);
            pw.close(); // clost the file
         }
      } 
      catch (IOException e) //
      {
         System.out.println("File Error");
      }
   }

   
   public static void main(String[] args)
   {
      launch(args);
   }
}

