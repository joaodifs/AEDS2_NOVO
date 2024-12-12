import java.util.Scanner;
class Fracao{
    int n;
    int d;

    public Fracao(int n, int d){
this.n = n;
this.d = d;
    }

    public Fracao(){
    }
}
public class TDARacional {

    Fracao soma(){
        return new Fracao()
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int quant = scanner.nextInt();
        scanner.nextLine();
        while(quant>0){
            String expressao = scanner.nextLine();
            String partes[] = expressao.split(" ");
            Fracao F1 = new Fracao(Integer.parseInt(partes[0]),Integer.parseInt(partes[2]));
            Fracao F2 = new Fracao(Integer.parseInt(partes[4]),Integer.parseInt(partes[6]));
            Fracao resultado = new Fracao();
            switch (partes[3]) {
                case "+":
                    resultado = 
                    break;
                case "-":
                    
                    break;
                case "*":
                    
                    break;
                case "/":
                    
                    break;
            }
            quant--;
        }
    }
}
