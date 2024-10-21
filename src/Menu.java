import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Menu {
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

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
        System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=" +
                "\nBem-vindo(a) ao Conversor de Moedas!" +
                "\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
    }

    private void mensagemOpcoes() {
        System.out.print("1) Dólar -> Peso Argentino" +
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
            System.out.print("Opcão Inválida, Tente Novamente: ");
            opcao = sc.nextInt();
        }
        if (opcao == 7) {
            System.exit(0);
        }

        System.out.print("Valor para converter: ");
        Double valor = sc.nextDouble();

        switch (opcao) {
            case 1:  converter("USD", "ARS", valor);
                break;
            case 2:  converter("ARS", "USD", valor);
                break;
            case 3:  converter("USD", "BRL", valor);
                break;
            case 4:  converter("BRL", "USD", valor);
                break;
            case 5:  converter("USD", "COP", valor);
                break;
            case 6:  converter("COP", "USD", valor);
        }
    }

    private void converter(String moeda1, String moeda2, Double valor) {
        Conversor conversor = buscarAPI(moeda1);
        Double valorConvertido = conversor.converter(moeda2, valor);

        System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        System.out.printf("Valor %.2f [%s] \nCorresponde ao valor %.2f [%s]\n",valor, moeda1 ,valorConvertido, moeda2);
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

        System.out.println("Deseja fazer outra conversão? [s/n]");
        String opcao = sc.next();
        if (opcao.equalsIgnoreCase("s")) {
            opcao();
        }
    }

    private Conversor buscarAPI(String moedaBase) {
        String endereco = this.endereco + moedaBase;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endereco))
                    .build();

            HttpResponse<String> response = null;

            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();

            ValoresConversao vc = gson.fromJson(json, ValoresConversao.class);
            return new Conversor(vc.conversion_rates());

        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
