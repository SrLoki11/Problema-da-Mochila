import java.util.*;

public class GeneticAlgorithmKnapsack {
    static class Item {
        int weight;
        int value;

        public Item(int weight, int value) {
            this.weight = weight;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        // Exemplo de itens e capacidade da mochila
        Item[] items = {
            new Item(2, 10),
            new Item(3, 7),
            new Item(4, 14),
            new Item(5, 5),
            new Item(6, 3)
        };
        int capacity = 10;

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
                System.out.println("Item " + (i+1) + ": Peso = " + items[i].weight + ", Valor = " + items[i].value);
            }
        }
    }

    // Função para implementar o algoritmo genético para o Problema da Mochila
    int[] genetic_algorithm_knapsack(Item[] items, int capacity, int populationSize, double crossoverRate, double mutationRate, int numGenerations) {
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

    // Função para gerar uma população inicial aleatória
    int[][] generate_initial_population(int size, int populationSize) {
        int[][] population = new int[populationSize][size];
        Random rand = new Random();
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < size; j++) {
                population[i][j] = rand.nextInt(2); // 0 ou 1 (selecionado ou não selecionado)
            }
        }
        return population;
    }

    // Função de fitness para calcular o valor total da mochila
    int fitness_function(int[] solution, Item[] items, int capacity) {
        int totalValue = 0;
        int totalWeight = 0;
        for (int i = 0; i < solution.length; i++) {
            if (solution[i] == 1) {
                totalValue += items[i].value;
                totalWeight += items[i].weight;
            }
        }
        // Penalize soluções que excedam a capacidade da mochila
		// Implemente o método de penalização que achar mais adequado
        if (totalWeight > capacity) {
            totalValue = 0;
        }
        return totalValue;
    }

    // Função para realizar a seleção por torneio
    int[] tournament_selection(int[][] population, Item[] items, int capacity) {
        
        Random rand = new Random();
        int tournamentSize = 5; // Tamanho do torneio
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

    // Função para realizar a seleção por roleta
    static int[] roulette_selection(int[][] population, Item[] items, int capacity) {
        Random rand = new Random();
        int totalFitness = 0;
        int[] cumulativeFitness = new int[population.length];
        int selectedIdx = -1;

        // Calcula o fitness total e o fitness cumulativo
        for (int i = 0; i < population.length; i++) {
            int fitness = fitness_function(population[i], items, capacity);
            totalFitness += fitness;
            cumulativeFitness[i] = totalFitness;
        }

        // Gera um número aleatório dentro do fitness total
        int randomFitness = rand.nextInt(totalFitness);

        // Encontra o índice do indivíduo selecionado
        for (int i = 0; i < population.length; i++) {
            if (randomFitness < cumulativeFitness[i]) {
                selectedIdx = i;
                break;
            }
        }

        return population[selectedIdx];
    }

    // Função para realizar o cruzamento (crossover)
    int[] crossover(int[] parent1, int[] parent2, double crossoverRate) {
        
        Random rand = new Random();
        int[] offspring = new int[parent1.length];
        
        if (rand.nextDouble() < crossoverRate) { // Realiza o crossover com a taxa especificada
            
            int crossoverPoint = rand.nextInt(parent1.length); // Ponto de corte aleatório
            for (int i = 0; i < crossoverPoint; i++) {
                offspring[i] = parent1[i];
            }
            
            for (int i = crossoverPoint; i < parent2.length; i++) {
                offspring[i] = parent2[i];
            }
        } else { // Se não houver crossover, retorna um dos pais
            offspring = parent1;
        }
        return offspring;
    }
        // Completar a função de cruzamento
        // Retorna um novo indivíduo resultante do crossover entre parent1 e parent2
		// Não esqueça da probabiidade de cruzamento
        return null;
    }

    // Função para realizar a mutação
    void mutate(int[] solution, double mutationRate) {
        
        Random rand = new Random();
        for (int i = 0; i < solution.length; i++) {
            if (rand.nextDouble() < mutationRate) { // Aplica a mutação com a taxa especificada
                solution[i] = (solution[i] == 0) ? 1 : 0; // Inverte o valor do gene
            }
        }
    }
        // Completar a função de mutação
        // Aplica a mutação na solução fornecida
		// Não esqueça da probabiidade de mutação
        
        // Função para implementar o algoritmo genético para o Problema da Mochila
    int[] geneticAlgorithmKnapsack(List<Item> items, int capacity, int populationSize, double crossoverRate, double mutationRate, int numGenerations) {
        // Implementação do algoritmo genético aqui...
    }

