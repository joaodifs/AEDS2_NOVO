public class Selecao {

    public static void selecao(int array[]){
      int n = array.length;
        for (int i = 0; i < (n - 1); i++) {
            int menor = i;
             for (int j = (i + 1); j < n; j++){
             if (array[menor] > array[j]){
             menor = j;
            }
            }
            int temp = array[menor];
            array[menor] = array[i];
            array[i] = temp;
            }
            
    }
    public static void selecao2(int array[]){
      int n = array.length;
      for(int i=0; i<n-1;i++){
         int menor = array[i];
         for(int j=i+1;j<n;j++){
            if(menor>array[j]){
               menor = j;
            }
            int temp = array[i];
            array[i] = array[menor];
            array[menor]=temp;
         }
      }
    }

   public static void main(String[] args) {
       
   }
}