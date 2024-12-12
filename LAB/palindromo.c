#include <stdio.h>
#include <string.h>
#include <stdbool.h>
    bool isFim(char palavra[]){
        return(strlen(palavra) < 4 && palavra[0]=='F' && palavra[1]=='I' && palavra[2]=='M');
    }

    void isPalindromo(char palavra[]){
        int direita = strlen(palavra)-1;
        bool resultado = 1;
        for(int esquerda = 0; esquerda<direita && resultado==1; esquerda++,direita--){
            if(palavra[esquerda]!=palavra[direita]){
                resultado=0;
            }
        }
        if(resultado){
        printf("SIM\n");
        }else{
        printf("NAO\n");
        }
    }
int main(){

    char palavra[100];
    scanf("%s",palavra);
    while(!isFim(palavra)){
    isPalindromo(palavra);
    scanf("%s",palavra);
    }
    return 0;
}