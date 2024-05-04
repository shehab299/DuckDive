package com.page_rank;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.page_rank.Utils.*;
import com.page_rank.Models.*;

import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class PageRank {
    private static List<Document> retrievedPages;
    private static final double diveFactor = 0.85;
    private static final double EPSILON = 1e-10;

    public static List<List<String>> getOutlinks() {
        List<List<String>> outlinks = new ArrayList<>();
        try {
            // You can adjust the number of pages to be fetched according to your needs
            for (int i = 0; i < 15; i++) {
                Document page = retrievedPages.get(i);
                System.out.println("Fetching outlinks for page " + (i + 1));
                List<String> currentOutlinks = page.getList("outlinks", String.class);
                if (currentOutlinks == null) {
                    System.out.println("Outlinks for page " + (i + 1) + " are null");
                }
                outlinks.add(currentOutlinks);
            }
        } catch (Exception e) {
            System.out.println("Error in fetching outlinks: " + e.getMessage());
            return null;
        }
        return outlinks;
    }

    private static boolean[][] buildAdjacencyMatrix(List<List<String>> outlinks) {
        Map<String, Integer> urlToIndex = new HashMap<>();
        List<String> indexToUrl = new ArrayList<>();
        if (outlinks == null) {
            System.out.println("Builder: Error in fetching outlinks");
            return null;
        }
        if (retrievedPages == null || retrievedPages.isEmpty()) {
            System.out.println("Builder: Error in fetching documents");
            return null;
        }
        int numPages = 15;
        System.out.println("Number of pages: " + numPages);
        int mappingIndex = 0;
        for (int i = 0; i < numPages; i++) {
            urlToIndex.put(retrievedPages.get(i).getString("url"), mappingIndex++);
            indexToUrl.add(retrievedPages.get(i).getString("url"));
        }
        boolean[][] adjacencyMatrix = new boolean[numPages][numPages];
        for (int i = 0; i < numPages; ++i) {
            for (int j = 0; j < numPages; ++j) {
                adjacencyMatrix[i][j] = false;
            }
        }
        for (Document page : retrievedPages) {
            String url = page.getString("url");
            if (url == null || url.isEmpty()) {
                continue;
            }
            Integer sourceIndex = urlToIndex.get(url);
            if (sourceIndex == null) {
                continue;
            }
            List<String> myOutlinks = outlinks.get(sourceIndex);
            if (myOutlinks != null) {
                for (String outlink : myOutlinks) {
                    if (outlink == null || outlink.isEmpty() || outlink.equals(url)) {
                        continue;
                    }
                    if (urlToIndex.containsKey(outlink)) {
                        int destinationIndex = urlToIndex.get(outlink);
                        adjacencyMatrix[sourceIndex][destinationIndex] = true;
                    }
                }
            }
        }
        return adjacencyMatrix;
    }

    private static void getAllDocuments(PageRankService service) {
        retrievedPages = service.fetchAllDocuments();
    }

    private static void weightedRankAlgorithm(boolean[][] adjacencyMatrix) {
        HashMap<Integer, Double> pageRankValue = new HashMap<>();
        int numIterations = 1, numPages = 15;
        double sum = 0;
        for (int i = 0; i < numPages; ++i) {
            pageRankValue.put(i, 1.0);
        }
        for (int i = 0; i < numIterations; i++) {
            for (int j = 0; j < numPages; j++) {
                for (int k = 0; k < numPages; k++) {
                    double currentValue = rankMe(adjacencyMatrix, k, numPages, pageRankValue);
                    pageRankValue.put(k, (1 - diveFactor) + diveFactor * currentValue);
                }
            }
        }
        for (int i = 0; i < numPages; i++) {
            sum += pageRankValue.get(i);
            System.out.println("Page " + i + " has rank " + pageRankValue.get(i));
        }
        System.out.println("Sum of all page ranks: " + sum);
    }

    public static double rankMe(boolean[][] adjacencyMatrix, int pageIndex, int numPages,
            HashMap<Integer, Double> pageRankValue) {
        int value = 0;
        for (int i = 0; i < numPages; i++) {
            int k = 0;
            if (adjacencyMatrix[i][pageIndex]) {
                k = 0;
                for (int j = 0; j < numPages; j++) {
                    if (adjacencyMatrix[i][j]) {
                        value++;
                    }
                }
                value += pageRankValue.get(i) / k * inputsWeight(adjacencyMatrix, i, pageIndex, pageRankValue)
                        * outputsWeight(adjacencyMatrix, i, pageIndex, pageRankValue);
            }
        }
        return value;
    }

    public static double inputsWeight(boolean[][] adjacencyMatrix, int m, int pageIndex,
            HashMap<Integer, Double> pageRankValue) {
        double k = 0, l = 0;
        for (int i = 0; i < 15; i++) {
            if (adjacencyMatrix[i][m]) {
                k++;
            }
        }
        for (int i = 0; i < 15; i++) {
            if (adjacencyMatrix[pageIndex][i]) {
                for (int j = 0; j < 15; j++) {
                    if (adjacencyMatrix[j][i]) {
                        l++;
                    }
                }
            }
        }
        return k / l;
    }

    public static double outputsWeight(boolean[][] adjacencyMatrix, int m, int pageIndex,
            HashMap<Integer, Double> pageRankValue) {
        double k = 0, l = 0;
        for (int i = 0; i < 15; i++) {
            if (adjacencyMatrix[0][i]) {
                k++;
            }
        }
        for (int i = 0; i < 15; i++) {
            if (adjacencyMatrix[pageIndex][i]) {
                for (int j = 0; j < 15; j++) {
                    if (adjacencyMatrix[i][j]) {
                        l++;
                    }
                }
            }
        }
        return k / l;
    }

    /*
     * Do not remove it as it will be the blue print for detecting the steady state
     * of the calculations
     */
    private static boolean isConverged(double[] oldPageRank, double[] newPageRank) {
        for (int i = 0; i < oldPageRank.length; i++) {
            if (Math.abs(oldPageRank[i] - newPageRank[i]) > EPSILON) {
                return false; // Not converged
            }
        }
        return true; // Converged
    }

    /*
     * N.B: To change the number of pages to be fetched, adjust the limit in
     * searching for (15) in the file and replace it whether by
     * (retrievedPages.size()) or your desired number
     */
    public static void main(String[] args) throws URISyntaxException {
        MongoDatabase connection = DBManager.connect("mongodb://localhost:27017", "SearchEngine");
        PageRankService pagerankService = new PageRankService(connection);

        PageRank.getAllDocuments(pagerankService);
        System.out.println("First size:" + retrievedPages.size());

        // Removing duplicate pages from the list
        Set<String> uniqueUrls = new HashSet<>();
        List<Document> uniqueDocuments = new ArrayList<>();

        for (Document document : retrievedPages) {
            String url = document.getString("url");
            if (uniqueUrls.add(url)) {
                uniqueDocuments.add(document);
            }
        }
        retrievedPages = uniqueDocuments;
        System.out.println("Second size:" + retrievedPages.size());

        List<List<String>> outlinks = getOutlinks();

        if (outlinks == null) {
            System.out.println("Page-Rank: Error in fetching outlinks in main method");
            return;
        }

        boolean[][] adjacencyMatrix = buildAdjacencyMatrix(outlinks);
        if (adjacencyMatrix == null) {
            System.out.println("Error in building adjacency matrix");
            return;
        }

        /*
         * Print adjacency matrix -> You can adjust the size of the printed sub matrix
         * according to your needs
         */
        for (int i = 0; i < 15; ++i) {
            for (int j = 0; j < 15; ++j) {
                System.out.print(adjacencyMatrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Adjacency matrix built successfully");
        System.out.println();
        System.out.println();
        weightedRankAlgorithm(adjacencyMatrix);

    }
}