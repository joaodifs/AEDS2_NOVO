#include <stdbool.h>
#include <stdio.h>
#include <string.h>
    void combinarPalavras(char palavra1[], char palavra2[]){
        int i = 0;
        int j = 0;
        int k = 0;
        char resultado[100];
        while(j<strlen(palavra1) && k<strlen(palavra2)){
            resultado[i++] = palavra1[j++];
            resultado[i++] = palavra2[k++];
        }
        while(j<strlen(palavra1)){
            resultado[i++] = palavra1[j++];
        }
        while(k<strlen(palavra2)){
            resultado[i++] = palavra2[k++];
        }
        resultado[i] = '\0';
        printf("%s",resultado);
    }
int main(){
    char palavra1[100];
    char palavra2[100];
    scanf("%s",palavra1);
    scanf("%s",palavra2);
    combinarPalavras(palavra1, palavra2);
}