import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Menu {
    Scanner sc = new Scanner(System.in);
    String endereco;

    public Menu(String apiKey) {
        endereco = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/";
    }

    public void iniciarMenu() {
        mensagemBemVindo();
        opcao();
    }

    private void mensagemBemVindo() {
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=" +
                "\nBem-vindo(a) ao Conversor de Moedas!" +
                "\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
    }

    private void mensagemOpcoes() {
        System.out.println("1) Dólar -> Peso Argentino" +
                "\n2) Peso Argentino -> Dólar" +
                "\n3) Dólar -> Real Brasileiro" +
                "\n4) Real Brasileiro -> Dólar" +
                "\n5) Dólar -> Peso Colombiano" +
                "\n6) Peso Colombiano -> Dólar" +
                "\n7) Sair" +
                "\nEscolha uma opcão válida: ");
    }

    public void opcao() {
        mensagemOpcoes();
        int opcao = sc.nextInt();

        while (opcao < 1 || opcao > 7) {
            System.out.println("Opcão Inválida, Tente Novamente: ");
            opcao = sc.nextInt();
        }
        switch (opcao) {
            case 1:  converter("USD", "ARS");
            case 2:  converter("ARS", "USD");
            case 3:  converter("USD", "BRL");
            case 4:  converter("BRL", "USD");
            case 5:  converter("USD", "ars");
            case 6:  converter("usd", "USD");
            case 7:  System.exit(0);
            default: break;
        }
    }

    private void converter(String moeda1, String moeda2) {
        String endereco = this.endereco + moeda1;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endereco))
                    .build();

            HttpResponse<String> response = null;

            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();
            System.out.println(json);
            
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
