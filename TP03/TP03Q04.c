#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>

#define MAX_TAM 5 

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

Pokemon createPokemon(int id, int generation, char *name,
    char *description, char *type1, char *type2, char *abilities[6], double weight,
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

        Pokemon p;
        memset(&p, 0, sizeof(Pokemon)); 
        char *fields[12]; 
        int field_count = split_csv_line(line, fields, 12);


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

void imprimirPokemon(Pokemon *p) {
    printf("[#%d -> %s: %s - ['", p->id, p->name, p->description);

    if (strlen(p->types[0]) > 0) {
        printf("%s", p->types[0]);
    }
    if (strlen(p->types[1]) > 0) {
        printf("', '%s", p->types[1]);
    }
    printf("'] - ");


    printf("[");
    for (int i = 0; i < 6 && strlen(p->abilities[i]) > 0; i++) {
        printf("'%s'", p->abilities[i]);
        if (i < 5 && strlen(p->abilities[i + 1]) > 0) {
            printf(", ");
        }
    }
    printf("] - ");

    printf("%.1fkg - ", p->weight);
    printf("%.1fm - ", p->height);
    printf("%d%% - ", p->captureRate);
    printf("%s - ", p->isLegendary ? "true" : "false");
    printf("%d gen] - ", p->generation);
    char *data = dateToString(p->captureDate);
    printf("%s", data);
    free(data);

    printf("\n");
}

int compareDates(Date *date1, Date *date2) {
    if (date1->year != date2->year) {
        return date1->year - date2->year;
    }

    if (date1->month != date2->month) {
        return date1->month - date2->month;
    }

    return date1->day - date2->day;
}

Pokemon* findPokemonById(Pokemon pokedex[], int n, int id) {
    for (int i = 0; i < n; i++) {
        if (pokedex[i].id == id) {
            return &pokedex[i];
        }
    }
    return NULL;
}

typedef struct Fila {
    Pokemon **pokemons;
    int inicio;
    int fim;
    int n;
} Fila;

Fila inicializarFila(int qtde) {
    Fila fila;
    fila.pokemons = malloc(qtde * sizeof(Pokemon*));
    fila.inicio = 0;
    fila.fim = 0;
    fila.n = 0;
    return fila;
}

Pokemon *remover(Fila *fila) {
    if (fila->n == 0) {
        printf("Erro: Fila vazia!\n");
        exit(EXIT_FAILURE);
    }
    Pokemon *pokemonRemovido = fila->pokemons[fila->inicio];
    fila->inicio = (fila->inicio + 1) % MAX_TAM;
    fila->n--;
    return pokemonRemovido;
}

void inserir(Fila *fila, Pokemon *pokemon) {
    if (fila->n >= MAX_TAM) {
        remover(fila);
    }
    fila->pokemons[fila->fim] = pokemon;
    fila->fim = (fila->fim + 1) % MAX_TAM;
    fila->n++;
}

int mediaPokemonsFila(Fila *fila) {
    float total = 0.0;
    int count = 0;
    for (int i = fila->inicio; count < fila->n; i = (i + 1) % MAX_TAM) {
        total += fila->pokemons[i]->captureRate;
        count++;
    }
    return (int)roundf(total / fila->n);
}

void imprimirFila(Fila *fila) {
    int count = 0;
    printf("\n");
    for (int i = fila->inicio; count < fila->n; i = (i + 1) % MAX_TAM) {
        printf("[%d] ", count);
        imprimirPokemon(fila->pokemons[i]);
        count++;
    }
}

int main () {
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

    Fila filaPokemons = inicializarFila(MAX_TAM);

    char inputId[10];
    scanf("%s", inputId);
    
    while (strcmp(inputId, "FIM") != 0) {
        int id = atoi(inputId);
        Pokemon* p = findPokemonById(pokedex, n, id);
        if(p != NULL) {
            inserir(&filaPokemons, p);
            printf("Média: %d\n", mediaPokemonsFila(&filaPokemons));
        }
        
        scanf("%s", inputId); 
    }

    int qtdComandos;
    char comandos[3];
    scanf("%i", &qtdComandos);

    for (int i = 0; i < qtdComandos; i++) {
        scanf("%s", comandos);

        if (strcmp(comandos, "I") == 0) {
            scanf("%s", inputId);
            int id = atoi(inputId); 
            Pokemon* p = findPokemonById(pokedex, n, id);
            if (p != NULL) {
                inserir(&filaPokemons, p);
            }
            printf("Média: %d\n", mediaPokemonsFila(&filaPokemons));
        } else if (strcmp(comandos, "R") == 0) {
            Pokemon* p = remover(&filaPokemons);
            if (p != NULL) {
                printf("(R) %s\n", p->name);
            }
        }
    }

    imprimirFila(&filaPokemons);
 
    for (int i = 0; i < n; i++) {
        free(pokedex[i].name);
        free(pokedex[i].description);
    }

    return 0;
}