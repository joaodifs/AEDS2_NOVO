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
        for (int i = 0; i < 801; i++) {
            if (pokemon[i].id == id) {
                return pokemon[i];
            }
        }
        return null;
    }
}

class TP02Q15 {
    // Função para verificar se a entrada é "FIM"
    public static boolean isFim(String id) {
        return id.equals("FIM");
    }

    public static Pokemon[] selecaoParcial(Pokemon[] array, int n, int k) {
        // Limita k ao número de elementos presentes (n)
        k = Math.min(k, n);
        for (int i = 0; i < k; i++) {
            int menor = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j] != null && array[menor] != null) {
                    if (array[menor].name.compareTo(array[j].name) > 0) {
                        menor = j;
                    }
                }
            }
            if (array[menor] != null && array[i] != null) {
                Pokemon temp = array[menor];
                array[menor] = array[i];
                array[i] = temp;
            }
        }
        return array;
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

        // Ordenação parcial com k = 10
        pokemon1 = selecaoParcial(pokemon1, pos, 10);

        // Exibir a saída
        for (int i = 0; i < 10; i++) {
            if (pokemon1[i] != null) {
                MyIO.println("[#" + pokemon1[i].id + " -> " + pokemon1[i].name + ": " + pokemon1[i].description +
                        " - " + pokemon1[i].types + " - " + pokemon1[i].abilities + " - " + pokemon1[i].weight + "kg - " +
                        pokemon1[i].height + "m - " + pokemon1[i].captureRate + "% - " + pokemon1[i].isLegendary + " - " +
                        pokemon1[i].generation + " gen] - " + pokemon1[i].captureDate);
            }
        }
    }
}
