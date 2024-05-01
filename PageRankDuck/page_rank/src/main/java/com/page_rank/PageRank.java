package com.page_rank;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.ConnectException;
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
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import org.jsoup.nodes.Element;

public class PageRank {
    private static List<Document> retrievedPages;

    public static List<List<String>> getOutlinks() {
        List<List<String>> outlinks = new ArrayList<>();
        try {
            // for (Document page : retrievedPages) {
            for (int i = 0; i < retrievedPages.size(); i++) {
                Document page = retrievedPages.get(i);
                System.out.println("Fetching outlinks for page " + (outlinks.size() + 1));
                List<String> currentOutlinks = new ArrayList<>();
                String currentPageURL = page.getString("url");
                org.jsoup.nodes.Document doc = Jsoup.connect(currentPageURL).get();
                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    String outlink = link.attr("abs:href");
                    Url url = new Url(outlink);
                    String normalizedOutlink = url.getNormalized();
                    currentOutlinks.add(normalizedOutlink);
                }
                outlinks.add(currentOutlinks);
            }
        } catch (ConnectException e) {
            System.err.println("Connection timed out: " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("Error fetching document: " + e.getMessage());
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
        if (retrievedPages == null) {
            System.out.println("Builder: Error in fetching documents");
            return null;
        }
        int numPages = retrievedPages.size();
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
            Integer sourceIndex = urlToIndex.get(page.getString("url"));
            if (sourceIndex == null) {
                continue;
            }
            List<String> myOutlinks = outlinks.get(sourceIndex);
            if (myOutlinks != null) {
                for (String outlink : myOutlinks) {
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
        int numIterations = retrievedPages.size(), numPages = retrievedPages.size();
        float diveFactor = 0.85f;
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
        for (int i = 0; i < retrievedPages.size(); i++) {
            if (adjacencyMatrix[i][pageIndex]) {
                k++;
            }
        }
        for (int i = 0; i < retrievedPages.size(); i++) {
            if (adjacencyMatrix[pageIndex][i]) {
                for (int j = 0; j < retrievedPages.size(); j++) {
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
        for (int i = 0; i < retrievedPages.size(); i++) {
            if (adjacencyMatrix[0][i]) {
                k++;
            }
        }
        for (int i = 0; i < retrievedPages.size(); i++) {
            if (adjacencyMatrix[pageIndex][i]) {
                for (int j = 0; j < retrievedPages.size(); j++) {
                    if (adjacencyMatrix[i][j]) {
                        l++;
                    }
                }
            }
        }
        return k / l;
    }

    public static void main(String[] args) throws URISyntaxException {
        MongoDatabase connection = DBManager.connect("mongodb://localhost:27017", "SearchEngine");
        DocumentService docService = new DocumentService(connection);
        PageRankService pagerankService = new PageRankService(connection);

        PageRank.getAllDocuments(pagerankService);
        System.out.println("First size:" + retrievedPages.size());
        // for (Document doc : retrievedPages) {
        // System.out.println(doc.getString("url"));
        // }
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
        // for (Document doc : retrievedPages) {
        // System.out.println(doc.getString("url"));
        // }
        // Document doc = retrievedPages.get(0);
        // System.out.println(doc.getString("url"));
        List<List<String>> outlinks = getOutlinks();

        // Print the outlinks to check on them
        // int k = 1;
        // if (outlinks != null)
        // for (List<String> outlink : outlinks) {
        // System.out.println("Outlinks for page " + k++);
        // for (String link : outlink) {
        // System.out.println(link);
        // }
        // }

        boolean[][] adjacencyMatrix = buildAdjacencyMatrix(outlinks);
        if (adjacencyMatrix == null) {
            // the matrix is null
            System.out.println("Error in building adjacency matrix");
            return;
        }
        for (int i = 0; i < retrievedPages.size(); ++i) {
            for (int j = 0; j < retrievedPages.size(); ++j) {
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
