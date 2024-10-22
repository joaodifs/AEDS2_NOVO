#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#define MAX_POKEMON 801
#define MAX_LINE_LENGTH 1000
#define MAX_TYPES 2
#define MAX_ABILITIES 4
#define K 10 // Set k to 10 for partial sorting

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

void swap(Pokemon** a, Pokemon** b) {
    Pokemon* temp = *a;
    *a = *b;
    *b = temp;
}

void reconstruir(Pokemon** array, int n, int i) {
    int maior = i; // Inicializa maior como raiz
    int esquerda = 2 * i + 1; // Filho à esquerda
    int direita = 2 * i + 2; // Filho à direita

    // Se o filho à esquerda for maior que a raiz
    if (esquerda < n && ((array[esquerda] != NULL && array[maior] != NULL &&
         (array[esquerda]->height > array[maior]->height ||
         (array[esquerda]->height == array[maior]->height && strcmp(array[esquerda]->name, array[maior]->name) > 0))))) {
        maior = esquerda;
    }

    // Se o filho à direita for maior que o maior até agora
    if (direita < n && ((array[direita] != NULL && array[maior] != NULL &&
         (array[direita]->height > array[maior]->height ||
         (array[direita]->height == array[maior]->height && strcmp(array[direita]->name, array[maior]->name) > 0))))) {
        maior = direita;
    }

    // Se o maior não for a raiz
    if (maior != i) {
        swap(&array[i], &array[maior]); // Troca
        reconstruir(array, n, maior); // Recursivamente reconstrói a subárvore afetada
    }
}

void heapsort(Pokemon** array, int n) {
    // Construção do heap com os k primeiros elementos
    for (int tam = K / 2 - 1; tam >= 0; tam--) {
        reconstruir(array, K, tam);
    }

    // Para cada um dos (n-k) demais elementos
    for (int i = K; i < n; i++) {
        if (array[i] != NULL && array[0] != NULL &&
            (array[i]->height < array[0]->height ||
            (array[i]->height == array[0]->height && strcmp(array[i]->name, array[0]->name) > 0))) {
            swap(&array[0], &array[i]); // Troca o menor com a raiz
            reconstruir(array, K, 0); // Reconstrói o heap
        }
    }

    // Ordenação propriamente dita
    int tam = K;
    while (tam > 1) {
        swap(&array[0], &array[--tam]); // Troca a raiz com o último elemento
        reconstruir(array, tam, 0); // Reconstrói o heap
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
        id[strcspn(id, "\n")] = 0;
        if (isFim(id)) break;

        Pokemon* pokemon1 = searchById(atoi(id), pokemon);
        if (pokemon1 != NULL) {
            selectedPokemon[selectedCount++] = pokemon1;
        }
    }

    // Perform partial heapsort on the selected Pokémon
    heapsort(selectedPokemon, selectedCount);

    // Print the sorted Pokémon
    for (int i = 2; i < 12; i++) {
        if (selectedPokemon[i] != NULL) {
            char habilidadesStr[200] = "";
            for (int j = 0; j < MAX_ABILITIES && selectedPokemon[i]->abilities[j][0] != '\0'; j++) {
                strcat(habilidadesStr, selectedPokemon[i]->abilities[j]);
                if (j < MAX_ABILITIES - 1 && selectedPokemon[i]->abilities[j + 1][0] != '\0') {
                    strcat(habilidadesStr, ",");
                }
            }

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
