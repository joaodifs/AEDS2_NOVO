import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.io.RandomAccessFile;

public class TP01Q08 {
    public static void main(String[] args) throws Exception {
        int quant = 0;
        double numeros = 0;
        quant = MyIO.readInt();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.######",symbols);
        df.setGroupingUsed(false);
        int i = 0;
            RandomAccessFile file = new RandomAccessFile("file.txt", "rw");
            while (i < quant) {
                numeros = MyIO.readDouble();  
                file.writeBytes(df.format(numeros) + "\n");
                i++;
            }
            file.close();
            RandomAccessFile readFile = new RandomAccessFile("file.txt", "r");
            long fileLength = readFile.length(); // Tamanho do arquivo
            long pointer = fileLength - 1; // Começando do último byte do arquivo
            readFile.seek(pointer); // Colocando o ponteiro no final do arquivo
    
            StringBuilder sb = new StringBuilder();
    
            // Ler o arquivo de trás para frente
            while (pointer >= 0) {
                readFile.seek(pointer);
                char c = (char) readFile.read();
    
                if (c == '\n') {
                    if (sb.length() > 0) {
                        System.out.println(sb.reverse().toString()); // Imprime a linha (revertida)
                        sb.setLength(0); // Limpa o StringBuilder
                    }
                } else {
                    sb.append(c); // Adiciona o caractere à linha
                }
    
                // Atualiza o ponteiro para o byte anterior
                pointer--;
            }
    
            // Imprimir a última linha (ou primeira no caso original) se não terminar com '\n'
            if (sb.length() > 0) {
                System.out.println(sb.reverse().toString());
            }
    
            readFile.close(); // Fecha o arquivo após a leitura
        }
    }

    /*
     import java.io.RandomAccessFile;

public class LerArquivoDeTrasParaFrente {
    public static void main(String[] args) {
        try {
            RandomAccessFile file = new RandomAccessFile("file.txt", "r");

            // Posição inicial do ponteiro (final do arquivo)
            long fileLength = file.length();
            file.seek(fileLength - 1);

            // Ler o arquivo de trás para frente
            for (long pointer = fileLength - 1; pointer >= 0; pointer--) {
                file.seek(pointer);
                int b = file.read();
                
                // Verifica se o byte é parte de um caractere e o imprime
                System.out.print((char) b);
            }

            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
     */