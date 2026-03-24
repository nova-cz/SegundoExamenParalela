import java.util.Random;

public class SumaPromedioParalela {

    static final int N = 50_000_000;
    static double[] A = new double[N];
    static double[] sumasParciales;

    static class HiloSuma extends Thread {
        int id, inicio, fin;

        HiloSuma(int id, int inicio, int fin) {
            this.id = id;
            this.inicio = inicio;
            this.fin = fin;
        }

        @Override
        public void run() {
            double suma = 0;
            for (int i = inicio; i < fin; i++) {
                suma += A[i];
            }
            sumasParciales[id] = suma;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Generar N valores aleatorios en [0.0, 10.0]
        Random rng = new Random(42);
        for (int i = 0; i < N; i++) {
            A[i] = rng.nextDouble() * 10.0;
        }

        // VERSION SECUENCIAL
        long t0 = System.currentTimeMillis();
        double sumaSeq = 0;
        for (int i = 0; i < N; i++) sumaSeq += A[i];
        double promSeq = sumaSeq / N;
        long t1 = System.currentTimeMillis();
        System.out.println("=== Version Secuencial ===");
        System.out.printf("Suma    : %.4f%n", sumaSeq);
        System.out.printf("Promedio: %.4f%n", promSeq);
        System.out.println("Tiempo  : " + (t1 - t0) + " ms\n");

        // VERSION PARALELA
        int numHilos = Runtime.getRuntime().availableProcessors();
        sumasParciales = new double[numHilos];
        Thread[] hilos = new Thread[numHilos];
        int chunk = N / numHilos;

        long t2 = System.currentTimeMillis();
        for (int i = 0; i < numHilos; i++) {
            int inicio = i * chunk;
            int fin = (i == numHilos - 1) ? N : inicio + chunk;
            hilos[i] = new HiloSuma(i, inicio, fin);
            hilos[i].start();
        }
        for (Thread h : hilos) h.join();

        double sumaPar = 0;
        for (int i = 0; i < numHilos; i++) {
            sumaPar += sumasParciales[i];
        }
        double promPar = sumaPar / N;
        long t3 = System.currentTimeMillis();
        
        System.out.println("=== Version Paralela ===");
        System.out.printf("Suma    : %.4f%n", sumaPar);
        System.out.printf("Promedio: %.4f%n", promPar);
        System.out.println("Tiempo  : " + (t3 - t2) + " ms");
    }
}
