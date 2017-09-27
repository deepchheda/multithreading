import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VersionFive {
	// list of hash maps for each thread
	List<HashMap<String, Touple>> listOfHashMaps = new ArrayList<HashMap<String, Touple>>();
	
	// This method adds or updates key-value pairs in the hashmap 
	public void updateHash(HashMap<String, Touple> h, List<String> lines) {
		for (String line : lines) {
			String[] value = line.split(",");
			if (value[2].equalsIgnoreCase("TMAX")) {
				if (!h.containsKey(value[0])) {
					int temp = fibonacci(17);
					h.put(value[0], new Touple(Double.parseDouble(value[3]), 1));
				} else {
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

	// method to merge all hashmaps obtained from threads and return one final hashmap
	public HashMap<String, Touple> mergeMaps() {
		HashMap<String, Touple> tempMap = new HashMap<String, Touple>();
		for (HashMap<String, Touple> hm : listOfHashMaps) {
			for (Map.Entry<String, Touple> entry : hm.entrySet()) {
				if (tempMap.containsKey(entry.getKey())) {
					int temp = fibonacci(17);
					tempMap.get(entry.getKey()).average = ((tempMap.get(entry.getKey()).average
							* tempMap.get(entry.getKey()).count) + (entry.getValue().average * entry.getValue().count))
							/ (tempMap.get(entry.getKey()).count + entry.getValue().count);
					tempMap.get(entry.getKey()).count = (tempMap.get(entry.getKey()).count + entry.getValue().count);
				} else {
					tempMap.put(entry.getKey(), entry.getValue());
				}
			}
		}

		return tempMap;

	}

	// this is the starting point for no-sharing 
	public HashMap<String, Touple> getData(ArrayList<String> lines) {

		int numberOfThreads = Runtime.getRuntime().availableProcessors();
		Thread threads[] = new Thread[numberOfThreads];
		int partitionSize = lines.size() / numberOfThreads;
		for (int i = 0; i < numberOfThreads; i++) {
			int mapIndex = i;
			// logic to partition the arraylist to send each thread
			int fromIndex = i * partitionSize;
			int toIndex;
			HashMap<String, Touple> hh = new HashMap<String, Touple>();
			listOfHashMaps.add(hh);
			// for the last thread, send the remainder of arraylist instead of partition (edge case)
			if (i + 1 >= numberOfThreads) {
				toIndex = lines.size();
			} else {
				toIndex = (i + 1) * partitionSize;
			}
			
			threads[i] = new Thread(new Runnable() {

				@Override
				public void run() {
					updateHash(listOfHashMaps.get(mapIndex), lines.subList(fromIndex, toIndex));
				}

			});
			threads[i].start();
		}
		// logic to close all threads
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		HashMap<String, Touple> finalHashmap = mergeMaps();
		return finalHashmap;
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
