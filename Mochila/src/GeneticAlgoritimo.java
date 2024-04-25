
import java.util.*;
import java.io.*;

public class GeneticAlgoritimo {
    static class Item {
        int weight;
        int value;

        public Item(int weight, int value) {
            this.weight = weight;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        // Ler dados do arquivo
        String fileName = "C:/Users/caual/Documents/MInha Pasta 3/instancias-mochila/KNAPDATA40";
        Item[] items;
        int capacity;
        try {
            items = readItems(fileName);
            capacity = readCapacity(fileName);
        } catch (IOException e) {
            System.err.println("Erro na leitura do arquivo: " + e.getMessage());
            return;
        }

        // Parâmetros do algoritmo genético
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

    static Item[] readItems(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        reader.readLine(); // Pular a linha da capacidade
        int numItems = Integer.parseInt(reader.readLine().trim());
        Item[] items = new Item[numItems];

        for (int i = 0; i < numItems; i++) {
            String[] itemData = reader.readLine().trim().split(",");
            items[i] = new Item(Integer.parseInt(itemData[1]), Integer.parseInt(itemData[2]));
        }

        reader.close();
        return items;
    }

    static int readCapacity(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        int capacity = Integer.parseInt(reader.readLine().trim());
        reader.close();
        return capacity;
    }

    static int[] genetic_algorithm_knapsack(Item[] items, int capacity, int populationSize, double crossoverRate, double mutationRate, int numGenerations) {
        int[][] population = generate_initial_population(items.length, populationSize);
        Random rand = new Random();

        for (int gen = 0; gen < numGenerations; gen++) {
            int[][] nextGeneration = new int[populationSize][items.length];
            for (int i = 0; i < populationSize; i++) {
                int[] parent1 = tournament_selection(population, items, capacity);
                int[] parent2 = roulette_selection(population, items, capacity);
                int[] offspring = crossover(parent1, parent2, crossoverRate);
                mutate(offspring, mutationRate);
                nextGeneration[i] = offspring;
            }
            population = nextGeneration;
        }

        // Encontrar a melhor solução na última geração
        int[] bestSolution = population[0];
        int bestFitness = fitness_function(population[0], items, capacity);
        for (int i = 1; i < populationSize; i++) {
            int fitness = fitness_function(population[i], items, capacity);
            if (fitness > bestFitness) {
                bestSolution = population[i];
                bestFitness = fitness;
            }
        }

        return bestSolution;
    }

    static int[][] generate_initial_population(int size, int populationSize) {
        int[][] population = new int[populationSize][size];
        Random rand = new Random();
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < size; j++) {
                population[i][j] = rand.nextInt(2); // 0 ou 1 (selecionado ou não selecionado)
            }
        }
        return population;
    }

    static int fitness_function(int[] solution, Item[] items, int capacity) {
        int totalValue = 0;
        int totalWeight = 0;
        for (int i = 0; i < solution.length; i++) {
            if (solution[i] == 1) {
                totalValue += items[i].value;
                totalWeight += items[i].weight;
            }
        }
        if (totalWeight > capacity) {
            totalValue -= (totalWeight - capacity) * 2; // Penaliza baseado no excesso de peso
        }
        return Math.max(totalValue, 1); // Garante que o fitness mínimo é sempre positivo
    }

    static int[] tournament_selection(int[][] population, Item[] items, int capacity) {
        Random rand = new Random();
        int tournamentSize = 5;
        int[] bestSolution = null;
        int bestFitness = Integer.MIN_VALUE;

        for (int i = 0; i < tournamentSize; i++) {
            int[] solution = population[rand.nextInt(population.length)];
            int fitness = fitness_function(solution, items, capacity);
            if (fitness > bestFitness) {
                bestSolution = solution;
                bestFitness = fitness;
            }
        }

        return bestSolution;
    }

    static int[] roulette_selection(int[][] population, Item[] items, int capacity) {
        Random rand = new Random();
        int totalFitness = 0;
        int[] cumulativeFitness = new int[population.length];

        for (int i = 0; i < population.length; i++) {
            int fitness = fitness_function(population[i], items, capacity);
            totalFitness += fitness;
            cumulativeFitness[i] = totalFitness;
        }

        if (totalFitness > 0) { // Verifica se o fitness total é positivo
            int randomFitness = rand.nextInt(totalFitness);
            for (int i = 0; i < population.length; i++) {
                if (randomFitness < cumulativeFitness[i]) {
                    return population[i];
                }
            }
        } else {
            // Se o fitness total for zero, usa seleção aleatória ou por torneio
            return population[rand.nextInt(population.length)];
        }
        return null; // Caso inesperado, deveria ser tratado adequadamente
    }

    static int[] crossover(int[] parent1, int[] parent2, double crossoverRate) {
        Random rand = new Random();
        int[] offspring = new int[parent1.length];
        int crossoverPoint = rand.nextInt(parent1.length);

        if (rand.nextDouble() < crossoverRate) {
            for (int i = 0; i < parent1.length; i++) {
                if (i < crossoverPoint) {
                    offspring[i] = parent1[i];
                } else {
                    offspring[i] = parent2[i];
                }
            }
        } else {
            offspring = (rand.nextDouble() < 0.5) ? parent1.clone() : parent2.clone();
        }
        return offspring;
    }

    static void mutate(int[] solution, double mutationRate) {
        Random rand = new Random();
        for (int i = 0; i < solution.length; i++) {
            if (rand.nextDouble() < mutationRate) {
                solution[i] = 1 - solution[i]; // Inverte o gene
            }
        }
}
}