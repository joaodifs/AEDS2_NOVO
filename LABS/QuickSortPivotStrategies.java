import java.util.Random;
import java.util.Arrays;
import java.util.Random;
public class QuickSortPivotStrategies {

    // Troca dois elementos do array
    public static void swap(int i, int j, int[] array) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // QuickSort usando o primeiro elemento como pivô
    public static void QuickSortFirstPivot(int[] array, int left, int right) {
        if (left < right) {
            int pivotIndex = left;  // Primeiro elemento como pivô
            int pivotNewIndex = partition(array, left, right, pivotIndex);
            QuickSortFirstPivot(array, left, pivotNewIndex - 1);
            QuickSortFirstPivot(array, pivotNewIndex + 1, right);
        }
    }

    // QuickSort usando o último elemento como pivô
    public static void QuickSortLastPivot(int[] array, int left, int right) {
        if (left < right) {
            int pivotIndex = right;  // Último elemento como pivô
            int pivotNewIndex = partition(array, left, right, pivotIndex);
            QuickSortLastPivot(array, left, pivotNewIndex - 1);
            QuickSortLastPivot(array, pivotNewIndex + 1, right);
        }
    }

    // QuickSort usando um pivô aleatório
    public static void QuickSortRandomPivot(int[] array, int left, int right) {
        if (left < right) {
            Random rand = new Random();
            int pivotIndex = left + rand.nextInt(right - left + 1);  // Pivô aleatório
            int pivotNewIndex = partition(array, left, right, pivotIndex);
            QuickSortRandomPivot(array, left, pivotNewIndex - 1);
            QuickSortRandomPivot(array, pivotNewIndex + 1, right);
        }
    }

    // QuickSort usando a mediana de três (primeiro, meio e último elementos)
    public static void QuickSortMedianOfThree(int[] array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            int pivotIndex = medianOfThree(array, left, mid, right);  // Mediana de três
            int pivotNewIndex = partition(array, left, right, pivotIndex);
            QuickSortMedianOfThree(array, left, pivotNewIndex - 1);
            QuickSortMedianOfThree(array, pivotNewIndex + 1, right);
        }
    }

    // Função para realizar a partição
    private static int partition(int[] array, int left, int right, int pivotIndex) {
        int pivotValue = array[pivotIndex];
        swap(pivotIndex, right, array);  // Mover o pivô para o final
        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (array[i] < pivotValue) {
                swap(i, storeIndex, array);
                storeIndex++;
            }
        }
        swap(storeIndex, right, array);  // Mover o pivô para sua posição final
        return storeIndex;
    }

    // Função para calcular a mediana de três
    private static int medianOfThree(int[] array, int left, int mid, int right) {
        if (array[left] > array[mid]) swap(left, mid, array);
        if (array[left] > array[right]) swap(left, right, array);
        if (array[mid] > array[right]) swap(mid, right, array);
        return mid;  // O elemento do meio após a ordenação é a mediana
    }

public class ArrayGenerator {

    // Gera um array aleatório
    public static int[] generateRandomArray(int size) {
        Random rand = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt(10000);  // Valores entre 0 e 9999
        }
        return array;
    }

    // Gera um array ordenado
    public static int[] generateSortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }

    // Gera um array quase ordenado (por exemplo, 10% dos elementos fora de ordem)
    public static int[] generateNearlySortedArray(int size) {
        int[] array = generateSortedArray(size);
        Random rand = new Random();
        for (int i = 0; i < size / 10; i++) {
            int index1 = rand.nextInt(size);
            int index2 = rand.nextInt(size);
            QuickSortPivotStrategies.swap(index1, index2, array);
        }
        return array;
    }
}

    public static void main(String[] args) {
        int[] sizes = {100, 1000, 10000};  // Diferentes tamanhos de arrays
        for (int size : sizes) {
            testQuickSort(size);
        }
    }

    public static void testQuickSort(int size) {
        int[] randomArray = ArrayGenerator.generateRandomArray(size);
        int[] sortedArray = ArrayGenerator.generateSortedArray(size);
        int[] nearlySortedArray = ArrayGenerator.generateNearlySortedArray(size);

        System.out.println("Testando com array de tamanho: " + size);

        // Testando QuickSort com pivô no primeiro elemento
        runQuickSortTest("FirstPivot", randomArray, sortedArray, nearlySortedArray, QuickSortPivotStrategies::QuickSortFirstPivot);

        // Testando QuickSort com pivô no último elemento
        runQuickSortTest("LastPivot", randomArray, sortedArray, nearlySortedArray, QuickSortPivotStrategies::QuickSortLastPivot);

        // Testando QuickSort com pivô aleatório
        runQuickSortTest("RandomPivot", randomArray, sortedArray, nearlySortedArray, QuickSortPivotStrategies::QuickSortRandomPivot);

        // Testando QuickSort com mediana de três
        runQuickSortTest("MedianOfThree", randomArray, sortedArray, nearlySortedArray, QuickSortPivotStrategies::QuickSortMedianOfThree);
    }

    public static void runQuickSortTest(String pivotType, int[] randomArray, int[] sortedArray, int[] nearlySortedArray, QuickSortFunction quickSortFunction) {
        System.out.println("Estratégia: " + pivotType);

        // Teste com array aleatório
        int[] array = Arrays.copyOf(randomArray, randomArray.length);
        long start = System.nanoTime();
        quickSortFunction.sort(array, 0, array.length - 1);
        long duration = System.nanoTime() - start;
        System.out.println("Array Aleatório: " + duration + " ns");

        // Teste com array ordenado
        array = Arrays.copyOf(sortedArray, sortedArray.length);
        start = System.nanoTime();
        quickSortFunction.sort(array, 0, array.length - 1);
        duration = System.nanoTime() - start;
        System.out.println("Array Ordenado: " + duration + " ns");

        // Teste com array quase ordenado
        array = Arrays.copyOf(nearlySortedArray, nearlySortedArray.length);
        start = System.nanoTime();
        quickSortFunction.sort(array, 0, array.length - 1);
        duration = System.nanoTime() - start;
        System.out.println("Array Quase Ordenado: " + duration + " ns");

        System.out.println();
    }

    // Interface funcional para usar nas chamadas de teste
    @FunctionalInterface
    interface QuickSortFunction {
        void sort(int[] array, int left, int right);
    }


}
