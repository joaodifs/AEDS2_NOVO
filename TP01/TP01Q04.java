import java.util.Random;
import java.util.Scanner;
public class TP01Q04 {

    public static boolean isFim(String linha){
        boolean result=false;
        if(linha.length()==3 && linha.charAt(0)=='F' && linha.charAt(1)=='I' && linha.charAt(2)=='M'){
            result=true;
        }
        return result;
    }

    public static void palavraCodificada(String s, char inicial, char finals){
        s = s.replace(inicial, finals);
        MyIO.println(""+s);
    }

    public static void main(String[] args) {
        String linha = "";
        char inicial = ' ';
        char finals = ' ';
       // linha=scanner.nextLine();
       Random gerador = new Random();
        gerador.setSeed(4);
       linha=MyIO.readLine();
        while(!isFim(linha)){
            inicial = (char)('a' + (Math.abs(gerador.nextInt()) % 26));
            finals = (char)('a' + (Math.abs(gerador.nextInt()) % 26));
            palavraCodificada(linha, inicial, finals);
        linha=MyIO.readLine();
        }
       // scanner.close();
    }
}
