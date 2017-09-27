
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VersionFour {
	HashMap<String, Touple> h = new HashMap<>();
	

	// this is the function that gets locked when updating the record in the data structure
	public synchronized void updateVal(String[] value) {
		h.putIfAbsent(value[0], new Touple(0, 0));
		int temp = fibonacci(17);
		h.get(value[0]).average = ((h.get(value[0]).average * h.get(value[0]).count) + Double.parseDouble(value[3]))
				/ (h.get(value[0]).count + 1);
		h.get(value[0]).count++;
	}

	public void updateHash(List<String> lines) {
		for (String line : lines) {
			String[] value = line.split(",");
			if (value[2].equalsIgnoreCase("TMAX")) {
				
				updateVal(value);

			}

			else {
				continue;
			}
		}
	}

	// this is the starting point for fine lock
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
			threads[i] = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						updateHash(lines.subList(fromIndex, toIndex));
					} catch (NullPointerException e) {
						System.out.println(e);
					}
				}

			});
			threads[i].start();
		}
		// logic to close all threads
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
