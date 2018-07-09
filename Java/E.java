import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class E implements Runnable {
	private boolean _ReadFromFile = System.getProperty("ONLINE_JUDGE") == null;
	private boolean _WriteToFile = true;
	static final String TASK_ID = "k1";
	static final String IN_FILE = TASK_ID + ".in";
	static final String OUT_FILE = TASK_ID + ".out";
	BufferedReader reader;
	StringTokenizer tokenizer;
	PrintWriter writer;
	
	class Point implements Comparable<Point>{
		int x, y, z;
		int dist;
		public Point(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
			dist = x*x + y*y + z*z;
		}
		public int compareTo(Point o) {
			if (dist != o.dist) return dist - o.dist;
			if (x != o.x) return x - o.x;
			if (y != o.y) return y - o.y;
			if (z != o.z) return z - o.z;
			return 0;
		}
	}

	private void core() throws Exception {
		int len = 100;
		Point[] all = new Point[len*len*len];
		int id = 0;
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				for (int k = 0; k < len; k++) {
					all[id++] = new Point(i, j, k);
				}
			}
		}
		Arrays.sort(all);
		int[][][] grundy = new int[len][len][len];
		boolean[] mex = new boolean[50000];
		int last = -1;
		int val = 0;
		for (int i = 0; i < all.length; i++) {
			while(all[last+1].dist < all[i].dist) {
				++last;
				int x = all[last].x;
				int y = all[last].y;
				int z = all[last].z;
				mex[grundy[x][y][z]] = true;
			}
			for(;mex[val];++val);
			int x = all[i].x;
			int y = all[i].y;
			int z = all[i].z;
			grundy[x][y][z] = val;
		}
		
		int ntest = nextInt();
		for (int test = 0; test < ntest; test++) {
			int n = nextInt(), k = nextInt();
			int[][] coord = new int[n][3];
			for (int i = 0; i < coord.length; i++) {
				for (int j = 0; j < coord[i].length; j++) {
					coord[i][j] = nextInt();
					coord[i][j] = Math.abs(coord[i][j]);
				}
			}
			
			if (k == n)
				writer.println("Player 1");
			else {
				int xor = 0;
				for (int i = 0; i < coord.length; i++) {
					int x = coord[i][0];
					int y = coord[i][1];
					int z = coord[i][2];
					int cur = grundy[x][y][z];
					xor ^= cur;
				}
				if (xor > 0)
					writer.println("Player 1");
				else
					writer.println("Player 2");
			}
		}
	}
	private void writeCase(int test) {
		writer.printf("Case #%d:", test+1);
	}
	void debug(Object...os) {
		System.out.println(Arrays.deepToString(os));
	}
	//--------------------- IO stuffs ---------------------
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new K());
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
