import java.util.*;
import java.io.*;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.time.Duration;

class TP04Q02{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Instant start = Instant.now();

        List<Pokemon> pokedex = Pokemon.lerPokemons();
        Arvore arvore = new Arvore();
        
       
        boolean fim = true;
        do{
            String entrada = sc.nextLine();
            
            if(entrada.equals("FIM")){fim = false;}

            else{
                        
                try{
                 int id = Integer.parseInt(entrada);
                 Pokemon inserido = pokedex.get(id - 1);
                 arvore.inserirAuxiliar(arvore.raiz, inserido);
                }catch(NumberFormatException e){e.printStackTrace();
                }catch (Exception e) {
                    System.out.println("Não foi possível inserir: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }while(fim);

        fim = true;
        do{
            String entrada = sc.nextLine();
            
            Pokemon procurado = new Pokemon();

            if(entrada.equals("FIM")){fim = false;}
            else{

                for(Pokemon p : pokedex){
                    if(p.getName().equals(entrada)){procurado = Pokemon.clonePokemon(p);}
                }

                System.out.println("=> " + entrada);
                System.out.print("raiz ");
                
                
                boolean achou = arvore.pesquisar(arvore.raiz, procurado);

                if(achou){System.out.println(" SIM");}
                else{System.out.println(" NAO");}
                
            }


        }while(fim);

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        String matricula = "729281";

        try (PrintWriter escritor = new PrintWriter(new FileWriter("matrícula_arvoreArvore.txt"))) {
            escritor.println(matricula + "\t" + duration + "\t" + arvore.contador);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
       
sc.close(); }
}

class Pokemon {
    private int id;
    private int generation;
    private String name;
    private String description;
    private List<String> types;
    private List<String> abilities;
    private double weight;
    private double height;
    private int captureRate;
    private boolean isLegendary;
    private LocalDate captureDate;

    // Implementação dos construtores
    public Pokemon() {
        this.id = 0;
        this.name = "";
        this.generation = 1;
        this.description = "";
        this.weight = 0;
        this.height = 0;
        this.captureRate = 0;
        this.isLegendary = false;
        this.types = new ArrayList<>();
        this.abilities = new ArrayList<>();
    }
    
    public Pokemon(int id, String name, int generation, String description, List<String> types, List<String> abilities, double weight, double height, boolean isLegendary, int captureRate, LocalDate captureDate) {
        this.id = id;
        this.name = name;
        this.generation = generation;
        this.description = description;
        this.types = types;
        this.abilities = abilities;
        this.weight = weight;
        this.height = height;
        this.isLegendary = isLegendary;
        this.captureRate = captureRate;
        this.captureDate = captureDate;
    }

    // Implementação do método para clonar um Pokémon
    public static Pokemon clonePokemon(Pokemon p) {
        return new Pokemon(p.getId(), p.getName(), p.getGeneration(), p.getDescription(), p.getTypes(), p.getAbilities(), p.getWeight(), p.getHeight(), p.getIsLegendary(), p.getCaptureRate(), p.getCaptureDate());
    }

    // Implementação do código para ler os Pokémons do arquivo CSV
    public static List<Pokemon> lerPokemons() {
        List<Pokemon> pokedex = new ArrayList<>();

        try (BufferedReader leitor = new BufferedReader(new FileReader("pokemon.csv"))) {
            String linha = leitor.readLine();
            
            while ((linha = leitor.readLine()) != null) {
                String[] atributos = linha.split(",");
                int pointerInicio = 0;
                int pointerFim = 0;

                for (int i = 0; i < atributos.length; i++) {
                    for (int j = 0; j < atributos[i].length(); j++) {
                        if (atributos[i].charAt(j) == '[') {
                            pointerInicio = i;
                        } else if (atributos[i].charAt(j) == ']') {
                            pointerFim = i;
                        }
                    }
                }
                // Tira os colchetes, vírgulas, aspas duplas e simples
                linha = linha.replaceAll("[\"'\\[\\]]", "").trim();
                atributos = linha.split(",");

                Pokemon p = new Pokemon();
                int cont = 0;        
                try {
                    p.setId(Integer.parseInt(atributos[cont].trim()));
                    cont++;
                    p.setGeneration(Integer.parseInt(atributos[cont].trim()));
                    cont++;
                    p.setName(atributos[cont].trim());
                    cont++;
                    p.setDescription(atributos[cont].trim());
                    cont++;
                    p.setType(atributos[cont].trim());
                    cont++;
                    
                    // Checar se da erro
                    if (!atributos[cont].isEmpty()) {
                        p.setType(atributos[cont].trim());
                    }
                    cont++;

                    int qtdAbilites = (pointerFim - pointerInicio) + 1;

                    for (int i = 6; i <= pointerFim; i++) {
                        p.setAbility(atributos[i].trim());
                    }
                    
                    cont += qtdAbilites;
                    
                    if (!(atributos[cont].isEmpty())) {
                        p.setWeight(Double.parseDouble(atributos[cont].trim()));
                        cont++;    
                    } else {
                        p.setWeight(0);
                        cont++;
                    }

                    if (!(atributos[cont].isEmpty())) {
                        p.setHeight(Double.parseDouble(atributos[cont].trim()));
                        cont++;
                    } else {
                        p.setHeight(0);
                        cont++;
                    }

                    if (!(atributos[cont].isEmpty())) {
                        p.setCaptureRate(Integer.parseInt(atributos[cont].trim()));
                        cont++;
                    } else {
                        p.setCaptureRate(0);
                        cont++;
                    }

                    if (atributos[cont].equals("0")) {
                        p.setIsLegendary(false);
                        cont++;
                    } else {
                        p.setIsLegendary(true);
                        cont++;
                    }
                  
                    if (!(atributos[cont].isEmpty())) {
                        LocalDate data = Pokemon.parseDate(atributos[cont].trim());
                        p.setCaptureDate(data);
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                pokedex.add(p);
            }
        
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pokedex;
    }

    private static LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateStr, formatter);
    }
    
    public static void imprimePokemon(Pokemon p){
        
        String formattedDate = "Nulo";

    if (p.getCaptureDate() != null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            formattedDate = p.getCaptureDate().format(dateFormatter);
        }

     String tiposFormatados = p.getTypes().stream().map(tipo -> "'" + tipo + "'")  // Colocando aspas simples em volta de cada tipo
     .collect(Collectors.joining(", "));  // Juntando os tipos separados por vírgula

     String abilitiesFormatadas = p.getAbilities().stream()
    .map(ability -> "'" + ability + "'")  // Colocando aspas simples em volta de cada habilidade
    .collect(Collectors.joining(", "));  // Juntando as habilidades separadas por vírgula


     System.out.print("[#" + p.getId() + " -> " + p.getName() +": " + p.getDescription() + " - " + "[" + tiposFormatados + "]" + " - " + "[" + abilitiesFormatadas + "]" + " - " + p.getWeight() + "kg" + " - " + p.getHeight() + "m" + " - " +  p.getCaptureRate() + "%" + " - ");
     System.out.println(p.getIsLegendary() + " - " + p.getGeneration() + " gen" + "]" + " - " + formattedDate);
    
    }

}

class No{
    int elemento;
    No esq, dir;
    No2 raiz2;

    public No(int x){
        this.elemento = x;
        esq = dir  = null;
        raiz2 = null;
   
    }
}


//Implementação de uma árvore binária de busca com o atributo chave sendo o nome do Pokemon.
class Arvore{
    No raiz;
    int contador;

    public Arvore(){
        raiz = null;
        contador = 0;
        inserir(7);
        inserir(3);
        inserir(11);
        inserir(1);
        inserir(5);
        inserir(9);
        inserir(13);
        inserir(0);
        inserir(2);
        inserir(4);
        inserir(6);
        inserir(8);
        inserir(10);
        inserir(12);
        inserir(14);
    
    }

    public void caminhar(No i){
        if(i != null){
            System.out.println(i.elemento);
            caminhar(i.esq);
            caminhar(i.dir);
        }
    }

    public void inserir(int x){
        try{
        raiz = inserir(raiz, x);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private No inserir(No i, int x) throws Exception{
        if(i == null){
            i = new No(x);

        }
        else if(x < i.elemento){
            i.esq = inserir(i.esq, x);
        }
        else if(x > i.elemento){
            i.dir = inserir(i.dir, x);
        }
        else{
            throw new Exception("Número repetido");
        }
        return i;
    }

    public No2 inserirNo2(No2 i, Pokemon p) throws Exception{
         String nome = p.getName();       
        if(i == null){
            i = new No2(nome);
        }
        else if(i.elemento.compareTo(nome) > 0){
            i.esq = inserirNo2(i.esq, p);
        }
        else if(i.elemento.compareTo(nome) < 0){
            i.dir = inserirNo2(i.dir, p);
        }
        else{
            throw new Exception("Pokemon repetido");
        }

        return i;

    }

    public No inserirAuxiliar(No i, Pokemon p) throws Exception {
        int resto = p.getCaptureRate() % 15;
    
        if (i == null) {
            System.out.println("i é null, entrando no primeiro if.");
            throw new Exception("CaptureRate inexistente");
        }
    
        else if (i.elemento > resto) {

            i.esq = inserirAuxiliar(i.esq, p);
        } else if (i.elemento < resto) {

            i.dir = inserirAuxiliar(i.dir, p);
        } else {

            i.raiz2 = inserirNo2(i.raiz2, p);
        }
    
        return i;
    }
    

    public boolean pesquisar2(No2 i, String nome){
        boolean resp;
        if(i == null){
            resp = false;
        }
        else if(i.elemento.equals(nome)){
            return true;
        }   
        else if(i.elemento.compareTo(nome) > 0){
            System.out.print("esq ");
            resp = pesquisar2(i.esq, nome);
        }
        else{
            System.out.print("dir ");
            resp = pesquisar2(i.dir, nome);
        }
    
        return resp;

        }
        public boolean pesquisar(No i, Pokemon p){
           boolean resp;
           contador++;

           if(i != null){
            resp = pesquisar2(i.raiz2, p.getName());

            if(!resp){
                System.out.print(" ESQ ");
                resp = pesquisar(i.esq, p);
            }
            if(!resp){
                System.out.print(" DIR ");
                resp = pesquisar(i.dir, p);
            }
           
           }

           else{
            resp = false;
           }
           return resp;
        }
        
}

class No2{
  String elemento;
  No2 esq, dir;

  public No2(String nome){
   this.elemento = nome;
   esq = dir = null;
 }

}