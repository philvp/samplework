import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class B implements Runnable {
	private boolean _ReadFromFile = System.getProperty("ONLINE_JUDGE") == null;
	private boolean _WriteToFile = true;
	static final String TASK_ID = "bl";
	static final String IN_FILE = TASK_ID + ".in";
	static final String OUT_FILE = TASK_ID + ".out";
	BufferedReader reader;
	StringTokenizer tokenizer;
	PrintWriter writer;

	private void core() throws Exception {
		int ntest = nextInt();
		for (int test = 0; test < ntest; test++) {
			int nCombine = nextInt();
			char[][] combine = new char[255][255];
			for (int i = 0; i < nCombine; i++) {
				char[] s = nextToken().toCharArray();
				combine[s[0]][s[1]] = combine[s[1]][s[0]] = s[2];
			}
			
			int nOppose = nextInt();
			boolean[][] oppose = new boolean[255][255];
			for (int i = 0; i < nOppose; i++) {
				char[] s = nextToken().toCharArray();
				oppose[s[0]][s[1]] = oppose[s[1]][s[0]] = true;
			}
			
			int n = nextInt();
			char[] s = nextToken().toCharArray();
			LinkedList<Character> list = new LinkedList<Character>();
			for (char c : s) {
				if (list.size() == 0)
					list.add(c);
				else {
					char last = list.getLast();
					if (combine[c][last] != 0) {
						list.removeLast();
						list.add(combine[c][last]);
					}
					else if (opposeAny(c, list, oppose)) {
						list.clear();
					}
					else 
						list.add(c);
				}
			}
			
			writeCase(test);
			writer.println(list);
			writer.flush();
		}
	}
	private boolean opposeAny(char c, LinkedList<Character> list,
			boolean[][] oppose) {
		for (char x : list) {
			if (oppose[c][x])
				return true;
		}
		return false;
	}
	private void writeCase(int test) {
		writer.printf("Case #%d: ", test+1);
	}
	void debug(Object...os) {
		System.out.println(Arrays.deepToString(os));
	}
	//--------------------- IO stuffs ---------------------
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new B());
        thread.start();
        thread.join();
    }
	public void run() {
        try {
        	reader = _ReadFromFile ? new BufferedReader(new FileReader(IN_FILE)) : new BufferedReader(new InputStreamReader(System.in));
        	writer = _WriteToFile ? new PrintWriter(OUT_FILE) : new PrintWriter(new BufferedOutputStream(System.out));
            tokenizer = null;
            core();
            reader.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    int nextInt() throws Exception {
        return Integer.parseInt(nextToken());
    }
    long nextLong() throws Exception {
        return Long.parseLong(nextToken());
    }
    double nextDouble() throws Exception {
        return Double.parseDouble(nextToken());
    }
    String nextToken() throws Exception {
        while (tokenizer == null || !tokenizer.hasMoreTokens()) {
            tokenizer = new StringTokenizer(reader.readLine());
        }
        return tokenizer.nextToken();
    }
}
