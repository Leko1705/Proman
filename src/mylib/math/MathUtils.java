package mylib.math;

import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public final class MathUtils {


    public static final double GOLDEN_RATIO = (1 + Math.sqrt(5)) / 2;


    /**
     * never let anyone instantiate this class
     */
    private MathUtils(){
    }


    /**
     * Returns the <code>n</code>-th digit from <code>i</code> by the radix of 10.
     * This method will return 0 if <code>n < 0</code> or if <code>n</code>
     * is greater than the given integer length.
     * @param i the number to extract the digit
     * @param n the index
     * @return the <code>n</code>-th digit from <code>i</code>
     */
    public static int getDigitAt(int i, int n){
        return getDigitAt(i, n, 10);
    }

    /**
     * Returns the <code>n</code>-th digit from <code>i</code> by the given radix.
     * This method will return 0 if <code>n < 0</code> or if <code>n</code>
     * is greater than the given integer length.
     * @param i the number to extract the digit
     * @param n the index
     * @return the <code>n</code>-th digit from <code>i</code>
     */
    public static int getDigitAt(int i, int n, int radix){
        if (n < 0) return 0;
        n = (int) Math.pow(radix, n);
        i -= i % n;
        i /= n;
        return i % radix;
    }


    /**
     * returns the amount of digits of a number <code>n</code> by the radix of 10.
     * If the number is 0 the result is one.
     * For negative numbers the result is <code>digitCount(abs(n))</code>.
     * @return the amount od digits of <code>n</code>
     */
    public static int digitCount(int n){
        return digitCount(n, 10);
    }


    /**
     * returns the amount of digits of a number <code>n</code> by the given radix.
     * If the number is 0 the result is one.
     * For negative numbers the result is <code>digitCount(abs(n))</code>.
     * @return the amount od digits of <code>n</code>
     */
    public static int digitCount(int n, int radix){
        if (n < 0) n *= -1;
        else if (n == 0) return 1;
        int l = 0;
        for (; n > 0; l++)
            n /= radix;
        return l;
    }


    /**
     * Returns the <code>n</code>-th bit from <code>i</code> where
     * for <code>n = 0</code> the lsb will be returned.
     * This method will return 0 if <code>n < 0</code>.
     * @param i the number to extract the bit
     * @param n the index
     * @return the <code>n</code>-th bit from <code>i</code>
     */
    public static int getBitAt(int i, int n){
        if (n < 0) return 0;
        return (i >>> n) & 1;
    }



    /**
     * Returns the <code>n</code>-th bit from <code>i</code> where
     * for <code>n = 0</code> the lsb will be returned.
     * This method will return 0 if <code>n < 0</code>.
     * @param i the number to extract the bit
     * @param n the index
     * @return the <code>n</code>-th bit from <code>i</code>
     */
    public static int getBitAt(long i, int n){
        if (n < 0) return 0;
        return (int) ((i >>> n) & 1);
    }


    /**
     * returns the <code>n</code>-th fibonacci number
     * @param n input <code>n</code>
     * @return the <code>n</code>-th fibonacci number
     */
    public static int fib(int n){
        return (int) fib((long) n);
    }

    /**
     * returns the <code>n</code>-th fibonacci number
     * @param n input <code>n</code>
     * @return the <code>n</code>-th fibonacci number
     */
    public static long fib(long n){
        if (n == 0) return 0;
        int a = 1;
        int b = 0;
        for (int i = 0; i < n-1; i++){
            int c = a;
            a += b;
            b = c;
        }
        return n < 0 && n % 2 == 0 ? -a : a;
    }


    /**
     * calculates the faculty of <code>n</code>
     * for a negative number 1 is returned
     * @return faculty of <code>n</code>
     */
    public static long fak(int n){
        return fak((long) n);
    }

    /**
     * calculates the faculty of <code>n</code>
     * for a negative number 1 is returned
     * @return faculty of <code>n</code>
     */
    public static long fak(long n){
        long v = 1;
        for (int i = 1; i < n+1; i++)
            v *= i;
        return v;
    }

    /**
     * calculates the binomialCoefficient <code>n</code> choose <code>k</code>
     * @return the coefficient n over k
     */
    public static long binomialCoefficient(long n, long k){
        if (k == 0 || n == k) return 1;
        if (k > n) return 0;
        return fak(n) / (fak(k) * fak(n - k));
    }

    /**
     * calculates the binomialCoefficient <code>n</code> choose <code>k</code>
     * @return the coefficient n over k
     */
    public static long binomialCoefficient(int n, int k){
        if (k == 0 || n == k) return 1;
        if (k > n) return 0;
        return fak(n) / (fak(k) * fak(n - k));
    }


    public static double catalan(double n){
        return (1 / n) * binomialCoefficient((long) (2*(n-1)), (long) (n-1));
    }


    /**
     * Returns the greatest common divider from a given <code>a</code> and <code>b</code>.
     * @return the greatest common divider from <code>a</code> and <code>b</code>
     */
    public static int ggT(int a, int b){
        return (int) ggT(a, (long) b);
    }

    /**
     * Returns the greatest common divider from a given <code>a</code> and <code>b</code>.
     * @return the greatest common divider from <code>a</code> and <code>b</code>
     */
    public static long ggT(long a, long b){
        b = Math.abs(b);
        if (b == 0)
            return a;
        return ggT(b, a % b);
    }


    /**
     * Returns the bezout coefficients from a given <code>a</code> and <code>b</code>.
     * @return the bezout coefficients from <code>a</code> and <code>b</code>
     */
    public static int[] eeA(int a, int b){
        long[] bez = eeA(a, (long) b);
        return new int[]{(int) bez[0], (int) bez[1]};
    }

    /**
     * Returns the bezout coefficients from a given <code>a</code> and <code>b</code>.
     * @return the bezout coefficients from <code>a</code> and <code>b</code>
     */
    public static long[] eeA(long a, long b){
        if (a % b == 0){
            int r = b < 0 ? -1 : 1;
            return new long[]{0, r};
        }
        long[] bez = eeA(b, a % b);
        long d = bez[0] - (a / b) * bez[1];
        bez[0] = bez[1];
        bez[1] = d;
        return bez;
    }


    /**
     * Checks if the given number is prime.
     * It returns true if it is prime, otherwise false.
     * @param n the checked number
     * @return true if it is prime, otherwise false
     */
    public static boolean isPrime(int n){
        return isPrime((long) n);
    }

    /**
     * Checks if the given number is prime.
     * It returns true if it is prime, otherwise false.
     * @param n the checked number
     * @return true if it is prime, otherwise false
     */
    public static boolean isPrime(long n) {
        if(n < 2) return false;
        if(n == 2 || n == 3) return true;
        if(n%2 == 0 || n%3 == 0) return false;
        long sqrtN = (long) Math.sqrt(n) + 1;
        for(long i = 6L; i <= sqrtN; i += 6) {
            if (n % (i - 1) == 0 || n % (i + 1) == 0)
                return false;
        }
        return true;
    }


    /**
     * Generates a random Prime number with 32 bits
     * @return a Prime Number
     */
    public static int genPrimeInt(){
        while (true)
            try {
                return genPrime(Integer.SIZE).intValueExact();
            }
            catch (ArithmeticException ignored){
            }
    }

    /**
     * Generates a random Prime number with 64 bits
     * @return a Prime Number
     */
    public static long genPrimeLong(){
        while (true)
            try {
                return genPrime(Long.SIZE).longValueExact();
            }
            catch (ArithmeticException ignored){
            }
    }

    /**
     * Generates a random Prime number with <code>n</code> bits
     * @param n the bit size
     * @return a Prime Number
     */
    public static BigInteger genPrime(int n){
        BigInteger max = BigInteger.ONE.shiftLeft(n);
        BigInteger prime;
        do {
            // Pick a random number between 0 and 2 ^ numBits - 1
            BigInteger integer = new BigInteger(n, ThreadLocalRandom.current());
            // Pick the next prime. Caution, it can exceed n^numBits, hence the loop
            prime = integer.nextProbablePrime();
        } while(prime.compareTo(max) > 0);
        return prime;
    }



    public static long modPower(long a, long b, long n) {
        long p = 1;
        for (int i = Long.SIZE-1; i >= 0; i--){
            p = (p * p) % n;
            if (getBitAt(b, i) == 1){
                p = (p * a) % n;
            }
        }
        return p;
    }


    /**
     * calculates the chinese remainder theorem to solve multiple congruences
     * for the same input at a time.
     * @param a the factors for the i-th equation
     * @param m the moduli for the i-th equation
     * @throws IllegalArgumentException if <code>a.length != m.length</code> or
     * if one of both length is 0 or if not for all <code>ggT(m[i], m[j]) == 1</code>
     * @throws NullPointerException if <code>a</code> or <code>b</code> is null
     * @return an int that solves all equations of the form x kong. a[i] mod m[i]
     */
    public static int chineseRemainderTheorem(int[] a, int[] m){
        verifyChineseRemainderTheoremInput(a, m);

        int[] p = new int[a.length];

        for (int i = 0; i < m.length; i++){
            p[i] = 1;
            for (int j = 0; j < m.length; j++){
                if (i != j){
                    if (ggT(m[i], m[j]) != 1) notifyNotCoprime(m[i], m[j]);
                    p[i] *= m[j];
                }
            }
        }

        int x = 0;
        for (int i = 1; i < a.length; i++){
            int t = eeA(m[i], p[i])[1];
            int xi = t * p[i];
            x += xi * a[i];
        }

        return x;
    }

    private static void verifyChineseRemainderTheoremInput(int[] a, int[] m){
        if (Objects.requireNonNull(a).length != Objects.requireNonNull(m).length)
            throw new IllegalArgumentException("input size does not match");
        if (a.length == 0)
            throw new IllegalArgumentException("invalid input size of 0");
    }

    private static void notifyNotCoprime(int n, int m){
        throw new IllegalArgumentException(n + " and " + m + " are not coprime");
    }


    /**
     * evaluates the gaussian algorithm on a given matrix and returns it
     * @param matrix the matrix to operate on
     * @return the given matrix with reduced rows
     * @throws IllegalArgumentException if the given matrix is invalid.
     * A matrix is invalid if it is a <code>0x0</code> matrix or if the size of the rows
     * does not match.
     */
    public static double[][] gaussianAlgorithm(double[][] matrix){

        verifyMatrix(matrix);

        final int rows = matrix.length;
        final int columns = matrix[0].length;

        for (int headerRow = 0; headerRow < rows-1; headerRow++){
            double leadingHeaderFactor = matrix[headerRow][headerRow];

            for (int operatedRow = headerRow+1; operatedRow < rows; operatedRow++){
                double leadingOperatedRowFactor = matrix[operatedRow][headerRow];

                for (int column = headerRow; column < columns; column++){

                    double currentHeaderFactor = matrix[headerRow][column];
                    double currentOperatedRowFactor = matrix[operatedRow][column];

                    matrix[operatedRow][column] =
                            currentHeaderFactor * leadingOperatedRowFactor
                                    - currentOperatedRowFactor * leadingHeaderFactor;
                }

            }

        }

        return matrix;
    }

    /*
    check weather it's a valid matrix
     */
    private static void verifyMatrix(double[][] matrix){
        if (matrix.length == 0)
            notifyInvalidMatrix();

        int len = matrix[0].length;
        for (int i = 1; i < matrix.length; i++)
            if (matrix[i].length != len)
                notifyInvalidMatrix();


    }

    private static void notifyInvalidMatrix(){
        throw new IllegalArgumentException("invalid matrix");
    }

    public static void printMatrix(double[][] m){
        for (double[] doubles : m) {
            for (double aDouble : doubles) {
                System.out.print(aDouble + ", ");
            }
            System.out.println();
        }
    }

}
