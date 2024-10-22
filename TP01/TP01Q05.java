public class TP01Q05 {
    public static void main(String[] args) {
        int letras = MyIO.readInt();
        int A = 0, B = 0, C = 0;
        int aberturaPar = 0, fechamentoPar = 0; // parênteses
        String expressao = new String();
        String exResult = new String(); //

        while (letras != 0) {
            // caso de duas ou três variáveis
            if (letras == 2) {
                A = MyIO.readInt();
                B = MyIO.readInt();
                
                // limpar os espaços da expressão
                expressao = MyIO.readLine().replaceAll(" ", "");

                // colocar valores nas variáveis da string
                if (A == 0) {
                    expressao = expressao.replaceAll("A", "0");
                } else {
                    expressao = expressao.replaceAll("A", "1");
                }
                if (B == 0) {
                    expressao = expressao.replaceAll("B", "0");
                } else {
                    expressao = expressao.replaceAll("B", "1");
                }
            } else if (letras == 3) {
                A = MyIO.readInt();
                B = MyIO.readInt();
                C = MyIO.readInt();

                // limpar os espaços da expressão
                expressao = MyIO.readLine().replaceAll(" ", "");

                // colocar valores nas variáveis da string
                if (A == 0) {
                    expressao = expressao.replaceAll("A", "0");
                } else {
                    expressao = expressao.replaceAll("A", "1");
                }
                if (B == 0) {
                    expressao = expressao.replaceAll("B", "0");
                } else {
                    expressao = expressao.replaceAll("B", "1");
                }
                if (C == 0) {
                    expressao = expressao.replaceAll("C", "0");
                } else {
                    expressao = expressao.replaceAll("C", "1");
                }
            }

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            while (expressao.length() > 1) {
                for (int i = 0; i < expressao.length(); i++) {
                    if (expressao.charAt(i) == '(') {
                        aberturaPar = i;
                    }
                    if (expressao.charAt(i) == ')') {
                        fechamentoPar = i;
                        i = expressao.length();
                    }
                }

                // chamar a função dependendo do caso
                if (expressao.charAt(aberturaPar - 1) == 'd') {

                    // CALCULA AND

                    exResult = new String();

                    // trocar os valores e limpar a expressão
                    for (int i = 0; i < expressao.length(); i++) {
                        if (i == (aberturaPar - 3)) {
                            // redundância na localização do termo para abranger dois ou três variáveis
                            if (expressao.charAt(aberturaPar + 1) == '1'
                                    && expressao.charAt(aberturaPar + 3) == '1'
                                    && expressao.charAt(fechamentoPar - 1) == '1') {
                                exResult += '1';
                            } else {
                                exResult += '0';
                            }
                        } else if (i > (aberturaPar - 3) && i <= fechamentoPar) {
                            exResult += "";
                        } else {
                            exResult += expressao.charAt(i);
                        }
                    }
                } else if (expressao.charAt(aberturaPar - 1) == 'r') {

                    // CALCULA OR

                    exResult = new String();

                    // trocar os valores e limpar a expressão
                    for (int i = 0; i < expressao.length(); i++) {
                        if (i == (aberturaPar - 2)) {
                            // redundância na localização do termo para abranger dois ou três variáveis
                            if (expressao.charAt(aberturaPar + 1) == '1'
                                    || expressao.charAt(aberturaPar + 3) == '1'
                                    || expressao.charAt(fechamentoPar - 1) == '1') {
                                exResult += '1';
                            } else {
                                exResult += '0';
                            }
                        } else if (i > (aberturaPar - 2) && i <= fechamentoPar) {
                            exResult += "";
                        } else {
                            exResult += expressao.charAt(i);
                        }
                    }

                } else if (expressao.charAt(aberturaPar - 1) == 't') {

                    // CALCULA NOT

                    exResult = new String();

                    // trocar os valores e limpar a expressão
                    for (int i = 0; i < expressao.length(); i++) {
                        if (i == (aberturaPar - 3)) {
                            if (expressao.charAt(aberturaPar + 1) == '0') {
                                exResult += '1';
                            } else {
                                exResult += '0';
                            }
                        } else if (i > (aberturaPar - 3) && i <= fechamentoPar) {
                            exResult += "";
                        } else {
                            exResult += expressao.charAt(i);
                        }
                    }
                }

                expressao = exResult;
            }

            // exibe o resultado
            MyIO.println(expressao);

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            letras = MyIO.readInt();
        }
    }
}
