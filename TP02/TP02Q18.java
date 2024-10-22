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

    // Constructor for the Pokemon class
    Pokemon(String[] atributosPre, String[] atributosPos, String abilitiesLine) throws Exception {
        this.id = Integer.parseInt(atributosPre[0]);
        this.generation = Integer.parseInt(atributosPre[1]);
        this.name = atributosPre[2];
        this.description = atributosPre[3];

        // Convert strings separated by commas to ArrayList
        if (atributosPre[5].equals("")) {
            this.types = new ArrayList<>(Arrays.asList(("'" + atributosPre[4] + "'" + "," + atributosPre[5]).split(",")));
        } else {
            this.types = new ArrayList<>(Arrays.asList(("'" + atributosPre[4] + "'" + "," + "'" + atributosPre[5] + "'").split(",")));
        }
        this.abilities = new ArrayList<>(Arrays.asList(abilitiesLine.split(",")));
        if (atributosPos[1].equals("")) {
            this.weight = 0.0;
        } else {
            this.weight = Double.parseDouble(atributosPos[1]);
        }
        if (atributosPos[2].equals("")) {
            this.height = 0.0;
        } else {
            this.height = Double.parseDouble(atributosPos[2]);
        }
        this.captureRate = Integer.parseInt(atributosPos[3]);

        // Check if legendary
        this.isLegendary = atributosPos[4].equals("1");
        this.captureDate = atributosPos[5];
    }

    // Method to search for a Pokémon by ID
    public static Pokemon searchById(int id, Pokemon[] pokemon) {
        for (int i = 0; i < pokemon.length; i++) {
            if (pokemon[i].id == id) {
                return pokemon[i];
            }
        }
        return null;
    }
}

class TP02Q18 {
    // Method to check if the input is "FIM"
    public static boolean isFim(String id) {
        return id.equals("FIM");
    }

    // Partial selection sort by Pokémon name
    public static Pokemon[] selecao(Pokemon[] array, int n) {
        for (int i = 0; i < (n - 1); i++) {
            int menor = i;
            for (int j = (i + 1); j < n; j++) {
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

    // Partial quicksort implementation
    public static void quicksort(Pokemon[] array, int esq, int dir, int k) {
        int i = esq, j = dir;
        Pokemon pivot = array[(esq + dir) / 2];

        while (i <= j) {
            while (i <= dir && (array[i] != null && 
                (array[i].generation < pivot.generation || 
                (array[i].generation == pivot.generation && array[i].name.compareTo(pivot.name) < 0)))) {
                i++;
            }
            while (j >= esq && (array[j] != null && 
                (array[j].generation > pivot.generation || 
                (array[j].generation == pivot.generation && array[j].name.compareTo(pivot.name) > 0)))) {
                j--;
            }
            if (i <= j) {
                swap(array, i, j);
                i++;
                j--;
            }
        }

        if (esq < j) {
            quicksort(array, esq, j, k);
        }
        if (i < k && i < dir) {
            quicksort(array, i, dir, k);
        }
    }

    private static void swap(Pokemon[] array, int i, int j) {
        Pokemon temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("/tmp/pokemon.csv", "r"); 
        file.readLine(); // Skip the header line
        Pokemon[] pokemon = new Pokemon[801]; 

        // Reading data from the CSV file
        for (int i = 0; i < 801; i++) {
            String line = file.readLine();
            String[] atributosTmp = line.split("\\[|\\]");
            String abilitiesLine = atributosTmp[1].trim();

            String[] atributosPre = atributosTmp[0].split(","); 
            String[] atributosPos = atributosTmp[2].split(","); 

            pokemon[i] = new Pokemon(atributosPre, atributosPos, abilitiesLine);
        }
        file.close();

        String id = "";
        id = MyIO.readLine();
        Pokemon[] selectedPokemons = new Pokemon[60];
        int pos = 0;

        while (!isFim(id)) {
            selectedPokemons[pos++] = Pokemon.searchById(Integer.parseInt(id), pokemon);
            id = MyIO.readLine();
        }

        // Sort selected Pokémon by generation and name
        quicksort(selectedPokemons, 0, pos - 1, 10); // Sort first 10 elements

        // Print the sorted Pokémon
        for (int i = 0; i < 10; i++) {
            if (selectedPokemons[i] != null) {
                MyIO.println("[#" + selectedPokemons[i].id + " -> " + selectedPokemons[i].name + ": " + selectedPokemons[i].description +
                        " - " + selectedPokemons[i].types + " - " + selectedPokemons[i].abilities + 
                        " - " + selectedPokemons[i].weight + "kg - " + selectedPokemons[i].height +
                        "m - " + selectedPokemons[i].captureRate + "% - " + selectedPokemons[i].isLegendary + 
                        " - " + selectedPokemons[i].generation + " gen] - " + selectedPokemons[i].captureDate);
            }
        }
    }
}
