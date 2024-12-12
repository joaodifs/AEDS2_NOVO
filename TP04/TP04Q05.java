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

    public Pokemon () { }

    public Pokemon (int id, int generation, String name, 
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

    void ler (String csvLine) {
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
        this.abilities = new ArrayList<>(Arrays.asList(abilitiesStr.split(",\\s*")));

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
            this.height = 0; // Define 0 ou outro valor padrão se o campo estiver vazio
        }

        // captureRate
        if (!data[9].isEmpty()) {
            this.captureRate = Integer.parseInt(data[9]);
        } else {
            this.captureRate = 0; // Define um valor padrão se o campo estiver vazio
        }

        this.isLegendary = data[10].equals("1") || data[10].equalsIgnoreCase("true");

        // captureDate
        LocalDate date = parseDate(data[11]);
        this.captureDate = date;
    }

    private LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateStr, formatter);
    }

    String imprimir () {
        StringBuilder sb = new StringBuilder();
        sb.append("[#");
        sb.append(this.id).append(" -> ");
        sb.append(this.name).append(": ");
        sb.append(this.description).append(" - ['");

        // types
        if (this.types.size() > 0) {
            sb.append(this.types.get(0));
        }
        sb.append("'");

        if (this.types.size() > 1) {
            sb.append(", '");
            sb.append(this.types.get(1)).append("'");

        }
        sb.append("] - ");

        // abilities
        sb.append("[");
        for (int i = 0 ; i < this.abilities.size() ; i++) {
            sb.append("'");
            sb.append(this.abilities.get(i));
            sb.append("'");
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

class Hash {
    Pokemon tabela[];
    int m1, m2, m, reserva;

    public Hash() {
        this(21, 9);
    }

    public Hash(int m1, int m2) {
        this.m1 = m1;
        this.m2 = m2;
        this.m = m1 + m2;
        this.tabela = new Pokemon[this.m];
        for (int i = 0 ; i < m1 ; i++) {
            tabela[i] = null;
        }
        reserva = 0;
    }

    public int calcularHash(Pokemon p) {
        int soma = 0;
        for (int i = 0 ; i < p.name.length() ; i++) {
            soma += (int)p.name.charAt(i);
        }
        return soma % m1;
    }

    public boolean inserir(Pokemon p) {
        boolean resp = false;
        if (p != null) {
            int pos = calcularHash(p);
            if(tabela[pos] == null) {
                tabela[pos] = p;
                resp = true;
            } else if (reserva < m2) {
                tabela[m1 + reserva] = p;
                reserva++;
                resp = true;
            }
        }

        return resp;
    }

    public boolean pesquisar(Pokemon p) {
        boolean resp = false;
        int pos = calcularHash(p);
        if (tabela[pos] == p) {
            System.out.println("(Posicao: " + pos + ") SIM");
            resp = true;
        } else if (tabela[pos] != null) {
            for (int i = 0 ; i < reserva ; i++) {
                if (tabela[m1 + i] == p) {
                    pos = m1 + i;
                    System.out.println("(Posicao: " + pos + ") SIM");
                    resp = true;
                    i = reserva;
                }
            }
            if (!resp) {
                System.out.println("NAO");
            }
        } 

        return resp;
    }
}

public class TP04Q05 {
    public static void main(String[] args) {
        String csvPath = "/tmp/pokemon.csv";
        ArrayList<Pokemon> pokedex = new ArrayList<Pokemon>();
        Hash hashDireta = new Hash();

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
                hashDireta.inserir(p);
            }
            idStr = sc.nextLine();
        }

        String name = sc.nextLine();
        while (!name.equals("FIM")) {
            Pokemon p = Pokemon.findPokemonByName(pokedex, name);
            System.out.print("=> " + p.name + ": ");

            boolean found = hashDireta.pesquisar(p);

            name = sc.nextLine();
        }

        sc.close();
    }
}
