import java.util.Scanner;
class Dicionario{
    String palavra;
    int valor;
    Dicionario(String atributos[]){
        this.palavra = atributos[0];
        this.valor = Integer.parseInt(atributos[1]);
    }
}
public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int palavras = scanner.nextInt();
        int pontos = scanner.nextInt();
        scanner.nextLine();
        String line;
        Dicionario dicionario[] = new Dicionario[palavras];
        for(int i = 0; i<palavras;i++){
            line=scanner.nextLine();
            String atributos[] = line.split(" ");
            dicionario[i] = new Dicionario(atributos);
        }
        while(pontos>0){
            int soma = 0;
            line = scanner.nextLine();
            while(line!="."){
        for(int i=0;i<palavras;i++){
            if(line.contains(dicionario[i].palavra)){
                soma = soma+dicionario[i].valor;
            }
        }
        line = scanner.nextLine();
    }
    pontos--;
    System.out.println(""+soma);
    }
    scanner.close();
}
}
