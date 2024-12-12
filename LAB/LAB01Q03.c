#include <stdio.h>
#include <stdbool.h>
#include <string.h>
void contarMaiusculas(char palavra[]){
    int tamanho = strlen(palavra);
    int cont = 0;
    for(int i=0;i<tamanho;i++){
        if(palavra[i]>='A' && palavra[i]<='Z'){
            cont++;
        }
    }
    printf("%i\n",cont);
}
bool isFim(char palavra[]){
    return(strlen(palavra)==3 && palavra[0]=='F' && palavra[1]=='I' && palavra[2]=='M');
}
int main(){
    char palavra[100];
    scanf("%[^\n]%*c",palavra);
    while(!isFim(palavra)){
        contarMaiusculas(palavra);
        scanf("%[^\n]%*c",palavra);
    }
    return 0;
}