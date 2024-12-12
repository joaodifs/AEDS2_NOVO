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

void selecao(Pokemon** array, int n) {
    for (int i = 0; i < (n - 1); i++) {
        int menor = i;
        for (int j = (i + 1); j < n; j++) {
            if (array[j] != NULL && array[menor] != NULL) {
                if (strcmp(array[menor]->name, array[j]->name) > 0) {
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

        strcpy(p->types[0], atributosPre[4]);
        if (strlen(atributosPre[5]) > 0) {
            strcpy(p->types[1], atributosPre[5]);
        } else {
            p->types[1][0] = '\0'; // Tipo secundário vazio
        }

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

    // Leitura dos IDs
    char id[10];
    Pokemon* selectedPokemon[100];
    int selectedCount = 0;

    while (1) {
        if (fgets(id, sizeof(id), stdin) == NULL) break;
        id[strcspn(id, "\n")] = 0;
        if (isFim(id)) break;

        Pokemon* p = searchById(atoi(id), pokemon, totalPokemons);
        if (p != NULL) {
            selectedPokemon[selectedCount++] = p;
        }
    }

    // Ordenação dos Pokémon selecionados
    selecao(selectedPokemon, selectedCount);

    // Impressão dos Pokémon ordenados
    for (int i = 0; i < selectedCount; i++) {
        if (selectedPokemon[i] != NULL) {
            char habilidadesStr[200] = "";
            for (int j = 0; j < MAX_ABILITIES && selectedPokemon[i]->abilities[j][0] != '\0'; j++) {
                strcat(habilidadesStr, selectedPokemon[i]->abilities[j]);
                if (j < MAX_ABILITIES - 1 && selectedPokemon[i]->abilities[j + 1][0] != '\0') {
                    strcat(habilidadesStr, ", ");
                }
            }

            printf("[#%d -> %s: %s - [%s, %s] - [%s] - %.1fkg - %.1fm - %d%% - %s - %d gen] - %s\n",
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
