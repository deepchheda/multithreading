import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//This class has a getData method which is the starting point for no-lock version
public class VersionTwo {
	HashMap<String, Touple> h = new HashMap<>();

	// this is called from the run and each thread accesses this method to modify the data structure
	public void updateHash(List<String> lines) {
		for (String line : lines) {
			String[] value = line.split(",");
			
			if (value[2].equalsIgnoreCase("TMAX")) {
				
				// adding key for the first time
				if (!h.containsKey(value[0])) {
					int temp = fibonacci(17);
					h.put(value[0], new Touple(Double.parseDouble(value[3]), 1));
				} else {
					
					// updating temperature average
					int temp = fibonacci(17);
					h.get(value[0]).average = ((h.get(value[0]).average * h.get(value[0]).count)
							+ Double.parseDouble(value[3])) / (h.get(value[0]).count + 1);
					h.get(value[0]).count++;
				}
			}

			else {
				continue;
			}
		}
	}
	
	// this is the starting point for no lock
	public HashMap<String, Touple> getData(ArrayList<String> lines) {

		int numberOfThreads = Runtime.getRuntime().availableProcessors();
		Thread threads[] = new Thread[numberOfThreads];
		int partitionSize = lines.size() / numberOfThreads;
		for (int i = 0; i < numberOfThreads; i++) {
			// logic to partition the arraylist to send each thread
			int fromIndex = i * partitionSize;
			int toIndex;

			// for the last thread, send the remainder of arraylist instead of partition (edge case)
			if (i + 1 >= numberOfThreads) {
				toIndex = lines.size();
			} else {
				toIndex = (i + 1) * partitionSize;
			}
			
			// making and starting threads
			
			threads[i] = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						updateHash(lines.subList(fromIndex, toIndex));
					} catch (NullPointerException n) {
						System.out.println("Exception in no-lock");

					} catch (ClassCastException a) {
						System.out.println("Exception in no-lock");
					}
				}

			});
			threads[i].start();
		}
		// logic to close all threads before we return from this function
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {

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