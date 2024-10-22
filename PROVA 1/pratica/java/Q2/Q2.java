import java.util.Scanner;

class Presenca {
    String nome;
    String assinatura;

    Presenca(String[] atributos) {
        this.nome = atributos[0];
        this.assinatura = atributos[1];
    }
}

public class Q2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            int n = scanner.nextInt();
            if (n == 0) break; // Termina a execução se n for 0

            scanner.nextLine(); // Limpa o buffer de nova linha

            // Cria arrays para armazenar as assinaturas originais e assinaturas de aula
            Presenca[] assinaturas = new Presenca[n];
            for (int i = 0; i < n; i++) {
                String line = scanner.nextLine();
                String[] atributos = line.split(" ");
                assinaturas[i] = new Presenca(atributos);
            }

            int m = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer de nova linha

            int quantAssFalsas = 0;

            for (int i = 0; i < m; i++) {
                String line = scanner.nextLine();
                String[] atributos = line.split(" ");
                Presenca assinatura2 = new Presenca(atributos);

                // Verifica a assinatura
                boolean assinaturasValidas = false;
                for (int j = 0; j < n; j++) {
                    if (assinatura2.nome.equals(assinaturas[j].nome)) {
                        int diferencas = contarDiferencas(assinaturas[j].assinatura, assinatura2.assinatura);
                        if (diferencas > 1) {
                            quantAssFalsas++;
                        }
                        assinaturasValidas = true;
                        break; // Encontrou a assinatura original correspondente
                    }
                }

                // Se não encontrou o nome correspondente, também considera falso
                if (!assinaturasValidas) {
                    quantAssFalsas++;
                }
            }

            System.out.println(quantAssFalsas);
        }

        scanner.close();
    }

    // Função para contar as diferenças entre duas strings
   static int contarDiferencas(String original, String comparacao) {
        int diferencas = 0;
        for (int i = 0; i < original.length(); i++) {
            if (original.charAt(i) != comparacao.charAt(i)) {
                diferencas++;
            }
        }
        return diferencas;
    }
}