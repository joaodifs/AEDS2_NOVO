#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#define MAX_POKEMONS 801
#define MAX_NAME_LENGTH 100
#define MAX_DESC_LENGTH 200
#define MAX_TYPE_LENGTH 50
#define MAX_ABILITY_LENGTH 50
#define MAX_CAPTURE_DATE_LENGTH 11

typedef struct {
    int id;
    int generation;
    char name[MAX_NAME_LENGTH];
    char description[MAX_DESC_LENGTH];
    char types[2][MAX_TYPE_LENGTH]; // Supondo que só haja 2 tipos
    char abilities[MAX_ABILITY_LENGTH];
    double weight;
    double height;
    int captureRate;
    bool isLegendary;
    char captureDate[MAX_CAPTURE_DATE_LENGTH];
} Pokemon;

// Função para verificar se a entrada é "FIM"
bool isFim(char* id) {
    return strcmp(id, "FIM") == 0;
}

// Função para buscar um Pokémon pelo ID
Pokemon* searchById(int id, Pokemon pokemon[], int total) {
    for (int i = 0; i < total; i++) {
        if (pokemon[i].id == id) {
            return &pokemon[i];
        }
    }
    return NULL;
}

int main() {
    FILE *file = fopen("/tmp/pokemon.csv", "r");
    if (file == NULL) {
        perror("Erro ao abrir o arquivo");
        return EXIT_FAILURE;
    }

    Pokemon pokemon[MAX_POKEMONS];
    char line[512];
    int count = 0;

    // Lendo os dados do arquivo e criando os objetos de Pokemon
    fgets(line, sizeof(line), file); // Pular a primeira linha
    while (fgets(line, sizeof(line), file) && count < MAX_POKEMONS) {
        sscanf(line, "%d,%d,%[^,],%[^,],%[^,],%[^,],%lf,%lf,%d,%d,%s",
               &pokemon[count].id,
               &pokemon[count].generation,
               pokemon[count].name,
               pokemon[count].description,
               pokemon[count].types[0],  // primeiro tipo
               pokemon[count].types[1],  // segundo tipo
               &pokemon[count].weight,
               &pokemon[count].height,
               &pokemon[count].captureRate,
               (int*)&pokemon[count].isLegendary,  // converter para bool
               pokemon[count].captureDate);
        count++;
    }
    fclose(file);

    char id[10];
    while (true) {
        scanf("%s", id);
        if (isFim(id)) {
            break;
        }

        int pokemonId = atoi(id);
        Pokemon* pokemon1 = searchById(pokemonId, pokemon, count);
        if (pokemon1 != NULL) {
            // Printando no formato solicitado
            printf("[#%d -> %s: %s - [%s, %s] - [%s] - %.1fkg - %.1fm - %d%% - %s - %d gen] - %s\n",
                   pokemon1->id,
                   pokemon1->name,
                   pokemon1->description,
                   pokemon1->types[0], pokemon1->types[1],
                   pokemon1->abilities,
                   pokemon1->weight,
                   pokemon1->height,
                   pokemon1->captureRate,
                   pokemon1->isLegendary ? "true" : "false",
                   pokemon1->generation,
                   pokemon1->captureDate);
        }
    }

    return EXIT_SUCCESS;
}