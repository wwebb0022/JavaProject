import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import java.util.*;
import java.io.*;
import java.text.*;

//this is an example object
public class Mine extends DrawableObject
{

   private float x;
   private float y;
   private int mineX;
   private int mineY;
   private int playerGridX;
   private int playerGridY;
   private int randX;
   private int randY;

   
   // color changing variables
   private double opacityR;
   private double opacityW;
   private int randColor;
   private boolean redToWhite;
   private Color red = new Color(1,0,0,opacityR);
   private Color white = new Color(1,1,1,opacityW);

   Random rand = new Random();

// Takes in the mines position
   public Mine(float x, float y)
   {
      super(x,y);
      this.x = x;
      this.y = y;
   }
   
// Start at either red or white at random
   public void randColor()
   {
      randColor = rand.nextInt(1,3); // Generate 1 or 2
      if(randColor==1) // if 1 then set color to red
      {
         opacityR = 1;
         opacityW = 0;
         redToWhite = true; // set red to white to true
      }
      else // if 2 then set color to white
      {
         opacityR = 0;
         opacityW = 1;
         redToWhite = false; // set red to white as false
      }
   }
   
// Method to change the mines color between red and white
   public void opacity()
   {      
      if(redToWhite == true) // if red then switch to white
      {
         opacityR -= .05; // decrease reds opacity
         opacityW += .05; // increase whites opacity
         
         if(opacityR <= 0) // if reds opacity is 0 then switch from white to red
         {
            opacityR = 0;
            opacityW = 1;
            redToWhite = false;
         }
      }
      else // if white then switch to red
      {
         opacityR += .05; // increase reds opacity
         opacityW -= .05; // decrease whites opacity

         if(opacityW <= 0) // if whites opacity is 0 then switch from red to white
         {
            opacityW = 0;
            opacityR = 1;
            redToWhite = true;
         }
      }
   }

// Update mine's position based on background movement
   public void updatePosition(float backgroundX, float backgroundY) {
      x += backgroundX;
      y += backgroundY;
   }  
   
// Draw mine to canvas
   public void drawMe(float x, float y, GraphicsContext gc)
   {
      red = new Color(.19, .92, .30, opacityR);
      gc.setFill(red);
      gc.fillOval(x,y,12,12);
      white = new Color(.90, 1, .09, opacityW);
      gc.setFill(white);
      gc.fillOval(x,y,12,12);
   }   
}
