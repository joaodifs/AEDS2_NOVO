public class PesquisaBinaria {

    static public boolean pesquisa(int x, int array[]){
    boolean resp = false;
    int dir = (array.length - 1), esq = 0, meio;

    while (esq <= dir){
       meio = (esq + dir) / 2;
       if(x == array[meio]){
          resp = true;
          esq = dir + 1;
       } else if (x > array[meio]) {
          esq = meio + 1;
       } else {
          dir = meio - 1;
       }
    }
    return resp;
}


static public boolean pesquisa2(int x, int array[]){
   int dir = array.length-1,esq=0,meio;
   boolean resp = false;
   while(esq<=dir){
      meio = (esq+dir)/2;
      if(x==array[meio]){
         esq=dir+1;
         resp=true;
      }else if(x>array[meio]){
         esq = meio+1;
      } else {
         dir = meio -1;
      }

   }
   return resp;
}
    public static void main(String[] args) {
        
    }
}
