import java.util.Scanner;
class Noiva{
    String horas;
    String minutos;
    String quemViu;

Noiva(String[] atributos){
    this.horas = atributos[0];
    this.minutos = atributos[1];
    this.quemViu = atributos[2];
}
}
public class Q3149_noiva {
    public static void main(String[] args) throws Exception{
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        int quant = scanner.nextInt();
        Noiva noiva[] = new Noiva[8];
        quant--;
        scanner.nextLine();
        for(int i = 0; i<=quant; i++){
            String line = scanner.nextLine().replaceAll(":", " ");
            String[] atributos = line.split(" ");
            noiva[i] = new Noiva(atributos);
        }
        for(int i = 0; i<quant; i++){
            if(Integer.parseInt(noiva[i].horas)==23 && Integer.parseInt(noiva[i].minutos) < (60-m) || Integer.parseInt(noiva[i].horas)==0 && Integer.parseInt(noiva[i].minutos) > (m)){
                Noiva temp = noiva[quant];
                noiva[quant] = noiva[i];
                noiva[i] = temp;
                quant--;
            }
        }
        for(int i = 0; i<quant-1; i++){
            for(int j = 0; j < quant-1-i; j++){
                if(noiva[j].horas.equals("00") && noiva[j+1].horas.equals("23")){
                    Noiva temp = noiva[j];
                    noiva[j] = noiva[j+1];
                    noiva[j+1] = temp;
                }else if(noiva[j].horas.equals("23") && noiva[j+1].horas.equals("23")){
                    if(Integer.parseInt(noiva[j].minutos)<Integer.parseInt(noiva[j+1].minutos)){
                        Noiva temp = noiva[j];
                        noiva[j] = noiva[j+1];
                        noiva[j+1] = temp;
                    }
                }else if(noiva[j].horas.equals("00") && noiva[j+1].horas.equals("00")){
                    if(Integer.parseInt(noiva[j].minutos)>Integer.parseInt(noiva[j+1].minutos)){
                        Noiva temp = noiva[j];
                        noiva[j] = noiva[j+1];
                        noiva[j+1] = temp;
                    }
                }
            }
        }
        for(int i = 0; i<quant; i++){
            System.out.printf("%s:%s %s %d\n",noiva[i].horas,noiva[i].minutos,noiva[i].quemViu, quant);
            }
    
    }
}