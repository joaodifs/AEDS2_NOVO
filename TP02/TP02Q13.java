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

class TP02Q13 {
    // Função para verificar se a entrada é "FIM"
    public static boolean isFim(String id) {
        return id.equals("FIM");
    }

    public static void mergeSort(Pokemon[] pokemons, int esq, int dir) {
        if (esq < dir) {
            int meio = (esq + dir) / 2;
            mergeSort(pokemons, esq, meio);
            mergeSort(pokemons, meio + 1, dir);
            intercalar(pokemons, esq, meio, dir);
        }
    }

    public static void intercalar(Pokemon[] pokemons, int esq, int meio, int dir) {
        int nEsq = meio - esq + 1;
        int nDir = dir - meio;
        
        Pokemon[] arrayEsq = new Pokemon[nEsq];
        Pokemon[] arrayDir = new Pokemon[nDir];

        // Copiando para os subarrays
        for (int i = 0; i < nEsq; i++) {
            arrayEsq[i] = pokemons[esq + i];
        }
        for (int i = 0; i < nDir; i++) {
            arrayDir[i] = pokemons[meio + 1 + i];
        }

        int iEsq = 0, iDir = 0, i = esq;

        // Intercalando os subarrays
        while (iEsq < nEsq && iDir < nDir) {
            // Comparar tipos e desempatar pelo nome
            String typeEsq = arrayEsq[iEsq].types.get(0);
            String typeDir = arrayDir[iDir].types.get(0);
            if (typeEsq.compareTo(typeDir) < 0) {
                pokemons[i++] = arrayEsq[iEsq++];
            } else if (typeEsq.compareTo(typeDir) > 0) {
                pokemons[i++] = arrayDir[iDir++];
            } else {
                // Empate no tipo, então comparar pelos nomes
                if (arrayEsq[iEsq].name.compareTo(arrayDir[iDir].name) < 0) {
                    pokemons[i++] = arrayEsq[iEsq++];
                } else {
                    pokemons[i++] = arrayDir[iDir++];
                }
            }
        }

        // Copiando os restantes
        while (iEsq < nEsq) {
            pokemons[i++] = arrayEsq[iEsq++];
        }
        while (iDir < nDir) {
            pokemons[i++] = arrayDir[iDir++];
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

        // Reduzir o tamanho do array
        pokemon1 = Arrays.copyOf(pokemon1, pos);

        // Ordenação usando Merge Sort
        mergeSort(pokemon1, 0, pokemon1.length - 1);

        // Exibir a saída
        for (Pokemon p : pokemon1) {
            if (p != null) {
                MyIO.println("[#" + p.id + " -> " + p.name + ": " + p.description +
                        " - " + p.types + " - " + p.abilities + " - " + p.weight + "kg - " +
                        p.height + "m - " + p.captureRate + "% - " + p.isLegendary + " - " +
                        p.generation + " gen] - " + p.captureDate);
            }
        }
    }
}
