import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class ConsultarMoneda {
    private static final String API_KEY = "0ae86dd36b35caf6e88e4fd1";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";
    private final HttpClient client;

    public ConsultarMoneda() {
        this.client = HttpClient.newHttpClient();
    }

    public CompletableFuture<Monedas> buscarMonedaAsync(String monedaBase, String monedaTarget) {
        URI direccion = URI.create(BASE_URL + monedaBase + "/" + monedaTarget);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(direccion)
                .timeout(Duration.ofSeconds(10)) // Ajusta el tiempo de espera según tus necesidades
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(responseBody -> {
                    System.out.println("Respuesta de la API: " + responseBody); // Para depuración
                    return new Gson().fromJson(responseBody, Monedas.class);
                })
                .exceptionally(e -> {
                    throw new RuntimeException("Error en la solicitud: " + e.getMessage(), e);
                });
    }
}