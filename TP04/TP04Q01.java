import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Pokemon {
    public int id;
    public int generation;
    public String name;
    public String description;
    public ArrayList<String> types;
    public ArrayList<String> abilities;
    public double weight;
    public double height;
    public int captureRate;
    public boolean isLegendary;
    public LocalDate captureDate;

    public Pokemon() {}

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

    public void ler(String csvLine) {
        String[] data = csvLine.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");

        this.id = Integer.parseInt(data[0]);
        this.generation = Integer.parseInt(data[1]);
        this.name = data[2];
        this.description = data[3];

        // types
        this.types = new ArrayList<>();
        this.types.add(data[4]);
        if (!data[5].isEmpty()) this.types.add(data[5]);

        // abilities
        String abilitiesStr = data[6].replace("[", "").replace("]", "").replace("'", "").trim();
        this.abilities = new ArrayList<>(Arrays.asList(abilitiesStr.split(",\\s*")));

        // weight
        this.weight = data[7].isEmpty() ? 0 : Double.parseDouble(data[7]);

        // height
        this.height = data[8].isEmpty() ? 0 : Double.parseDouble(data[8]);

        // captureRate
        this.captureRate = data[9].isEmpty() ? 0 : Integer.parseInt(data[9]);

        this.isLegendary = data[10].equals("1") || data[10].equalsIgnoreCase("true");

        // captureDate
        this.captureDate = parseDate(data[11]);
    }

    private LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateStr, formatter);
    }

    public String imprimir() {
        StringBuilder sb = new StringBuilder();
        sb.append("[#").append(this.id).append(" -> ");
        sb.append(this.name).append(": ");
        sb.append(this.description).append(" - ['");

        // types
        if (this.types.size() > 0) {
            sb.append(this.types.get(0));
        }
        sb.append("'");
        if (this.types.size() > 1) {
            sb.append(", '").append(this.types.get(1)).append("'");
        }
        sb.append("] - ");

        // abilities
        sb.append("[");
        for (int i = 0; i < this.abilities.size(); i++) {
            sb.append("'").append(this.abilities.get(i)).append("'");
            if (i < this.abilities.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("] - ");

        sb.append(this.weight).append("kg - ");
        sb.append(this.height).append("m - ");
        sb.append(this.captureRate).append("% - ");
        sb.append(this.isLegendary ? "true" : "false").append(" - ");
        sb.append(this.generation).append(" gen] - ");
        sb.append(this.captureDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        return sb.toString();
    }

    public static Pokemon findPokemonByName(ArrayList<Pokemon> pokedex, String name) {
        for (Pokemon p : pokedex) {
            if (p.name.equals(name)) {
                return p;
            }
        }

        return null;
    }
}

class No {
    public Pokemon pokemon;
    public No esq;
    public No dir;

    public No(Pokemon pokemon) {
        this(pokemon, null, null);
    }

    public No(Pokemon pokemon, No esq, No dir) {
        this.pokemon = pokemon;
        this.esq = esq;
        this.dir = dir;
    }
}

class ArvoreBinaria {
    private No raiz;

    public ArvoreBinaria() {
        raiz = null;
    }

    public boolean pesquisar(String name) {
        return pesquisar(name, raiz);
    }

    private boolean pesquisar(String name, No i) {
        boolean resp;
        if (i == null) {
            resp = false;
        } else if (name.compareTo(i.pokemon.name) == 0) {
            resp = true;
        } else if (name.compareTo(i.pokemon.name) < 0) {
            System.out.print("esq ");
            resp = pesquisar(name, i.esq);
        } else {
            System.out.print("dir ");
            resp = pesquisar(name, i.dir);
        }

        return resp;
    }

    public void inserir(Pokemon p) {
        raiz = inserir(p, raiz);
    }

    private No inserir(Pokemon p, No i) {
        if (i == null) {
            i = new No(p);
        } else if (p.name.compareTo(i.pokemon.name) < 0) {
            i.esq = inserir(p, i.esq);
        } else if (p.name.compareTo(i.pokemon.name) > 0) {
            i.dir = inserir(p, i.dir);
        }

        return i;
    }
}

public class TP04Q01 {
    public static void main(String[] args) {
        String csvPath = "/tmp/pokemon.csv";
        ArrayList<Pokemon> pokedex = new ArrayList<>();
        ArvoreBinaria pokemonTree = new ArvoreBinaria();

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
                pokemonTree.inserir(p);
            }
            idStr = sc.nextLine();
        }

        String name = sc.nextLine();
        while (!name.equals("FIM")) {
            Pokemon p = Pokemon.findPokemonByName(pokedex, name);

            System.out.println("=> "+p.name);
            System.out.print("raiz ");

            boolean found = pokemonTree.pesquisar(name);

            System.out.println(found ? "SIM" : "NAO");

            name = sc.nextLine();
        }

        sc.close();
    }
}
