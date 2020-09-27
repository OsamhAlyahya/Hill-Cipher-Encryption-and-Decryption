
public class Cipher {

	public int determinant;
	public int resprocalMod; 
	public int adjMatrix[][];
	public int keyMatrix[][];
	public int textMatrix[];
	public int matrixSize;


	public Cipher(char[] word, char[] key, int size) {

		this.matrixSize = size;
		this.determinant =1;
		this.keyMatrix = new int[matrixSize][matrixSize];
		this.textMatrix = new int[matrixSize];
		this.adjMatrix = new int[matrixSize][matrixSize];
		int m =0; // keyM atrix counter

		for (int i = 0; i < matrixSize; i++) {   //Transform key from letters to numbers
			for (int j = 0; j < matrixSize; j++) {
				keyMatrix[i][j] = key[m] - 'A';
				m++;
			}
		}
		for (int i = 0; i < word.length; i++) {   //Transform text from letters to numbers
			textMatrix[i] = word[i] - 'A';
		}
	}

	public void calculateDeterminant(){
		/*
		| a00 a01 a02 |
		| a10 a11 a12 |
		| a20 a21 a22 |
		 */
		int det=-1;

		if(matrixSize==2) {
			det=(keyMatrix[0][0]*keyMatrix[1][1])-(keyMatrix[1][0]*keyMatrix[0][1]);

		}else {

			int det1,det2,det3=0;

			//   1*(a00)*(ad-cb)

			det1=(1)*(keyMatrix[0][0])*( (keyMatrix[1][1]*keyMatrix[2][2] )-( (keyMatrix[2][1]*keyMatrix[1][2]) ));

			det2=(-1)*(keyMatrix[0][1])*( (keyMatrix[1][0]*keyMatrix[2][2] )-( (keyMatrix[2][0]*keyMatrix[1][2])));

			det3=(1)*(keyMatrix[0][2])*( (keyMatrix[1][0]*keyMatrix[2][1] )-( (keyMatrix[2][0]*keyMatrix[1][1]) ));

			det=det1+det2+det3; 
		}
		if(det==0) {
			System.out.println("the keyMatrix doesn't have an inverse");
			this.determinant = -1;
			return;
			
		}
		
		this.determinant=det;	
		
	}

	public void calculateReciprocalMod(){

		int r1 = 26;
		int r2 = determinant;
		int q = 0;
		int r = 0;
		int t2 = 1;
		int t1 = 0;
		int t = 0;

		while (r2 > 0) {

			q = r1 / r2;
			r = r1 - q * r2;
			r1 = r2;
			r2 = r;

			t = t1 - q * t2;
			t1 = t2;
			t2 = t;
		}

		if (r1 > 1) {
			System.out.println("gcd(26," + determinant + ") = " + r1 + "| " + determinant + " doesn't have a multiplicative inverse in Z26");
			this.resprocalMod = -1;
			return;

		}
		while (t1 < 0) {
			t1 = t1 + 26;
		}

		this.resprocalMod=t1;
	}

	public void calculateAdjoint() {

		int cofactorMatrix[][]= new int [matrixSize][matrixSize];
		/*
		 * 2x2
		| a00 a01 |
		| a10 a11 |
		 *   3x3
		| a00 a01 a02 |
		| a10 a11 a12 |
		| a20 a21 a22 |
		 */
		if(matrixSize==2) {

			adjMatrix[0][0] = keyMatrix[1][1];
			adjMatrix[0][1] = (-1) * keyMatrix[0][1];
			adjMatrix[1][0] = (-1) * keyMatrix[1][0];
			adjMatrix[1][1] = keyMatrix[0][0];

			return;
		}

		//-------------------------------1st row-----------------------------------

		cofactorMatrix[0][0]= (1)  * ( ( keyMatrix[1][1] * keyMatrix[2][2] ) - ( keyMatrix[2][1] * keyMatrix[1][2] ) );

		cofactorMatrix[0][1]= (-1) * ( ( keyMatrix[1][0] * keyMatrix[2][2] ) - ( keyMatrix[2][0] * keyMatrix[1][2] ) );

		cofactorMatrix[0][2]= (1)  * ( ( keyMatrix[1][0] * keyMatrix[2][1] ) - ( keyMatrix[2][0] * keyMatrix[1][1] ) );

		//-------------------------------2nd row-----------------------------------

		cofactorMatrix[1][0]= (-1) * ( ( keyMatrix[0][1] * keyMatrix[2][2] ) - ( keyMatrix[2][1] * keyMatrix[0][2] ) );

		cofactorMatrix[1][1]= (1)  * ( ( keyMatrix[0][0] * keyMatrix[2][2] ) - ( keyMatrix[2][0] * keyMatrix[0][2] ) );

		cofactorMatrix[1][2]= (-1) * ( ( keyMatrix[0][0] * keyMatrix[2][1] ) - ( keyMatrix[2][0] * keyMatrix[0][1] ) );

		//-------------------------------3rd row-----------------------------------

		cofactorMatrix[2][0]= (1)  * ( ( keyMatrix[0][1] * keyMatrix[1][2] ) - ( keyMatrix[1][1] * keyMatrix[0][2] ) );

		cofactorMatrix[2][1]= (-1) * ( ( keyMatrix[0][0] * keyMatrix[1][2] ) - ( keyMatrix[1][0] * keyMatrix[0][2] ) );

		cofactorMatrix[2][2]= (1)  * ( ( keyMatrix[0][0] * keyMatrix[1][1] ) - ( keyMatrix[1][0] * keyMatrix[0][1] ) );


		for (int i = 0; i < matrixSize; i++) {
			for (int j = 0; j < matrixSize; j++) {
				adjMatrix[i][j] = cofactorMatrix[j][i];
			}
		}
	}

	public void encrypt() {
		
		calculateDeterminant(); 
		calculateReciprocalMod();
		
		if(this.resprocalMod == -1 || this.determinant == -1) {
			HillCipherMain.calculationsCounter++;
			return;
		}

		int[] arr = new int[matrixSize];
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length; j++) {
				arr[i] += (keyMatrix[i][j] * textMatrix[j]);
			}
			arr[i] = arr[i] % 26;
		}
		printLetters(arr);
	}

	public void decrypt() {

		calculateDeterminant(); 
		calculateReciprocalMod();
		calculateAdjoint();
		
		if(this.resprocalMod == -1 || this.determinant == -1) {
			HillCipherMain.calculationsCounter++;
			return;
		}
		
		for (int i = 0; i < matrixSize; i++) {
			for (int j = 0; j < matrixSize; j++) {
				while(adjMatrix[i][j]<0) { 
					adjMatrix[i][j] = adjMatrix[i][j]+26;
				}
				adjMatrix[i][j] = adjMatrix[i][j]*resprocalMod;
				adjMatrix[i][j] = adjMatrix[i][j]%26;
			}
		}
		int[] arr = new int[matrixSize];
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length; j++) {
				arr[i] += (adjMatrix[i][j] * textMatrix[j]);
			}
			arr[i] = arr[i] % 26;
		}
		printLetters(arr); 
	}
	public void printLetters(int arr[]) {
		for (int i = 0; i < matrixSize; i++) {
			System.out.print(Character.toChars('A' + arr[i]));
		}
	}
}