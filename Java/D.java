import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class C implements Runnable {
	private boolean _ReadFromFile = System.getProperty("ONLINE_JUDGE") == null;
	private boolean _WriteToFile = true;
	static final String TASK_ID = "cl";
	static final String IN_FILE = TASK_ID + ".in";
	static final String OUT_FILE = TASK_ID + ".out";
	BufferedReader reader;
	StringTokenizer tokenizer;
	PrintWriter writer;
	
	class DisjointSet { 
		int[] parent, rank;
		public DisjointSet(int n) {
			parent = new int[n];
			for (int i = 0; i < parent.length; i++) {
				parent[i] = i;
			}
			rank = new int[n];
		}
		public int getRoot (int node) {
			if (parent[node] == node) return node;
			return parent[node] = getRoot(parent[node]);
		}
		public void join(int node, int anotherNode) {
			int p1 = getRoot(node);
			int p2 = getRoot(anotherNode);
			if (p1 == p2) return;
			if (rank[p1] > rank[p2])
				parent[p2] = p1;
			else {
				parent[p1] = p2;
				if (rank[p1] == rank[p2])
					++rank[p2];
			}
		}
	}
	
	class Rectangle {
		int xmin, ymin, xmax, ymax;
		public Rectangle(int minx, int miny, int maxx, int maxy) {
			this.xmin = minx;
			this.ymin = miny;
			this.xmax = maxx;
			this.ymax = maxy;
		}
		public boolean inside(int x, int y) {
			return xmin <= x && x <= xmax && ymin <= y && y <= ymax;
		}
		public boolean intersect(Rectangle o) {
			int x1 = Math.max(xmin, o.xmin);
			int x2 = Math.min(xmax, o.xmax);
			int y1 = Math.max(ymin, o.ymin);
			int y2 = Math.min(ymax, o.ymax);
			return x1 <= x2 && y1 <= y2;
		}
	}

	private void core() throws Exception {
		int ntest = nextInt();
		for (int test = 0; test < ntest; test++) {
			int n = nextInt();
			Rectangle[] a = new Rectangle[n];
			for (int i = 0; i < a.length; i++) {
				a[i] = new Rectangle(nextInt(), nextInt(), nextInt(), nextInt());
			}

			DisjointSet dSet = new DisjointSet(n);
			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a.length; j++) {
					if (hasEdge(a[i], a[j])) {
						dSet.join(i, j);
					}
				}
			}
			
			int[] minSum = new int[n], maxx = new int[n], maxy = new int[n];
			Arrays.fill(minSum, Integer.MAX_VALUE);
			Arrays.fill(maxx, Integer.MIN_VALUE);
			Arrays.fill(maxy, Integer.MIN_VALUE);
			for (int i = 0; i < a.length; i++) {
				int root = dSet.getRoot(i);
				minSum[root] = Math.min(minSum[root], a[i].xmin + a[i].ymin);
				maxx[root] = Math.max(maxx[root], a[i].xmax);
				maxy[root] = Math.max(maxy[root], a[i].ymax);
			}
			
			int res = 0;
			for (int i = 0; i < maxy.length; i++) {
				int root = dSet.getRoot(i);
				int now = maxx[root] + maxy[root] - minSum[root] + 1;
				if (now > res) res = now;
			}
			
			writeCase(test);
			writer.println(" " + res);
		}
	}
	
	private boolean hasEdge(Rectangle rectangle, Rectangle anotherRectangle) {
		if (rectangle.intersect(anotherRectangle)) return true;
		int[][] d = new int[][] {{-1,0}, {0,-1}, {1,-1}};
		for (int i = 0; i < d.length; i++) {
			int dx = d[i][0];
			int dy = d[i][1];
			int x1 = anotherRectangle.xmin + dx;
			int x2 = anotherRectangle.xmax + dx;
			int y1 = anotherRectangle.ymin + dy;
			int y2 = anotherRectangle.ymax + dy;
			if (rectangle.intersect(new Rectangle(x1, y1, x2, y2)))
				return true;
		}
		return false;
	}

	private void writeCase(int test) {
		writer.printf("Case #%d:", test+1);
	}
	void debug(Object...os) {
		System.out.println(Arrays.deepToString(os));
	}
	//--------------------- IO stuffs ---------------------
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new C());
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
