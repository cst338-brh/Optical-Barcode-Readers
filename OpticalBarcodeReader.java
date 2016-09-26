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
            "* * * * * * * * * * * * * * * * * * * * * ",
            "*                                       * ",
            "****** **** ****** ******* ** *** *****   ",
            "*     *    ****************************** ",
            "* **    * *        **  *    * * *   *     ",
            "*   *    *  *****    *   * *   *  **  *** ",
            "*  **     * *** **   **  *    **  ***  *  ",
            "***  * **   **  *   ****    *  *  ** * ** ",
            "*****  ***  *  * *   ** ** **  *   * *    ",
            "***************************************** "  
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
         
         String[] sImageIn_3 = {
               "* * * * * * * * * * * * * * * * * *",
               "*                                 *",
               "***** ** * **** ****** ** **** **  ",  
               "* **************      *************",
               "**  *  *        *  *   *        *  ",  
               "* **  *     **    * *   * ****   **",
               "**         ****   * ** ** ***   ** ", 
               "*   *  *   ***  *       *  ***   **",
               "*  ** ** * ***  ***  *  *  *** *   ",   
               "***********************************"
         };
        
         BarcodeImage bc = new BarcodeImage(sImageIn_3);
         
         DataMatrix dm = new DataMatrix(bc);
         
         //dm.readText("Hello world!");
         //dm.generateImageFromText();
         dm.translateImageToText();
         dm.displayTextToConsole();
         dm.generateImageFromText();
         
         /*
        
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

/*
 * This class goes through the barcode and analyzes the information
 */
class DataMatrix implements BarcodeIO {
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
   private BarcodeImage image;
   private String text;
   private int actualWidth;
   private int actualHeight;
   
   /*
    * Default constructor for the matrix. All values set to basic values
    */
   public DataMatrix() {
      image = new BarcodeImage();
      actualWidth = 0;
      actualHeight = 0;
      text = "undefined";
   }
   
   /*
    * Constructor where you receive an image but have no text
    */
   public DataMatrix(BarcodeImage image){
      scan(image);
      text = "undefined";
   }
   
   /*
    * Constructor where you receive a text but need to create an image
    */
   public DataMatrix(String text){
      // Use the default BarcodeImage
      image = new BarcodeImage();
      actualWidth = 0;
      actualHeight = 0;
      
      readText(text);
   }
   
   /*
    * This method reads the input of text
    */
   public boolean readText(String text) {
      if (text == null) {
         return false;
      } else {
         this.text = text;
         return true;
      }
   }
   
   /*
    * This method builds the private barcode
    */
   public boolean scan(BarcodeImage image) {
      // TODO: this is supposed to have error catching and other stuff
      if (image == null) {
         return false;
      } else {      
         this.image = image.clone();
         actualWidth = 35; // TODO: Get from computeSignalWidth()
         actualHeight = 10; // TODO: Get from computeSignalHeight()
         return true;
      }
   }
   
   /**
    * Translates the internal text String to the internal BarcodeImage.
    * Use readText(String) to update the internal text String.
    * @return        true if text is non-null; otherwise false
    */
   public boolean generateImageFromText() {
      // Return without doing anything if text isn't set
      if (text == null) return false;
      
      // Set all image pixels to white
      clearImage();
      
      // Split the string into an array of characters to translate
      char[] chars = text.toCharArray();
      
      // Translate and copy chars to the image
      int bottomRow = BarcodeImage.MAX_HEIGHT - 1;
      int maxBinaryLength = 0;
      for (int col = 1; col < chars.length; col++) {
         // Translate the char to the binary digits '1' and '0' in a char array
         char[] binary = Integer.toBinaryString((int)chars[col]).toCharArray();
         // Store the length of the longest binary String for adding borders later
         if (binary.length > maxBinaryLength) maxBinaryLength = binary.length;
         
         // Starting from one row above the bottom of the image and the least significant binary digit, 
         // loop through the binary digits backwards and translate to image
         for (int row = bottomRow - 1; row > bottomRow - 1 - binary.length; row--) {
            if (binary[binary.length - (bottomRow - row)] == '1') {
               image.setPixel(row, col, true);
            } else {
               image.setPixel(row, col, false);
            }
         }
      }
      
      // Add left and right borders
      for (int row = bottomRow - maxBinaryLength - 1; row < bottomRow; row++) {
         image.setPixel(row, 0, true);                                 // Left column (every row)
         if ((bottomRow - row) % 2 == 0) image.setPixel(row, chars.length + 1, true); // Right column (every other row)  
      }
      
      // Add top and bottom borders
      for (int col = 0; col < chars.length + 2; col++) {
         image.setPixel(bottomRow, col, true);              // Bottom row (every column)
         if (col % 2 == 0) image.setPixel(bottomRow - maxBinaryLength - 2, col, true);    // Top row (every other column)
      }
      
      image.displayToConsole();

      return true;
   }

   
   /**
    * Translates the internal BarcodeImage to the internal text String.
    * @return        true if image is non-null; otherwise false
    */
   public boolean translateImageToText() {
      // Return without doing anything if image is not set
      if (image == null) return false;
      
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
   }
   
   /**
    * Prints the text String to the console.
    */
   public void displayTextToConsole() {
      System.out.println(text);
   }
   
   /**
    * Prints the image to the console using '*' for black and ' ' for white.
    */
   public void displayImageToConsole() {
      // TODO
   }
   
   public int getActualWidth() {
      return actualWidth;
   }
   
   public int getActualHeight() {
      return actualHeight;
   }
   
   private int computeSignalWidth() {
      return 0; // TODO fix
   }
   
   private int computeSignalHeight() {
      return 0; // TODO fix
   }
   
   private void cleanImage() {
      // TODO
   }
   
   /**
    * Set all pixels in the BarcodeImage to white == false.
    */
   private void clearImage() {
      for (int row = 0; row < BarcodeImage.MAX_HEIGHT; row++) {
         for (int col = 0; col < BarcodeImage.MAX_WIDTH; col++) {
            image.setPixel(row, col, false);
         }
      }
   }
   
   
   
}
