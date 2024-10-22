import java.util.Scanner;
public class TP01Q01 {

    public static boolean isFim(String linha){
        boolean result=false;
        if(linha.length()==3 && linha.charAt(0)=='F' && linha.charAt(1)=='I' && linha.charAt(2)=='M'){
            result=true;
        }
        return result;
    }

    public static boolean isPalindromo(String s){
        boolean resp = true;
        for(int i=0, j=s.length()-1; i<j; i++,j--){
            if(s.charAt(i)!=s.charAt(j)){
                resp=false;
                j=0;
            }
        }
        return resp;
    }
    public static void main(String[] args) {
        String linha = "";
        Scanner scanner = new Scanner(System.in);
        linha=scanner.nextLine();
        while(!isFim(linha)){
            if(isPalindromo(linha)){
                System.out.println("SIM");            
            }else{
                System.out.println("NAO");  
            }
        linha=scanner.nextLine();
        }
        scanner.close();
    }
}





