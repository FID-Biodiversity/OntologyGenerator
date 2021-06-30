package de.biofid.services.data.gbif;

import de.biofid.services.exceptions.NoGbifUriException;

public class GbifUriFactory {

    public static final String GBIF_STRING = "gbif";

    /**
     * Extracts a GBIF taxon ID from a given URI.
     * If the given URI does not contain a GBIF ID, throws a NoSuchElementException.
     */
    public static String extractGbifIdFromUri(String uri) throws NoGbifUriException {
        if (!uri.toLowerCase().contains(GBIF_STRING)) {
            throwBecauseNoGbifIdIsGiven();
        }

        String gbifIdString;
        if (uri.contains("http")) {
            gbifIdString = extractFromBiofidUri(uri);
        } else {
            gbifIdString = extractFromGbifNamespace(uri);
        }

        if (!isNumeric(gbifIdString)) {
            throw new NoGbifUriException("The given URI \"" + uri + "\" is not a GBIF URI!");
        }

        return gbifIdString;
    }

    private static String extractFromBiofidUri(String uri) {
        String[] splitUri = uri.split("/");
        return splitUri[splitUri.length - 1];
    }

    private static String extractFromGbifNamespace(String uri) {
        String[] splitUri = uri.split(":");
        return splitUri[splitUri.length - 1];
    }

    private static boolean isNumeric(String string) {
        try {
            int i = Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }

    private static void throwBecauseNoGbifIdIsGiven() throws NoGbifUriException {
        throw new NoGbifUriException("The given URI does not contain a GBIF ID!");
    }
}
