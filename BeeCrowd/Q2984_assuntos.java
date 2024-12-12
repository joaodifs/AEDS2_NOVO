import java.util.Scanner;
public class Q2984_assuntos {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int quant = 2;
        String expressao;
        while(quant>0){
            expressao = scanner.nextLine();
            int parenteses = 0;
                for(int i = 0; i< expressao.length();i++){
                    char c = expressao.charAt(i);
                    if(c=='('){
                        parenteses++;
                    }else if(c==')'){
                        if(parenteses>0){
                            parenteses--;
                        }
                    }
                }
            quant--;
            if(parenteses>0){
                System.out.printf("Ainda temos %d assunto(s) pendente(s)!\n",parenteses);
            }else{
                System.out.printf("Partiu RU!\n");
            }
        }
        scanner.close();
    }
}
