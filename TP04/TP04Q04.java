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
                   String description, ArrayList<String> types, ArrayList<String> abilities, 
                   double weight, double height, int captureRate, boolean isLegendary, LocalDate captureDate) {
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
        this.weight = !data[7].isEmpty() ? Double.parseDouble(data[7]) : 0;

        // height
        this.height = !data[8].isEmpty() ? Double.parseDouble(data[8]) : 0;

        // captureRate
        this.captureRate = !data[9].isEmpty() ? Integer.parseInt(data[9]) : 0;

        this.isLegendary = data[10].equals("1") || data[10].equalsIgnoreCase("true");

        // captureDate
        this.captureDate = parseDate(data[11]);
    }

    private LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateStr, formatter);
    }

    private void setAbilities(String abilities) {
        abilities = abilities.replaceAll("[\\[\\]\"']", "").trim();
        this.abilities = new ArrayList<>(Arrays.asList(abilities.split(",\\s*")));
    }

    String imprimir() {
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
        for (int i = 0; i < this.abilities.size(); i++) {
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

class No {
    public Pokemon pokemon;
    public No esq;
    public No dir;
    public boolean cor;

    public No(Pokemon pokemon) {
        this(pokemon, null, null, false);
    }

    public No(Pokemon pokemon, boolean cor) {
        this(pokemon, null, null, cor);
    }

    public No(Pokemon pokemon, No esq, No dir, boolean cor) {
        this.pokemon = pokemon;
        this.esq = esq;
        this.dir = dir;
        this.cor = cor;
    }
}

class ArvoreAlvinegra {
    private No raiz;

    public ArvoreAlvinegra() {
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
        if (raiz == null) {
            raiz = new No(p, false);
        } else if (raiz.esq == null && raiz.dir == null) {
            if (p.name.compareTo(raiz.pokemon.name) < 0) {
                raiz.esq = new No(p);
            } else {
                raiz.dir = new No(p);
            }
        } else if (raiz.esq == null) {
            if (p.name.compareTo(raiz.pokemon.name) < 0) {
                raiz.esq = new No(p);
            } else if (p.name.compareTo(raiz.dir.pokemon.name) < 0) {
                raiz.esq = new No(raiz.pokemon);
                raiz.pokemon = p;
            } else {
                raiz.esq = new No(raiz.pokemon);
                raiz.pokemon = raiz.dir.pokemon;
                raiz.dir.pokemon = p;
            }
            raiz.esq.cor = raiz.dir.cor = false;
        } else if (raiz.dir == null) {
            if (p.name.compareTo(raiz.pokemon.name) > 0) {
                raiz.dir = new No(p);
            } else if (p.name.compareTo(raiz.esq.pokemon.name) > 0) {
                raiz.dir = new No(raiz.pokemon);
                raiz.pokemon = p;
            } else {
                raiz.dir = new No(raiz.pokemon);
                raiz.pokemon = raiz.esq.pokemon;
                raiz.esq.pokemon = p;
            }
            raiz.esq.cor = raiz.dir.cor = false;
        } else {
            inserir(p, null, null, null, raiz);
        }
        raiz.cor = false;
    }

    private void inserir(Pokemon p, No bisavo, No avo, No pai, No i) {
        if (i == null) {
            if (p.name.compareTo(pai.pokemon.name) < 0) {
                i = pai.esq = new No(p, true);
            } else {
                i = pai.dir = new No(p, true);
            }
            if (pai.cor == true && avo != null) {
                balancear(bisavo, avo, pai, i);
            } 
        } else {
            if (i.esq != null && i.dir != null && i.esq.cor && i.dir.cor) {
                i.cor = true;
                i.esq.cor = i.dir.cor = false;
                if (i == raiz) {
                    i.cor = false;
                } else if (pai.cor == true && avo != null) {
                    balancear(bisavo, avo, pai, i);
                }
            }
            if (p.name.compareTo(i.pokemon.name) < 0) {
                inserir(p, avo, pai, i, i.esq);
            } else if (p.name.compareTo(i.pokemon.name) > 0) {
                inserir(p, avo, pai, i, i.dir);
            }
        }
    }

    private void balancear(No bisavo, No avo, No pai, No i) {
        if (avo != null && pai != null && pai.cor == true) {
            if (pai.pokemon.name.compareTo(avo.pokemon.name) < 0) {
                if (i.pokemon.name.compareTo(pai.pokemon.name) < 0) {
                    avo = rotacaoDir(avo);
                } else {
                    avo = rotacaoEsqDir(avo);
                }
            } else {
                if (i.pokemon.name.compareTo(pai.pokemon.name) > 0) {
                    avo = rotacaoEsq(avo);
                } else {
                    avo = rotacaoDirEsq(avo);
                }
            }
            if (bisavo == null) {
                raiz = avo;
            } else if (avo.pokemon.name.compareTo(bisavo.pokemon.name) < 0) {
                bisavo.esq = avo;
            } else {
                bisavo.dir = avo;
            }

            avo.cor = false;
            if (avo.esq != null) avo.esq.cor = true;
            if (avo.dir != null) avo.dir.cor = true;
        }
    }

    private No rotacaoDir(No no) {
        No noEsq = no.esq;
        No noEsqDir = noEsq.dir;

        noEsq.dir = no;
        no.esq = noEsqDir;

        return noEsq;
    }

    private No rotacaoEsq(No no) {
        No noDir = no.dir;
        No noDirEsq = noDir.esq;

        noDir.esq = no;
        no.dir = noDirEsq;

        return noDir;
    }

    private No rotacaoDirEsq(No no) {
        no.dir = rotacaoDir(no.dir);
        return rotacaoEsq(no);
    }

    private No rotacaoEsqDir(No no) {
        no.esq = rotacaoEsq(no.esq);
        return rotacaoDir(no);
    }
}


public class TP04Q04 {
    public static void main(String[] args) {
        String csvPath = "/tmp/pokemon.csv";
        ArrayList<Pokemon> pokedex = new ArrayList<Pokemon>();
        ArvoreAlvinegra pokemonTree = new ArvoreAlvinegra();

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
            if (p != null) {
                System.out.println(p.name);
            } else {
                System.out.println(name);
            }
            System.out.print("raiz ");

            boolean found = pokemonTree.pesquisar(name);
            
            System.out.println(found ? "SIM" : "NAO");

            name = sc.nextLine();
        }

        sc.close();
    }
}
