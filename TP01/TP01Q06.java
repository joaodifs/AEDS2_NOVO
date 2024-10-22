import java.util.Random;
import java.util.Scanner;
public class TP01Q06 {

    public static boolean isFim(String linha){
        boolean result=false;
        if(linha.length()==3 && linha.charAt(0)=='F' && linha.charAt(1)=='I' && linha.charAt(2)=='M'){
            result=true;
        }
        return result;
    }

    public static void respostas(String linha){
        System.out.printf("%s %s %s %s\n",X1(linha)?"SIM" : "NAO",X2(linha)?"SIM" : "NAO",X3(linha)?"SIM" : "NAO",X4(linha)?"SIM" : "NAO");
    }

    public static boolean X1(String s) {
        int tamanho = s.length();
        boolean resp = true;
        for (int i = 0; i < tamanho; i++) {
            if (!(s.charAt(i) == 'a' || s.charAt(i) == 'e' || s.charAt(i) == 'i' || s.charAt(i) == 'o'
                    || s.charAt(i) == 'u' ||
                    s.charAt(i) == 'A' || s.charAt(i) == 'E' || s.charAt(i) == 'I' || s.charAt(i) == 'O'
                    || s.charAt(i) == 'U')) {
                i = tamanho;
                resp = false;
            }
        }
        return (resp);
    }

    public static boolean X2(String s) {
        int tamanho = s.length();
        boolean resp = true;
        for (int i = 0; i < tamanho; i++) {
            if (!(s.charAt(i) == 'b' || s.charAt(i) == 'c' || s.charAt(i) == 'd' || s.charAt(i) == 'f'
                    || s.charAt(i) == 'g' ||
                    s.charAt(i) == 'h' || s.charAt(i) == 'j' || s.charAt(i) == 'k' || s.charAt(i) == 'l'
                    || s.charAt(i) == 'm' ||
                    s.charAt(i) == 'n' || s.charAt(i) == 'p' || s.charAt(i) == 'q' || s.charAt(i) == 'r'
                    || s.charAt(i) == 's' ||
                    s.charAt(i) == 't' || s.charAt(i) == 'v' || s.charAt(i) == 'w' || s.charAt(i) == 'x'
                    || s.charAt(i) == 'y' || s.charAt(i) == 'z' ||
                    s.charAt(i) == 'B' || s.charAt(i) == 'C' || s.charAt(i) == 'D' || s.charAt(i) == 'F'
                    || s.charAt(i) == 'G' ||
                    s.charAt(i) == 'H' || s.charAt(i) == 'J' || s.charAt(i) == 'K' || s.charAt(i) == 'L'
                    || s.charAt(i) == 'M' ||
                    s.charAt(i) == 'N' || s.charAt(i) == 'P' || s.charAt(i) == 'Q' || s.charAt(i) == 'R'
                    || s.charAt(i) == 'S' ||
                    s.charAt(i) == 'T' || s.charAt(i) == 'V' || s.charAt(i) == 'W' || s.charAt(i) == 'X'
                    || s.charAt(i) == 'Y' || s.charAt(i) == 'Z')) {
                i = tamanho;
                resp = false;
            }
        }
        return (resp);
    }

    public static boolean X3(String s) {
        int tamanho = s.length();
        boolean resp = true;
        for (int i = 0; i < tamanho; i++) {
            if (!(s.charAt(i) >= '0' && s.charAt(i) <= '9')) {
                i = tamanho;
                resp = false;
            }
        }
        return (resp);
    }

    public static boolean X4(String s) {
        int tamanho = s.length();
        boolean resp = true;
        int quant = 0;
        for (int i = 0; i < tamanho; i++) {
            if (s.charAt(i) == ',' || s.charAt(i) == '.') {
                quant++;
                if (quant > 1) {
                    i = tamanho;
                    resp = false;
                }
            } else {
                if (!(s.charAt(i) >= '0' && s.charAt(i) <= '9')) {
                    i = tamanho;
                    resp = false;
                }
            }
        }
        return (resp);
    }

    public static void main(String[] args) {
        String linha = "";
       // linha=scanner.nextLine();
       Random gerador = new Random();
        gerador.setSeed(4);
       linha=MyIO.readLine();
        while(!isFim(linha)){
            respostas(linha);
        linha=MyIO.readLine();
        }
       // scanner.close();
    }
}
