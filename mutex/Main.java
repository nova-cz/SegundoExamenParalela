package Mutex;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

// Clase que representa el recurso compartido (el pastel)
class Cake {
    private int quantity;

    public Cake(int quantity) {
        this.quantity = quantity;
    }

    public void biteMe() {
        this.quantity--;
    }

    public int getQuantity() {
        return quantity;
    }
}

// Clase que representa el hilo que consume el recurso
class Person implements Runnable {
    private Cake cake;
    private String name;
    private ReentrantLock mutex;

    public Person(Cake cake, String name, ReentrantLock mutex) {
        this.cake = cake;
        this.name = name;
        this.mutex = mutex;
    }

    @Override
    public void run() {
        // Las personas intentan morder mientras haya pastel
        while (cake.getQuantity() > 0) {
            
            // Sección Crítica protegida por el Mutex
            mutex.lock(); 
            try {
                // Doble comprobación: ¿sigue habiendo pastel después de obtener el bloqueo?
                if (cake.getQuantity() > 0) {
                    System.out.println(name + " le muerde al pastel. Quedan: " + cake.getQuantity());
                    cake.biteMe();
                    System.out.println(name + " deja el pastel. Quedan: " + cake.getQuantity());
                }
            } finally {
                // Siempre liberar el mutex en un bloque finally
                mutex.unlock();
            }
            
            // Pequeña pausa opcional para simular tiempo de masticado y permitir otros hilos
            try { Thread.sleep(10); } catch (InterruptedException e) {}
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // Inicializamos el pastel con 100 porciones
        Cake myCake = new Cake(100);
        
        // El objeto Mutex que compartiran todas las personas
        ReentrantLock mutex = new ReentrantLock();

        // Lista de personas (Hilos)
        String[] names = {"Héctor", "Juan", "Ana", "Rafael", "Brenda"};
        List<Thread> threads = new ArrayList<>();

        for (String name : names) {
            Thread t = new Thread(new Person(myCake, name, mutex));
            threads.add(t);
            t.start(); // Iniciamos el hilo
        }
    }
}
