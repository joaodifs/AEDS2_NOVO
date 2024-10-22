import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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
    String captureDate;  // Manter como String

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
    public static Pokemon searchById(int id, Pokemon[] pokemon) {
        for (int i = 0; i < 801; i++) {
            if (pokemon[i].id == id)
                return pokemon[i];
        }
        return null;
    }
}

class TP02Q07 {
    // Função para verificar se a entrada é "FIM"
    public static boolean isFim(String id) {
        return id.equals("FIM");
    }

    // Método de ordenação por inserção baseado na data de captura
    public static void insercao(Pokemon[] array, int n) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = 1; i < n; i++) {
            Pokemon tmp = array[i];
            int j = i - 1;
    
            // Converte a captureDate para Date
            Date tmpDate = dateFormat.parse(tmp.captureDate);
    
            // Comparação de datas
            while (j >= 0 && array[j] != null) {
                Date currentDate = dateFormat.parse(array[j].captureDate);
                if (currentDate.compareTo(tmpDate) > 0) {
                    array[j + 1] = array[j];
                    j--;
                } else {
                    break; // Se a data atual for menor ou igual, não precisa mais mover
                }
            }
            array[j + 1] = tmp;
        }
    }
    

    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("/tmp/pokemon.csv", "r"); // para mandar no verde é "/tmp/pokemon.csv"
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
        id = MyIO.readLine();
        Pokemon[] pokemon1 = new Pokemon[60];
        int pos = 0;
        while (!isFim(id)) {
            pokemon1[pos++] = Pokemon.searchById(Integer.parseInt(id), pokemon);
            id = MyIO.readLine();
        }

        // Ordenando pelo campo `captureDate` usando inserção
        insercao(pokemon1, pos);

        int i = 0;
        while (i < pos) {
            if (pokemon1[i] != null) {
                MyIO.println("[#" + pokemon1[i].id + " -> " + pokemon1[i].name + ": " + pokemon1[i].description +
                        " - " + pokemon1[i].types + " - " + pokemon1[i].abilities + " - " + pokemon1[i].weight + "kg - " + 
                        pokemon1[i].height + "m - " + pokemon1[i].captureRate + "% - " + pokemon1[i].isLegendary + " - " + 
                        pokemon1[i].generation + " gen] - " + pokemon1[i].captureDate);
            }
            i++;
        }
    }
}
