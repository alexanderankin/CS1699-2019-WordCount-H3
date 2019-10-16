import java.io.*;
import java.util.*;

public class WordCountLocal {
	private static void die(String message) {
		System.err.println(message);
		System.exit(1);
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			die("Please provide files to parse");
		}

		for (String fn : args) {
			if (!(new File(fn).exists()))
				die("files must exist");
		}

		try {
			long before = System.currentTimeMillis();
			WordCountLocal wordCountLocal = new WordCountLocal(args);
			long after = System.currentTimeMillis();
			long difference = after - before;
			System.out.println("The total time is " + difference + "ms");
			System.out.println(wordCountLocal.toString());
		} catch (FileNotFoundException e) {
			die("Files were removed while the program was running");
		}
	}

	private HashMap<String, Integer> wordCount;
	private Scanner scanner = null;

	public WordCountLocal(String[] filenames) throws FileNotFoundException {
		wordCount = new HashMap<String, Integer>() {
			@Override
			public Integer get(Object key) {
				return containsKey(key) ? super.get(key): 0;
			}
		};

		for (String filename : filenames) {
			if (scanner != null)
				scanner.close();
			scanner = new Scanner(new File(filename));
			while (scanner.hasNext()) {
				String next = scanner.next();
				wordCount.put(next, wordCount.get(next) + 1);
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		List<Map.Entry<String, Integer>> entries = new ArrayList<>(wordCount.entrySet());
		Collections.sort(entries, new WordCountComparator());
		for (Map.Entry<String, Integer> entry : entries) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			sb.append(value);
			sb.append("\t");
			sb.append(key);
			sb.append("\n");
		}
		return sb.toString();
	}

	private static class WordCountComparator implements Comparator<Map.Entry<String, Integer>> {
		public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
			return a.getKey().compareTo(b.getKey());
		}
	}
}
