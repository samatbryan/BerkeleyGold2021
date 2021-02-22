import java.util.Arrays;

public class Mathematics {

	public static void main(String[] args) {
	}

	static long factorial(int n) {
		if (n <= 1) {
			return 1;
		}
		return n * factorial(n - 1);
	}

	static long choose(int n, int k) {
		return factorial(n) / factorial(k) / factorial(n - k);
	}

	/**
	 * log n!
	 * useful because n! is very large
	 */
	static double logFactorial(int n) {
		if (n <= 1) {
			return 0;
		}
		return Math.log(n) + logFactorial(n - 1);
	}

	/**
	 * log n choose k
	 * useful because n choose k is very large
	 */
	static double logChoose(int n, int k) {
		return logFactorial(n) - logFactorial(k) - logFactorial(n - k);
	}

	
	static int gcd(int a, int b) {
		if (a == 0 || b == 0) {
			return a + b;
		} else if (a == 1 || b == 1) {
			return 1;
		} else if (a > b) {
			return gcd(a % b, b);
		} else {
			return gcd(b % a, a);
		}
	}

	static int lcm(int a, int b) {
		return a * b / gcd(a, b);
	}

	/**
	 * Calculates inverse of a mod b
	 */
	public static int inverse(int a, int b) {
		int r1 = b;
		int r2 = a;
		int s1 = 1, s2 = 0, t1 = 0, t2 = 1;
		while (true) {
			if (r2 == 0) {
				if (t1 < 0) {
					t1 += b;
				}
				return t1;
			}
			int q = r1 / r2;
			int r = r1 % r2;
			int sn = s1 - q * s2;
			int tn = t1 - q * t2;
			r1 = r2;
			r2 = r;
			s1 = s2;
			s2 = sn;
			t1 = t2;
			t2 = tn;
		}
	}

	/**
	 * Calculates an integer E such that E == a mod b and E == c mod d
	 */
	public static int chineseRemainderTheorem(int a, int b, int c, int d) {
		int N = b * d;
		int minvn = (inverse(b % d, d) * b) % N;
		int ninvm = (inverse(d % b, b) * d) % N;
		int cm = (c * minvn) % N;
		int an = (a * ninvm) % N;
		return (cm + an) % N;
	}

	/**
	 * generates a permutation of (0, 1, ..., limit - 1)
	 * 
	 * @param a     current index
	 * @param b     array (-1 indicates blank)
	 */
	public static void permutationGenerator(int a, int[] b) {
		if (a == b.length) {
			for (int c: b) {
				System.out.print(c + " ");
			}
			System.out.println();
			// do something here
			return;
		}
		for (int i = 0; i < b.length; i++) {
			if (b[i] != -1) {
				continue;
			}
			b[i] = a;
			permutationGenerator(a + 1, b);
			b[i] = -1;
		}
		return;
	}

	/**
	 * generates pythagorean triples using euclid's algorithm
	 */
	public static void pythagoreanGenerator() { 
		int limit = 100;
		for (int i = 1; i < limit; i++) {
			for (int j = 1; j < i; j++) {
				if (gcd(i, j) != 1) {
					continue;
				}
				if (i % 2 == 1 && j % 2 == 1) {
					continue;
				}
				int a = i * i - j * j;
				int b = 2 * i * j;
				int c = i * i + j * j;
				int p = a + b + c;
				int k = 1;
				
				System.out.println(a + " " + b + " " + c);
				
			}
		}
	}


	/**
	 * generates an array of primes in increasing order, using the sieve of
	 * eratosthenes
	 */
	public static void primeGenerator() {
		boolean[] prime = new boolean[1000000000];
		Arrays.fill(prime, true);
		prime[0] = false;
		prime[1] = false;
		for (int i = 2; i <= Math.sqrt(prime.length); i++) {
			if (!prime[i])
				continue;
			int a = i;
			while (true) {
				a += i;
				if (a > prime.length - 1)
					break;
				prime[a] = false;
			}
		}
		int numprimes = 0;
		for (int i = 0; i < prime.length; i++) {
			if (prime[i])
				numprimes++;
		}
		int[] primes = new int[numprimes];
		int index = 0;
		for (int i = 0; i < prime.length; i++) {
			if (prime[i]) {
				primes[index] = i;
				index++;
			}
		}
		System.out.println(numprimes);
	}


	/**
	 * calculates number of divisors of an integer
	 * 
	 * @param a number
	 * @return number of integers less than or equal to a that also divide a
	 */
	public static int numDivisors(int a) {
		int product = 1;
		int y = 0;
		while (a % 2 == 0) {
			a = a / 2;
			y++;
		}
		product *= (y + 1);
		for (int i = 3; i <= Math.sqrt(a); i += 2) {
			int x = 0;
			while (a % i == 0) {
				a = a / i;
				x++;
			}
			if (x == 0)
				continue;
			product *= (x + 1);
		}
		if (a != 1) {
			product *= 2;
		}
		return product;
	}

	/**
	 * generates an array of totient, where each index is totient(i)
	 * 
	 * @param a number
	 */
	public static void totientGenerator(int a) { // uses a sieve
		long[] totient = new long[a / 2 + 1];
		for (int i = 2; i < totient.length; i++) {
			totient[i] = i;
		}
		for (int i = 2; i < totient.length; i++) {
			if (totient[i] != i)
				continue;
			int k = i;
			while (k < totient.length) {
				totient[k] *= (i - 1);
				totient[k] /= i;
				k += i;
			}
		}
	}


	/**
	 * euler's totient function, number of numbers less than a relatively prime to a
	 * 
	 * @param a number
	 * @return number of numbers less than a relatively prime to a
	 */
	public static int totient(int a) {
		int product = 1;
		int y = 0;
		while (a % 2 == 0) {
			a = a / 2;
			y++;
		}
		if (y != 0)
			product = product * (2 - 1) * (int) Math.pow(2, y - 1);
		for (int i = 3; i <= Math.sqrt(a); i += 2) {
			int x = 0;
			while (a % i == 0) {
				a = a / i;
				x++;
			}
			if (x == 0)
				continue;
			product *= (i - 1) * (int) Math.pow(i, x - 1);
		}
		if (a != 1) {
			product *= (a - 1);
		}
		return product;
	}

	/**
	 * prime factorizes an integer
	 * 
	 * @param a number
	 */
	public static void primeFactorize(int a) {
		while (a % 2 == 0) {
			a /= 2;
			System.out.println(2);
		}
		for (int i = 3; i <= Math.sqrt(a); i += 2) {
			if (a % i == 0) {
				System.out.println(i);
				a = a / i;
				i -= 2;
			}
		}
		if (a != 1) {
			System.out.println(a);
		}
	}

	/**
	 * tests whether an integer is prime or not
	 * 
	 * @param a number
	 * @return if a is prime
	 */
	public static boolean isPrime(int a) {
		if (a == 2)
			return true;
		if (a % 2 == 0)
			return false;
		for (int i = 3; i <= Math.sqrt(a); i += 2) {
			if (a % i == 0)
				return false;
		}
		return true;
	}
}
