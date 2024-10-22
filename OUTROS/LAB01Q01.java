import java.util.Scanner;
public class LAB01Q01{

    public static boolean isFim(String linha){
        boolean result=false;
        if(linha.length()==3 && linha.charAt(0)=='F' && linha.charAt(1)=='I' && linha.charAt(2)=='M'){
            result=true;
        }
        return result;
    }
    public static void maiusculas(String linha){
        int cont = 0;
        for(int i=0;i<linha.length();i++){
            if(linha.charAt(i)>='A' && linha.charAt(i)<='Z'){
                cont++;
            }
        }
        System.out.printf("%d\n",cont);
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