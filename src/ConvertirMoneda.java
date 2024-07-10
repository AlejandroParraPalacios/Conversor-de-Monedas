import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class ConvertirMoneda {
    public static void convertir(String monedaBase, String monedaTarget, ConsultarMoneda consulta, Scanner lectura) {
        System.out.println("Ingrese la cantidad de " + monedaBase);
        double cantidad = Double.parseDouble(lectura.nextLine());

        CountDownLatch latch = new CountDownLatch(1); // Crear un CountDownLatch con un conteo de 1

        CompletableFuture<Monedas> futureMonedas = consulta.buscarMonedaAsync(monedaBase, monedaTarget);
        futureMonedas.thenAccept(monedas -> {
            double cantidadConvertida = cantidad * monedas.conversion_rate();
            System.out.println("La tasa de conversión para hoy es\n " + monedaBase + " = " + monedas.conversion_rate() + " " + monedaTarget);
            System.out.println(cantidad + " " + monedaBase + " = " + cantidadConvertida + " " + monedas.target_code());
            latch.countDown(); // Disminuir el conteo del CountDownLatch
        }).exceptionally(e -> {
            System.err.println("Error al obtener la tasa de conversión: " + e.getMessage());
            latch.countDown(); // Asegurarse de disminuir el conteo en caso de error
            return null;
        });

        try {
            latch.await(); // Esperar a que el CountDownLatch llegue a cero
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void convertirOtraMoneda(ConsultarMoneda consulta, Scanner lectura) {
        System.out.println("Ingrese el código de la moneda base:");
        String monedaBase = lectura.nextLine().toUpperCase();
        System.out.println("Ingrese la moneda a convertir:");
        String monedaObjetivo = lectura.nextLine().toUpperCase();
        convertir(monedaBase, monedaObjetivo, consulta, lectura);
    }
}