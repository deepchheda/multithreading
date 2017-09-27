import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class Assignment {
	// accepts the absolute path of the .gz file and returns an arraylist
	public ArrayList<String> loadFile(String file) throws FileNotFoundException, IOException {
		GZIPInputStream in = new GZIPInputStream(new FileInputStream(file));
		Reader decoder = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(decoder);
		ArrayList<String> result = new ArrayList<>();
		String line;
		while ((line = br.readLine()) != null) {
			result.add(line);
		}
		in.close();
		return result;
	}

	// this code runs all versions of the program and returns the times (with
	// fibonacci delay)
	public static void main(String[] args) throws FileNotFoundException, IOException {
		Assignment r = new Assignment();
		// to use any .csv.gz file modify below to the absolute path of your
		// file
		ArrayList<String> lines = r.loadFile("src/1912.csv.gz");

		// a few variables to save times
		long times[][] = new long[5][10];
		long start;
		long end;

		// Running SEQ version of the code 10 times
		for (int i = 0; i < 10; i++) {
			VersionOne one = new VersionOne();
			start = System.currentTimeMillis();
			HashMap<String, Touple> firstHashMap = one.getData(lines);
			end = System.currentTimeMillis();
			times[0][i] = end - start;
		}

		// Running No lock version of the code 10 times
		for (int i = 0; i < 10; i++) {
			VersionTwo two = new VersionTwo();
			start = System.currentTimeMillis();
			HashMap<String, Touple> secondHashMap = two.getData(lines);
			end = System.currentTimeMillis();
			times[1][i] = end - start;
		}

		// Running Coarse lock version of the code 10 times
		for (int i = 0; i < 10; i++) {
			VersionThree three = new VersionThree();
			start = System.currentTimeMillis();
			HashMap<String, Touple> thirdHashMap = three.getData(lines);
			end = System.currentTimeMillis();
			times[2][i] = end - start;

		}

		// running fine lock version of the code 10 times
		for (int i = 0; i < 10; i++) {
			VersionFour four = new VersionFour();
			start = System.currentTimeMillis();
			HashMap<String, Touple> fourthHashMap = four.getData(lines);
			end = System.currentTimeMillis();
			times[3][i] = end - start;
		}

		// running no-sharing version of code 10 times
		for (int i = 0; i < 10; i++) {
			VersionFive five = new VersionFive();
			start = System.currentTimeMillis();
			HashMap<String, Touple> fifthHashMap = five.getData(lines);
			end = System.currentTimeMillis();
			times[4][i] = end - start;
		}

		// local variavbles to help with printing
		String[] versions = { "SEQ", "NO-LOCK", "COARSE-LOCK", "FINE-LOCK", "NO-SHARING" };
		// printing all values
		for (int i = 0; i < 5; i++) {
			System.out.println("Values for " + versions[i]);
			long sum = 0;
			long max = times[i][0];
			long min = times[i][0];
			for (int j = 0; j < 10; j++) {
				sum += times[i][j];
				if (times[i][j] > max) {
					max = times[i][j];
				}

				if (times[i][j] < min) {
					min = times[i][j];
				}
			}

			sum /= 10;
			System.out.println("Average is: " + sum);
			System.out.println("Min value is: " + min);
			System.out.println("Max value is: " + max);
			System.out.println("---------------------------");
		}

	}
}