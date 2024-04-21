package com.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;

public class RobotsHandler {
    private static final String USER_AGENT = "DuckDive";
    static String robotsDirectory = "SpiderDuck/src/main/java/com/crawler/Robots/";

    private static boolean readRobotsFile(String path, Url url) {
        boolean found = false;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null) {
                String AD = line.substring(0, 3);
                String base = url.getBase();
                if (line.substring(3).equals(url.getNormalized()) || line.substring(3).equals(base)
                        || line.substring(3).equals(base.concat("/"))) // skipping "D: " or "A: "
                {
                    if (AD.equals("D: "))
                        found = true;
                    else if (AD.equals("A: "))
                        found = false;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return found;
    }

    private static boolean checkRobotsDirectory(Url url, boolean[] disallowed) // returns true if robots file exists
    {
        String fileName = url.getBase().replace("https://", "");
        fileName = fileName.replace("http://", "");
        File directory = new File(robotsDirectory);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File i : files) {
                if (i.getName().equals(fileName)) {
                    // robots file exists, start looking for the given url in this file
                    String thisUrlRobots = i.getAbsolutePath();
                    boolean found = readRobotsFile(thisUrlRobots, url);
                    if (found)
                        disallowed[0] = true;
                    else
                        disallowed[0] = false;
                    return true; // the robots exists
                }
            }
        }
        return false; // the robots file doesn't exist in the directory
    }

    private static boolean writeRobotsFile(Url url) {
        String thisUrl = url.getNormalized();
        String thisBase = url.getBase();

        BufferedWriter writer = null;
        BufferedReader reader = null;

        boolean found = false;
        String properName = thisBase.replace("http://", "");
        properName = properName.replace("https://", "");
        String fileName = robotsDirectory + "\\" + properName;
        File file = new File(fileName);
        try {
            URI uri = new URI(url.getRobots());
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            writer = new BufferedWriter(new FileWriter(file));
            String line;
            boolean foundAgent = false;

            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().startsWith("user-agent"))
                    if (line.endsWith("*") || line.endsWith(USER_AGENT))
                        foundAgent = true;

                if (foundAgent) {
                    while ((line = reader.readLine()) != null && foundAgent == true) {
                        if (line.toLowerCase().startsWith("user-agent"))
                            foundAgent = false; // a break would do bc it doesn't matter in our case but i think this is
                                                // the right thing to do
                        else if (line.toLowerCase().startsWith("disallow: ")) {
                            String path = line.substring("disallow: ".length());
                            try { // writing in the file
                                writer.write("D: " + thisBase + path);
                                writer.newLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (url.getNormalized().contains(path))
                                found = true;

                        } else if (line.toLowerCase().startsWith("allow: ")) {
                            String path = line.substring("allow: ".length());
                            try {
                                writer.write("A: " + thisBase + path);
                                writer.newLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String exactPath = thisBase + line.substring("allow: ".length());
                            if (thisUrl.equals(exactPath)) {
                                found = false;
                            }
                        }
                    }
                }
            }
            writer.flush();
        } catch (IOException e) {
            System.err.println("Unable to access this url(403)");
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }

        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return found;
    }

    public static boolean canBeCrawled(Url url) {
        boolean[] disallowed = new boolean[1];
        disallowed[0] = false;
        boolean robotsFileExists = checkRobotsDirectory(url, disallowed);
        if (robotsFileExists)
            return !disallowed[0];
        return !writeRobotsFile(url);
    }

    // test function
    public static void Test() {
        try {
            FileReader fileReader = new FileReader("SpiderDuck\\src\\main\\java\\com\\crawler\\seed.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Url url = new Url(line);
                System.out.println(canBeCrawled(url));
            }

            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    public static void main(String[] argv) {
        Test();
    }

}
