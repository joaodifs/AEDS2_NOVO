#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <stdlib.h>

#define MAX_TAM 100 // Tamanho máximo da lista

typedef struct Date {
    int day, month, year;
} Date;

typedef struct Pokemon {
    int id, generation, captureRate;
    char *name, *description, types[2][50], abilities[6][50];
    double weight, height;
    bool isLegendary;
    Date captureDate;
} Pokemon;

typedef struct Pilha {
    Pokemon *pokemons[MAX_TAM];
    int topo;
} Pilha;

// Funções auxiliares
Date stringToDate(const char *str) {
    Date date = {0, 0, 0};
    if (str && strlen(str) > 0) sscanf(str, "%d/%d/%d", &date.day, &date.month, &date.year);
    return date;
}

char *dateToString(Date date) {
    char *str = malloc(11);
    sprintf(str, "%02d/%02d/%04d", date.day, date.month, date.year);
    return str;
}

char *my_strdup(const char *s) {
    char *copy = malloc(strlen(s) + 1);
    return copy ? strcpy(copy, s) : NULL;
}

// Função de criação de Pokémon
Pokemon createPokemon(int id, int gen, const char *name, const char *desc, 
                      const char *type1, const char *type2, char *abilities[6], 
                      double weight, double height, int captureRate, 
                      bool isLegendary, Date captureDate) {
    Pokemon p;
    p.id = id;
    p.generation = gen;
    p.captureRate = captureRate;
    p.name = my_strdup(name);
    p.description = my_strdup(desc);
    p.weight = weight;
    p.height = height;
    p.isLegendary = isLegendary;
    p.captureDate = captureDate;

    strcpy(p.types[0], type1 ? type1 : "");
    strcpy(p.types[1], type2 ? type2 : "");

    for (int i = 0; i < 6; i++) {
        strcpy(p.abilities[i], abilities[i] ? abilities[i] : "");
    }

    return p;
}


// Função para separar linha CSV
int splitLine(char *line, char *fields[], int maxFields) {
    int count = 0;
    char *start = line;
    while (*line && count < maxFields) {
        if (*line == ',' || *line == '\n') {
            *line = '\0';
            fields[count++] = start;
            start = line + 1;
        }
        line++;
    }
    if (count < maxFields) fields[count++] = start;
    return count;
}

// Função para ler Pokémon de um arquivo
void lerPokemon(FILE *file, Pokemon pokedex[], int *count) {
    char line[1024];
    fgets(line, sizeof(line), file); // Ignorar cabeçalho

    while (fgets(line, sizeof(line), file)) {
        char *fields[12];
        splitLine(line, fields, 12);

        char *abilitiesField = fields[6];
        char *abilities[6] = {NULL};
        int aIdx = 0;

        // Processar habilidades separadas por vírgula
        if (*abilitiesField == '"') abilitiesField++;
        while (*abilitiesField && aIdx < 6) {
            char *end = strchr(abilitiesField, ',');
            if (!end) end = abilitiesField + strlen(abilitiesField);
            while (*abilitiesField == '\'' || *abilitiesField == ' ') abilitiesField++;
            abilities[aIdx++] = my_strdup(abilitiesField);
            abilitiesField = (*end) ? end + 1 : end;
        }

        pokedex[(*count)++] = createPokemon(
            atoi(fields[0]), atoi(fields[1]), fields[2], fields[3], 
            fields[4], fields[5], abilities, atof(fields[7]), 
            atof(fields[8]), atoi(fields[9]), atoi(fields[10]), 
            stringToDate(fields[11])
        );
    }
}

// Função para inicializar uma pilha
Pilha inicializarPilha() {
    Pilha pilha = {.topo = -1};
    return pilha;
}

// Funções de manipulação da pilha
void inserirPilha(Pilha *pilha, Pokemon *pokemon) {
    if (pilha->topo < MAX_TAM - 1) pilha->pokemons[++pilha->topo] = pokemon;
    else printf("Erro: Pilha cheia!\n");
}

Pokemon *removerPilha(Pilha *pilha) {
    if (pilha->topo >= 0) return pilha->pokemons[pilha->topo--];
    printf("Erro: Pilha vazia!\n");
    return NULL;
}

// Função para imprimir Pokémon
void imprimirPokemon(Pokemon *p) {
    printf("#%d %s - %.1fkg - %.1fm\n", p->id, p->name, p->weight, p->height);
}

// Função principal
int main() {
    FILE *file = fopen("/tmp/pokemon.csv", "r");
    if (!file) {
        printf("Erro ao abrir o arquivo.\n");
        return 1;
    }

    Pokemon pokedex[MAX_TAM];
    int count = 0;
    lerPokemon(file, pokedex, &count);
    fclose(file);

    Pilha pilha = inicializarPilha();
    char comando[16];
    
    // Processar comandos
    while (true) {
        scanf("%s", comando);
        if (strcmp(comando, "FIM") == 0) break;

        if (isdigit(comando[0])) {
            int id = atoi(comando);
            for (int i = 0; i < count; i++) {
                if (pokedex[i].id == id) {
                    inserirPilha(&pilha, &pokedex[i]);
                    break;
                }
            }
        } else if (strcmp(comando, "I") == 0) {
            int id;
            scanf("%d", &id);
            for (int i = 0; i < count; i++) {
                if (pokedex[i].id == id) {
                    inserirPilha(&pilha, &pokedex[i]);
                    break;
                }
            }
        } else if (strcmp(comando, "R") == 0) {
            Pokemon *p = removerPilha(&pilha);
            if (p) printf("(R) %s\n", p->name);
        }
    }

    // Imprimir a pilha
    for (int i = pilha.topo; i >= 0; i--) {
        imprimirPokemon(pilha.pokemons[i]);
    }

    // Liberar memória
    for (int i = 0; i < count; i++) {
        free(pokedex[i].name);
        free(pokedex[i].description);
    }

    return 0;
}
