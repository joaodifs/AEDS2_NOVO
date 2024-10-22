import java.util.Scanner;
public class TP01Q03 {

    public static boolean isFim(String linha){
        boolean result=false;
        if(linha.length()==3 && linha.charAt(0)=='F' && linha.charAt(1)=='I' && linha.charAt(2)=='M'){
            result=true;
        }
        return result;
    }

    public static void palavraCodificada(String s, int chave){
        char novaLetra;
        int tam = s.length();
        String palavraCod = "";
        for(int i = 0; i<tam; i++){
            novaLetra = (char)(s.charAt(i)+chave);
            palavraCod = palavraCod + novaLetra;
        }
        MyIO.println(""+palavraCod);
    }

    public static void main(String[] args) {
        String linha = "";
       // linha=scanner.nextLine();
       linha=MyIO.readLine();
        while(!isFim(linha)){
            palavraCodificada(linha, 3);
        linha=MyIO.readLine();
        }
       // scanner.close();
    }
}
