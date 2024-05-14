package com.page_rank;

import java.net.URISyntaxException;
import java.util.*;


import com.page_rank.Utils.*;
import com.page_rank.Models.*;
import com.mongodb.client.MongoDatabase;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.bson.Document;

public class PageRank {

    private static List<Document> retrievedPages;
    private static final double diveFactor = 0.85;
    private static final double EPSILON = 1e-10;

    private static PageService pageService = null;

    private static void initalizeDatabase() {

        MongoDatabase connection = DBManager.connect("mongodb://localhost:27017", "DummySearchEngine");
        pageService = new PageService(connection);
    }

    private static RealMatrix BuildMatrix(HashMap<String, Integer> urlDict) {

        for (int i = 0; i < retrievedPages.size(); i++) {
            urlDict.put((String) retrievedPages.get(i).get("url"), i);
        }

        Double[][] matrix = new Double[retrievedPages.size()][retrievedPages.size()];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = 0.;
            }
        }

        int[] totalLinksPerPage = new int[retrievedPages.size()];
        for (int i = 0; i < retrievedPages.size(); i++) {

            Document currentPage = retrievedPages.get(i);
            List<String> urls = (List<String>) currentPage.get("outlinks");

            for (String url : urls) {
                if (urlDict.containsKey(url)) {
                    totalLinksPerPage[i]++;
                    matrix[i][urlDict.get(url)]++;
                }
            }
        }

        RealMatrix M = new Array2DRowRealMatrix(matrix.length, matrix[0].length);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (totalLinksPerPage[i] == 0) {
                    M.addToEntry(i, j, 0);
                    continue;
                } else {
                    M.addToEntry(i, j, matrix[i][j] / totalLinksPerPage[i]);
                }
            }
        }

        M.transpose();
        System.out.println("Matrix: " + M.toString());
        return M;

    }

    private static double[] runPageRank(RealMatrix matrix, double diveFactor, double EPSILON) {
        RealVector previousRank = new ArrayRealVector(matrix.getRowDimension());
        RealVector currentRank = new ArrayRealVector(matrix.getRowDimension());

        previousRank.set(1.0 / matrix.getRowDimension());
        RealMatrix M_hat = matrix.scalarMultiply(diveFactor);
        currentRank = M_hat.operate(previousRank).mapAdd(1 - diveFactor);

        while (currentRank.subtract(previousRank).getNorm() > EPSILON) {
            previousRank = currentRank;
            currentRank = M_hat.operate(previousRank).mapAdd(1 - diveFactor);
        }

        return currentRank.toArray();
    }

    public static void main(String[] args) throws URISyntaxException {

        initalizeDatabase();
        retrievedPages = pageService.fetchAllDocuments();

        if (retrievedPages == null) {
            System.out.println("Nothing to rank");
            System.out.println("Exiting Program...");
            return;
        }

        System.out.println("Size:" + retrievedPages.size());

        HashMap<String, Integer> urlDict = new HashMap<>();
        RealMatrix M = BuildMatrix(urlDict);
        double[] ranks = new double[M.getRowDimension()];
        ranks = runPageRank(M, diveFactor, EPSILON);

        for (int i = 0; i < ranks.length; i++) {
            System.out.println("Page: " + retrievedPages.get(i).get("url") + " Rank: " + ranks[i]);
        }

    }
}