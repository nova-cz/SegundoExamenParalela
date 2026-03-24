public class SumaParalela {

    static int[] arreglo = new int[16];
    static int[] resultados = new int[4];

    static class HiloSuma extends Thread {
        int id;
        int inicio;
        int fin;

        HiloSuma(int id, int inicio, int fin) {
            this.id = id;
            this.inicio = inicio;
            this.fin = fin;
        }

        @Override
        public void run() {
            int suma = 0;
            for (int i = inicio; i <= fin; i++) {
                suma += arreglo[i];
            }
            resultados[id - 1] = suma;
            System.out.println("Hilo " + id + ": " + suma);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Generar arreglo 1..16
        for (int i = 0; i < 16; i++) {
            arreglo[i] = i + 1;
        }

        // Crear 4 hilos, cada uno con 4 casillas
        Thread[] hilos = {
            new HiloSuma(1, 0, 3),
            new HiloSuma(2, 4, 7),
            new HiloSuma(3, 8, 11),
            new HiloSuma(4, 12, 15)
        };

        // Iniciar todos
        for (Thread h : hilos) h.start();

        // Esperar a que todos terminen
        for (Thread h : hilos) h.join();

        System.out.println("Todos los hilos han terminado.");
    }
}
