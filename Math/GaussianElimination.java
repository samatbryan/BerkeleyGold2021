import java.util.*;
import java.io.*;
public class GaussianElimination {
	static long[] mods = {1000000007, 998244353, 1000000009};
	static long mod = mods[0];
	public static MyScanner sc;
    public static PrintWriter out;
    static int[] inverse;
	public static void main(String[] havish) throws Exception{
		// TODO Auto-generated method stub
 		double[][] testA = new double[][]{new double[]{1, 2, 3}, new double[]{3, 4, 5}, new double[]{4, 5, 7}};
 		out = new PrintWriter(System.out);
 		double[] b = new double[]{8, 9, 0};
 		GS gs = new GS(testA, b);
 		out.println(gs.numSolutions());
 		
	 		
 		out.close();
 	}
	static class GS {
	    private static final double EPSILON = 1e-8;

	    private final int m;      // number of rows
	    private final int n;      // number of columns
	    private double[][] a;     // m-by-(n+1) augmented matrix

	    /**
	     * Solves the linear system of equations <em>Ax</em> = <em>b</em>,
	     * where <em>A</em> is an <em>m</em>-by-<em>n</em> matrix and <em>b</em>
	     * is a length <em>m</em> vector.
	     *
	     * @param  A the <em>m</em>-by-<em>n</em> constraint matrix
	     * @param  b the length <em>m</em> right-hand-side vector
	     * @throws IllegalArgumentException if the dimensions disagree, i.e.,
	     *         the length of {@code b} does not equal {@code m}
	     */
	    public GS(double[][] A, double[] b) {
	        m = A.length;
	        n = A[0].length;

	        if (b.length != m) throw new IllegalArgumentException("Dimensions disagree");

	        // build augmented matrix
	        a = new double[m][n+1];
	        for (int i = 0; i < m; i++)
	            for (int j = 0; j < n; j++)
	                a[i][j] = A[i][j];
	        for (int i = 0; i < m; i++)
	            a[i][n] = b[i];

	        forwardElimination();

	    }

	    // forward elimination
	    private void forwardElimination() {
	        for (int p = 0; p < Math.min(m, n); p++) {

//	            // find pivot row using partial pivoting
	            int max = p;
	            for (int i = p+1; i < m; i++) {
	                if (Math.abs(a[i][p]) > Math.abs(a[max][p])) {
	                    max = i;
	                }
	            }

	            // swap
	            swap(p, max);

	            // singular or nearly singular
	            int index = p;
	            while(index < a[0].length - 1 && Math.abs(a[p][index]) <= EPSILON)
	        		index++;
	            if (index == a[0].length - 1) {
	                continue;
	            }

	            // pivot
	            pivot(p);
	        }
	    }

	    // swap row1 and row2
	    private void swap(int row1, int row2) {
	        double[] temp = a[row1];
	        a[row1] = a[row2];
	        a[row2] = temp;
	    }

	    // pivot on a[p][p]
	    private void pivot(int p) {
	        for (int i = p+1; i < m; i++) {
	        	int index = p;
	        	while(index < a[0].length - 1 && Math.abs(a[p][index]) <= EPSILON)
	        		index++;
	        	if (index >= a[0].length - 1)
	        		return;
	            double alpha = a[i][index] /a[p][index];
	            for (int j = index; j <= n; j++) {
	                a[i][j] -= alpha * a[p][j];
	            }
	        }
	    }

	    /**
	     * Returns a solution to the linear system of equations <em>Ax</em> = <em>b</em>.
	     *      
	     * @return a solution <em>x</em> to the linear system of equations
	     *         <em>Ax</em> = <em>b</em>; {@code null} if no such solution
	     */
	    public double[] primal() {
	        // back substitution
	        double[] x = new double[n];
	        for (int i = Math.min(n-1, m-1); i >= 0; i--) {
	            double sum = 0;
	            for (int j = i+1; j < n; j++) {
	                sum += a[i][j] * x[j];
	            }
	            int index = i;
	            while(index < a[0].length - 1 && Math.abs(a[i][index]) < EPSILON)
	            	index++;
	            if (index < a[0].length - 1 && Math.abs(a[i][index]) > EPSILON){
	                x[index] = (a[i][n] - sum)/a[i][index];
	            }
	            else if (Math.abs(a[i][n] - sum) > EPSILON)
	                return null;
	        }

	        // redundant rows
	        for (int i = n; i < m; i++) {
	            double sum = 0;
	            for (int j = 0; j < n; j++) {
	                sum += a[i][j] * x[j];
	            }
	            if (Math.abs(a[i][n] - sum) > EPSILON)
	                return null;
	        }
	        return x;
	    }

	    /**
	     * Returns true if there exists a solution to the linear system of
	     * equations <em>Ax</em> = <em>b</em>.
	     *      
	     * @return {@code true} if there exists a solution to the linear system
	     *         of equations <em>Ax</em> = <em>b</em>; {@code false} otherwise
	     */
	    public boolean isFeasible() {
	        return primal() != null;
	    }


	    // check that Ax = b
	    private boolean certifySolution(double[][] A, double[] b) {
	        if (!isFeasible()) return false;
	        double[] x = primal();
	        for (int i = 0; i < m; i++) {
	            int sum = 0;
	            for (int j = 0; j < n; j++) {
	                sum += A[i][j] * x[j];
	            }
	            if (Math.abs(sum - b[i]) > EPSILON) {
	                return false;
	            }
	        }
	        return true;
	    }
	    // return 0 if no solutions 1 if 1 solution, 2 if inf solutions
	    private int numSolutions() {
	    	if (!isFeasible())
	    		return 0;
	    	boolean allzero = true;
 			for(int j = 0;j<a[0].length;j++) {
 				if (a[a.length-1][j] != 0)
 					allzero = false;
 			}
 			if (allzero)
 				return 2;
 			else
 				return 1;
	    	
	    }
	}
	public static int charToNum(char input) {
		if (input >= 'A' && input <= 'Z')
			return input-'A';
		else if (input >= '0' && input <= '9')
			return (input -'0') + 26;
		else
			return 36;
	}
	public static void sort(int[] array){
		ArrayList<Integer> copy = new ArrayList<>();
		for (int i : array)
			copy.add(i);
		Collections.sort(copy);
		for(int i = 0;i<array.length;i++)
			array[i] = copy.get(i);
	}
	static String[] readArrayString(int n){
		String[] array = new String[n];
		for(int j =0 ;j<n;j++)
			array[j] = sc.next();
		return array;
	}
	static int[] readArrayInt(int n){
    	int[] array = new int[n];
    	for(int j = 0;j<n;j++)
    		array[j] = sc.nextInt();
    	return array;
    }
	static int[] readArrayInt1(int n){
		int[] array = new int[n+1];
		for(int j = 1;j<=n;j++){
			array[j] = sc.nextInt();
		}
		return array;
	}
	static long[] readArrayLong(int n){
		long[] array = new long[n];
		for(int j =0 ;j<n;j++)
			array[j] = sc.nextLong();
		return array;
	}
	static double[] readArrayDouble(int n){
		double[] array = new double[n];
		for(int j =0 ;j<n;j++)
			array[j] = sc.nextDouble();
		return array;
	}
	static int minIndex(int[] array){
		int minValue = Integer.MAX_VALUE;
		int minIndex = -1;
		for(int j = 0;j<array.length;j++){
			if (array[j] < minValue){
				minValue = array[j];
				minIndex = j;
			}
		}
		return minIndex;
	}
	static int minIndex(long[] array){
		long minValue = Long.MAX_VALUE;
		int minIndex = -1;
		for(int j = 0;j<array.length;j++){
			if (array[j] < minValue){
				minValue = array[j];
				minIndex = j;
			}
		}
		return minIndex;
	}
	static int minIndex(double[] array){
		double minValue = Double.MAX_VALUE;
		int minIndex = -1;
		for(int j = 0;j<array.length;j++){
			if (array[j] < minValue){
				minValue = array[j];
				minIndex = j;
			}
		}
		return minIndex;
	}
	static long power(long x, long y){
		if (y == 0)
			return 1;
		if (y%2 == 1)
			return (x*power(x, y-1))%mod;
		return power((x*x)%mod, y/2)%mod;
	}
	static void verdict(boolean a){
        out.println(a ? "YES" : "NO");
    }
    public static class MyScanner {
        BufferedReader br;
        StringTokenizer st;
        public MyScanner() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }
        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } 
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }
        int nextInt() {
            return Integer.parseInt(next());
        }
        long nextLong() {
            return Long.parseLong(next());
        }
        double nextDouble() {
            return Double.parseDouble(next());
        }
        String nextLine() {
            String str = "";
            try{
                str = br.readLine();
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }
        
    }	
}

//StringJoiner sj = new StringJoiner(" "); 
//sj.add(strings)
//sj.toString() gives string of those stuff w spaces or whatever that sequence is
