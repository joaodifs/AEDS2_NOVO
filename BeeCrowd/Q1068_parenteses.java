// Balanço de Parênteses I - questão 1068

import java.util.Scanner;

public class Q1068_parenteses {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expressao = scanner.nextLine();
        while (!expressao.equals("FIM")) {
            int parenteses = 0;
            boolean correto = true;
            for (int i = 0; i < expressao.length(); i++) {
                char c = expressao.charAt(i);
                if (c == '(') {
                    parenteses++;
                } else if (c == ')') {
                    if (parenteses > 0) {
                        parenteses--;
                    } else {
                        correto = false;
                        break;
                    }
                }
            }

            if (correto && parenteses == 0) {
                System.out.println("correct");
            } else {
                System.out.println("incorrect");
            }
            expressao = scanner.nextLine();
        }

        scanner.close();
    }
}
