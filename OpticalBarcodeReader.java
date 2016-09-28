/*
 * Heather McCabe
 * Brett Hansen
 * Randall Rood
 * CST 338
 * 9/27/16
 * Module 4: Optical Barcode Reader
 */
import java.lang.CloneNotSupportedException;

public class OpticalBarcodeReader {

   public static void main(String[] args)
      {
         String[] sImageIn =
         {
            "                                               ",
            "                                               ",
            "                                               ",
            "     * * * * * * * * * * * * * * * * * * * * * ",
            "     *                                       * ",
            "     ****** **** ****** ******* ** *** *****   ",
            "     *     *    ****************************** ",
            "     * **    * *        **  *    * * *   *     ",
            "     *   *    *  *****    *   * *   *  **  *** ",
            "     *  **     * *** **   **  *    **  ***  *  ",
            "     ***  * **   **  *   ****    *  *  ** * ** ",
            "     *****  ***  *  * *   ** ** **  *   * *    ",
            "     ***************************************** ",  
            "                                               ",
            "                                               ",
            "                                               "

         };      
               
            
         
         String[] sImageIn_2 =
         {
               "                                          ",
               "                                          ",
               "* * * * * * * * * * * * * * * * * * *     ",
               "*                                    *    ",
               "**** *** **   ***** ****   *********      ",
               "* ************ ************ **********    ",
               "** *      *    *  * * *         * *       ",
               "***   *  *           * **    *      **    ",
               "* ** * *  *   * * * **  *   ***   ***     ",
               "* *           **    *****  *   **   **    ",
               "****  *  * *  * **  ** *   ** *  * *      ",
               "**************************************    ",
               "                                          ",
               "                                          ",
               "                                          ",
               "                                          "

         };
        
         BarcodeImage bc = new BarcodeImage(sImageIn);
         DataMatrix dm = new DataMatrix(bc);
        
         // First secret message
         System.out.println("========================================\nFIRST SECRET MESSAGE:");
         dm.translateImageToText();
         dm.displayTextToConsole();
         System.out.println("\nBarcode Image: \n");
         dm.displayImageToConsole();
         
         // Second secret message
         System.out.println("\n========================================\nSECOND SECRET MESSAGE:");
         bc = new BarcodeImage(sImageIn_2);
         dm.scan(bc);
         dm.translateImageToText();
         dm.displayTextToConsole();
         System.out.println("\nBarcode Image: \n");
         dm.displayImageToConsole();
         
         // Create your own message
         System.out.println("\n========================================\nTHIRD SECRET MESSAGE:");
         dm.readText("What a great resume builder this is!");
         dm.generateImageFromText();
         dm.displayTextToConsole();
         System.out.println("\nBarcode Image: \n");
         dm.displayImageToConsole();
      }   
}

/**
 * Classes that implement BarcodeIO are expected to store some version of 
 * an image and some version of the text associated with that image.
 */
interface BarcodeIO {
   public boolean scan(BarcodeImage bc);
   public boolean readText(String text);
   public boolean generateImageFromText();
   public boolean translateImageToText();
   public void displayTextToConsole();
   public void displayImageToConsole();
}


class BarcodeImage implements Cloneable {
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;
   private boolean[][] image_data;

   /**
    * Default constructor. Creates a blank image. 
    */
   public BarcodeImage() {
      // Set image dimensions to default max
      image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
      
      // Fill image with blanks
      for (int i = 0; i < MAX_HEIGHT; i++) {
         for (int j = 0; j < MAX_WIDTH; j++) {
            image_data[i][j] = false;
         }
      }
   }
   
   /**
    * Constructor. Converts a 1D array of Strings into a 2D boolean image array.
    * @param str_data      a 1D array of Strings consisting of chars '*' for black and ' ' for white
    */
   public BarcodeImage(String[] str_data) {
      // Set image dimensions to default max
      image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
      
      // Translate str_data to image_data if it is a valid size
      if (checkSize(str_data)) {
         
         for (int row = MAX_HEIGHT - 1; row >= 0; row--) {
            // Get adjusted vertical index for str_data so image can be placed in lower-left corner
            int str_row = str_data.length - (MAX_HEIGHT - row);
            for (int col = 0; col < MAX_WIDTH; col++ ) {
               
               // Translate '*' char to true; translate ' ' to false; fill in extra space with false
               if (str_row >= 0 && col < str_data[str_row].length() 
                     && str_data[str_row].charAt(col) == '*') {
                  // Current horizontal position is '*' char in String
                  image_data[row][col] = true;
               } else {
                  // Current horizontal position is ' ' char in String 
                  // Or filling in image outside of Strings with blanks
                  image_data[row][col] = false;
               }               
            }
         }
      }
   }
   
   /**
    * Gets the value of the pixel at a given location.
    * @param row     the index of the vertical row of the pixel to get
    * @param col     the index of the horizontal column of the pixel to get
    * @return        true if pixel is true/black; false if pixel is false/blank or indices are invalid
    */
   public boolean getPixel(int row, int col) {
      if (row < image_data.length && col < image_data[row].length) {
         return image_data[row][col];
      } else {
         return false;
      }
   }
   
   /**
    * Sets the value of the pixel at a given location.
    * @param row     the index of the vertical row of the pixel to set
    * @param col     the index of the horizontal column of the pixel to set
    * @param value   the value to set the pixel to (true or false)
    * @return        true if the indices were within bounds; false otherwise
    */
   public boolean setPixel(int row, int col, boolean value) {
      if (row < image_data.length && col < image_data[row].length) {
         image_data[row][col] = value;
         return true;
      } else {
         return false;
      }
   }
     
   /**
    * Creates and returns a copy of this object.
    * @return     A BarcodeImage clone of this instance
    * @see        Cloneable
    */
   public BarcodeImage clone() throws CloneNotSupportedException{
      BarcodeImage image = new BarcodeImage();
      
      for (int row = 0; row < MAX_HEIGHT; row++) {
         for (int col = 0; col < MAX_WIDTH; col++) {
            image.setPixel(row, col, this.getPixel(row, col));
         }
      }
      
      return image;
   }
   
   /**
    * Valides the size of a given 1D String array. It must not be null, its length
    * must be less than MAX_HEIGHT, and the length of its Strings must be less
    * than MAX_WIDTH. 
    * @param str_data      the 1D String array to check
    * @return              true if the array is valid; false otherwise
    */
   private boolean checkSize(String[] str_data) {
      if (str_data == null) return false; 
      
      if (str_data.length > MAX_HEIGHT) return false;
      
      for (String str : str_data) {
         if (str.length() > MAX_WIDTH) return false;
      }
      
      return true;
   }
   
}

class DataMatrix implements BarcodeIO {
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
   private BarcodeImage image;
   private String text;
   private int actualWidth;
   private int actualHeight;
   
   /**
    * Default constructor. The DataMatrix is initialized with a
    * blank BarcodeImage and undefined text.
    */
   public DataMatrix() {
      image = new BarcodeImage();
      actualWidth = 0;
      actualHeight = 0;
      
      text = "undefined";
   }
   
   /**
    * Constructor. The DataMatrix is initialized with a given
    * BarcodeImage and undefined text.
    * @param BarcodeImage     the BarcodeImage to store
    * @see BarcodeImage
    */
   public DataMatrix(BarcodeImage image) {
      scan(image);
      
      text = "undefined";
   }
   
   /**
    * Constructor. The Datamatrix is initialized with a given
    * text String and a blank BarcodeImage.
    * @param String     the text to store
    */
   public DataMatrix(String text) {
      image = new BarcodeImage();
      actualWidth = 0;
      actualHeight = 0;
      
      readText(text);
   }
   
   /**
    * Sets text to the given value.
    * @param String     the text to store
    * @return           true if the String is non-null; false otherwise
    */
   public boolean readText(String text) {
      if (text == null) {
         return false;
      } else {
         this.text = text;
         return true;
      }
   }
   
   /**
    * Sets the internal BarcodeImage to the given BarcodeImage.
    * @param BarcodeImage     a BarcodeImage to store
    * @return                 true if the BarcodeImage is non-null; false otherwise
    * @see BarcodeImage
    */
   public boolean scan(BarcodeImage image) {
      if (image == null) {
         return false;
      } else {
         try {
            this.image = image.clone();
         } catch(CloneNotSupportedException e){
           // Do nothing
         }
         
         cleanImage(); // Moves the barcode signal to the lower-left corner
         
         // Update dimensions
         actualWidth = computeSignalWidth(); 
         actualHeight = computeSignalHeight();
         
         return true;
      }
   }
   
   /**
    * Translates the internal text String into an internal BarcodeImage.
    * Use readText(String) to update the internal text String.
    * @return        true if text is non-null and within length limits; otherwise false
    */
   public boolean generateImageFromText() {
      final int maxBinaryLength = 8;
      if (text != null && text.length() < BarcodeImage.MAX_WIDTH) {
         // Set all image pixels to white
         clearImage();
         
         // Split the string into an array of characters to translate
         char[] chars = text.toCharArray();
         
         // Loop through characters, translate to binary, then translate to true/false in image
         int bottomRow = BarcodeImage.MAX_HEIGHT - 1; // Makes code more readable
         for (int col = 1; col < chars.length + 1; col++) {
            
            // Translate the char to the binary digits '1' and '0' in a char array
            char[] binary = Integer.toBinaryString((int)chars[col-1]).toCharArray();

            // Starting from one row above the bottom of the image and the least significant binary digit, 
            // loop through the binary digits backwards and translate to image
            // Looping is done backwards because Integer.toBinaryString does not left-pad its result
            for (int row = bottomRow - 1; row > bottomRow - 1 - binary.length; row--) {
               if (binary[binary.length - (bottomRow - row)] == '1') {
                  image.setPixel(row, col, true);
               } else {
                  image.setPixel(row, col, false);
               }
            }
         }
         
         // Add image borders around the encoded characters
         // addBorders args: topRow, bottomRow, leftCol, rightCol
         addBorders(bottomRow - maxBinaryLength - 1, bottomRow, 0, chars.length + 1);
         
         actualWidth = computeSignalWidth();
         actualHeight = computeSignalHeight();
   
         return true;
         
      } else {
         // Text is null or too long
         return false;
      }
   }
   
   /**
    * Add borders around the data encoded in a BarcodeImage. 
    * The left and bottom borders are black every row/column, 
    * and the top and right boarders are black every other row/column.
    * @param topRow           an int representing the BarcodeImage horizontal index of the top border
    * @param bottomRow        an int representing the BarcodeImage horizontal index of the bottom border
    * @param leftCol          an int representing the BarcodeImage vertical index of the left border
    * @param rightCol         an int representing the BarcodeImage vertical index of the right border
    * @return                 true if the indices are within bounds; false otherwise
    */
   private boolean addBorders(int topRow, int bottomRow, int leftCol, int rightCol) {
      if (topRow >= 0 && bottomRow < BarcodeImage.MAX_HEIGHT
          && leftCol >=0 && rightCol < BarcodeImage.MAX_WIDTH) {
         // Indices are within bounds
         // Set left and right borders
         for (int row = topRow; row <= bottomRow; row++) {
            image.setPixel(row, leftCol, true);                               // Left border
            if ((topRow - row) % 2 == 0) image.setPixel(row, rightCol, true); // Right border 
         }
         for (int col = leftCol; col <= rightCol; col++) {
            image.setPixel(bottomRow, col, true);                             // Bottom border
            if ((col - leftCol) % 2 == 0) image.setPixel(topRow, col, true);  // Top border
         }
         return true;
         
      } else {
         // Indices are out of bounds
         return false;
      }
   }

   
   /**
    * Translates the internal BarcodeImage to the internal text String.
    * @return        true if image is non-null; false otherwise
    */
   public boolean translateImageToText() {
      if (image != null) {
      
         // Clear any existing text value
         text = "";
         
         // Get the binary representation from each column and translate to a char
         // Skip left column, right column, top row, and bottom row as these are borders
         for (int col = 1; col < actualWidth - 1; col++) {
            String binary = "";
            for (int row = BarcodeImage.MAX_HEIGHT - actualHeight + 1; row < BarcodeImage.MAX_HEIGHT - 1; row++) {
               if (image.getPixel(row, col) == true) {
                  binary += "1";
               } else {
                  binary += "0";
               }
            }
            // Translate binary String to char and append to text
            text += (char)Integer.parseInt(binary,2);         
         }
         
         return true;
         
      } else {
         // Image is null
         return false;
      }
   }
   
   /**
    * Prints the text String to the console.
    */
   public void displayTextToConsole() {
      System.out.println(text);
   }
   
   /**
    * Prints the image to the console using '*' for black and ' ' for white. 
    * Also draws a lined border using "-" for top/bottom and "|" for left/right.
    */
   public void displayImageToConsole() {
      // Draw the top border with dashes
      for (int i = 0; i < actualWidth + 2; i++) System.out.print("-");
      System.out.println();
      
      // Draw the rows of the signals with "|" borders on either end
      for (int row = BarcodeImage.MAX_HEIGHT - actualHeight; row < BarcodeImage.MAX_HEIGHT; row++) {
         System.out.print("|");
         for (int col = 0; col < actualWidth; col++) {
            if (image.getPixel(row, col)) {
               System.out.print("*");
            } else {
               System.out.print(" ");
            }
         }
         System.out.println("|");
      }
      
      // Draw the bottom border with dashes
      for (int i = 0; i < actualWidth + 2; i++) System.out.print("-");
      System.out.println();
      
   }
   
   /**
    * Gets the width of the internal BarcodeImage's signal.
    * @return     the width of the internal BarcodeImage's signal
    */
   public int getActualWidth() {
      return actualWidth;
   }
   
   /**
    * Gets the height of the internal BarcodeImage's signal.
    * @return     the height of the internal BarcodeImage
    */
   public int getActualHeight() {
      return actualHeight;
   }
   
   /**
    * Computes the width of the internal BarcodeImage's signal by 
    * counting the width of the bottom border of the image. This 
    * returns the value but does not update the internal actualWidth 
    * variable.
    * @return     the width of the internal BarcodeImage signal
    */
   private int computeSignalWidth() {
      int signalWidth = 0;
      for (int i = 0; i < BarcodeImage.MAX_WIDTH; i++) {
        if (image.getPixel(BarcodeImage.MAX_HEIGHT-1, i)) {
           signalWidth++;
        }
      }

      return signalWidth;
   }
   
   /**
    * Computes the height of the internal BarcodeImage's signal by 
    * counting the height of the left border of the image. This 
    * returns the value but does not update the internal
    * actualHeight variable.
    * @return     the height of the internal BarcodeImage signal
    */
   private int computeSignalHeight() {
      int signalHeight = 0;
      for (int i = 0; i < BarcodeImage.MAX_HEIGHT; i++) {
         if (image.getPixel(i, 0) == true) {
            signalHeight++;
         }
      }
      
      return signalHeight;
   }
   
   /**
    * Moves the BarcodeImage signal to the lower-left corner of its 
    * 2D array.
    */
   private void cleanImage() {
      int start_row = BarcodeImage.MAX_HEIGHT - 1; 
      int start_col = 0;                           
      
      
      // Starting at the bottom left, loop right through columns 
      // and up through rows until the lower-left corner of the 
      // signal is found.
      boolean found = false;
      for (int row = start_row; row >= 0; row--) {
        if (found) break; // Stop looping once the signal is found
        for (int col = 0; col < BarcodeImage.MAX_WIDTH; col++) {
           if (image.getPixel(row,col) == true && found == false) {
              start_row = row;
              start_col = col;
              found = true;
           }
        }
      }
      
      // 
      for (int i = 0; i < BarcodeImage.MAX_HEIGHT; i++) {
        for (int j = 0; j < BarcodeImage.MAX_WIDTH; j++) {
           if ((start_row - i >= 0) && (start_col + j < BarcodeImage.MAX_WIDTH)) {
              boolean tmpVal = image.getPixel(start_row - i, start_col + j);
              image.setPixel(BarcodeImage.MAX_HEIGHT-1-i, j, tmpVal);
           }
        }
      }
   }
   
   /**
    * Set all pixels in the BarcodeImage to white (false).
    */
   private void clearImage() {
      for (int row = 0; row < BarcodeImage.MAX_HEIGHT; row++) {
         for (int col = 0; col < BarcodeImage.MAX_WIDTH; col++) {
            image.setPixel(row, col, false);
         }
      }
   }
}