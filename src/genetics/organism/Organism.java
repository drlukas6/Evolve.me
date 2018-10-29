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
    private int numberOfRows;
    private int numberOfColumns;
    private int numberOfInputs;
    private int numberOfOutputs;
    private int levelsBack;
    private NetworkFactory networkFactory;
    private List<Double> inputValues = new ArrayList<>();
    private List<Double> outputValues = new ArrayList<>();
    private List<Network> networkPopulation = new ArrayList<>();
    private int generation = 0;
    private int maxGeneration;
    Comparator<Network> fitnessComparator = Comparator.comparing(Network::getFitness);
    private Network bestOfAllTime;
    Random r = new Random();

    public Organism(int numberOfNetworks, int numberOfRows,
                    int numberOfColumns, int levelsBack,
                    int numberOfInputs, int numberOfOutputs,
                    List<Double> inputValues, List<Double> outputValues,
                    int maxGeneration) {
        this.numberOfNetworks = numberOfNetworks;
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.numberOfInputs = numberOfInputs;
        this.numberOfOutputs = numberOfOutputs;
        this.levelsBack = levelsBack;
        this.maxGeneration = maxGeneration;
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


    public void startTraining() {
        generateInitialNetworks();
        PrintWriter out = null;
        try {
            out = new PrintWriter(new OutputStreamWriter(
                    new BufferedOutputStream(new FileOutputStream("OrganismOutput.txt")), "UTF-8"));
            out.println("-------------------- GENERATION: " + generation + "--------------------");
            out.flush();
            executeInitialNetworks();
            sortNetworks();
            bestOfAllTime = networkPopulation.get(0);
            while (generation < maxGeneration) {
                createNextGeneration();
                mutateAndExecuteNetworks();
                sortNetworks();
                if(bestOfAllTime.getFitness() > networkPopulation.get(0).getFitness()) {
                    bestOfAllTime = networkPopulation.get(0);
                }
                out.println("-------------------- GENERATION: " + generation + "--------------------");
                System.out.println("-------------------- GENERATION: " + generation + "--------------------");
                out.println("BEST NETWORK: ");
                out.println(networkPopulation.get(0).getFitness());
                out.println(networkPopulation.get(0).getNetworkDescriptor());
                out.flush();
            }
            out.println("-------------------- MAX GENERATION REACHED --------------------");
            out.println("-------------------- BEST FITNESS: " + networkPopulation.get(0).getFitness() + "--------------------");
            out.println(networkPopulation.get(0).getNetworkDescriptor());
            out.println("-------------------- BEST NETWORK: --------------------");
            out.println(bestOfAllTime.getNetworkDescriptor());
            out.println(bestOfAllTime.getFitness());
            out.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(out != null) {
                out.flush();
                out.close();
            }
        }
    }

    public void testBestNetwork() {
        bestOfAllTime.testNetworkPerformances();
    }


}
