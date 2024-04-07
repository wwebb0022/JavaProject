import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import java.io.*;
import java.util.*;

// Player Object
public class Player extends DrawableObject
{

   private float forceX;
   private float forceY;
   private boolean left, right, up, down, noKeyPressed;
   private String dir;
   private boolean life = true;
   private double playerOp = .20;
   private boolean opUp = true;
   
// Takes in players position
   public Player(float x, float y)
   {
      super(x,y);
   }
   
// Change force based on key user pressed
   public void act(boolean up, boolean down, boolean left, boolean right, boolean noKeyPressed)
   {
      if(life) // check if player is alive
      {
         if(up) // if user presses w decrease y force by .10
         {
            forceY -= 0.10;
            setLimits();
         }
         
         else if(down) // if user presses s increase y force by .10
         {
            forceY+=0.10;
            setLimits();
         }
      
         if(left) // if user presses a decrease x force by .10
         {
            forceX-=0.10;
            setLimits();
         }
         
         else if(right) // if user presses d increase x force by .10
         {
            forceX+=0.10;
            setLimits();
         }  
         // call subforce method to decrease force if key is not pressed
         subForce(up, down, left, right, noKeyPressed);
      }
   }
   
// Method to decrease player force
   public void subForce(boolean up, boolean down, boolean left, boolean right, boolean noKeyPressed)
   {
      if(life) // check if player is alive
      {  
      
         // decrease forceX until 0 if A or D is not pressed
         if((up || down) && !(left || right))
         {
            if(forceX > .26)
               forceX -= .025;
            else if(forceX < -.26)
               forceX += .025;
            else if(forceX >= -.25 && forceX <= .25)
               forceX = 0;
         }
         // decrease forceY until 0 if w or s is not pressed
         if((left || right) && !(up || down))
         {
            if(forceY > .26)
               forceY -= .025;
            else if(forceY < -.26)
               forceY += .025;
            else if(forceY >= -.25 || forceY <= .25)
               forceY = 0;  
         }
         // decrease forceX and forceY  until 0 if no keys are pressed pressed
         if(noKeyPressed)
         {
            if(forceY > .26)
               forceY -= .025;
            else if(forceY < -.26)
               forceY += .025;
            else if(forceY >= -.25 || forceY <= .25)
               forceY = 0; 
            if(forceX > .26)
               forceX -= .025;
            else if(forceX < -.26)
               forceX += .025;
            else if(forceX >= -.25 || forceX <= .25)
               forceX = 0;  
         }
         
      }
      else // if player is dead set force to 0
      {
         forceX = 0;
         forceY = 0;
      }
   }
   
// Method to set max force to 5 or -5
   private void setLimits() 
   {
      if (forceY >= 5)
      {
         forceY = 5;
      }
      if (forceY <= -5)
      {
         forceY = -5;
      }
      if (forceX >= 5)
      {
         forceX = 5;
      }
      if (forceX <= -5)
      {
         forceX = -5;
      }  
   }
   
// Method to get X force
   public float getForceX()
   {
      return forceX;
   }
   
// Method to get Y force
   public float getForceY()
   {
      return forceY;
   }
   
// Method to draw the player to the canvas
   public void drawMe(float x, float y, GraphicsContext gc)
   {
      Random rand = new Random();
      double r = rand.nextDouble();
      double g = rand.nextDouble();
      double b = rand.nextDouble();
      if(life)
      {
         gc.setFill(Color.BLACK);
         gc.fillOval(x,y,27,27);
         gc.setFill(Color.GRAY);
         gc.fillOval(x+1,y+1,25,25);
         gc.setFill(Color.BLACK);
         gc.fillOval(x+4,y+4,19,19);
         gc.setFill(new Color(.5,1,1,playerOp));
         gc.fillOval(x+6,y+6,15,15);
        
         gc.setFill(new Color(1,1,1,.5));
         gc.fillOval(x+9,y+9,9,9);
         playerOpacity();
      }
   }
   
// Method to change the players opacity
   public void playerOpacity()
   {
      if(opUp) // increase opacity until .60
      {
         playerOp+=.01;
         if (playerOp >= .6)
         {
            playerOp = .6;
            opUp = false; // switch to decrease
         }
      }
      else // decrease opacity until .20
      {
         playerOp-=.01;
         if(playerOp <= .20)
         {
            playerOp = .20;
            opUp = true; // switch to increase
         }
      }
   }

// remove player by setting life to false
   public void remove()
   {
      life = false;
   }
   
// method to check if player is alive
   public boolean checkLife()
   {
      return life;
   }
   
}
