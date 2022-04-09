//завдання 5
import java.math.BigDecimal;
import java.util.Locale;

public class Main {

    public static class ComputeArcSin{
        private BigDecimal arcsin;

        private BigDecimal bigFact(BigDecimal k){
            int a=k.compareTo(BigDecimal.ZERO);
            return (a == 0) ? BigDecimal.ONE : k.multiply(bigFact(k.subtract(BigDecimal.ONE)));
        }

        public void bigArcSin(int n, double x){
            BigDecimal fact1, fact2, numer, denom;
            BigDecimal a = new BigDecimal(x);
            BigDecimal arcsin = BigDecimal.ZERO;

            for(int i = 0; i < n; i++){
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

    public static class VectorThread extends Thread {
        private ComputeArcSin compute;
        private int n;
        private double x;

        public VectorThread(ComputeArcSin compute, int n, double x){
            this.compute = compute;
            this.n = n;
            this.x = x;
        }

        public void run(){
            compute.bigArcSin(n, x);
        }
    }

    public static final int TERMS_COUNT = 100;
    public static final int THREADS_COUNT = 16;
    public static final double DX = 0.85/THREADS_COUNT;

    public static void main(String[] args) {
        long p1 = System.nanoTime();

        double[] x = new double[THREADS_COUNT];
        for(int i=0; i<THREADS_COUNT; i++){
            x[i] = DX*(i+1);
        }

        ComputeArcSin[] compute = new ComputeArcSin[THREADS_COUNT];
        VectorThread[] vth = new VectorThread[THREADS_COUNT];
        for(int i=0; i<THREADS_COUNT; i++){
            compute[i] = new ComputeArcSin();
            vth[i] = new VectorThread(compute[i], TERMS_COUNT, x[i]);
        }

        p1 = System.nanoTime() - p1;
        long t = System.nanoTime();

        for(int i = 0; i < THREADS_COUNT; i++){
            vth[i].start();
        }
        for(int i = 0; i < THREADS_COUNT; i++){
            try{
                vth[i].join();
            } catch (InterruptedException e){
                e.printStackTrace(System.err);
            }
        }
        BigDecimal[] y = new BigDecimal[THREADS_COUNT];
        BigDecimal[] err = new BigDecimal[THREADS_COUNT];

        t = System.nanoTime() - t;
        long p2 = System.nanoTime();

        for(int i = 0; i < THREADS_COUNT; i++){
            y[i] = compute[i].getResult();
            err[i] = new BigDecimal(Math.asin(x[i]));
            err[i] = err[i].subtract(y[i]).abs();
            System.out.printf(Locale.ENGLISH, "x = %4.3f Arcsin = %20.19f err = %.3e\n", x[i], y[i], err[i]);
        }

        p2 = System.nanoTime() - p2;
        double p = (1.E-9 *(double)(p1 + p2)) / (1.E-9 *(double)(p1 + p2 + t));
        double s = 1/(p+(1-p)/THREADS_COUNT);

        System.out.printf(Locale.ENGLISH, "tp = %4.3f\n", 1.E-9 * (double) t);
        System.out.printf(Locale.ENGLISH, "Speedup = %4.3f\n", s );
    }
}
