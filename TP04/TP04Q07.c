#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>

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
    }

    for (int i = 0; i < 6; i++) {
        if (abilities[i] != NULL) {
            strncpy(p.abilities[i], abilities[i], sizeof(p.abilities[i]) - 1);
            p.abilities[i][sizeof(p.abilities[i]) - 1] = '\0';
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

// Função para ler os Pokémons do arquivo CSV
void lerPokemon(FILE *file, Pokemon *pokedex, int *n) {
    char line[1024];

    fgets(line, sizeof(line), file); // Lê o cabeçalho do CSV

    while (fgets(line, sizeof(line), file) != NULL) {
        line[strcspn(line, "\n")] = '\0'; 

        Pokemon p;
        memset(&p, 0, sizeof(Pokemon)); 

        char *fields[12]; 
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
        strncpy(p.types[0], fields[4], sizeof(p.types[0]) - 1);
        p.types[0][sizeof(p.types[0]) - 1] = '\0';
        if (strlen(fields[5]) > 0) {
            strncpy(p.types[1], fields[5], sizeof(p.types[1]) - 1);
            p.types[1][sizeof(p.types[1]) - 1] = '\0';
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
                strncpy(p.abilities[abilityIndex], abilityToken, sizeof(p.abilities[abilityIndex]) - 1);
                p.abilities[abilityIndex][sizeof(p.abilities[abilityIndex]) - 1] = '\0';
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
    int numTypes = (strlen(p->types[0]) > 0) + (strlen(p->types[1]) > 0);
    if(numTypes > 0) {
        printf("%s", p->types[0]);
    }
    if (numTypes > 1) {
        printf("', '%s", p->types[1]);
    }
    printf("'] - ");

    // abilities
    int numAbilities = 0;
    for (int i = 0; i < 6; i++) {
        if (strlen(p->abilities[i]) > 0) {
            numAbilities++;
        }
    }

    printf("[");
    for (int i = 0; i < numAbilities; i++) {
        printf("'%s'", p->abilities[i]);
        if (i < numAbilities - 1) {
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

Pokemon* findPokemonById(Pokemon pokedex[], int n, int id) {
    for (int i = 0; i < n; i++) {
        if (pokedex[i].id == id) {
            return &pokedex[i];
        }
    }
    return NULL;
}

Pokemon* findPokemonByName(Pokemon pokedex[], int n, char name[]) {
    for (int i = 0 ; i < n ; i++) {
        if (strcmp(pokedex[i].name, name) == 0) {
            return &pokedex[i];
        }
    }

    return NULL;
}

typedef struct Celula {
    Pokemon *pokemon;
    struct Celula* prox;
} Celula;

Celula* novaCelula(Pokemon *pokemon) {
    Celula* nova = (Celula*) malloc(sizeof(Celula));
    nova->pokemon = pokemon;
    nova->prox = NULL;
    return nova;
}

typedef struct Lista {
    Celula* primeiro;
    Celula* ultimo;
    int n;
} Lista;

void inicializarLista(Lista *lista) {
    lista->primeiro = NULL;
    lista->ultimo = NULL;
    lista->n = 0;
}

void inserirInicio(Lista* lista, Pokemon* p) {
    Celula* tmp = novaCelula(p);
    tmp->prox = lista->primeiro;
    lista->primeiro = tmp;
    if (lista->ultimo == NULL) {
        lista->ultimo = tmp;
    }
}

void inserirFim(Lista* lista, Pokemon* p) {
    Celula* tmp = novaCelula(p);
    if (lista->primeiro == NULL) {
        lista->primeiro = tmp;
        lista->ultimo = tmp;
    } else {
        lista->ultimo->prox = tmp;
        lista->ultimo = tmp;
    }
}

void inserir(Lista* lista, Pokemon* p, int pos) {
    if (pos == 0) {
        inserirInicio(lista, p);
    } else if (pos == lista->n) {
        inserirFim(lista, p);
    } else {
        Celula *i = lista->primeiro;
        for (int count = 0 ; count < pos - 1 ; count++) {
            i = i->prox;
        }
        Celula* tmp = novaCelula(p);
        tmp->prox = i->prox;
        i->prox = tmp;
        lista->n++;
    }
}

bool pesquisar(Lista* lista, Pokemon* p, int pos) {
    bool resp = false;
    Celula* i = lista->primeiro;
    while (i != NULL) {
        if (i->pokemon == p) {
            printf("(Posicao: %i) SIM\n", pos);
            resp = true;
            i = lista->ultimo;
        }
        i = i->prox;
    }
    if (!resp) {
        printf("NAO\n");
    }

    return resp;
}

typedef struct Hash {
    Lista** tabela;
    int tam;
} Hash;

Hash* inicializarHash(Hash *h, int tam) {
    h->tam = tam;
    h->tabela = (Lista**) malloc(tam * sizeof(Lista*));
    for (int i = 0 ; i < tam ; i++) {
        h->tabela[i] = (Lista*) malloc(sizeof(Lista));
        inicializarLista(h->tabela[i]);
    }
}

int calcularHash(Pokemon* p, int tam) {
    int soma = 0;
    char* name = p->name;
    for (int i = 0 ; i < strlen(name) ; i++) {
        soma += (int) name[i];
    }

    return soma % tam;
}

void inserirHash(Hash *h, Pokemon* p) {
    int pos = calcularHash(p, h->tam);
    Lista* lista = h->tabela[pos];
    inserirInicio(lista, p);
}

bool pesquisarHash(Hash* h, Pokemon* p) {
    int pos = calcularHash(p, h->tam);
    Lista* lista = h->tabela[pos];
    return pesquisar(lista, p, pos);
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

    clock_t start = clock();

    Hash hash; 
    inicializarHash(&hash, 21);

    char inputId[10];
    scanf("%s", inputId);
    
    while (strcmp(inputId, "FIM") != 0) {
        int id = atoi(inputId);
        Pokemon* p = findPokemonById(pokedex, n, id);
        if(p != NULL) {
            inserirHash(&hash, p);
        }
        
        scanf("%s", inputId); 
    }

    char name[20];
    scanf("%s", name);
    while(strcmp(name, "FIM") != 0) {
        Pokemon* p = findPokemonByName(pokedex, n, name);
        printf("=> %s: ", name);

        pesquisarHash(&hash, p);

        scanf("%s", name);
    }

    clock_t end = clock();

    double executionTime = ((double)(end - start)) / CLOCKS_PER_SEC * 1000.0;

    // txt

    FILE *arquivo = fopen("843610_hash.txt", "w");
    if (arquivo == NULL) {
        printf("Erro ao abrir o arquivo!\n");
        return 1;
    }
    return 0;
}