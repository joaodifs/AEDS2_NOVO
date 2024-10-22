public class Insercao {
    void insercao(int array[]){
        int n = array.length;
        for(int i = 1; i<n;i++){
            int tmp = array[i];
            int j=i-1;
            while(j>=0 && array[j]>tmp){
                array[j+1] = array[j];
            }
            array[j+1]=tmp;
        }
    }
    public static void selecao(int array[]){
        int n = array.length;
        for(int i = 0; i<n-1; i++){
            int menor = i;
            for(int j = i+1; j<n;j++){
                if(array[menor]>array[j]){
                    menor = j;
                }
            }
        }
    }

    void insercao3(int array[]){
        int n = array.length;
        for(int i = 1; i < n; i++){
            int tmp = array[i];
            int j = i-1;
            while(j>=0 && array[j]>tmp){
                array[j+1]=array[j];
            }
            array[j+1]=tmp;
        }
    }
    public static void main(String[] args) {
        
    }
}
