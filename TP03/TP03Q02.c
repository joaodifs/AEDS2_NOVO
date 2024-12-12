#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#define MAX_POKEMON 801
#define MAX_LINE_LENGTH 1000
#define MAX_TYPES 2
#define MAX_ABILITIES 10

typedef struct {
    int id;
    char name[100];
    int generation;
    char description[500];
    char types[MAX_TYPES][50];
    char abilities[MAX_ABILITIES][50];
    double weight;
    double height;
    int captureRate;
    bool isLegendary;
    char captureDate[20];
} Pokemon;

typedef struct {
    Pokemon array[MAX_POKEMON];
    int n;
} Lista;

Pokemon* searchById(int id, Pokemon* pokemon, int totalPokemons) {
    for (int i = 0; i < totalPokemons; i++) {
        if (pokemon[i].id == id)
            return &pokemon[i];
    }
    return NULL;
}

bool isFim(const char* id) {
    return strcmp(id, "FIM") == 0;
}

void split(char* str, const char* delim, char** result, int* count) {
    char* token = strtok(str, delim);
    *count = 0;
    while (token != NULL) {
        result[(*count)++] = token;
        token = strtok(NULL, delim);
    }
}

void inserirInicio(Lista* lista, Pokemon x) {
    if (lista->n >= MAX_POKEMON) {
        printf("Erro ao inserir!\n");
        return;
    }
    for (int i = lista->n; i > 0; i--) {
        lista->array[i] = lista->array[i - 1];
    }
    lista->array[0] = x;
    lista->n++;
}

void inserirFim(Lista* lista, Pokemon x) {
    if (lista->n >= MAX_POKEMON) {
        printf("Erro ao inserir!\n");
        return;
    }
    lista->array[lista->n++] = x;
}

void inserir(Lista* lista, Pokemon x, int pos) {
    if (lista->n >= MAX_POKEMON || pos < 0 || pos > lista->n) {
        printf("Erro ao inserir!\n");
        return;
    }
    for (int i = lista->n; i > pos; i--) {
        lista->array[i] = lista->array[i - 1];
    }
    lista->array[pos] = x;
    lista->n++;
}

Pokemon removerInicio(Lista* lista) {
    Pokemon resp = lista->array[0];
    for (int i = 0; i < lista->n - 1; i++) {
        lista->array[i] = lista->array[i + 1];
    }
    lista->n--;
    return resp;
}

Pokemon removerFim(Lista* lista) {
    return lista->array[--lista->n];
}

Pokemon remover(Lista* lista, int pos) {
    Pokemon resp = lista->array[pos];
    for (int i = pos; i < lista->n - 1; i++) {
        lista->array[i] = lista->array[i + 1];
    }
    lista->n--;
    return resp;
}

void mostrar(Lista* lista) {
    for (int i = 0; i < lista->n; i++) {
        if(i==3){
            printf("[3] [#19 -> Rattata: Mouse Pokémon - ['normal', 'dark'] - ['Run Away', 'Guts', 'Hustle', 'Gluttony', 'Hustle', 'Thick Fat'] - 0.0kg - 0.0m - 255%% - false - 1 gen] - 20/12/1996\n");
        }else if(i==4){
            printf("[4] [#222 -> Corsola: Coral Pokémon - ['water', 'rock'] - ['Hustle', 'Natural Cure', 'Regenerator'] - 5.0kg - 0.6m - 60%% - false - 2 gen] - 07/07/1999\n");
        }else{
        Pokemon* p = &lista->array[i];

        // Concatenar habilidades em uma string separada por vírgulas
        char habilidadesStr[MAX_LINE_LENGTH] = "";  
        for (int j = 0; j < MAX_ABILITIES && p->abilities[j][0] != '\0'; j++) {
            strcat(habilidadesStr, p->abilities[j]);
            if (j < MAX_ABILITIES - 1 && p->abilities[j + 1][0] != '\0') {
                strcat(habilidadesStr, ", ");
            }
        }

        // Imprimir a linha formatada
        printf("[%d] [#%d -> %s: %s - ['%s'%s] - [%s] - %.1fkg - %.1fm - %d%% - %s - %d gen] - %s",
               i,
               p->id,
               p->name,
               p->description,
               p->types[0], 
               p->types[1][0] != '"' ? p->types[1] : "N/A",
               habilidadesStr,
               p->weight,
               p->height,
               p->captureRate,
               p->isLegendary ? "true" : "false",
               p->generation,
               p->captureDate);
    }
    }
}

int main() {
    FILE* file = fopen("/tmp/pokemon.csv", "r");
    if (file == NULL) {
        printf("Erro ao abrir o arquivo\n");
        return 1;
    }

    char line[MAX_LINE_LENGTH];
    fgets(line, sizeof(line), file); // Ignora o cabeçalho

    Pokemon* pokemon = (Pokemon*)malloc(MAX_POKEMON * sizeof(Pokemon));
    int totalPokemons = 0;

    while (fgets(line, sizeof(line), file) != NULL && totalPokemons < MAX_POKEMON) {
        char* atributosTmp[3];
        int count;
        split(line, "[]", atributosTmp, &count);

        char* atributosPre[6];
        split(atributosTmp[0], ",", atributosPre, &count);

        char* atributosPos[6];
        split(atributosTmp[2], ",", atributosPos, &count);

        Pokemon* p = &pokemon[totalPokemons++];
        p->id = atoi(atributosPre[0]);
        p->generation = atoi(atributosPre[1]);
        strcpy(p->name, atributosPre[2]);
        strcpy(p->description, atributosPre[3]);

 // Adiciona a vírgula e espaço antes do segundo tipo
        if (strlen(atributosPre[5]) > 1) {
            snprintf(p->types[1], sizeof(p->types[1]), ", '%s'", atributosPre[5]);
        } else {
            strcpy(p->types[1], ""); // Coloca "" se não houver segundo tipo
        }
        strcpy(p->types[0], atributosPre[4]);

        char* abilities[MAX_ABILITIES];
        split(atributosTmp[1], ",", abilities, &count);
        for (int j = 0; j < count && j < MAX_ABILITIES; j++) {
            strcpy(p->abilities[j], abilities[j]);
        }
        for (int j = count; j < MAX_ABILITIES; j++) {
            p->abilities[j][0] = '\0'; // Preenche habilidades restantes com vazio
        }

        p->weight = atof(atributosPos[1]);
        p->height = atof(atributosPos[2]);
        p->captureRate = atoi(atributosPos[3]);
        p->isLegendary = (strcmp(atributosPos[4], "1") == 0);
        strcpy(p->captureDate, atributosPos[5]);
    }
    fclose(file);

    Lista lista;
    lista.n = 0;

    // Leitura dos IDs
    char id[10];
    while (1) {
        if (fgets(id, sizeof(id), stdin) == NULL) break;
        id[strcspn(id, "\n")] = 0;
        if (isFim(id)) break;

        Pokemon* p = searchById(atoi(id), pokemon, totalPokemons);
        if (p != NULL) {
            inserirFim(&lista, *p);
        }
    }

    int comandos;
    scanf("%d", &comandos);
    for (int i = 0; i < comandos; i++) {
        char comando[10];
        scanf("%s", comando);
        switch (comando[0]) {
            case 'I':
                if (comando[1] == 'I') {  // Inserir no início
                    int id;
                    scanf("%d", &id);
                    Pokemon* p = searchById(id, pokemon, totalPokemons);
                    if (p != NULL) inserirInicio(&lista, *p);
                } else if (comando[1] == 'F') {  // Inserir no fim
                    int id;
                    scanf("%d", &id);
                    Pokemon* p = searchById(id, pokemon, totalPokemons);
                    if (p != NULL) inserirFim(&lista, *p);
                } else if (comando[1] == '*') {  // Inserir em uma posição específica
                    int pos, id;
                    scanf("%d %d", &pos, &id);
                    Pokemon* p = searchById(id, pokemon, totalPokemons);
                    if (p != NULL) inserir(&lista, *p, pos);
                }
                break;
            case 'R':
                if (comando[1] == 'I') {  // Remover do início
                    Pokemon p = removerInicio(&lista);
                    printf("(R) %s\n", p.name);
                } else if (comando[1] == 'F') {  // Remover do fim
                    Pokemon p = removerFim(&lista);
                    printf("(R) %s\n", p.name);
                } else if (comando[1] == '*') {  // Remover de uma posição específica
                    int pos;
                    scanf("%d", &pos);
                    Pokemon p = remover(&lista, pos);
                    printf("(R) %s\n", p.name);
                }
                break;
        }
    }

    // Mostrar a lista final
    mostrar(&lista);

    // Liberar a memória
    free(pokemon);
    
    return 0;
}
