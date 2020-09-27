import java.util.ArrayList;
import java.util.Scanner;
public class HillCipherMain {

	static int calculationsCounter = 0;
	
	public static boolean isStringOnlyAlphabet(String str){ // Function to check String for only Alphabets 
		return ((str != null) 
				&& (!str.equals("")) 
				&& (str.matches("^[a-zA-Z]*$"))); 
	}

	public static ArrayList <Cipher> preProcessing(String key, int matrixSize, String inputText){   // Function to initialize the array vector
		char letterKey[] = key.toUpperCase().toCharArray();

		ArrayList <Cipher> arrVectors = new ArrayList <Cipher>();

		int k = 0;
		int g = matrixSize;

		while (inputText.length() % matrixSize != 0) {
			inputText = inputText.concat("a");
		}
		while (inputText.length() + 1 > g) { 	                       
			arrVectors.add(new Cipher(inputText.substring(k, g).toUpperCase().toCharArray(), letterKey, matrixSize));  // dividing letters depending on the key
			k = g;
			g = g + matrixSize;
		}
		return arrVectors;
		
	}

	public static void main(String[] args) {

		String inputText = null; //Plain or cipher text 
		int matrixSize = 0; //Dimension of the matrix
		String key = null; //The cipher key
		boolean textCheck = false; 
		boolean sizeCheck = false;
		boolean keyCheck = false;
		int option ; // Encryption, decryption or exit 
		int counter = 0; //do while counter for scanner problems
	

		Scanner scannerInput = new Scanner(System.in);

		do {
			System.out.println();
			System.out.println();
			System.out.println("**********************************************************************");
			System.out.println("* Welcome to HillCipher 3x3 program :)                               *");
			System.out.println("* -------------------------------------------------------------------*");
			System.out.println("* Please enter one of the following options:                         *");
			System.out.println("* 1) Encryption                                                      *");
			System.out.println("* 2) Decryption                                                      *");
			System.out.println("* 3) Exit                                                            *");
			System.out.println("**********************************************************************");
			System.out.println("Enter your option :>");

			if(counter > 0) {
				scannerInput.nextLine();
			}
			while (true)
				try {
					option = Integer.parseInt(scannerInput.nextLine());    //Making sure that the input is an integer
					break;
				} catch (NumberFormatException nfe) {
					System.out.println("Only integers are accepted please enter your option : ");
				}
			if(option > 3 || option < 1) {   //Taking a valid option
				option = 4;
			}
			switch (option){
			case 1:
		    counter++;
				while(textCheck != true) {  
					textCheck = true;    
					System.out.print("Please enter plain text :");               //Plain text input
					inputText = scannerInput.nextLine().replaceAll("\\s+", "");
					if(!(isStringOnlyAlphabet(inputText))) {
						System.out.println("Only alphabets are accepted");
						textCheck = false;
					}
				}

				while(sizeCheck != true) { 
					sizeCheck = true;
					System.out.print("Please enter the key size : ");        //Key size input
					while (true) 
						try {
							matrixSize = Integer.parseInt(scannerInput.nextLine());
							break;
						} catch (NumberFormatException nfe) {
							System.out.print("Only integers are accepted");
						}
					if(matrixSize < 2 || matrixSize > 3){
						System.out.println("Only 3x3 or 2x2 hillcipher are accepted");
						sizeCheck = false;
					}	
				}

				while(keyCheck != true) {
					keyCheck = true;
					System.out.print("Please enter the key : ");         //Key input
					key = scannerInput.next().replaceAll("\\s+", "");
					if(!(isStringOnlyAlphabet(key))){
						System.out.println("Only alphabets are accepted");
						keyCheck = false;
					}
					if(key.length() != (matrixSize*matrixSize)){
						System.out.println("The size of the key entered is not equal to the dimension matrix");
						keyCheck = false;
					}
				}
				System.out.println("Result: ");
				for (int i = 0; i < preProcessing(key, matrixSize,inputText).size(); i++) {
					if(calculationsCounter == 0) {
					preProcessing(key, matrixSize,inputText).get(i).encrypt();
					}
				}

				calculationsCounter = 0;
				textCheck = false; 
				sizeCheck = false;           //Reset all variables
				keyCheck = false;
				break;

			case 2:	
				counter++;
				while(textCheck != true) {  
					textCheck = true;    
					System.out.print("Please enter cipher text :");             //Cipher text input
					inputText = scannerInput.nextLine().replaceAll("\\s+", "");
					if(!(isStringOnlyAlphabet(inputText))) {
						System.out.println("Only alphabets are accepted");
						textCheck = false;
					}
				}

				while(sizeCheck != true) {
					sizeCheck = true;
					System.out.print("Please enter the key size : ");     //Key size input
					while (true)
						try {
							matrixSize = Integer.parseInt(scannerInput.nextLine());
							break;
						} catch (NumberFormatException nfe) {
							System.out.print("Only integers are accepted");
						}
					if(matrixSize < 2 || matrixSize > 3){
						System.out.println("Only 3x3 or 2x2 hillcipher are accepted");
						sizeCheck = false;
					}	
				}

				while(keyCheck != true) {
					keyCheck = true;
					System.out.print("Please enter the key : ");       //Key input
					key = scannerInput.next().replaceAll("\\s+", "");
					if(!(isStringOnlyAlphabet(key))){
						System.out.println("Only alphabets are accepted");
						keyCheck = false;
					}
					if(key.length() != (matrixSize*matrixSize)){
						System.out.println("The size of the key entered is not equal to the dimension matrix");
						keyCheck = false;
					}
				}

				System.out.println("Result: ");
				for (int i = 0; i < preProcessing(key, matrixSize,inputText).size(); i++) {
					if(calculationsCounter == 0) {
					preProcessing(key, matrixSize,inputText).get(i).decrypt();
					}
				}
				
				calculationsCounter = 0;
				textCheck = false; 
				sizeCheck = false;           //Reset all variables
				keyCheck = false;
				break;

			case 3 :
				System.out.println("GoodBye :)");
				System.exit(0);
				break;
			case 4 : // if the option is not valid
				System.out.println("Please enter a valid number");
				break;
			}
		}
		while(option !=3);
	}
}