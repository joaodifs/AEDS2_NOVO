#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>

#define MAX_TAM 801 // Tamanho máximo da lista de Pokémons

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

    strncpy(p.types[0], type1, sizeof(p.types[0]) - 1);
    p.types[0][sizeof(p.types[0]) - 1] = '\0';
    if (type2 != NULL) {
        strncpy(p.types[1], type2, sizeof(p.types[1]) - 1);
        p.types[1][sizeof(p.types[1]) - 1] = '\0';
    } else {
        p.types[1][0] = '\0';
    }

    for (int i = 0; i < 6; i++) {
        if (abilities[i] != NULL) {
            strncpy(p.abilities[i], abilities[i], sizeof(p.abilities[i]) - 1);
            p.abilities[i][sizeof(p.abilities[i]) - 1] = '\0';
        } else {
            p.abilities[i][0] = '\0';
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
    fgets(line, sizeof(line), file); // Lê o cabeçalho do CSV

    while (fgets(line, sizeof(line), file) != NULL) {
        line[strcspn(line, "\n")] = '\0'; // Remove a nova linha

        Pokemon p;
        memset(&p, 0, sizeof(Pokemon));

        char *fields[12];
        int field_count = split_csv_line(line, fields, 12);

        p.id = atoi(fields[0]);
        p.generation = atoi(fields[1]);
        p.name = my_strdup(fields[2]);
        p.description = my_strdup(fields[3]);
        
        strncpy(p.types[0], fields[4], sizeof(p.types[0]) - 1);
        p.types[0][sizeof(p.types[0]) - 1] = '\0';
        if (strlen(fields[5]) > 0) {
            strncpy(p.types[1], fields[5], sizeof(p.types[1]) - 1);
            p.types[1][sizeof(p.types[1]) - 1] = '\0';
        } else {
            p.types[1][0] = '\0';
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
                strncpy(p.abilities[abilityIndex], abilityToken, sizeof(p.abilities[abilityIndex]) - 1);
                p.abilities[abilityIndex][sizeof(p.abilities[abilityIndex]) - 1] = '\0';
                abilityIndex++;
            }
        }
        for (; abilityIndex < 6; abilityIndex++) {
            p.abilities[abilityIndex][0] = '\0';
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

    int numTypes = 0;
    for (int i = 0; i < 2; i++) {
        if (strlen(p->types[i]) > 0) {
            if (numTypes > 0) {
                printf("', ");
            }
            printf("%s", p->types[i]);
            numTypes++;
        }
    }
    printf("'] - ");

    int numAbilities = 0;
    printf("[");
    for (int i = 0 ; i < 6 ; i++) {
        if (strlen(p->abilities[i]) > 0) {
            printf("'%s'", p->abilities[i]);
            if (i < 5 && strlen(p->abilities[i + 1]) > 0) {
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

// Estrutura de lista encadeada para os Pokémons
typedef struct Nodo {
    Pokemon* pokemon;
    struct Nodo *proximo;
} Nodo;

typedef struct Lista {
    Nodo *inicio;
    Nodo *fim;
} Lista;

void inicializarLista(Lista* lista) {
    lista->inicio = NULL;
    lista->fim = NULL;
}

void inserirFim(Lista* lista, Pokemon* p) {
    Nodo* novoNodo = (Nodo*)malloc(sizeof(Nodo));
    novoNodo->pokemon = p;
    novoNodo->proximo = NULL;

    if (lista->fim != NULL) {
        lista->fim->proximo = novoNodo;
    }
    lista->fim = novoNodo;

    if (lista->inicio == NULL) {
        lista->inicio = novoNodo;
    }
}

// Função para inserir um Pokémon no início da lista
void inserirInicio(Lista *lista, Pokemon* p) {
    Nodo* novoNodo = (Nodo*)malloc(sizeof(Nodo));
    novoNodo->pokemon = p;
    novoNodo->proximo = lista->inicio;

    lista->inicio = novoNodo;
    if (lista->fim == NULL) {
        lista->fim = novoNodo;
    }
}

// Função para inserir um Pokémon em uma posição específica da lista
void inserir(Lista *lista, Pokemon* p, int pos) {
    if (pos < 0) return; // Evita posições inválidas

    Nodo* novoNodo = (Nodo*)malloc(sizeof(Nodo));
    novoNodo->pokemon = p;

    if (pos == 0) {
        novoNodo->proximo = lista->inicio;
        lista->inicio = novoNodo;
        if (lista->fim == NULL) {
            lista->fim = novoNodo;
        }
        return;
    }

    Nodo* atual = lista->inicio;
    int i = 0;
    while (atual != NULL && i < pos - 1) {
        atual = atual->proximo;
        i++;
    }

    if (atual == NULL) {
        free(novoNodo);
        return;
    }

    novoNodo->proximo = atual->proximo;
    atual->proximo = novoNodo;

    if (novoNodo->proximo == NULL) {
        lista->fim = novoNodo;
    }
}

void imprimirLista(Lista* lista) {
    Nodo* atual = lista->inicio;
    int i = 0;
    while (atual != NULL) {
        imprimirPokemon(i, atual->pokemon);
        atual = atual->proximo;
        i++;
    }
}

Pokemon* findPokemonById(Pokemon *pokedex, int n, int id) {
    for (int i = 0; i < n; i++) {
        if (pokedex[i].id == id) {
            return &pokedex[i];
        }
    }
    return NULL;
}

Pokemon* removerInicio(Lista *lista) {
    if (lista->inicio == NULL) {
        return NULL;
    }
    Nodo* removido = lista->inicio;
    lista->inicio = removido->proximo;
    if (lista->inicio == NULL) {
        lista->fim = NULL;
    }
    Pokemon* pokemonRemovido = removido->pokemon;
    free(removido);
    return pokemonRemovido;
}

Pokemon* removerFim(Lista *lista) {
    if (lista->fim == NULL) {
        return NULL;
    }
    Nodo* atual = lista->inicio;
    while (atual != NULL && atual->proximo != lista->fim) {
        atual = atual->proximo;
    }
    if (atual != NULL) {
        atual->proximo = NULL;
    }
    Nodo* removido = lista->fim;
    lista->fim = atual;
    Pokemon* pokemonRemovido = removido->pokemon;
    free(removido);
    return pokemonRemovido;
}

Pokemon* remover(Lista *lista, int pos) {
    if (pos < 0) return NULL;

    Nodo* atual = lista->inicio;
    Nodo* anterior = NULL;
    int i = 0;
    while (atual != NULL && i < pos) {
        anterior = atual;
        atual = atual->proximo;
        i++;
    }
    if (atual == NULL) {
        return NULL;
    }
    if (anterior != NULL) {
        anterior->proximo = atual->proximo;
    } else {
        lista->inicio = atual->proximo;
    }
    if (atual == lista->fim) {
        lista->fim = anterior;
    }
    Pokemon* pokemonRemovido = atual->pokemon;
    free(atual);
    return pokemonRemovido;
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

    Lista listaPokemons; 
    inicializarLista(&listaPokemons);

    char inputId[10];
    scanf("%s", inputId);
    
    while (strcmp(inputId, "FIM") != 0) {
        int id = atoi(inputId);
        Pokemon* p = findPokemonById(pokedex, n, id);
        if(p != NULL) {
            inserirFim(&listaPokemons, p);
        }
        
        scanf("%s", inputId); 
    }

    int qtdComandos;
    char comandos[3];
    scanf("%i", &qtdComandos);

    for (int i = 0 ; i < qtdComandos ; i++) {
        scanf("%s", comandos);

        if(strcmp(comandos, "II") == 0) {
            scanf("%s", inputId);
            int id = atoi(inputId); 
            Pokemon* p = findPokemonById(pokedex, n, id);
            if (p != NULL) {
                inserirInicio(&listaPokemons, p);
            }
        } else if (strcmp(comandos, "I*") == 0) {
            int pos;
            scanf("%d", &pos);
            scanf("%s", inputId); 
            int id = atoi(inputId); 
            Pokemon* p = findPokemonById(pokedex, n, id);
            if (p != NULL) {
                inserir(&listaPokemons, p, pos);
            }
        } else if (strcmp(comandos, "IF") == 0) {
            scanf("%s", inputId);
            int id = atoi(inputId); 
            Pokemon* p = findPokemonById(pokedex, n, id);
            if (p != NULL) {
                inserirFim(&listaPokemons, p);
            }
        } else if (strcmp(comandos, "RI") == 0) {
            Pokemon* p = removerInicio(&listaPokemons);
            if (p != NULL) {
                printf("(R) %s\n", p->name);
            }
        } else if (strcmp(comandos, "R*") == 0) {
            int pos;
            scanf("%d", &pos);
            Pokemon* p = remover(&listaPokemons, pos);
            if (p != NULL) {
                printf("(R) %s\n", p->name);
            }
        } else if (strcmp(comandos, "RF") == 0) {
            Pokemon* p = removerFim(&listaPokemons);
            if (p != NULL) {
                printf("(R) %s\n", p->name);
            }
        }
    }

    imprimirLista(&listaPokemons);
 
    // Libera a memória alocada
    for (int i = 0; i < n; i++) {
        free(pokedex[i].name);
        free(pokedex[i].description);
    }

    return 0;
}
