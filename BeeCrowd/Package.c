#include <stdio.h>
#include <string.h>
int main(){
    int numero =1;
    while(numero==1){
        char str[10][10];
        int valor[10];
        int tamanho =0;
        while (strcmp(str[tamanho], "0") != 0) {  // Aqui a comparação é feita dentro do while
            scanf("%s", str[tamanho]);  // Lê o nome do pacote
            if (strcmp(str[tamanho], "0") == 0) {
                break;  // Saímos do loop, se necessário
            }
            scanf("%d", &valor[tamanho]);  // Lê o valor
            tamanho++;
        }
        for(int i =0;i<tamanho-1;i++){
            for(int j =0; j < tamanho - 1 -i; j++){
                if(valor[j]>valor[j+1]){
                    int temp = valor[j];
                    valor[j] = valor[j+1];
                    valor[j+1] = temp;
                }
            }
        }
        for(int i = 0; i<tamanho-1; i++){
        printf("Package 00%d\n",valor[i]);
        }
                scanf("%d",&numero);
    }
    return 0;
}