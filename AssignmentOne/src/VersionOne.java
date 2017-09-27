import java.util.ArrayList;
import java.util.HashMap;

// This class has a getData method which is the starting point for Sequential version of the code
public class VersionOne {
	
	// the starting point of sequential program
	public HashMap<String, Touple> getData(ArrayList<String> lines) {
		
		// this hashmap will store the keys and temperature averages
		HashMap<String, Touple> h = new HashMap<>();
		for (String line : lines) {
			String[] value = line.split(",");
			
			if (value[2].equalsIgnoreCase("TMAX")) {

				// adding key for the first time
				if (!h.containsKey(value[0])) {
					int temp = fibonacci(17);
					h.put(value[0], new Touple(Double.parseDouble(value[3]), 1));
				} 
				
				// updating temperature average
				else {
					int temp = fibonacci(17);
					h.get(value[0]).average = ((h.get(value[0]).average * h.get(value[0]).count)
							+ Double.parseDouble(value[3])) / (h.get(value[0]).count + 1);
					h.get(value[0]).count++;
				}
			} else {
				continue;
			}
		}
		return h;
	}

	// function to introduce fibonacci delay
	public static int fibonacci(int number) {
		if (number == 1 || number == 2) {
			return 1;
		}
		int fib1 = 1, fib2 = 1, sum = 1;
		for (int i = 3; i <= number; i++) {
			sum = fib1 + fib2;
			fib1 = fib2;
			fib2 = sum;

		}
		return sum;
	}
}
