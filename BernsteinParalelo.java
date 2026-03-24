public class BernsteinParalelo {

    static double a, b, c, d;
    static double p1, p2, p3, x;

    // S1: p1 = a * b
    static class H1 extends Thread {
        public void run() { p1 = a * b; }
    }

    // S2: p2 = c / p1   (depende de p1 - ejecutar despues de S1)
    static class H2 extends Thread {
        public void run() { p2 = c / p1; }
    }

    // S3: p3 = -d    (independiente de S1)
    static class H3 extends Thread {
        public void run() { p3 = -d; }
    }

    public static void main(String[] args) throws InterruptedException {
        // Valores de prueba 1
        a = 2.0;  b = 3.0;  c = 12.0;  d = 1.0;
        System.out.println("Valores: a=" + a + "  b=" + b
                           + "  c=" + c + "  d=" + d);

        // ETAPA 1: S1 || S3   (paralelos - independientes)
        H1 h1 = new H1();
        H3 h3 = new H3();
        h1.start();   h3.start();
        h1.join();    h3.join();
        System.out.println("  p1 = a*b = " + p1);
        System.out.println("  p3 = -d  = " + p3);

        // ETAPA 2: S2   (requiere p1 listo)
        H2 h2 = new H2();
        h2.start();   h2.join();
        System.out.println("  p2 = c/p1 = " + p2);

        // ETAPA 3: combinacion final
        x = p1 + p2 + p3;
        System.out.println("Resultado x = " + x);

        // Verificacion
        double xSeq = a * b + c / (a * b) - d;
        System.out.println("Verificacion secuencial = " + xSeq);
        System.out.println("Resultados iguales: "
                           + (Math.abs(x - xSeq) < 1e-10));

        // Valores de prueba 2
        System.out.println("\n--- Segunda ejecucion: a=5 b=2 c=20 d=3 ---");
        a = 5;  b = 2;  c = 20;  d = 3;
        H1 h1b = new H1();  H3 h3b = new H3();
        h1b.start();  h3b.start();  h1b.join();  h3b.join();
        H2 h2b = new H2();  h2b.start();  h2b.join();
        x = p1 + p2 + p3;
        System.out.println("x paralelo     = " + x
                           + "  |  x secuencial = " + (a*b + c/(a*b) - d));
    }
}
