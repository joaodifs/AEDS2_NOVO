#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>

#define MAX_TAM 100 // Tamanho máximo da lista

typedef struct Date {
    int day;
    int month;
    int year;
} Date;

char *dateToString(Date date) {
    char *str = (char *)malloc(11 * sizeof(char));
    sprintf(str, "%02d/%02d/%04d", date.day, date.month, date.year);
    return str;
}

Date stringToDate(char *str) {
    Date date;
    if (str != NULL && strlen(str) > 0) {
        sscanf(str, "%d/%d/%d", &date.day, &date.month, &date.year);
    } else {
        date.day = 0;
        date.month = 0;
        date.year = 0;
    }
    return date;
}

typedef struct Pokemon {
    int id;
    int generation;
    char *name;
    char *description;
    char types[2][50];
    char abilities[6][50];
    double weight;
    double height;
    int captureRate;
    bool isLegendary;
    Date captureDate;
} Pokemon;

char* my_strdup(const char* s) {
    char* copy = (char*)malloc(strlen(s) + 1);
    if (copy != NULL) {
        strcpy(copy, s);
    }
    return copy;
}

Pokemon createPokemon(int id, int generation, char *name, char *description, 
                      char *type1, char *type2, char *abilities[6], double weight,
                      double height, int captureRate, bool isLegendary, Date captureDate) {
    Pokemon p;
    p.id = id;
    p.generation = generation;
    p.name = my_strdup(name);
    p.description = my_strdup(description);

    strcpy(p.types[0], type1);
    if (type2 != NULL) {
        strcpy(p.types[1], type2);
    } else {
        strcpy(p.types[1], "");
    }

    for (int i = 0; i < 6; i++) {
        if (abilities[i] != NULL) {
            strcpy(p.abilities[i], abilities[i]);
        } else {
            strcpy(p.abilities[i], "");
        }
    }

    p.weight = weight;
    p.height = height;
    p.captureRate = captureRate;
    p.isLegendary = isLegendary;
    p.captureDate = captureDate;
    return p;
}

int split_csv_line(char *line, char **fields, int max_fields) {
    int field_count = 0;
    char *ptr = line;
    int in_quotes = 0;
    char *field_start = ptr;

    while (*ptr && field_count < max_fields) {
        if (*ptr == '"') {
            in_quotes = !in_quotes;
        } else if (*ptr == ',' && !in_quotes) {
            *ptr = '\0';
            fields[field_count++] = field_start;
            field_start = ptr + 1;
        }
        ptr++;
    }
    if (field_count < max_fields) {
        fields[field_count++] = field_start;
    }
    return field_count;
}

void lerPokemon(FILE *file, Pokemon *pokedex, int *n) {
    char line[1024];
    fgets(line, sizeof(line), file);

    while (fgets(line, sizeof(line), file) != NULL) {
        line[strcspn(line, "\n")] = '\0';
        char *fields[12];
        int field_count = split_csv_line(line, fields, 12);

        Pokemon p;
        p.id = atoi(fields[0]);
        p.generation = atoi(fields[1]);
        p.name = my_strdup(fields[2]);
        p.description = my_strdup(fields[3]);

        strcpy(p.types[0], fields[4]);
        if (strlen(fields[5]) > 0) {
            strcpy(p.types[1], fields[5]);
        } else {
            strcpy(p.types[1], "");
        }

        char *abilities_field = fields[6];
        if (abilities_field[0] == '"' && abilities_field[strlen(abilities_field) - 1] == '"') {
            abilities_field[strlen(abilities_field) - 1] = '\0';
            abilities_field++;
        }
        if (abilities_field[0] == '[' && abilities_field[strlen(abilities_field) - 1] == ']') {
            abilities_field[strlen(abilities_field) - 1] = '\0';
            abilities_field++;
        }

        char *abilityToken;
        char *restAbilities = abilities_field;
        int abilityIndex = 0;
        while ((abilityToken = strtok_r(restAbilities, ",", &restAbilities)) && abilityIndex < 6) {
            while (*abilityToken == ' ' || *abilityToken == '\'') abilityToken++;
            char *tempEnd = abilityToken + strlen(abilityToken) - 1;
            while (tempEnd > abilityToken && (*tempEnd == ' ' || *tempEnd == '\'')) {
                *tempEnd = '\0';
                tempEnd--;
            }
            if (strlen(abilityToken) > 0) {
                strcpy(p.abilities[abilityIndex], abilityToken);
                abilityIndex++;
            }
        }
        for (; abilityIndex < 6; abilityIndex++) {
            strcpy(p.abilities[abilityIndex], "");
        }

        p.weight = atof(fields[7]);
        p.height = atof(fields[8]);
        p.captureRate = atoi(fields[9]);
        p.isLegendary = atoi(fields[10]);
        p.captureDate = stringToDate(fields[11]);

        pokedex[*n] = p;
        (*n)++;
    }
}

void imprimirPokemon(int i, Pokemon *p) {
    printf("[%d] [#%d -> %s: %s - ['",i, p->id, p->name, p->description);
    printf("%s", p->types[0]);
    if (strlen(p->types[1]) > 0) {
        printf("', '%s", p->types[1]);
    }
    printf("'] - [");
    for (int i = 0; i < 6; i++) {
        if (strlen(p->abilities[i]) > 0) {
            printf("'%s'", p->abilities[i]);
            if (i < 5 && strlen(p->abilities[i + 1]) > 0) {
                printf(", ");
            }
        }
    }
    printf("] - %.1fkg - %.1fm - %d%% - %s - %d gen] - ", p->weight, p->height, p->captureRate, 
           p->isLegendary ? "true" : "false", p->generation);
    char *date = dateToString(p->captureDate);
    printf("%s\n", date);
    free(date);
}

Pokemon* findPokemonById(Pokemon pokedex[], int n, int id) {
    for (int i = 0; i < n; i++) {
        if (pokedex[i].id == id) {
            return &pokedex[i];
        }
    }
    return NULL;
}

typedef struct Pilha {
    Pokemon **pokemons;
    int topo;
    int n;
} Pilha;

Pilha inicializarPilha(int qtde) {
    Pilha pilha;
    pilha.pokemons = malloc(qtde * sizeof(Pokemon*));
    pilha.topo = -1;
    pilha.n = 0;
    return pilha;
}

void inserir(Pilha *pilha, Pokemon *pokemon) {
    if (pilha->topo >= MAX_TAM - 1) {
        printf("Erro: Pilha cheia!\n");
        return;
    }
    pilha->topo++;
    pilha->pokemons[pilha->topo] = pokemon;
    pilha->n++;
}

Pokemon *remover(Pilha *pilha) {
    if (pilha->topo == -1) {
        printf("Erro: Pilha vazia!\n");
        exit(EXIT_FAILURE);
    }
    Pokemon *pokemonRemovido = pilha->pokemons[pilha->topo];
    pilha->topo--;
    pilha->n--;
    return pokemonRemovido;
}

void imprimirPilha(Pilha *pilha) {
    for (int i = 0; i <= pilha->topo; i++) {
        imprimirPokemon(i, pilha->pokemons[i]);
    }
}

int main() {
    char *csvPath = "/tmp/pokemon.csv";
    FILE *file = fopen(csvPath, "r");
    if (file == NULL) {
        printf("Erro ao abrir o arquivo CSV.\n");
        return 1;
    }

    Pokemon pokedex[801];
    int n = 0;
    lerPokemon(file, pokedex, &n);
    fclose(file);

    Pilha pilhaPokemons = inicializarPilha(MAX_TAM);

    char inputId[10];
    scanf("%s", inputId);
    while (strcmp(inputId, "FIM") != 0) {
        int id = atoi(inputId);
        Pokemon* p = findPokemonById(pokedex, n, id);
        if (p != NULL) {
            inserir(&pilhaPokemons, p);
        }
        scanf("%s", inputId);
    }

    int qtdComandos;
    char comandos[3];
    scanf("%d", &qtdComandos);

    for (int i = 0; i < qtdComandos; i++) {
        scanf("%s", comandos);
        if (strcmp(comandos, "I") == 0) {
            scanf("%s", inputId);
            int id = atoi(inputId);
            Pokemon* p = findPokemonById(pokedex, n, id);
            if (p != NULL) {
                inserir(&pilhaPokemons, p);
            }
        } else if (strcmp(comandos, "R") == 0) {
            Pokemon* p = remover(&pilhaPokemons);
            if (p != NULL) {
                printf("(R) %s\n", p->name);
            }
        }
    }

    imprimirPilha(&pilhaPokemons);

    for (int i = 0; i < n; i++) {
        free(pokedex[i].name);
        free(pokedex[i].description);
    }

    return 0;
}
