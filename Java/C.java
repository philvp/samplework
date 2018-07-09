import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class C implements Runnable {
	private boolean _ReadFromFile = System.getProperty("ONLINE_JUDGE") == null;
	private boolean _WriteToFile = true;
	static final String TASK_ID = "c2";
	static final String IN_FILE = TASK_ID + ".in";
	static final String OUT_FILE = TASK_ID + ".out";
	BufferedReader reader;
	StringTokenizer tokenizer;
	PrintWriter writer;

	private void core() throws Exception {
		int ntest = nextInt();
		for (int test = 0; test < ntest; test++) {
			TreeMap<String, LinkedList<String>> depend = new TreeMap<String, LinkedList<String>>();
			TreeMap<String, LinkedList<Object>> equation = new TreeMap<String, LinkedList<Object>>();
			int n = nextInt();
			for (int i = 0; i < n; i++) {
				String lhs = nextToken();
				String temp = nextToken();
				
				LinkedList<String> dep = new LinkedList<String>();
				LinkedList<Object> eq = new LinkedList<Object>();
				while(tokenizer.hasMoreElements()) {
					temp = nextToken();
					try {
						Integer cur = Integer.parseInt(temp);
						eq.add(cur);
					} catch (Exception e) {
						eq.add(temp);
						if (!temp.equals("+") && !temp.equals("-") && 
								!temp.equals("*") && !temp.equals("/")) {
							dep.add(temp);
						}
					}
				}
				depend.put(lhs, dep);
				equation.put(lhs, eq);
			}
			
			TreeMap<String, Long> valMap = new TreeMap<String, Long>();
			while(true) {
				boolean finish = true;
				for (String lhsString : depend.keySet()) {
					if (valMap.containsKey(lhsString)) continue;
					finish = false;
					boolean ok = true;
					for (String item : depend.get(lhsString)) {
						if (!valMap.containsKey(item)) {
							ok = false;
							break;
						}
					}
					if (ok) {
						Long res = compute(lhsString, equation.get(lhsString), valMap);
						valMap.put(lhsString, res);
					}
				}	
				if (finish) 
					break;
			}
			
			if (test > 0)
				writer.println();
			for (String lhsString : depend.keySet()) {
				writer.println(lhsString + " = " + valMap.get(lhsString));
			}
		}
	}
	private long compute(String lhsString, LinkedList<Object> equation,
			TreeMap<String, Long> valMap) {
		LinkedList<String> input = new LinkedList<String>();
		for (Object object : equation) {
			String string = "" + object;
			if (valMap.containsKey(string)) {
				input.add("" + valMap.get(string));
			}
			else input.add(string);
		}
		return (long) solve(input);
	}
	
	private int solve(LinkedList<String> input) {
		int newRes = doall(input);
		return newRes;
	}
	private int doall(LinkedList<String> input) {
		LinkedList<Integer> res = new LinkedList<Integer>();
		LinkedList<Character> operatorStack = new LinkedList<Character>();
		for (String string : input) {
			try {
				Integer number = Integer.parseInt(string);
				res.add(number);
			} catch (Exception e) {	//not a number
				Character op = string.charAt(0);
				if (op == '(') {
					operatorStack.push(op);
				}
				else if (op == ')') {
					while(!operatorStack.isEmpty() && operatorStack.peek() != '(') {
						addOperator(res, operatorStack.peek());
						operatorStack.removeFirst();
					}
					operatorStack.removeFirst();
				}
				else { 
					while(!operatorStack.isEmpty() && precedence(op) <= precedence(operatorStack.peek())) {
						addOperator(res, operatorStack.peek());
						operatorStack.removeFirst();
					}
					operatorStack.push(op);
				}
			}
		}
		for (Character character : operatorStack) {
			addOperator(res, character);
		}
		return res.getFirst();
	}
	private void addOperator(LinkedList<Integer> operands, Character operator) {
		int right = operands.removeLast();
		int left = operands.removeLast();
		int res = 0;
		switch (operator) {
			case '+': res = left + right; break;
			case '-': res = left - right; break;
			case '*': res = left * right; break;
			case '/': res = left / right; break;
		}
		operands.add(res);
	}
	private int precedence(Character op) {
		switch (op) {
			case '(' : return 0;
			case '+' :
			case '-' : return 1;
			case '*' :
			case '/' : return 2;
		}
		return Integer.MAX_VALUE;
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
