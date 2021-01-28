import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * I, Michael Mena, 000817498 certify that this material is my original work.  No other person's work has been used without due acknowledgement.
 *
 *  Date: 1/27/2021
 *
 *  This java file handles the data in ELEVATIONS.txt and is my submission for assignment 1 in COMP-10205-01B
 */
public class Main {
    public static void main (String[] args) throws Exception {
        int[][] doubleArray = create2DArrayFromFile("src/ELEVATIONS.txt");

        // Number one
        int[] answerOne = lowestElevationOccurences(doubleArray);
        System.out.println("The lowest elevation is " + answerOne[0] + " and it appears " + answerOne[1] + " times");



        //Number two
        int[][] answerTwo = numberTwo(doubleArray); //Holds the answer for number 2

        for (int i = 0; i < answerTwo.length; i++){ //Prints out all of the local peaks
            int row = i+1; // Makes the occurrences start at 1 instead of 0
            System.out.println("Local peak " + row +" is " + answerTwo[i][0] + " the row is " + answerTwo[i][1] + " and the column is " + answerTwo[i][2]);

        }


        //Number 3
        double answerThree = numberThree(answerTwo);
        System.out.println(answerThree);


        //Number 4
        int[] answerFour = numberFour(doubleArray);
        System.out.println("The most common number is " + answerFour[0] + " and it appears " + answerFour[1] + " times");


    }


    /**
     * Reads the file and separates data into 2 a dimensional array
     * @param filename The file to be read
     * @return The two dimensional array
     * @throws Exception If there is an error with reading the file
     */
    public static int[][] create2DArrayFromFile(String filename) throws Exception {
        int[][] doubleArray;

        File file = new File(filename);
        Scanner scan = new Scanner(file);
        int rowCount = 0;
        int columnsCount = 0;
        int  exclusionRadius = 0;
        boolean firstLine =true;
        int rowTick = 0;

        try (Scanner sc = new Scanner(file)){
            if (firstLine){
                String[] tempRow = sc.nextLine().split(" ");
                int temp1 = Integer.parseInt(tempRow[0]);
                int temp2 = Integer.parseInt(tempRow[1]);
                int temp3 = Integer.parseInt(tempRow[2]);
                rowCount = temp1;
                columnsCount = temp2;
                exclusionRadius = temp3;
                doubleArray = new int[rowCount][columnsCount];
                firstLine = false;
            }
            doubleArray = new int[rowCount][columnsCount];
            while(scan.hasNextLine()) {
                if (sc.hasNextLine()) {
                    String[] tempRow = sc.nextLine().split(" ");
                    for (int i = 0; i < tempRow.length; i++) {
                        doubleArray[rowTick][i] = Integer.parseInt(tempRow[i]);

                    }
                    rowTick++;
                }
                else{
                    sc.close();
                    break;
                }
            }

        }
        catch (IOException e) {
            e.printStackTrace();
            doubleArray = new int[5][5];
        }
        return doubleArray;
    }


    /**
     * The answer to number one, this method finds the number of occurrences for the lowest number in the data set
     * @param doubleArray takes the data that got read off of the file into the doubleArray variable
     * @return returns an array which has the lowest number in dataSet[0] and the number of occurrences in dataSet[1]
     */
    public static int[] lowestElevationOccurences(int[][] doubleArray){
        int lowestElevation = 0;
        int occurences = 0;
        int[] dataSet = new int[2];
        for (int i = 0; i < doubleArray.length; i++){
            for (int j = 0; j < doubleArray[i].length; j++){
                if (doubleArray[i][j] < lowestElevation || lowestElevation == 0){
                    lowestElevation = doubleArray[i][j];
                }
            }
        }

        for (int i = 0; i < doubleArray.length; i++){
            for (int j = 0; j < doubleArray[i].length; j++){
                if (doubleArray[i][j] == lowestElevation){
                    occurences++;
                }
            }
        }
        dataSet[0] = lowestElevation;
        dataSet[1] = occurences;

        return dataSet;


    }

    /**
     * The answer to number two, this method returns all of the peaks with the exclusion radius of -11
     * @param doubleArray takes the parameter doubleArray which is read off of the .txt file
     * @return returns 2 dimensional array where with each of the base arrays holding {local peak number, row, column}
     */
    public static int[][] numberTwo(int[][] doubleArray){
    int[][] localPeaks = new int[1000][3];
    int rowTick = 0;
    int potentialPeak = 0;
    boolean peakTrue = false;
    int potentialRow = 0;
    int potentialColumn = 0;
    int rowRange = doubleArray.length-11;
    int columnRange = doubleArray[5].length-11;




    //Goes through all rows of the array
    for(int i = 0; i < doubleArray.length; i++){
        //Goes through all columns in the array
        for(int j = 0; j < doubleArray[5].length; j++){

            //This is to hold the potential peak, the column, and the row if the number is greater than or equal to 98480
            if(doubleArray[i][j] >= 98480){
                potentialPeak = doubleArray[i][j];
                potentialRow = i;
                potentialColumn = j;
            }



            // Everything contained in this if statement checks the surrounding area of the potential peak to see if any numbers are greater than or equal to it
            if(potentialRow - 11 >= 0 && potentialColumn - 11 >= 0 && potentialColumn + 11 <=doubleArray[5].length && potentialRow + 11 <doubleArray.length) {
                //Goes through all of the rows +- 11
                for (int subI = potentialRow - 11; subI <= potentialRow + 11; subI++) {
                    //Goes through all columns +- 11
                    for (int subJ = potentialColumn - 11; subJ <= potentialColumn + 11; subJ++) {
                        //If something shows reason for not being a peak, breaks the above 2 for loops
                        if (doubleArray[subI][subJ] >= potentialPeak && !(subI == potentialRow && subJ == potentialColumn)) {
                                subI = potentialRow + 11;
                                subJ = potentialColumn + 11;
                                potentialPeak = 0;
                        }

                        //If it gets this far, then the peak is true
                        if (subI == potentialRow + 10 && subJ == potentialColumn + 10) {
                            peakTrue = true;
                        }
                    }
                }
            }
            //Adds the peak to the list of peaks
            if(peakTrue == true &&(i >= 11 && i <= rowRange && j >= 11 && j <= columnRange) && potentialPeak !=0){
                localPeaks[rowTick][0] = potentialPeak;
                localPeaks[rowTick][1] = i;
                localPeaks[rowTick][2] = j;
                rowTick++;
                potentialPeak = 0;
                peakTrue = false;
            }



        }
    }

    //Trims the array
    for(int i = 0; i < localPeaks.length; i++){
        if (localPeaks[i][1] == 0){
            localPeaks = Arrays.copyOf(localPeaks, i);
            break;
        }
    }

    return localPeaks;
}

    /**
     * The answer to number three, finds the two closest peaks using the formula (Answer = (row1 - row2)^2 + (column1 - column2)^2 )
     * @param answerTwo takes the answer to number two as the parameter
     * @return returns a double variable that is the answer to the solution
     */
    public static double numberThree(int[][] answerTwo){
        int[] positions = new int[4]; //(row1, column1, row2, column2)
        double answerThree = 0;
        double tempAnswerRow = 0;
        double tempAnswerColumn = 0;
        double tempAnswerTotal = 0;
        double dc1; //Translates column to a double variable to be used in calculations
        double dc2; //Translates column to a double variable to be used in calculations
        double dr1; //Translates row to a double variable to be used in calculations
        double dr2; //Translates row to a double variable to be used in calculations

        for (int i = 0; i < answerTwo.length; i++){
            for(int j = 0; j < answerTwo.length; j++){
                if (i != j){
                    if(answerTwo[i][1] > answerTwo[j][1]){ // If the first number is higher
                        dr1 = answerTwo[i][1];
                        dr2 = answerTwo[j][1];

                        tempAnswerRow = dr1-dr2;
                    }
                    else{ //If the second number is higher
                        dr1 = answerTwo[i][1];
                        dr2 = answerTwo[j][1];

                        tempAnswerRow = dr2-dr1;
                    }
                    if(answerTwo[i][2] > answerTwo[j][2]){
                        dc1 = answerTwo[i][2];
                        dc2 = answerTwo[j][2];

                        tempAnswerColumn = dc1-dc2;
                    }
                    else{
                        dc1 = answerTwo[i][2];
                        dc2 = answerTwo[j][2];

                        tempAnswerColumn = dc2-dc1;
                    }

                    tempAnswerTotal = Math.sqrt((tempAnswerColumn*tempAnswerColumn) + (tempAnswerRow*tempAnswerRow));

                    if(tempAnswerTotal < answerThree || answerThree == 0){
                        answerThree = tempAnswerTotal;

                        positions[0] = answerTwo[i][1]; //row 1
                        positions[1] = answerTwo[i][2]; //column 1
                        positions[2] = answerTwo[j][1]; //row 2
                        positions[3] = answerTwo[j][2]; //column 2

                    }




                }
            }
        }

        double temp = answerThree;
        BigDecimal bd = new BigDecimal(temp).setScale(2,RoundingMode.HALF_UP); //Translates to the answer being cut off at 2 decimal places
        answerThree = bd.doubleValue();
        return answerThree;
    }

    /**
     * The answer to number four, filters through all of the numbers to find the most common of all of the numbers in the data set
     * @param doubleArray takes the initial doubleArray that was read from the .txt file
     * @return returns an array which holds {most common number, number of occurrences}
     */
    public static int[] numberFour(int[][] doubleArray){
        int[] answer4 = new int[2]; //{most common number, number of matches}
        int matchesCount = 0;
        int matchNumber = 1;





        for (int i = 1; i == 1; i++){
            for(int j = 0; j < doubleArray[3].length; j++){
                int tempMatchesCount = 0;

                //These two signify the numbers that the first number will be compared against
                for (int iB = 0; iB < doubleArray.length; iB++){
                    for(int jB = 0; jB < doubleArray[3].length; jB++){


                        if(!(i == iB && j == jB)){ //Makes sure its not just comparing [1][1] against [1][1] for example
                            if(doubleArray[i][j] == doubleArray[iB][jB]){
                                tempMatchesCount++;
                            }

                            //If this number has more matches than any of the previous numbers
                            if(tempMatchesCount > matchesCount){
                                matchesCount = tempMatchesCount;
                                matchNumber = doubleArray[i][j];


                            }


                        }

                    }
                }

            }

        }
        answer4[0] = matchNumber; //Number that is the most common
        answer4[1] = matchesCount+1; //number of matches (The +1 is so it doesn't start at 0)

        return answer4;

    }

}
