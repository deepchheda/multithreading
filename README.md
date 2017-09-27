# Analyze Data with Sequential and Concurrent Java Programs

- Wrote a loader routine that takes an inputs filename, reads the file, and returns a String[] or
	List<String> containing the lines of the file.
- Wrote five versions of the program.
	All of them will parsed the lines of the file and then calculate the average TMAX temperature by station.
	A record would usually have format (StationId, Date, Type, Reading,...), but, as in any real data set, there could be errors or missing values hence the data was cleaned first.

	1. SEQ: Sequential version that calculated the average of the TMAX temperatures by station Id.
	2. NO-LOCK: Multi-threaded version that assigned subsets of the input String[] (or List<String>) for concurrent processing by separate threads. This version used a single shared accumulation data structure and used no locks or synchronization on it, i.e., it completely ignores any possible data inconsistency due to parallel execution.
	3. COARSE-LOCK: Multi-threaded version that assigns subsets of the input String[] (or List<String>) for processing by separate threads. This version also used a single shared accumulation data structure and could only use the single lock on the entire data structure.
	The design was to ensure
		(1) correct multithreaded execution and
		(2) minimal delays by holding the lock only when absolutely necessary.
	4. FINE-LOCK: Multi-threaded version that assigned subsets of the input String[] (or List<String>) for processing by separate threads. This version also used a single shared accumulation data structure, but locked only the accumulation value objects and not the whole data structure.
	The design was to ensure
		(1) correct multithreaded execution and
		(2) minimal delays by holding the locks only when absolutely necessary.
	5. NO-SHARING: Per-thread data structure multi-threaded version that assigned subsets of the input String[] (or List<String>) for processing by separate threads.
	Each thread worked on its own separate instance of the accumulation data structure. Hence no locks were needed. 

I then timed all versions of the program and introduced delay to see the effect of large processing on each version.
