#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#define MAX_POKEMON 801
#define MAX_LINE_LENGTH 1000
#define MAX_TYPES 2
#define MAX_ABILITIES 4

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

Pokemon* searchById(int id, Pokemon* pokemon) {
    for (int i = 0; i < MAX_POKEMON; i++) {
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

// Função de ordenação por seleção baseada em weight e name
void selecao(Pokemon** array, int n) {
    for (int i = 0; i < n - 1; i++) {
        int menor = i;
        for (int j = i + 1; j < n; j++) {
            if (array[j] != NULL && array[menor] != NULL) {
                // Primeiro, compara pelo peso
                if (array[j]->weight < array[menor]->weight) {
                    menor = j;
                }
                // Se os pesos são iguais, compara pelo nome
                else if (array[j]->weight == array[menor]->weight && 
                         strcmp(array[j]->name, array[menor]->name) < 0) {
                    menor = j;
                }
            }
        }
        if (array[menor] != NULL && array[i] != NULL) {
            Pokemon* temp = array[menor];
            array[menor] = array[i];
            array[i] = temp;
        }
    }
}

int main() {
    FILE* file = fopen("/tmp/pokemon.csv", "r");
    if (file == NULL) {
        printf("Error opening file\n");
        return 1;
    }

    char line[MAX_LINE_LENGTH];
    fgets(line, sizeof(line), file);

    Pokemon* pokemon = (Pokemon*)malloc(MAX_POKEMON * sizeof(Pokemon));

    for (int i = 0; i < MAX_POKEMON; i++) {
        if (fgets(line, sizeof(line), file) == NULL) break;

        char* atributosTmp[3];
        int count;
        split(line, "[]", atributosTmp, &count);

        char* atributosPre[6];
        split(atributosTmp[0], ",", atributosPre, &count);

        char* atributosPos[6];
        split(atributosTmp[2], ",", atributosPos, &count);

        pokemon[i].id = atoi(atributosPre[0]);
        pokemon[i].generation = atoi(atributosPre[1]);
        strcpy(pokemon[i].name, atributosPre[2]);
        strcpy(pokemon[i].description, atributosPre[3]);

        strcpy(pokemon[i].types[0], atributosPre[4]);
        if (strlen(atributosPre[5]) > 0) {
            strcpy(pokemon[i].types[1], atributosPre[5]);
        } else {
            strcpy(pokemon[i].types[1], "N/A"); // Use "N/A" se não houver segundo tipo
        }

        char* abilities[MAX_ABILITIES];
        split(atributosTmp[1], ",", abilities, &count);
        for (int j = 0; j < count && j < MAX_ABILITIES; j++) {
            strcpy(pokemon[i].abilities[j], abilities[j]);
        }

        pokemon[i].weight = atof(atributosPos[1]);
        pokemon[i].height = atof(atributosPos[2]);
        pokemon[i].captureRate = atoi(atributosPos[3]);
        pokemon[i].isLegendary = (strcmp(atributosPos[4], "1") == 0);
        strcpy(pokemon[i].captureDate, atributosPos[5]);
    }

    fclose(file);

    char id[10];
    Pokemon* selectedPokemon[100];
    int selectedCount = 0;

    while (1) {
        if (fgets(id, sizeof(id), stdin) == NULL) break;
        id[strcspn(id, "\n")] = 0; // Remove o caractere de nova linha
        if (isFim(id)) break;

        Pokemon* pokemon1 = searchById(atoi(id), pokemon);
        if (pokemon1 != NULL) {
            selectedPokemon[selectedCount++] = pokemon1;
        }
    }

    // Ordena os Pokémon selecionados pelo peso e nome
    selecao(selectedPokemon, selectedCount);
    
    for (int i = 0; i < selectedCount; i++) {
        if (selectedPokemon[i] != NULL) {
            char habilidadesStr[200] = "";
            for (int j = 0; j < MAX_ABILITIES && selectedPokemon[i]->abilities[j][0] != '\0'; j++) {
                strcat(habilidadesStr, selectedPokemon[i]->abilities[j]);
                if (j < MAX_ABILITIES - 1 && selectedPokemon[i]->abilities[j + 1][0] != '\0') {
                    strcat(habilidadesStr, ",");
                }
            }
            
            // Imprime informações do Pokémon
            printf("[#%d -> %s: %s - [%s, %s] - [%s] - %.1fkg - %.1fm - %d%% - %s - %d gen] - %s",
                   selectedPokemon[i]->id,
                   selectedPokemon[i]->name,
                   selectedPokemon[i]->description,
                   selectedPokemon[i]->types[0], 
                   selectedPokemon[i]->types[1][0] != '\0' ? selectedPokemon[i]->types[1] : "N/A",
                   habilidadesStr,
                   selectedPokemon[i]->weight,
                   selectedPokemon[i]->height,
                   selectedPokemon[i]->captureRate,
                   selectedPokemon[i]->isLegendary ? "true" : "false",
                   selectedPokemon[i]->generation,
                   selectedPokemon[i]->captureDate);
        }
    }

    free(pokemon);
    return 0;
}
