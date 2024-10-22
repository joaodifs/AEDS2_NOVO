package java.Q1;
import java.util.Scanner;
class Haypoint {
    int valor;
    String palavra;
    Haypoint(String[] atributos){
        this.palavra = atributos[0];
        this.valor = Integer.parseInt(atributos[1]);
    }
}

public class Q1{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int palavras = scanner.nextInt();
        int frases = scanner.nextInt();
        String line = scanner.nextLine();
        Haypoint[] haypoint = new Haypoint[palavras];
        for(int i =0; i<palavras;i++){
            line = scanner.nextLine();
            String atributos[] = line.split(" ");
            haypoint[i] = new Haypoint(atributos);
        }
        while(frases>0){
            int soma = 0;
            line = scanner.nextLine();
        while(!line.equals(".")){
            for(int i = 0; i < palavras; i++){
            if(line.contains(haypoint[i].palavra)){
                soma = soma + haypoint[i].valor;
            }
            }
            line = scanner.nextLine();
        }
        System.out.println(""+soma);
        frases--;
    }
    scanner.close();
    }
}
