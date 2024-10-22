public class Bolha {
    public void bolha(int array[]) {
        int n = array.length;
		for (int i = 0; i < n-1; i++) {
			for (int j = 0; j < n; j++) {
				if (array[j] > array[j + 1]) {
               int tmp = array[j];
               array[j]=array[j+1];
               array[j+1] = tmp;
				}
			}
		}
   }


   void insercao(int array[]){
    int n = array.length;
    for(int i = 1; i<n; i++){
        int tmp = array[i];
        int j = i-1;
        while(j>=0){
            array[j+1]=array[j];
        }
        array[j+1] = tmp;
    }
   }

   void selecao(int array[]){
    int n = array.length;
    for(int i = 0; i<n-1;i++){
        int menor = i;
        for(int j = i+1;j<n;j++){
            if(array[menor]>array[j]){
                menor= j;
            }
        }
    }
   }
   }
