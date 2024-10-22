public class Lista {
    int array[];
    int n;
    public Lista(){
        this(6);
    }
    public Lista(int tamanho){
        array = new int[tamanho];
        n=0;
    }

    public void inserirInicio(int x)throws Exception{
        if(n>=array.length){
            throw new Exception("Erro ao insirir no inicio");
        }
        for(int i=n;i>0;i--){
            array[i] = array[i-1];
        }
        array[0] = x;
        n++;
    }

    public void inserirFim(int x)throws Exception{
        if(n>=array.length){
            throw new Exception("Erro ao insirir no fim");
        }
        array[n]=x;
        n++;

    }

    public void inserir(int x, int pos)throws Exception{
        if(n>=array.length){
            throw new Exception("Erro ao inserir");
        }
        for(int i=n;i>pos;i--){
            array[i]=array[i-1];
        }
        n++;
    }

    public int removerInicio()throws Exception{
        int resp = 0;
        if(n==0){
            throw new Exception("Erro ao remover do incio");
        }
        resp = array[0];
        for(int i=0;i<n;i++){
            array[i]=array[i+1];
        }
        n--;
        return resp;
    }

    public int removerFim()throws Exception{
        if(n==0){
            throw new Exception("Erro ao remover do fim");
        }
        return array[--n];
    }

    public int remover(int pos)throws Exception{
        int resp;
        if(n==0){
            throw new Exception("Erro ao remover");
        }
        resp = array[pos];
        for(int i =pos;i<n;i++){
            array[i]= array[i+1];
        }
        return resp;
    }

    public void mostrar(){
        for(int i = 0; i<n; i++){
        System.out.printf(array[i]+" ");
        }
    }

}
