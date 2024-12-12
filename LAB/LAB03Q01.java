import java.util.Stack;
import java.util.Scanner;
public class LAB03Q01 {
    static boolean verificarExp(String expressao){
        Stack<Character> pilha = new Stack<>();
        for(int i =0; i<expressao.length(); i++){
        if(expressao.charAt(i)==')'){
            if(pilha.isEmpty()){
                return false;
            }
            pilha.pop();
        }else{
            if(expressao.charAt(i)=='('){
                pilha.push(expressao.charAt(i));
            }
        }
    }
    return pilha.isEmpty();
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expressao = scanner.nextLine();
        if(verificarExp(expressao)){
            System.out.printf("CORRETA\n");
        }else{
            System.out.printf("INCORRETA\n");
        }
    }
}
