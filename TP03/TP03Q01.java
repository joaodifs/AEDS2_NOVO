import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

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

        // Verificando se é lendário
        this.isLegendary = atributosPos[4].equals("1");
        this.captureDate = atributosPos[5];  // Captura a data como String
    }

    // Método para buscar um Pokémon pelo ID
    public static Pokemon searchById(int id, Pokemon[] pokemonArray) {
        for (Pokemon pokemon : pokemonArray) {
            if (pokemon != null && pokemon.id == id) return pokemon;
        }
        return null;
    }
}

class Lista {
    private Pokemon[] array;
    private int n;

    public Lista(int tamanho) {
        array = new Pokemon[tamanho];
        n = 0;
    }

    public void inserirInicio(Pokemon x) throws Exception {
        if (n >= array.length) throw new Exception("Erro ao inserir!");
        for (int i = n; i > 0; i--) array[i] = array[i - 1];
        array[0] = x;
        n++;
    }

    public void inserirFim(Pokemon x) throws Exception {
        if (n >= array.length) throw new Exception("Erro ao inserir!");
        array[n++] = x;
    }

    public void inserir(Pokemon x, int pos) throws Exception {
        if (n >= array.length || pos < 0 || pos > n) throw new Exception("Erro ao inserir!");
        for (int i = n; i > pos; i--) array[i] = array[i - 1];
        array[pos] = x;
        n++;
    }

    public Pokemon removerInicio() throws Exception {
        if (n == 0) throw new Exception("Erro ao remover!");
        Pokemon resp = array[0];
        for (int i = 0; i < n - 1; i++) array[i] = array[i + 1];
        n--;
        return resp;
    }

    public Pokemon removerFim() throws Exception {
        if (n == 0) throw new Exception("Erro ao remover!");
        return array[--n];
    }

    public Pokemon remover(int pos) throws Exception {
        if (n == 0 || pos < 0 || pos >= n) throw new Exception("Erro ao remover!");
        Pokemon resp = array[pos];
        for (int i = pos; i < n - 1; i++) array[i] = array[i + 1];
        n--;
        return resp;
    }

    public void mostrar() {
        for (int i = 0; i < n; i++) {
            Pokemon pokemon1 = array[i];
            if (pokemon1 != null) {
                MyIO.println("["+i+"] [#" + pokemon1.id + " -> " + pokemon1.name + ": " + pokemon1.description +
                    " - " + pokemon1.types + " - " + pokemon1.abilities + " - " + pokemon1.weight + "kg - " + pokemon1.height +
                    "m - " + pokemon1.captureRate + "% - "+pokemon1.isLegendary+ 
                    " - " + pokemon1.generation + " gen] - " + pokemon1.captureDate);
            }
        }
    }
    
}

class TP03Q01 {
    public static boolean isFim(String id) {
        return id.equals("FIM");
    }

    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("/tmp/pokemon.csv", "r"); // "/tmp/pokemon.csv" for online judge
        file.readLine(); // Skip header line
        Pokemon[] pokemonArray = new Pokemon[801]; // Array to store all Pokémon

        for (int i = 0; i < 801; i++) {
            String line = file.readLine();
            String[] splitLine = line.split("\\[|\\]");
            String abilitiesLine = splitLine[1].replaceAll(", ", ",");
            String[] atributosPre = splitLine[0].split(",");
            String[] atributosPos = splitLine[2].split(",");
            pokemonArray[i] = new Pokemon(atributosPre, atributosPos, abilitiesLine);
        }
        file.close();

        Scanner scanner = new Scanner(System.in);
        Lista lista = new Lista(801);
        String idStr = "";

        // Initial insertion loop
        while (!(idStr = scanner.nextLine()).equals("FIM")) {
            int id = Integer.parseInt(idStr);
            Pokemon pokemon = Pokemon.searchById(id, pokemonArray);
            if (pokemon != null) lista.inserirFim(pokemon);
        }

        // Commands
        int commands = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < commands; i++) {
            String[] command = scanner.nextLine().split(" ");
            Pokemon pokemon;
            switch (command[0]) {
                case "II":
                    pokemon = Pokemon.searchById(Integer.parseInt(command[1]), pokemonArray);
                    lista.inserirInicio(pokemon);
                    break;
                case "IF":
                    pokemon = Pokemon.searchById(Integer.parseInt(command[1]), pokemonArray);
                    lista.inserirFim(pokemon);
                    break;
                case "I*":
                    pokemon = Pokemon.searchById(Integer.parseInt(command[2]), pokemonArray);
                    lista.inserir(pokemon, Integer.parseInt(command[1]));
                    break;
                case "RI":
                    System.out.println("(R) " + lista.removerInicio().name);
                    break;
                case "RF":
                    System.out.println("(R) " + lista.removerFim().name);
                    break;
                case "R*":
                    System.out.println("(R) " + lista.remover(Integer.parseInt(command[1])).name);
                    break;
            }
        }

        // Display list
        lista.mostrar();
        scanner.close();
    }
}
