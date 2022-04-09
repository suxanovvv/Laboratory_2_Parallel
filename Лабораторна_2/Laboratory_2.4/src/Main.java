//завдання 4

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static class ComputeArcSin{
        private BigDecimal arcsin;

        private BigDecimal bigFact(BigDecimal k){
            int a=k.compareTo(BigDecimal.ZERO);
            return (a == 0) ? BigDecimal.ONE : k.multiply(bigFact(k.subtract(BigDecimal.ONE)));
        }

        public void bigArcSin(int n, double x, int threadNum, int numThreads){
            BigDecimal fact1, fact2, numer, denom;
            BigDecimal a = new BigDecimal(x);
            BigDecimal arcsin = BigDecimal.ZERO;

            for(int i = threadNum; i < n; i+=numThreads){
                BigDecimal four = new BigDecimal(4);
                fact1 = bigFact(new BigDecimal(i*2));
                fact2 = bigFact(new BigDecimal(i));
                fact2 = fact2.multiply(fact2);
                numer = a.pow(i*2+1);
                numer = numer.multiply(fact1);
                denom = four.pow(i);
                denom = denom.multiply(fact2);
                denom = denom.multiply(new BigDecimal(2*i+1));
                numer = numer.divide(denom, BigDecimal.ROUND_HALF_DOWN);
                arcsin = arcsin.add(numer);
            }
            this.arcsin = arcsin;

        }

        public BigDecimal getResult(){
            return arcsin; }
    }

    public static class ParallelThread extends Thread {
        private ComputeArcSin compute;
        private int n;
        private double x;
        private int threadNum;
        private int numThreads;

        public ParallelThread(ComputeArcSin compute, int n, double x, int threadNum, int numThreads){
            this.compute = compute;
            this.n = n;
            this.x = x;
            this.threadNum = threadNum;
            this.numThreads = numThreads;
        }

        public void run(){
            compute.bigArcSin(n, x, threadNum, numThreads);
        }
    }


    public static final int TERMS_COUNT = 100;
    public static final double X = 0.85;

    public static void main(String[] args) {

        Scanner inp = new Scanner(System.in);
        System.out.println("Enter thread number:");
        int numThreads = inp.nextInt();

        //int numThreads = Runtime.getRuntime().availableProcessors();

        long p1 = System.nanoTime();

        ComputeArcSin[] compute = new ComputeArcSin[numThreads];

        ParallelThread[] pth = new ParallelThread[numThreads];
        for(int i=0; i<numThreads; i++){
            compute[i] = new ComputeArcSin();
            pth[i] = new ParallelThread(compute[i], TERMS_COUNT, X, i, numThreads);
        }

        p1 = System.nanoTime() - p1;
        long t = System.nanoTime();

        for(int i = 0; i < numThreads; i++){
            pth[i].start();
        }
        for(int i = 0; i < numThreads; i++){
            try{
                pth[i].join();
            } catch (InterruptedException e){
                e.printStackTrace(System.err);
            }
        }
        BigDecimal y = new BigDecimal(0.);

        t = System.nanoTime() - t;
        long p2 = System.nanoTime();

        for(int i = 0; i < numThreads; i++){
            y = y.add(compute[i].getResult());
        }
        BigDecimal err = new BigDecimal(Math.asin(X));
        err = err.subtract(y).abs();

        p2 = System.nanoTime() - p2;

        double p = (1.E-9 *(double)(p1 + p2)) / (1.E-9 *(double)(p1 + p2 + t));
        double s = 1/(p+(1-p)/numThreads);

        System.out.printf(Locale.ENGLISH, "x = %4.3f arcsin = %20.19f err = %.3e\n", X, y, err);
        System.out.printf(Locale.ENGLISH, "tp = %4.3f\n", 1.E-9 * (double) t);
        System.out.printf(Locale.ENGLISH, "Speedup = %4.3f\n", s );
    }
}