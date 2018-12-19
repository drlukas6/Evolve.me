package genetics.organism;

import genetics.networks.Network;
import genetics.networks.NetworkFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Organism {
    private int numberOfNetworks;
    private NetworkFactory networkFactory;
    private List<Network> networkPopulation = new ArrayList<>();
    private int generation = 0;
    private int maxGeneration;
    private Comparator<Network> fitnessComparator = Comparator.comparing(Network::getFitness);
    private Network bestOfAllTime;
    private Writer networkWriter;
    private Random r = new Random();

    public Organism(int numberOfNetworks, int numberOfRows,
                    int numberOfColumns, int levelsBack,
                    int numberOfInputs, int numberOfOutputs,
                    List<List<Double>> inputValues, List<Double> outputValues,
                    int maxGeneration,
                    Writer networkWriter) {
        this.numberOfNetworks = numberOfNetworks;
        this.maxGeneration = maxGeneration;
        this.networkWriter = networkWriter;
        this.networkFactory = new NetworkFactory(outputValues, inputValues, numberOfRows, numberOfColumns, numberOfInputs, numberOfOutputs, levelsBack);
    }



    private void generateInitialNetworks() {
        for(int i = 0; i < numberOfNetworks; i++) {
            networkPopulation.add(networkFactory.createRandomNetwork());
        }
    }

    private void executeInitialNetworks() {
        for(Network network: networkPopulation) {
            network.executeInitialNetwork();
        }
    }

    private void sortNetworks() {
        networkPopulation.sort(fitnessComparator);
    }

    private void createNextGeneration() {
        List<Network> nextGeneration = new ArrayList<>();
        for (int i = 0; i < numberOfNetworks; i++) {
            List<Network> chosenNetworks = new ArrayList<>();
            chosenNetworks.add(networkPopulation.get(0));
            for (int j = 0; j < 2; j++) {
                int randomNetworkIndex = r.nextInt(numberOfNetworks);
                chosenNetworks.add(networkPopulation.get(randomNetworkIndex));
            }
            chosenNetworks.sort(fitnessComparator);
            nextGeneration.add(networkFactory.combineNetworks(chosenNetworks.get(0), chosenNetworks.get(1)));
        }
        networkPopulation = nextGeneration;
        generation++;
    }

    private void mutateAndExecuteNetworks() {
        for(Network network: networkPopulation) {
            network.checkActiveNodes();
            network.mutateNetwork();
            network.executeNetwork();
        }
    }


    public void startTraining() throws IOException{
        generateInitialNetworks();
        executeInitialNetworks();
        sortNetworks();
        bestOfAllTime = networkPopulation.get(0);
        while (generation <= maxGeneration) {
            createNextGeneration();
            mutateAndExecuteNetworks();
            sortNetworks();
            if(bestOfAllTime.getFitness() > networkPopulation.get(0).getFitness()) {
                bestOfAllTime = networkPopulation.get(0);
            }
            System.out.println("-------------------- GENERATION: " + generation + "--------------------");
        }
        System.out.println("\n\n---------- MAX GENERATION REACHED ----------\nBEST NETWORK:");
        System.out.println(bestOfAllTime.getStats());
        networkWriter.write(bestOfAllTime.getNetworkDescriptor());
    }

    public void testBestNetwork() {
        bestOfAllTime.testNetworkPerformances();
    }


}
