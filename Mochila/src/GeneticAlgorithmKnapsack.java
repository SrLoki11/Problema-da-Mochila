import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithmKnapsack {
    static class Item {
        int weight;
        int value;

        public Item(int weight, int value) {
            this.weight = weight;
            this.value = value;
        }
    }

    Item[] items; // Declaração como campo da classe
    int capacity; // Declaração como campo da classe

    public static void main(String[] args) {
        GeneticAlgorithmKnapsack knapsack = new GeneticAlgorithmKnapsack(); // Criar instância da classe
        knapsack.run(); // Chamar o método run
    }

    public void run() {
        // Ler dados do arquivo
        String fileName = "C:/Users/caual/Documents/MInha Pasta 3/instancias-mochila/KNAPDATA40";
        
        try {
            items = readItems(fileName);
            capacity = readCapacity(fileName);
        } catch (IOException e) {
            System.err.println("Erro na leitura do arquivo: " + e.getMessage());
            return;
        }

        int populationSize = 50;
        double crossoverRate = 0.8;
        double mutationRate = 0.1;
        int numGenerations = 500;

        // Chamada para a função que implementa o algoritmo genético
        int[] solution = genetic_algorithm_knapsack(items, capacity, populationSize, crossoverRate, mutationRate, numGenerations);
        
        // Imprimir a solução encontrada
        System.out.println("Itens selecionados:");
        for (int i = 0; i < items.length; i++) {
            if (solution[i] == 1) {
                System.out.println("Item " + (i + 1) + ": Peso = " + items[i].weight + ", Valor = " + items[i].value);
            }
        }
    }

    // Método para implementar o algoritmo genético
    public static int[] genetic_algorithm_knapsack(Item[] items, int capacity, int populationSize, double crossoverRate, double mutationRate, int numGenerations) {
        // Implementação do algoritmo genético aqui...
        return null; // Placeholder para a implementação real
    }

    // Método para ler os itens do arquivo
    public Item[] readItems(String fileName) throws IOException {
        List<Item> itemList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                int weight = Integer.parseInt(parts[0]);
                int value = Integer.parseInt(parts[1]);
                itemList.add(new Item(weight, value));
            }
        }

        return itemList.toArray(new Item[0]);
    }

    // Método para ler a capacidade da mochila do arquivo
    public static int readCapacity(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            // Lê apenas a primeira linha do arquivo, que contém a capacidade
            return Integer.parseInt(br.readLine().trim());
        }
    }
}
