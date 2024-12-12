import java.util.Scanner;
public class Sinuca {
    public static void main(String[] args) throws Exception{
        Scanner scanner = new Scanner(System.in);
        int quant = scanner.nextInt();
        scanner.nextLine();
        String bolasBaixo[] = scanner.nextLine().split(" ");
        String bolasCima[]= new String[quant-1];
        while(quant>1){
            int tmp = 0;
            for(int i = 0;i< bolasCima.length-tmp; i++){
                if(Integer.parseInt(bolasBaixo[i])==1 && Integer.parseInt(bolasBaixo[i+1])==1 
                    || Integer.parseInt(bolasBaixo[i])==-1 && Integer.parseInt(bolasBaixo[i+1])==-1){
                        bolasCima[i]="1";
                    }else{
                        bolasCima[i]="-1";
                    }
            }
            System.arraycopy(bolasCima, 0, bolasBaixo, 0, bolasCima.length);
            quant--;
            tmp++;
        }
        if(bolasCima[0].equals("1")){
            System.out.printf("Preta");
        }else if(bolasCima[0].equals("-1")){
            System.out.printf("Branca");
        }
    }
}
