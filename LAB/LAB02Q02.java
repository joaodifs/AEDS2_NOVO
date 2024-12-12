import java.util.Scanner;
public class LAB02Q02 {
    public static void main(String[] args) {
        int primeiro = 0;
        int ultimo = 0;
        Scanner scanner = new Scanner(System.in);
        primeiro = scanner.nextInt();
        ultimo = scanner.nextInt();
        String sequencia = "";
        for(int i = primeiro; i<=ultimo; i++){
            sequencia += i;
        }
        for(int i = ultimo; i>=primeiro; i--){
            sequencia += i;
        }
        System.out.printf("Sequencia: "+sequencia);


    }
}
