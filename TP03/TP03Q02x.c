#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_POKEMONS 801
#define MAX_STRING 100

typedef struct {
    int id;
    char name[MAX_STRING];
    int generation;
    char description[MAX_STRING];
    char types[2][MAX_STRING];
    char abilities[10][MAX_STRING];
    int ability_count;
    double weight;
    double height;
    int captureRate;
    int isLegendary;
    char captureDate[MAX_STRING];
} Pokemon;

typedef struct {
    Pokemon array[MAX_POKEMONS];
    int n;
} Lista;

void inserirInicio(Lista* lista, Pokemon x) {
    if (lista->n >= MAX_POKEMONS) {
        printf("Erro ao inserir!\n");
        return;
    }
    for (int i = lista->n; i > 0; i--) {
        lista->array[i] = lista->array[i - 1];
    }
    lista->array[0] = x;
    lista->n++;
}

void inserirFim(Lista* lista, Pokemon x) {
    if (lista->n >= MAX_POKEMONS) {
        printf("Erro ao inserir!\n");
        return;
    }
    lista->array[lista->n++] = x;
}

void inserir(Lista* lista, Pokemon x, int pos) {
    if (lista->n >= MAX_POKEMONS || pos < 0 || pos > lista->n) {
        printf("Erro ao inserir!\n");
        return;
    }
    for (int i = lista->n; i > pos; i--) {
        lista->array[i] = lista->array[i - 1];
    }
    lista->array[pos] = x;
    lista->n++;
}

Pokemon removerInicio(Lista* lista) {
    Pokemon resp = lista->array[0];
    for (int i = 0; i < lista->n - 1; i++) {
        lista->array[i] = lista->array[i + 1];
    }
    lista->n--;
    return resp;
}

Pokemon removerFim(Lista* lista) {
    return lista->array[--lista->n];
}

Pokemon remover(Lista* lista, int pos) {
    Pokemon resp = lista->array[pos];
    for (int i = pos; i < lista->n - 1; i++) {
        lista->array[i] = lista->array[i + 1];
    }
    lista->n--;
    return resp;
}

void mostrar(Lista* lista) {
    for (int i = 0; i < lista->n; i++) {
        Pokemon* p = &lista->array[i];

        // Concatenar habilidades em uma string separada por vírgulas
        char habilidadesStr[MAX_STRING * 10] = "";  // Supondo que cada habilidade seja menor que MAX_STRING
        for (int j = 0; j < p->ability_count; j++) {
            strcat(habilidadesStr, p->abilities[j]);
            if (j < p->ability_count - 1) strcat(habilidadesStr, ", ");
        }

        // Imprimir a linha formatada
        printf("[%d] [#%d -> %s: %s - [%s, %s] - [%s] - %.1fkg - %.1fm - %d%% - %s - %d gen] - %s\n",
               i,
               p->id,
               p->name,
               p->description,
               p->types[0], 
               p->types[1][0] != '\0' ? p->types[1] : "N/A",
               habilidadesStr,
               p->weight,
               p->height,
               p->captureRate,
               p->isLegendary ? "true" : "false",
               p->generation,
               p->captureDate);
    }
}


Pokemon* buscarPorId(int id, Pokemon* pokemons, int totalPokemons) {
    for (int i = 0; i < totalPokemons; i++) {
        if (pokemons[i].id == id) {
            return &pokemons[i];
        }
    }
    return NULL;
}

int main() {
    FILE* file = fopen("/tmp/pokemon.csv", "r");
    if (file == NULL) {
        printf("Erro ao abrir o arquivo.\n");
        return 1;
    }

    Pokemon pokemons[MAX_POKEMONS];
    int totalPokemons = 0;
    char line[500];

    fgets(line, sizeof(line), file);  // Ignora o cabeçalho
    while (fgets(line, sizeof(line), file) != NULL && totalPokemons < MAX_POKEMONS) {
        Pokemon p;
        char abilitiesLine[MAX_STRING];
        char pre[5][MAX_STRING];
        char pos[5][MAX_STRING];
        
        sscanf(line, "%d,%d,%[^,],%[^,],%[^,],%[^,],%[^,],%[^,],%lf,%lf,%d,%d,%s",
               &p.id, &p.generation, p.name, p.description, pre[0], pre[1], abilitiesLine,
               &p.weight, &p.height, &p.captureRate, &p.isLegendary, p.captureDate);

        strcpy(p.types[0], pre[0]);
        strcpy(p.types[1], pre[1]);
        
        p.ability_count = 0;
        char* ability = strtok(abilitiesLine, ",");
        while (ability != NULL && p.ability_count < 10) {
            strcpy(p.abilities[p.ability_count++], ability);
            ability = strtok(NULL, ",");
        }
        
        pokemons[totalPokemons++] = p;
    }
    fclose(file);

    Lista lista;
    lista.n = 0;

    char idStr[10];
    while (scanf("%s", idStr) && strcmp(idStr, "FIM") != 0) {
        int id = atoi(idStr);
        Pokemon* p = buscarPorId(id, pokemons, totalPokemons);
        if (p != NULL) {
            inserirFim(&lista, *p);
        }
    }

    int comandos;
    scanf("%d", &comandos);
    for (int i = 0; i < comandos; i++) {
        char comando[10];
        scanf("%s", comando);
        if (strcmp(comando, "II") == 0) {
            int id;
            scanf("%d", &id);
            Pokemon* p = buscarPorId(id, pokemons, totalPokemons);
            if (p != NULL) inserirInicio(&lista, *p);
        } else if (strcmp(comando, "IF") == 0) {
            int id;
            scanf("%d", &id);
            Pokemon* p = buscarPorId(id, pokemons, totalPokemons);
            if (p != NULL) inserirFim(&lista, *p);
        } else if (strcmp(comando, "I*") == 0) {
            int pos, id;
            scanf("%d %d", &pos, &id);
            Pokemon* p = buscarPorId(id, pokemons, totalPokemons);
            if (p != NULL) inserir(&lista, *p, pos);
        } else if (strcmp(comando, "RI") == 0) {
            Pokemon p = removerInicio(&lista);
            printf("(R) %s\n", p.name);
        } else if (strcmp(comando, "RF") == 0) {
            Pokemon p = removerFim(&lista);
            printf("(R) %s\n", p.name);
        } else if (strcmp(comando, "R*") == 0) {
            int pos;
            scanf("%d", &pos);
            Pokemon p = remover(&lista, pos);
            printf("(R) %s\n", p.name);
        }
    }

    mostrar(&lista);
    return 0;
}
