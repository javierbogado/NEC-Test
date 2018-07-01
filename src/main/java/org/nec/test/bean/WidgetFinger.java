package org.nec.test.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.camel.Body;
import org.springframework.beans.factory.annotation.Value;

public class WidgetFinger {
	private static final Logger logger = LoggerFactory.getLogger(WidgetFinger.class);
	private static final String MATRIX = "matrix";
	@Value("${body.matrix.rows}")
	private Integer matrix_rows;
	@Value("${body.matrix.colums}")
	private Integer matrix_colums;
	@Value("${finger.sequence.a}")
	private String finger_a;
	@Value("${finger.sequence.c}")
	private String finger_c;
	@Value("${finger.sequence.g}")
	private String finger_g;

	@SuppressWarnings("unchecked")
	public boolean isFingerPrint (@Body HashMap<String,Object> body){
		List <String> arrayOfString = (ArrayList<String>) body.get(MATRIX);
		char [][] matrixOfChars=new char[matrix_rows][matrix_colums];
		arrayOfStringToMatrixOfChars(arrayOfString,matrixOfChars);
		Set<String> words = new HashSet<String>();
        words.add(finger_a);
        words.add(finger_c);
        words.add(finger_g);
	    Set<String> wordsFound = findWords(matrixOfChars, words);
	    Boolean res = false;
        for(String word : wordsFound) {
        	res = true;
            System.out.println("Secuencias encontradas: " + word);
        }
	    return res;
	}
	
	private void arrayOfStringToMatrixOfChars(List<String> arrayOfString, char [][]matrixOfChars) {
		for (int s=0;s<arrayOfString.size();s++) {
			  for(int x=s;x<matrix_rows;x++){
		            for(int y=0;y<matrix_colums;y++){
		            	matrixOfChars[x][y] = arrayOfString.get(s).charAt(y);
		            }
		        }
		}
//		printMatrix(matrixOfChars);
	}
	
	public void printMatrix(char [][]matrixOfChars) {
		for(int x=0;x<matrix_rows;x++){
            for(int y=0;y<matrix_colums;y++){
            	System.out.print(matrixOfChars[x][y]);
            }
            System.out.println("");
        }
	}

    public Set<String> findWords(char[][] puzzle, Set<String> words) {
        Set<String> foundWords = new HashSet<String>();
        int minimumWordLength = findMinimumWordLength(words);
        Set<String> possibleWords = findPossibleWords(puzzle, minimumWordLength);
        for(String word : words) {
            for(String possibleWord : possibleWords) {
                if(possibleWord.contains(word) || possibleWord.contains(new StringBuffer(word).reverse())) {
                    foundWords.add(word);
                    break;
                }
            }
        }       
        return foundWords;
    }

    private int findMinimumWordLength(Set<String> words) {
        int minimumLength = Integer.MAX_VALUE;
        for(String word : words) {
            if(word.length() < minimumLength)
                minimumLength = word.length();
        }
        return minimumLength;
    }

    private Set<String> findPossibleWords(char[][] puzzle, int minimumWordLength) {
        Set<String> possibleWords = new LinkedHashSet<String>();
        int dimension = puzzle.length; //Assuming puzzle is square
        if(dimension >= minimumWordLength) {
            /* Every row in the puzzle is added as a possible word holder */
            for(int i = 0; i < dimension; i++) {
                if(puzzle[i].length >= minimumWordLength) {
                    possibleWords.add(new String(puzzle[i]));
                }
            }
            /* Every column in the puzzle is added as a possible word holder */
            for(int i = 0; i < dimension; i++) {
                StringBuffer temp = new StringBuffer();
                for(int j = 0; j < dimension; j++) {
                    temp = temp.append(puzzle[j][i]);
                }
                possibleWords.add(new String(temp));
            }
            /* Adding principle diagonal word holders */
            StringBuffer temp1 = new StringBuffer();
            StringBuffer temp2 = new StringBuffer();
            for(int i = 0; i < dimension; i++) {
                temp1 = temp1.append(puzzle[i][i]);
                temp2 = temp2.append(puzzle[i][dimension - i - 1]);
            }
            possibleWords.add(new String(temp1));
            possibleWords.add(new String(temp2));
            /* Adding non-principle diagonal word holders */
            for(int i = 1; i < dimension - minimumWordLength; i++) {
                temp1 = new StringBuffer();
                temp2 = new StringBuffer();
                StringBuffer temp3 = new StringBuffer();
                StringBuffer temp4 = new StringBuffer();
                for(int j = i, k = 0; j < dimension && k < dimension; j++, k++) {
                    temp1 = temp1.append(puzzle[j][k]);
                    temp2 = temp2.append(puzzle[k][j]);
                    temp3 = temp3.append(puzzle[dimension - j - 1][k]);
                    temp4 = temp4.append(puzzle[dimension - k - 1][j]);
                }
                possibleWords.add(new String(temp1));
                possibleWords.add(new String(temp2));
                possibleWords.add(new String(temp3));
                possibleWords.add(new String(temp4));
            }
        }
        return possibleWords;
    }
}
