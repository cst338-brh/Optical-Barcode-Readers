/*
 * Heather McCabe
 * Brett Hansen
 * Randall Rood
 * CST 338
 * 9/27/16
 * Module 4: Optical Barcode Reader
 */

public class OpticalBarcodeReader {

   public static void main(String[] args) {
      String[] sImageIn = {
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
               
            
         
         String[] sImageIn_2 = {
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
         bc.displayToConsole(); // REMOVE THIS BEFORE UNCOMMENTING THE CODE TO TEST DATAMATRIX
         /*
         DataMatrix dm = new DataMatrix(bc);
        
         // First secret message
         dm.translateImageToText();
         dm.displayTextToConsole();
         dm.displayImageToConsole();
         
         // second secret message
         bc = new BarcodeImage(sImageIn_2);
         dm.scan(bc);
         dm.translateImageToText();
         dm.displayTextToConsole();
         dm.displayImageToConsole();
         
         // create your own message
         dm.readText("What a great resume builder this is!");
         dm.generateImageFromText();
         dm.displayTextToConsole();
         dm.displayImageToConsole();
         */
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
    * Translates the image_data boolean values to Strings and prints to console. 
    * True values are represented by "*" and false values are represented by " ".
    */
   public void displayToConsole() {
      for (int i = 0; i < MAX_HEIGHT; i++) {
         for (int j = 0; j < MAX_WIDTH; j++) {
            if (image_data[i][j]) {
               System.out.print("*");
            } else {
               System.out.print(" ");
            }
         }
         System.out.println();
      }
   }
   
   /**
    * Creates and returns a copy of this object.
    * @return     A BarcodeImage clone of this instance
    * @see        Cloneable
    */
   public BarcodeImage clone() {
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

/*class DataMatrix implements BarcodeIO {
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
   private BarcodeImage image;
   private String text;
   private int actualWidth;
   private int actualHeight;
   
   public DataMatrix() {
      
   }
}*/
