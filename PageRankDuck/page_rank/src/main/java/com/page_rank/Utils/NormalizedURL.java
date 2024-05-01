package com.page_rank.Utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class NormalizedURL {
    public String normalize(String url, String baseUrl) throws MalformedURLException, URISyntaxException {
        if (url == null || url.isEmpty()) {
            throw new MalformedURLException("Empty URL");
        }
        url = url.trim();
        if (url.startsWith("ms-windows-store")) {
            System.err.println("Warning: Unknown protocol: ms-windows-store in URL: " + url);
            return url;
        }

        try {
            URI uri = new URI(url);
            url = uri.getScheme() + "://" + uri.getHost() + uri.getPath();
        } catch (URISyntaxException e) {
            int fragmentIndex = url.indexOf('#');
            if (fragmentIndex > -1) {
                url = url.substring(0, fragmentIndex);
            }
        }
        url = url.toLowerCase();
        if (!url.startsWith("http") && !url.startsWith("https") && baseUrl != null) {
            url = resolveRelativePath(url, baseUrl);
        }
        url = removeDefaultPort(url);
        return url;
    }

    private static String resolveRelativePath(String relativeUrl, String baseUrl)
            throws MalformedURLException, URISyntaxException {
        URI baseUri = new URI(baseUrl);
        URI relativeUri = new URI(relativeUrl);
        URI resolvedUri = baseUri.resolve(relativeUri);
        return resolvedUri.toString();
    }

    private static String removeDefaultPort(String url) throws MalformedURLException {
        URL parsedUrl = new URL(url);
        int defaultPort = parsedUrl.getDefaultPort();
        if (parsedUrl.getPort() == defaultPort) {
            return new URL(parsedUrl.getProtocol(), parsedUrl.getHost(), url).toString();
        }
        return url;
    }

}
