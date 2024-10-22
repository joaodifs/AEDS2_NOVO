import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.*;
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
        if(atributosPre[5].equals("")){
        this.types = new ArrayList<>(Arrays.asList(("'"+atributosPre[4]+"'"+","+atributosPre[5]).split(",")));
        }else{
            this.types = new ArrayList<>(Arrays.asList(("'"+atributosPre[4]+"'"+","+"'"+atributosPre[5]+"'").split(",")));
        }
         this.abilities = new ArrayList<>(Arrays.asList(abilitiesLine.split(",")));
        if(atributosPos[1].equals("")){
            this.weight = 0.0;
        }else{
       this.weight = Double.parseDouble(atributosPos[1]);
        }
        if(atributosPos[2].equals("")){
            this.height = 0.0;
        }else{
            this.height = Double.parseDouble(atributosPos[2]);
        }
      this.captureRate = Integer.parseInt(atributosPos[3]);

        // Verificando se é lendário
        if (atributosPos[4].equals("1")) {
            this.isLegendary = true;
        } else {
            this.isLegendary = false;
        }
        this.captureDate = atributosPos[5];
    }

    // Método para buscar um Pokémon pelo ID
    public static Pokemon searchById(int id, Pokemon[] pokemon) {
        for (int i = 0; i < 801; i++) {
            if (pokemon[i].id == id)
                return pokemon[i];
        }
        return null;
    }
}
class TP02Q01 {
    // Função para verificar se a entrada é "FIM"
    public static boolean isFim(String id) {
        return id.equals("FIM");
    }
    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("pokemon.csv", "r"); // para mandar no verde é "/tmp/pokemon.csv"
        file.readLine(); // Pular a primeira linha que não é útil
        Pokemon[] pokemon = new Pokemon[801]; // Criando um array de Pokémon para guardar todos

        // Lendo os dados do arquivo e criando os objetos de Pokemon
        for (int i = 0; i < 801; i++) {
            String line = file.readLine();
            String[] atributosTmp = line.split("\\[|\\]"); // Separando as linhas pelo colchete
            String abilitiesLine = atributosTmp[1];
            abilitiesLine = abilitiesLine.replaceAll(", ", ","); // para nao ficar com espaço duplo na hora de criar a lista com abilities

            String[] atributosPre = atributosTmp[0].split(","); // atributosPre[0] = ID // atributosPre[1] = generation // atributosPre[2] = nome 
                                                                // atributosPre[3] = description // atributosPre[4] = type1 // atributosPre[5] = type2

            String[] atributosPos = atributosTmp[2].split(","); // atributosPos[1] = weight // atributosPos[2] = height // atributosPos[3] = captureRate
                                                                // atributosPos[4] = isLegendary // atributosPos[5] = CaptureDate

            pokemon[i] = new Pokemon(atributosPre, atributosPos, abilitiesLine);
        }
        file.close();
        String id = "";
        
        while (!isFim(id)) {
            id = MyIO.readLine(); // Lendo a entrada do usuário
            if (!isFim(id)) {
                Pokemon pokemon1 = Pokemon.searchById(Integer.parseInt(id), pokemon);
                if (pokemon1 != null) {
                         MyIO.println("[#"+pokemon1.id+ " -> " +pokemon1.name+": "+pokemon1.description+
                            " - "+pokemon1.types+" - "+pokemon1.abilities+" - "+pokemon1.weight+"kg - "+pokemon1.height+
                          "m - "+pokemon1.captureRate+"% - "+pokemon1.isLegendary+" - "+pokemon1.generation+" gen] - "+pokemon1.captureDate);  
                  
                }
            }
        }
    }
}
