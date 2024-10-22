import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.InputMismatchException;
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
        System.out.println("""
                =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
                Bem-vindo(a) ao Conversor de Moedas!
                =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=""");
    }

    private void mensagemOpcoes() {
        System.out.print("""
                1) Dólar -> Peso Argentino
                2) Peso Argentino -> Dólar
                3) Dólar -> Real Brasileiro
                4) Real Brasileiro -> Dólar
                5) Dólar -> Peso Colombiano
                6) Peso Colombiano -> Dólar
                7) Sair
                Escolha uma opcão válida:\s""");
    }

    public void opcao() {
        mensagemOpcoes();
        int opcao = 0;
        double valor = 0.0;
        boolean entradaValida = false;

        while (!entradaValida) {
            try {
                opcao = sc.nextInt();
                sc.nextLine();

                while (opcao < 1 || opcao > 7) {
                    System.out.print("Opcão Inválida, Tente Novamente: ");
                    opcao = sc.nextInt();
                }
                if (opcao == 7) {
                    System.exit(0);
                }

                System.out.print("Valor para converter: ");
                valor = sc.nextDouble();

                entradaValida = true;
            } catch (InputMismatchException e) {
                System.out.print("VALOR INVÁLIDO, tente novamente: ");
                sc.next();
            }
        }

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

        String opcao;
        do {
            System.out.println("Deseja fazer outra conversão? [s/n]");
            opcao = sc.next();
        } while(!opcao.equalsIgnoreCase("s") && !opcao.equalsIgnoreCase("n"));

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

            HttpResponse<String> response;

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
