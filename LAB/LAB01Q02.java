import java.util.Scanner;
public class LAB01Q02{

    public static boolean isFim(String linha){
        boolean result=false;
        if(linha.length()==3 && linha.charAt(0)=='F' && linha.charAt(1)=='I' && linha.charAt(2)=='M'){
            result=true;
        }
        return result;
    }
    
    public static void maiusculas(String linha){
        System.out.println(maiusculasR(linha,0));
    }

    public static int maiusculasR(String linha, int pos) {
        int resultado;
        if (pos == linha.length()) {
            resultado = 0;
        } else {
            int cont = (linha.charAt(pos) >= 'A' && linha.charAt(pos) <= 'Z') ? 1 : 0;
            resultado = cont + maiusculasR(linha, pos + 1);
        }
        return resultado;
    }

    public static void main(String[] args) {
        String linha = "";
        Scanner scanner = new Scanner(System.in);
        linha=scanner.nextLine();
        while(!isFim(linha)){
            maiusculas(linha);
        linha=scanner.nextLine();
        }
        
    }
}