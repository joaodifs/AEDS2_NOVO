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
    if (str != NULL && strlen(str) > 0) { // Verifica se str não é NULL e não está vazia
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

// strdup aloca memória para a string
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
    
    // types
    strcpy(p.types[0], type1);
    if (type2 != NULL) {
        strcpy(p.types[1], type2);
    }

    // abilities
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

// Função para dividir uma linha CSV em campos, considerando double quotes
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
    // Adiciona o último campo
    if (field_count < max_fields) {
        fields[field_count++] = field_start;
    }

    return field_count;
}

// Função para ler os Pokémons do arquivo CSV
void lerPokemon(FILE *file, Pokemon *pokedex, int *n) {
    char line[1024];

    fgets(line, sizeof(line), file); // Lê o cabeçalho do CSV

    while (fgets(line, sizeof(line), file) != NULL) {
        line[strcspn(line, "\n")] = '\0'; // Remove a nova linha

        Pokemon p;
        memset(&p, 0, sizeof(Pokemon)); // Zera a memória da estrutura Pokemon p

        char *fields[12]; // Ajuste se houver mais campos
        int field_count = split_csv_line(line, fields, 12);

        // id
        p.id = atoi(fields[0]);

        // generation
        p.generation = atoi(fields[1]);

        // name
        p.name = my_strdup(fields[2]);

        // description
        p.description = my_strdup(fields[3]);
        
        // types
        strcpy(p.types[0], fields[4]);
        if (strlen(fields[5]) > 0) {
            strcpy(p.types[1], fields[5]);
        } else {
            strcpy(p.types[1], "");
        }

        // abilities
        char *abilities_field = fields[6];
        // remove double quotes
        if (abilities_field[0] == '"' && abilities_field[strlen(abilities_field) - 1] == '"') {
            abilities_field[strlen(abilities_field) - 1] = '\0';
            abilities_field++;
        }
        // remove colchetes
        if (abilities_field[0] == '[' && abilities_field[strlen(abilities_field) - 1] == ']') {
            abilities_field[strlen(abilities_field) - 1] = '\0';
            abilities_field++;
        }

        // divide nas abilities individuais
        char *abilityToken;
        char *restAbilities = abilities_field;
        int abilityIndex = 0;
        while ((abilityToken = strtok_r(restAbilities, ",", &restAbilities)) && abilityIndex < 6) {
            // remove simples quotes
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
        // Preenche o restante das habilidades com strings vazias
        for (; abilityIndex < 6; abilityIndex++) {
            strcpy(p.abilities[abilityIndex], "");
        }

        // weight
        p.weight = atof(fields[7]);

        // height
        p.height = atof(fields[8]);

        // captureRate
        p.captureRate = atoi(fields[9]);

        // isLegendary
        p.isLegendary = atoi(fields[10]);

        // captureDate
        p.captureDate = stringToDate(fields[11]);
        
        pokedex[*n] = p;
        (*n)++;
    }
}

void imprimirPokemon(Pokemon *p) {
    printf("[#%d -> %s: %s - ['", p->id, p->name, p->description);

    // types
    if(strlen(p->types[0]) > 0) {
        printf("%s", p->types[0]);
    }
    if (strlen(p->types[1]) > 0) {
        printf("', '%s", p->types[1]);
    }
    printf("'] - ");

    // abilities
    printf("[");
    for (int i = 0 ; i < 6 ; i++) {
        if (strlen(p->abilities[i]) > 0) {
            printf("'%s'", p->abilities[i]);
            if (i < 5 && strlen(p->abilities[i+1]) > 0) {
                printf(", ");
            }
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
    // Primeiro, compara os anos
    if (date1->year != date2->year) {
        return date1->year - date2->year;
    }

    // Se os anos são iguais, compara os meses
    if (date1->month != date2->month) {
        return date1->month - date2->month;
    }

    // Se os meses são iguais, compara os dias
    return date1->day - date2->day;
}

Pokemon* findPokemonById(Pokemon pokedex[], int n, int id) {
    for (int i = 0; i < n; i++) {
        if (pokedex[i].id == id) {
            return &pokedex[i];
        }
    }
}

typedef struct Celula {
    Pokemon *pokemon;
    struct Celula *prox;
} Celula;

Celula* novaCelula(Pokemon *pokemon) {
   Celula* nova = (Celula*) malloc(sizeof(Celula));
   nova->pokemon = pokemon;
   nova->prox = NULL;
   return nova;
}

typedef struct Pilha {
    Celula *topo;
    int n;
} Pilha;

void inicializarPilha(Pilha *pilha) {
    pilha->topo = NULL;
    pilha->n = 0;
}

void inserir(Pilha *pilha, Pokemon *pokemon) {
    Celula *nova = novaCelula(pokemon);
    nova->prox = pilha->topo;
    pilha->topo = nova;
    pilha->n++;
}

Pokemon *remover(Pilha *pilha) {
    if (pilha->topo == NULL) {
        printf("Erro: Pilha vazia!\n");
        exit(EXIT_FAILURE);
    }
    Celula *tmp = pilha->topo;
    Pokemon *pokemonRemovido = tmp->pokemon;
    pilha->topo = tmp->prox;
    free(tmp);
    pilha->n--;
    return pokemonRemovido;
}

void imprimirPilha_rec(Celula *current, int pos) {
    if (current == NULL) return;
    imprimirPilha_rec(current->prox, pos - 1);
    printf("[%d] ", (pos));
    imprimirPokemon(current->pokemon);
}

void imprimirPilha(Pilha *pilha) {
    imprimirPilha_rec(pilha->topo, pilha->n - 1);
}

// main
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

    Pilha pilhaPokemons;
    inicializarPilha(&pilhaPokemons);

    char inputId[10];
    scanf("%s", inputId);
    
    while (strcmp(inputId, "FIM") != 0) {
        int id = atoi(inputId);
        Pokemon* p = findPokemonById(pokedex, n, id);
        if(p != NULL) {
            inserir(&pilhaPokemons, p);
        }
        
        scanf("%s", inputId); 
    }

    int qtdComandos;
    char comandos[3];
    scanf("%i", &qtdComandos);

    for (int i = 0 ; i < qtdComandos ; i++) {
        scanf("%s", comandos);

        if(strcmp(comandos, "I") == 0) {
            scanf("%s", inputId);
            int id = atoi(inputId); // passa string para int
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

    // Libera a memória alocada
    for (int i = 0; i < n; i++) {
        free(pokedex[i].name);
        free(pokedex[i].description);
    }

    return 0;
}
