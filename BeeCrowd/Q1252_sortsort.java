import java.util.Scanner;
public class Q1252_sortsort {
    public static void main(String[] args) {
        int n,m;
        int[] array;
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        m = scanner.nextInt();
        while(n!= 0 && m!=0){
            array = new int[n];
            for(int i=0; i<n; i++){
                array[i] = scanner.nextInt();
            }
            for (int i = 0; i < n - 1; i++) { // Passa várias vezes no array
                for (int j = 0; j < n - 1 - i; j++) { // Compara os elementos adjacentes
                    if (array[j]%m > array[j + 1]%m) {
                        int temp = array[j];
                        array[j] = array[j + 1];
                        array[j + 1] = temp;
                    }else{
                        if (array[j]%m == array[j + 1]%m) {
                            if(array[j]%2==0 && array[j+1]%2==0 && array[j] > array[j+1]){
                                int temp = array[j];
                                array[j] = array[j + 1];
                                array[j + 1] = temp;
                            }else{
                            if(array[j]%2==1 && array[j+1]%2==1 && array[j] < array[j+1]){
                                int temp = array[j];
                                array[j] = array[j + 1];
                                array[j + 1] = temp;
                            }else{
                                if((array[j]%2==0 && array[j+1]%2==1) && array[j] < array[j+1]){
                                    int temp = array[j];
                                    array[j] = array[j + 1];
                                    array[j + 1] = temp;
                            }
                        }
                    }
                }
            }
        }
    }

            for(int i = 0; i<n;i++){
                System.out.printf(array[i]+" ");
            }
            System.out.println("");

            n = scanner.nextInt();
            m = scanner.nextInt();
        }
        scanner.close();   
    }
}
