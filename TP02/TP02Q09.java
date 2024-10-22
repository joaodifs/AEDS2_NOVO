import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;

class Pokemon {
    int id;
    String name;
    int generation;
    String description;
    ArrayList<String> types;
    ArrayList<String> abilities;
    double weight;
    double height;
    int captureRate;
    boolean isLegendary;
    String captureDate;

    // Construtor da classe Pokemon
    Pokemon(String[] atributosPre, String[] atributosPos, String abilitiesLine) throws Exception {
        this.id = Integer.parseInt(atributosPre[0]);
        this.generation = Integer.parseInt(atributosPre[1]);
        this.name = atributosPre[2];
        this.description = atributosPre[3];

        // Convertendo strings separadas por vírgulas para ArrayList
        if (atributosPre[5].equals("")) {
            this.types = new ArrayList<>(Arrays.asList(("'" + atributosPre[4] + "'" + "," + atributosPre[5]).split(",")));
        } else {
            this.types = new ArrayList<>(Arrays.asList(("'" + atributosPre[4] + "'" + "," + "'" + atributosPre[5] + "'").split(",")));
        }
        this.abilities = new ArrayList<>(Arrays.asList(abilitiesLine.split(",")));

        this.weight = atributosPos[1].equals("") ? 0.0 : Double.parseDouble(atributosPos[1]);
        this.height = atributosPos[2].equals("") ? 0.0 : Double.parseDouble(atributosPos[2]);
        this.captureRate = Integer.parseInt(atributosPos[3]);
        this.isLegendary = atributosPos[4].equals("1");
        this.captureDate = atributosPos[5];
    }

    // Método para buscar um Pokémon pelo ID
    public static Pokemon searchById(int id, Pokemon[] pokemon) {
        for (Pokemon p : pokemon) {
            if (p.id == id) {
                return p;
            }
        }
        return null;
    }
}

class TP02Q09 {
    // Função para verificar se a entrada é "FIM"
    public static boolean isFim(String id) {
        return id.equals("FIM");
    }

    public static Pokemon[] heapSort(int tam, Pokemon[] pokemons) {
        // Construir o heap inicial
        for (int i = tam / 2 - 1; i >= 0; i--) {
            heapify(pokemons, tam, i);
        }

        // Extrair elementos um por um do heap
        for (int i = tam - 1; i > 0; i--) {
            // Mover a raiz atual para o final do array
            Pokemon temp = pokemons[0];
            pokemons[0] = pokemons[i];
            pokemons[i] = temp;

            // Chamar max heapify no heap reduzido
            heapify(pokemons, i, 0);
        }

        return pokemons;
    }

    public static void heapify(Pokemon[] pokemons, int tam, int indiceRaiz) {
        int maior = indiceRaiz;
        int filhoEsquerda = 2 * indiceRaiz + 1;
        int filhoDireita = 2 * indiceRaiz + 2;

        // Se o filho esquerdo é maior que a raiz
        if (filhoEsquerda < tam && (pokemons[filhoEsquerda].height > pokemons[maior].height || 
            (pokemons[filhoEsquerda].height == pokemons[maior].height && 
            pokemons[filhoEsquerda].name.compareTo(pokemons[maior].name) > 0))) { // Para ordem crescente de nome
            maior = filhoEsquerda;
        }

        // Se o filho direito é maior que a raiz
        if (filhoDireita < tam && (pokemons[filhoDireita].height > pokemons[maior].height || 
            (pokemons[filhoDireita].height == pokemons[maior].height && 
            pokemons[filhoDireita].name.compareTo(pokemons[maior].name) > 0))) { // Para ordem crescente de nome
            maior = filhoDireita;
        }

        // Se a raiz não é a maior, trocar com o filho maior
        if (maior != indiceRaiz) {
            Pokemon temp = pokemons[indiceRaiz];
            pokemons[indiceRaiz] = pokemons[maior];
            pokemons[maior] = temp;

            // Recursivamente heapify a subárvore afetada
            heapify(pokemons, tam, maior);
        }
    }

    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("/tmp/pokemon.csv", "r"); // Para mandar no verde é "/tmp/pokemon.csv"
        file.readLine(); // Pular a primeira linha que não é útil
        Pokemon[] pokemon = new Pokemon[801]; // Criando um array de Pokémon para guardar todos

        // Lendo os dados do arquivo e criando os objetos de Pokemon
        for (int i = 0; i < 801; i++) {
            String line = file.readLine();
            String[] atributosTmp = line.split("\\[|\\]"); // Separando as linhas pelo colchete
            String abilitiesLine = atributosTmp[1];
            abilitiesLine = abilitiesLine.replaceAll(", ", ","); // Para não ficar com espaço duplo na hora de criar a lista com abilities

            String[] atributosPre = atributosTmp[0].split(","); // Atributos de Pokémon
            String[] atributosPos = atributosTmp[2].split(","); // Atributos de Pokémon

            pokemon[i] = new Pokemon(atributosPre, atributosPos, abilitiesLine);
        }
        file.close();

        String id = MyIO.readLine();
        Pokemon[] pokemon1 = new Pokemon[60];
        int pos = 0;

        while (!isFim(id)) {
            pokemon1[pos++] = Pokemon.searchById(Integer.parseInt(id), pokemon);
            id = MyIO.readLine();
        }

        // Ordenação usando HeapSort
        pokemon1 = Arrays.copyOf(pokemon1, pos); // Reduzir o tamanho do array
        pokemon1 = heapSort(pokemon1.length, pokemon1);

        // Exibir a saída
        for (int i = 0; i < pokemon1.length; i++) { // Mudança aqui
            if (pokemon1[i] != null) {
                MyIO.println("[#" + pokemon1[i].id + " -> " + pokemon1[i].name + ": " + pokemon1[i].description +
                        " - " + pokemon1[i].types + " - " + pokemon1[i].abilities + " - " + pokemon1[i].weight + "kg - " + 
                        pokemon1[i].height + "m - " + pokemon1[i].captureRate + "% - " + pokemon1[i].isLegendary + " - " + 
                        pokemon1[i].generation + " gen] - " + pokemon1[i].captureDate);
            }
        }
    }
}
