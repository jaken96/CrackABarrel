
public class UsefulMethods {
	/**
	 * This method calculates the factorial of a number.
	 * Only works for small integers.
	 * @param number of which to find the factorial
	 * @return factorial of number
	 */
	public static int factorial(int number){
		int sum = 0;
		while(number>0){
			sum += number;
			number--;
		}
		return sum;
	}
	
	public static int [] getXDistribution(int width){
		int [] xPoints = new int[factorial(5)];
		int tenth = width/10;
		int index = 0;
		for(int i = 1; i <=5; i++){
			for(int j = 1; j <= i; j++){
				xPoints[index] = (2*j + 4 - i)*(tenth);
				index++;
			}
		}
		return xPoints;
	}
	
	public static int [] getYDistribution( int height){
		int [] yPoints = new int[factorial(5)];
		int index = 0;
		for(int i = 1; i <=5; i++){
			for(int j = 1; j <= i; j++){
				yPoints[index] = height/7 + ((i-1)*height/(5));
				index++;
			}
		}
		return yPoints;
	}
	
	
}
