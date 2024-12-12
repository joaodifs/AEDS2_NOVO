import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Pokemon {
    int id;
    int generation;
    String name;
    String description;
    ArrayList<String> types;
    ArrayList<String> abilities;
    double weight;
    double height;
    int captureRate;
    boolean isLegendary;
    LocalDate captureDate;

    public Pokemon() { }

    public Pokemon(int id, int generation, String name, 
    String description, ArrayList<String> types, ArrayList<String> abilities, double weight, 
    double height, int captureRate, boolean isLegendary, LocalDate captureDate) {
        this.id = id;
        this.generation = generation;
        this.name = name;
        this.description = description;
        this.types = types;
        this.abilities = abilities;
        this.weight = weight;
        this.height = height;
        this.captureRate = captureRate;
        this.isLegendary = isLegendary;
        this.captureDate = captureDate;
    }

    void ler(String csvLine) {
        String[] data = csvLine.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
        
        this.id = Integer.parseInt(data[0]);
        this.generation = Integer.parseInt(data[1]);
        this.name = data[2];
        this.description = data[3];

        // types
        ArrayList<String> typesList = new ArrayList<>();
        typesList.add(data[4]);
        if (!data[5].isEmpty()) typesList.add(data[5]);
        this.types = typesList;

        // abilities
        String abilitiesStr = data[6].replace("[", "").replace("]", "").replace("'", "").trim();
        setAbilities(abilitiesStr);

        // weight
        if (!data[7].isEmpty()) {
            this.weight = Double.parseDouble(data[7]);
        } else {
            this.weight = 0;
        }

        // height
        if (!data[8].isEmpty()) {
            this.height = Double.parseDouble(data[8]);
        } else {
            this.height = 0;
        }

        // captureRate
        if (!data[9].isEmpty()) {
            this.captureRate = Integer.parseInt(data[9]);
        } else {
            this.captureRate = 0;
        }

        this.isLegendary = data[10].equals("1") || data[10].equalsIgnoreCase("true");

        // captureDate
        LocalDate date = parseDate(data[11]);
        this.captureDate = date;
    }

    private void setAbilities(String abilities) {
        abilities = abilities.replaceAll("[\\[\\]\"']", "").trim();
        this.abilities = new ArrayList<>(Arrays.asList(abilities.split(",\\s*")));
    }

    private LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateStr, formatter);
    }

    String imprimir() {
        StringBuilder sb = new StringBuilder();
        sb.append("[#");
        sb.append(id).append(" -> ");
        sb.append(name).append(": ");
        sb.append(description).append(" - ['");

        // types
        if (types.size() > 0) {
            sb.append(types.get(0));
        }
        sb.append("'");

        if (types.size() > 1) {
            sb.append(", '");
            sb.append(types.get(1)).append("'"); 
        }
        sb.append("] - ");

        // abilities
        sb.append("[");
        for (int i = 0; i < abilities.size(); i++) {
            sb.append("'").append(abilities.get(i)).append("'");
            if (i < abilities.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("] - ");

        sb.append(weight).append("kg - ");
        sb.append(height).append("m - ");
        sb.append(captureRate).append("% - ");
        sb.append(isLegendary ? "true" : "false").append(" - ");
        sb.append(generation).append(" gen] - ");
        sb.append(captureDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        return sb.toString();
    }

}

class Celula { 
    Pokemon pokemon;
    Celula prox;
    
    public Celula(Pokemon pokemon) {
        this.pokemon = pokemon;
        this.prox = null;
    }
}

class Pilha {
    private Celula topo;
    private int n;

    public Pilha() {
        topo = null;
        n = 0;
    }

    public void inserir(Pokemon pokemon) {
        Celula tmp = new Celula(pokemon);
        tmp.prox = topo;
        topo = tmp;
        tmp = null;
        n++;
    }  

    public Pokemon remover(){
        if (topo == null) {
            return null;
        }
        Pokemon pokemonRemovido = topo.pokemon;
        Celula tmp = topo;
        topo = topo.prox;
        tmp.prox = null;
        tmp = null;

        n--;

        return pokemonRemovido;
    }

    public void imprimirPilhaRec(Celula topo, int pos) {
        if (topo != null) {
            imprimirPilhaRec(topo.prox, pos - 1);
            System.out.print("[" + pos + "] ");
            System.out.println(topo.pokemon.imprimir());
        }
    }

    public void imprimirPokemon() {
        imprimirPilhaRec(topo, n - 1);
    }
}

public class TP03Q09 {
    public static void main(String[] args) {
        String csvPath = "/tmp/pokemon.csv";
        ArrayList<Pokemon> pokedex = new ArrayList<>();
        Pilha pokemonPilha = new Pilha();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            br.readLine(); 

            while (br.ready()) {
                String linha = br.readLine();
                Pokemon p = new Pokemon();
                p.ler(linha);
                pokedex.add(p);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Erro: Arquivo não encontrado em " + csvPath);
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo CSV: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);

        String idStr = sc.nextLine();
        while (!idStr.equals("FIM")) {
            int id = Integer.parseInt(idStr);
            Pokemon p = pokedex.get(id - 1);
            if (p != null) {
                pokemonPilha.inserir(p);
            }
            idStr = sc.nextLine();
        }

        int n = Integer.parseInt(sc.nextLine());
        
        for (int i = 0 ; i < n ; i++) {
            String line = sc.nextLine();
            // divide por espaco
            String[] tokens = line.split(" ");
            // primeira parte (o comando) para saber oq sera executado
            String comando = tokens[0];

            if (comando.equals("I")) {
                int id = Integer.parseInt(tokens[1]); // segunda parte recebe o id do pokemon
                Pokemon p = pokedex.get(id - 1); 
                if (p != null) {
                    pokemonPilha.inserir(p);
                }
            } else if (comando.equals("R")) {
                Pokemon p = pokemonPilha.remover();
                if (p != null) {
                    System.out.println("(R) " + p.name); // Imprime o nome do Pokémon removido
                }
            }
        }

        pokemonPilha.imprimirPokemon();

        sc.close();
    }
}
