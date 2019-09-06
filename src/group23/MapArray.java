package group23;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MapArray {

    int s = 32;

    public MapArray(){
        //readTilePos(1);
    }

    //Loads correct file and passes through array
    public int[][] readTilePos(int room, int[][]array){

        //accesses different txt files depending on state
        String fileName = "";

        if(room == 1){
            fileName = "res/map/tilePos1";
        } else if(room == 2){
            fileName = "res/map/tilePos2";
        } else if(room == 3){
            fileName = "res/map/tilePos3";
        }

        return readFile(fileName,array);
    }

    //Loads correct file and passes through array
    public int[][] readFillerPos(int room, int[][] array){
        //accesses different txt files depending on state
        String fileName = "";

        if(room == 1){
            fileName = "res/map/backgroundFillerPos1";
        } else if(room == 2){
            fileName = "res/map/backgroundFillerPos2";
        } else if(room == 3){
            fileName = "res/map/backgroundFillerPos3";
        }

        return readFile(fileName,array);

    }

    //Adapted from https://caveofprogramming.com/java/java-file-reading-and-writing-files-in-java.html
    private int[][] readFile(String fileName, int[][] array){

        // Loops through a pre determined txt file to get array positions
        // and returns 2D array
        int index = 0;
        String line  = null;

        try{
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {

                //Find the comma
                int commaIndex = line.indexOf(',');
                if(commaIndex != -1){

                    //gets first number & saves it
                    String number = line.substring(0,commaIndex);
                    array[index][0] = Integer.parseInt(number)*s;

                    //gets second number & saves it
                    number = line.substring(commaIndex+1);
                    array[index][1] = Integer.parseInt(number)*s;


                    index++;
                }




            }

            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
        return  array;
    }


}
