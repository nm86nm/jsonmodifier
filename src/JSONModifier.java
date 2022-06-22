import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;


public class JSONModifier {
    private static final int ABRIDGEMENT = 1;
    private static final int PLACES = 3;
    private static final String COUNTRY_NAME = "Russia";
    private static final String FILE_NAME = "russia.geojson";
    private static final String PATH_TO_DIRECTORY = "src/geojson_files/utc (a1p3)/russia/";
    private static final String PATH_TO_FILE = PATH_TO_DIRECTORY + FILE_NAME;

    public static void main(String[] args) throws URISyntaxException {
        JSONModifier jsonModifier = new JSONModifier();

        File file = new File("C:\\Users\\nm86n\\Documents\\Programming\\JSON\\GeoJSON\\Time Zones\\UTC+2\\Russia\\russia multipolygon pattern.geojson");
        List<String> baseFile = jsonModifier.fileReader(file);;
        jsonModifier.printListString(baseFile);

        // info
        /*jsonModifier.printListString(listPolygons);
        System.out.println("Polygons number: " + polygonsNumber);
        System.out.println("Index of the biggest polygon: " + indexOfTheBiggestPolygon);*/

        // first part
        /*List<String> geoJSONwithChosenPolygon = jsonModifier.geoJSONWithChosenPolygon(indexOfTheBiggestPolygon, PATH_TO_FILE, COUNTRY_NAME);
        jsonModifier.fileWriter("src//geojson_files//corrected_countries//" + COUNTRY_NAME.toLowerCase() + "//" + COUNTRY_NAME.toLowerCase()
                + " main polygon 1.geojson", geoJSONwithChosenPolygon);*/

        // second part
        /*List<String> geoJSONWithMainPolygon = jsonModifier.fileReader(
                "src//geojson_files//corrected_countries//" + COUNTRY_NAME.toLowerCase() + "//" +
                        COUNTRY_NAME.toLowerCase() + " main polygon 1 (corrected 1).geojson");
        jsonModifier.fileWriter("src//geojson_files//corrected_countries//" + COUNTRY_NAME.toLowerCase() + "//"
                        + COUNTRY_NAME.toLowerCase() + " (corrected 1).geojson",
                jsonModifier.replacePolygon(geoJSONwithAllPolygons, indexOfTheBiggestPolygon,
                        jsonModifier.abridgementOfTheCoordinates(jsonModifier.getListCoordinatesForCountryName(
                                jsonModifier.geoJSONformat(jsonModifier.correctedCoordinates(geoJSONWithMainPolygon)), COUNTRY_NAME))));*/

        // optional
        /*List<String> fileReader = jsonModifier.fileReader(PATH_TO_FILE);
        String correctedCoordinates = jsonModifier.correctedCoordinates(fileReader);
        List<String> geoJSONformat = jsonModifier.geoJSONformat(correctedCoordinates);
        List<String> listCoordinates = jsonModifier.getListCoordinatesForCountryName(geoJSONformat, COUNTRY_NAME);
        List<String> abridgementOfTheCoordinates = jsonModifier.abridgementOfTheCoordinates(listCoordinates);
        List<String> geoJSONwithoutCoordinates = jsonModifier.geoJSONwithoutCoordinates(geoJSONformat);

        jsonModifier.fileWriter("src//geojson_files//utc (a1p3)//democratic_republic_of_the_congo_UTC+1 (corrected).geojson",
                jsonModifier.createGeoJSONfile(geoJSONwithoutCoordinates, abridgementOfTheCoordinates));*/
    }

    public List<String> abridgementOfTheCoordinates(List<String> listCoordinates) {
        List<String> listIndexesPolygons = searchPolygons(listCoordinates);
        List<String> listAbridgmentCoordinates = new ArrayList<>();

        for (int i = 0; i < listIndexesPolygons.size(); i++) {
            String stringFirstIndexForPolygon = listIndexesPolygons.get(i).substring(listIndexesPolygons.get(i).indexOf("from") + 5,
                    listIndexesPolygons.get(i).indexOf("to") - 1);
            String stringLastIndexForPolygon = listIndexesPolygons.get(i).substring(listIndexesPolygons.get(i).indexOf("to") + 3,
                    listIndexesPolygons.get(i).indexOf(", size"));
            int intFirstIndexForPolygon = Integer.parseInt(stringFirstIndexForPolygon);
            int intLastIndexForPolygon = Integer.parseInt(stringLastIndexForPolygon);

            listAbridgmentCoordinates.add(roundCoordinates(listCoordinates.get(Integer.valueOf(stringFirstIndexForPolygon))));

            while ((intFirstIndexForPolygon + ABRIDGEMENT) < intLastIndexForPolygon) {
                listAbridgmentCoordinates.add(roundCoordinates(listCoordinates.get(intFirstIndexForPolygon + ABRIDGEMENT)));
                intFirstIndexForPolygon += ABRIDGEMENT;
            }

            listAbridgmentCoordinates.add(roundCoordinates(listCoordinates.get(Integer.valueOf(stringLastIndexForPolygon))));
        }

        return listAbridgmentCoordinates;
    }

    public List<String> addPolygonToMultiPolygon(List<String> multiPolygonFile, List<String> polygonCoordinates) {
        List<String> sumMultiPolygonAndPolygon = new ArrayList<>();

        for (int i1 = 0; i1 < multiPolygonFile.size(); i1++) {
            if (!multiPolygonFile.get(i1).contains("coordinates"))
                sumMultiPolygonAndPolygon.add(multiPolygonFile.get(i1));
            else {
                String temp = multiPolygonFile.get(i1).substring(0, multiPolygonFile.get(i1).length() - 6) + ", ";

                for (int i2 = 0; i2 < polygonCoordinates.size(); i2++) {
                    if (i2 < polygonCoordinates.size() - 1)
                        temp += polygonCoordinates.get(i2) + ", ";
                    else
                        temp += polygonCoordinates.get(i2) + " ] ] ] } }";
                }

                sumMultiPolygonAndPolygon.add(temp);
            }
        }

        return sumMultiPolygonAndPolygon;
    }

    public void allCountriesInSingleFiles() {
        List<String> listCountriesNames = getListCountriesNames(
                fileReader("src\\geojson_files\\all countries in one file\\countries.geojson"));

        int counterCountries = 1;
        for (String tmp : listCountriesNames) {
            int indexBracket = tmp.indexOf("(") - 1;

            if (tmp.substring(indexBracket + 3).contains("("))
                indexBracket = tmp.indexOf("(", indexBracket + 3) - 1;

            tmp = tmp.substring(0, indexBracket);

            List<String> listFileContent = createAbridgedGeoJSONFileWithOneCountry(tmp);
            tmp = tmp.toLowerCase();

            for (int i = 0; i < tmp.length(); i++) {
                if (tmp.charAt(i) == ' ')
                    tmp = tmp.substring(0, i) + "_" + tmp.substring(i + 1);
            }

            tmp += ".geojson";

            System.out.println(counterCountries + ". " + tmp);
            counterCountries++;

            File filePath = new File("src\\geojson_files\\every country in single files (a" + ABRIDGEMENT + "p" + PLACES + ")\\");
            Boolean pathExist = filePath.exists();

            if (!pathExist)
                filePath.mkdir();

            fileWriter("src\\geojson_files\\every country in single files (a" + ABRIDGEMENT + "p" + PLACES + ")\\" + tmp, listFileContent);
        }
    }

    public List<String> coordinatesForChosenPolygon(List<String> listCoordinates, int indexForChosenPolygon) {
        List<String> coordinatesForChosenPolygon = new ArrayList<>();
        List<String> indexesForPolygon = indexesForPolygons(listCoordinates);
        int firstIndex = Integer.parseInt(indexesForPolygon.get(indexForChosenPolygon).substring(0, indexesForPolygon.get(indexForChosenPolygon).indexOf(",")));
        int lastIndex = Integer.parseInt(indexesForPolygon.get(indexForChosenPolygon).substring(indexesForPolygon.get(indexForChosenPolygon).indexOf(",") + 2));

        for (int i = firstIndex; i <= lastIndex; i++)
            coordinatesForChosenPolygon.add(listCoordinates.get(i));

        return coordinatesForChosenPolygon;
    }


    public String correctedCoordinates(List<String> geojsonFile) {
        String data = "";

        for (int i = 0; i < geojsonFile.get(0).length(); i++) {
            switch (geojsonFile.get(0).charAt(i)) {
                case '[':
                    data += "[ ";
                    break;
                case ',':
                    data += ", ";
                    break;
                case ']':
                    data += " ]";
                    break;
                case '}':
                    data += " }";
                    break;
                case '{':
                    data += "{ ";
                    break;
                case ':':
                    data += ": ";
                    break;
                default:
                    data += geojsonFile.get(0).charAt(i);
            }
        }

        return data;
    }

    public List<String> createAbridgedGeoJSONFileWithAllCountries() {
        List<String> fileWithAllCountries = fileReader("src\\geojson_files\\base.geojson");
        List<String> listCountries = fileReader("src\\geojson_files\\all countries in one file\\countries.geojson");
        List<String> abridgmentListCoordinates = new ArrayList<>();
        StringBuilder stringCountry = new StringBuilder();

        for (int i = 4; i <= 258; i++) {
            abridgmentListCoordinates = abridgementOfTheCoordinates(getListCoordinatesForCountryIndex(listCountries, i));

            if (polygonsNumber(getListCoordinatesForCountryIndex(listCountries, i)) == 1) {
                stringCountry = new StringBuilder(listCountries.get(i));
                stringCountry = new StringBuilder(stringCountry.substring(0, stringCountry.indexOf("[") + 4));

                for (String tmp : abridgmentListCoordinates) {
                    stringCountry.append(tmp);
                    stringCountry.append(", ");
                }

                stringCountry = new StringBuilder(stringCountry.substring(0, stringCountry.length() - 2));
                stringCountry.append(" ] ] } }");

                fileWithAllCountries.add(i - 1, stringCountry.toString());
            } else if (polygonsNumber(getListCoordinatesForCountryIndex(listCountries, i)) > 1) {
                stringCountry = new StringBuilder(listCountries.get(i));
                stringCountry = new StringBuilder(stringCountry.substring(0, stringCountry.indexOf("[") + 6));
                List<String> listIndexesPolygons = searchPolygons(abridgmentListCoordinates);
                int firstIndexForPolygon = 0;
                int lastIndexForPolygon = 0;

                for (int i1 = 0; i1 < listIndexesPolygons.size(); i1++) {
                    firstIndexForPolygon = Integer.parseInt(listIndexesPolygons.get(i1).substring(listIndexesPolygons.get(i1).indexOf("from") + 5, listIndexesPolygons.get(i1).indexOf("to") - 1));
                    lastIndexForPolygon = Integer.parseInt(listIndexesPolygons.get(i1).substring(listIndexesPolygons.get(i1).indexOf("to") + 3));

                    for (int i2 = firstIndexForPolygon; i2 < lastIndexForPolygon; i2++)
                        stringCountry.append(abridgmentListCoordinates.get(i2)).append(", ");

                    stringCountry = new StringBuilder(stringCountry.substring(0, stringCountry.length() - 2));
                    stringCountry.append(" ] ], [ [ ");
                }

                stringCountry = new StringBuilder(stringCountry.substring(0, stringCountry.length() - 6) + " ] } }");

                fileWithAllCountries.add(i - 1, stringCountry.toString());
            }
        }

        return fileWithAllCountries;
    }

    public List<String> createAbridgedGeoJSONFileWithOneCountry(String countryName) {
        List<String> fileWithCountry = fileReader("src\\geojson_files\\base.geojson");
        List<String> listCountries = fileReader("src\\geojson_files\\all countries in one file\\countries.geojson");
        List<String> abridgmentListCoordinates = abridgementOfTheCoordinates(getListCoordinatesForCountryName(listCountries, countryName));
        StringBuilder stringCountry = new StringBuilder();
        boolean foundCountryName = getCountryIndex(listCountries, countryName) > 0;

        if (foundCountryName) {
            if (polygonsNumber(getListCoordinatesForCountryName(listCountries, countryName)) == 1) {
                stringCountry = new StringBuilder(listCountries.get(getCountryIndex(listCountries, countryName)));
                stringCountry = new StringBuilder(stringCountry.substring(0, stringCountry.indexOf("[") + 4));

                for (String tmp : abridgmentListCoordinates) {
                    stringCountry.append(tmp);
                    stringCountry.append(", ");
                }

                stringCountry = new StringBuilder(stringCountry.substring(0, stringCountry.length() - 2));
                stringCountry.append(" ] ] } }");

                fileWithCountry.add(3, stringCountry.toString());
            } else if (polygonsNumber(getListCoordinatesForCountryName(listCountries, countryName)) > 1) {
                stringCountry = new StringBuilder(listCountries.get(getCountryIndex(listCountries, countryName)));
                stringCountry = new StringBuilder(stringCountry.substring(0, stringCountry.indexOf("[") + 6));
                List<String> listIndexesPolygons = searchPolygons(abridgmentListCoordinates);
                int firstIndexForPolygon = 0;
                int lastIndexForPolygon = 0;

                for (int i1 = 0; i1 < listIndexesPolygons.size(); i1++) {
                    firstIndexForPolygon = Integer.parseInt(listIndexesPolygons.get(i1).substring(listIndexesPolygons.get(i1).indexOf("from") + 5, listIndexesPolygons.get(i1).indexOf("to") - 1));
                    lastIndexForPolygon = Integer.parseInt(listIndexesPolygons.get(i1).substring(listIndexesPolygons.get(i1).indexOf("to") + 3));

                    for (int i2 = firstIndexForPolygon; i2 <= lastIndexForPolygon; i2++) {
                        if (i2 != lastIndexForPolygon)
                            stringCountry.append(abridgmentListCoordinates.get(i2)).append(", ");
                        else
                            stringCountry.append(abridgmentListCoordinates.get(i2)).append("  ");
                    }

                    stringCountry = new StringBuilder(stringCountry.substring(0, stringCountry.length() - 2));

                    if (i1 != listIndexesPolygons.size())
                        stringCountry.append(" ] ], [ [ ");
                    else if (i1 == listIndexesPolygons.size() - 1)
                        stringCountry.append(" ] ] ] ");
                }

                stringCountry = new StringBuilder(stringCountry.substring(0, stringCountry.length() - 6) + " ] } }");

                fileWithCountry.add(3, stringCountry.toString());
            }
        } else
            System.out.println("Not Found Country Name " + countryName + "!");

        return fileWithCountry;
    }

    public List<String> createGeoJSONfile(List<String> geoJSONwithoutCoordinates, List<String> listCoordinates) {
        List<String> geoJSONfile = new LinkedList<>();
        String baseString = geoJSONwithoutCoordinates.get(3).substring(0, geoJSONwithoutCoordinates.get(3).indexOf("[ [") + 4);

        for (int i = 0; i < listCoordinates.size(); i++)
            if (i < listCoordinates.size() - 1)
                baseString += listCoordinates.get(i) + ", ";
            else
                baseString += listCoordinates.get(i) + " ] ] } }";

        for (int i = 0; i < geoJSONwithoutCoordinates.size(); i++)
            if (i != 3)
                geoJSONfile.add(geoJSONwithoutCoordinates.get(i));
            else
                geoJSONfile.add(baseString);

        return geoJSONfile;
    }

    public List<String> createGeoJSONFileWithCoordinates(List<String> listCoordinates) {    // String countryName
        List<String> fileWithCountry = fileReader("src\\geojson_files\\base.geojson");
        //List<String> listCountries = fileReader("src\\geojson_files\\every country in single files (a3p3)\\" + countryName.toLowerCase() + ".geojson");
        String countryData = "{ \"type\": \"Feature\", \"properties\": { \"ADMIN\": \"Canada\", \"ISO_A3\": \"CAN\" }, \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ ";

        for (int i = 0; i < listCoordinates.size(); i++) {
            if (i != listCoordinates.size() - 1)
                countryData += listCoordinates.get(i) + ", ";
            else
                countryData += listCoordinates.get(i);
        }

        countryData += " ] ] } }";
        fileWithCountry.add(3, countryData);

        return fileWithCountry;
    }

    public List<String> fileReader(File file) {
        List<String> listStringFile = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String stringLine;
            while ((stringLine = bufferedReader.readLine()) != null) {
                listStringFile.add(stringLine);
            }

            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return listStringFile;
    }

    public List<String> fileReader(String fileName) {
        List<String> listStringFile = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String stringLine;
            while ((stringLine = bufferedReader.readLine()) != null)
                listStringFile.add(stringLine);

            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return listStringFile;
    }

    public void fileWriter(String fileName, List<String> listFileContent) {
        String stringFileContent = "";

        for (String tmp : listFileContent) {
            stringFileContent += tmp;
            stringFileContent += "\n";
        }

        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(stringFileContent);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileWriter(String fileName, String fileContent) {
        /*String stringFileContent = "";

        for (String tmp : fileContent) {
            stringFileContent += tmp;
            stringFileContent += "\n";
        }*/

        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(fileContent);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public List<String> geojsonRightHandRule(List<String> geojsonFile){
        List<String> rightHandGeoJsonFile = geojsonFile;

        return rightHandGeoJsonFile;
    }*/

    public List<String> formattedCoordinates(List<String> geoJsonFile) {
        List<String> coordinates = new ArrayList<String>();
        //StringBuilder stringCountry = new StringBuilder(stringCountry.substring(0, stringCountry.indexOf("[") + 4));
        //String test = geoJsonFile.get(15).substring(10);

        for (int i = 11; i < geoJsonFile.size() - 5; i += 4)
            coordinates.add("[ " + geoJsonFile.get(i).substring(14) + geoJsonFile.get(i + 1).substring(13) + " ]");

        return coordinates;
    }

    public List<String> geoJSONformat(List<String> geoJSONfile) {
        List<String> geoJSONformated = new ArrayList<>();
        geoJSONformated.add("{");
        geoJSONformated.add("\"type\": \"FeatureCollection\",");
        geoJSONformated.add("\"features\": [");
        geoJSONformated.add(
                geoJSONfile.get(0).substring(geoJSONfile.get(0).indexOf("{ \"type\": \"Feature\""), geoJSONfile.get(0).length() - 4)
        );
        geoJSONformated.add("]");
        geoJSONformated.add("}");

        return geoJSONformated;
    }

    public List<String> geoJSONformat(String geoJSONfile) {
        List<String> geoJSONformated = new ArrayList<>();

        geoJSONformated.add("{");
        geoJSONformated.add("\"type\": \"FeatureCollection\",");
        geoJSONformated.add("\"features\": [");
        geoJSONformated.add(geoJSONfile.substring(geoJSONfile.indexOf("{ \"type\": \"Feature\""), geoJSONfile.length() - 4));
        geoJSONformated.add("]");
        geoJSONformated.add("}");

        return geoJSONformated;
    }

    public List<String> geoJSONwithoutCoordinates(List<String> geoJSONwithOneCountry) {
        List<String> geoJSONwithoutCoordinates = new ArrayList<>();

        for (int i = 0; i < geoJSONwithOneCountry.size(); i++) {
            if (!geoJSONwithOneCountry.get(i).contains("coordinates"))
                geoJSONwithoutCoordinates.add(geoJSONwithOneCountry.get(i));
            else if (geoJSONwithOneCountry.get(i).contains("coordinates") && geoJSONwithOneCountry.get(i).contains("MultiPolygon")) {
                int indexBracketsPlus6 = geoJSONwithOneCountry.get(i).indexOf("[ [ [") + 6;
                geoJSONwithoutCoordinates.add(geoJSONwithOneCountry.get(i).substring(0, indexBracketsPlus6) + " ] ] ] } }");
            } else if (geoJSONwithOneCountry.get(i).contains("coordinates") && geoJSONwithOneCountry.get(i).contains("Polygon")) {
                int indexBracketsPlus4 = geoJSONwithOneCountry.get(i).indexOf("[ [") + 4;
                geoJSONwithoutCoordinates.add(geoJSONwithOneCountry.get(i).substring(0, indexBracketsPlus4) + " ] ] } }");
            }
        }

        return geoJSONwithoutCoordinates;
    }

    public List<String> geoJSONWithChosenPolygon(int indexOfThePolygon, String pathToFile, String countryName) {
        List<String> geoJSONWithChosenPolygon = new ArrayList<>();
        List<String> geoJSONwithoutCoordinates = geoJSONwithoutCoordinates(fileReader(pathToFile));
        List<String> listCoordinatesForAllPolygons = getListCoordinatesForCountryName(fileReader(pathToFile), countryName);
        String stringCoordinatesForOnePolygon = "";
        List<String> searchedPolygons = searchPolygons(listCoordinatesForAllPolygons);
        String rangeCoordinates = searchedPolygons.get(indexOfThePolygon);
        int firstIndex = Integer.parseInt(rangeCoordinates.substring(rangeCoordinates.indexOf("from") + 5, rangeCoordinates.indexOf("to") - 1));
        int lastIndex = Integer.parseInt(rangeCoordinates.substring(rangeCoordinates.indexOf("to") + 3, rangeCoordinates.indexOf(", size")));

        for (int i = firstIndex; i <= lastIndex; i++) {
            if (i < lastIndex)
                stringCoordinatesForOnePolygon += listCoordinatesForAllPolygons.get(i) + ", ";
            else
                stringCoordinatesForOnePolygon += listCoordinatesForAllPolygons.get(i);
        }

        for (int i = 0; i < geoJSONwithoutCoordinates.size(); i++) {
            if (i != 3) {
                geoJSONWithChosenPolygon.add(geoJSONwithoutCoordinates.get(i));
            } else {
                geoJSONWithChosenPolygon.add(
                        geoJSONwithoutCoordinates.get(3).substring(0, geoJSONwithoutCoordinates.get(3).indexOf("MultiPolygon"))
                                + geoJSONwithoutCoordinates.get(3).substring(geoJSONwithoutCoordinates.get(3).indexOf("MultiPolygon") + 5,
                                geoJSONwithoutCoordinates.get(3).length() - 12) + stringCoordinatesForOnePolygon + " ] ] } }");
            }
        }

        return geoJSONWithChosenPolygon;
    }

    public List<String> getListCountriesNames(List<String> listCountries) {
        List<String> listCountriesNames = new ArrayList<>();

        for (int i1 = 4; i1 <= 258; i1++) {
            int indexQuote = listCountries.get(i1).indexOf("\"", 47);
            String countryName = listCountries.get(i1).substring(47, indexQuote);

            for (int i2 = 1; i2 < 4; i2++)
                indexQuote = listCountries.get(i1).indexOf("\"", indexQuote + 1);

            String countryAbridgementName = listCountries.get(i1).substring(indexQuote + 1,
                    listCountries.get(i1).indexOf("\"", indexQuote + 1));

            listCountriesNames.add(countryName + " (" + countryAbridgementName + ")");
        }

        return listCountriesNames;
    }

    public int getCountryIndex(List<String> text, String countryName) {
        int countryIndex = 3;

        int startLine = 0;
        for (int i = 0; i < text.size(); i++) {
            if (text.get(i).contains("\"features\": ["))
                startLine = ++i;
        }

        for (int i = startLine; i <= text.size() - 2; i++) {                       // old version i<=258
            if (text.get(i).length() > 152) {
                if (text.get(i).substring(47, text.get(i).indexOf(",", 48) - 1).equals(countryName)) {
                    countryIndex = i;
                    break;
                }

                if (!text.get(text.size() - 2).contains(countryName) && i == text.size() - 2) {
                    countryIndex = 0;
                }
            }
        }

        return countryIndex;
    }

    public List<String> getListCoordinatesForCountryIndex(List<String> listCountries, int countryIndex) {
        int startIndex = listCountries.get(countryIndex).indexOf("[");
        int endIndex = listCountries.get(countryIndex).indexOf("]") + 1;
        List<String> listCoordinates = new ArrayList<>();

        try {
            while (!listCountries.get(countryIndex).substring(endIndex, endIndex + 9).contains("}")) {
                while (listCountries.get(countryIndex).charAt(endIndex) == ']' && listCountries.get(countryIndex).charAt(endIndex + 2) == ']') {
                    endIndex += 2;
                    startIndex = listCountries.get(countryIndex).indexOf("[", startIndex + 1);
                }

                while (listCountries.get(countryIndex).charAt(startIndex) == '[' && listCountries.get(countryIndex).charAt(startIndex + 2) == '[') {
                    startIndex += 2;
                    endIndex = listCountries.get(countryIndex).indexOf("]", startIndex) + 1;

                    if (!(listCountries.get(countryIndex).charAt(startIndex) == '[' && listCountries.get(countryIndex).charAt(startIndex + 2) == '['))
                        listCoordinates.add(listCountries.get(countryIndex).substring(startIndex, endIndex));
                }

                startIndex = listCountries.get(countryIndex).indexOf("[", startIndex + 1);
                endIndex = listCountries.get(countryIndex).indexOf("]", endIndex + 1);

                listCoordinates.add(listCountries.get(countryIndex).substring(startIndex, endIndex + 1));
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return listCoordinates;
    }

    public List<String> getListCoordinatesForCountryName(List<String> geoJSONfile, String countryName) {
        int countryIndex = getCountryIndex(geoJSONfile, countryName);
        boolean foundNameCountry = true;

        if (countryIndex == 0)
            foundNameCountry = false;

        int startIndex = geoJSONfile.get(countryIndex).indexOf("[");
        int endIndex = geoJSONfile.get(countryIndex).indexOf("]") + 1;
        List<String> listCoordinates = new ArrayList<>();

        if (!foundNameCountry)
            listCoordinates.add("Not Found Country Name!");

        try {
            while (!geoJSONfile.get(countryIndex).substring(endIndex, endIndex + 9).contains("}") && foundNameCountry) {
                while (geoJSONfile.get(countryIndex).charAt(endIndex) == ']' && geoJSONfile.get(countryIndex).charAt(endIndex + 2) == ']') {
                    endIndex += 2;
                    startIndex = geoJSONfile.get(countryIndex).indexOf("[", startIndex + 1);
                }

                while (geoJSONfile.get(countryIndex).charAt(startIndex) == '[' && geoJSONfile.get(countryIndex).charAt(startIndex + 2) == '[') {
                    startIndex += 2;
                    endIndex = geoJSONfile.get(countryIndex).indexOf("]", startIndex) + 1;

                    if (!(geoJSONfile.get(countryIndex).charAt(startIndex) == '[' && geoJSONfile.get(countryIndex).charAt(startIndex + 2) == '['))
                        listCoordinates.add(geoJSONfile.get(countryIndex).substring(startIndex, endIndex));
                }

                startIndex = geoJSONfile.get(countryIndex).indexOf("[", startIndex + 1);
                endIndex = geoJSONfile.get(countryIndex).indexOf("]", endIndex + 1);

                listCoordinates.add(geoJSONfile.get(countryIndex).substring(startIndex, endIndex + 1));
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return listCoordinates;
    }

    public String getUTCforPolygon(List<String> polygon) {
        JSONModifier jsonModifier = new JSONModifier();
        String polygonData = polygon.get(3);
        int beginIndex = polygonData.indexOf("[ [ [ ") + 6;
        int endIndex = beginIndex + polygonData.substring(beginIndex, beginIndex + 10).indexOf(",");
        String longitudeString = polygonData.substring(beginIndex, endIndex);
        double longitude = Double.parseDouble(polygonData.substring(beginIndex, endIndex));

        int utcNumber = -12;
        for (int i = -165; i <= 180; i += 15) {
            if (longitude < i)
                break;
            utcNumber += 1;
        }

        String utcName = "";
        utcNumber += 1;
        if (utcNumber > 0)
            utcName = "UTC+" + utcNumber;
        else if (utcNumber == 0)
            utcName = "UTC";
        else
            utcName = "UTC" + utcNumber;

        return utcName;
    }

    public List<String> indexesForPolygons(List<String> listCoordinates) {
        List<String> searchedPolygons = searchPolygons(listCoordinates);
        List<String> indexesForPolygons = new ArrayList<>();
        String firstIndexForPolygon = "";
        String lastIndexForPolygon = "";

        for (int i = 0; i < searchedPolygons.size(); i++) {
            firstIndexForPolygon = searchedPolygons.get(i).substring(searchedPolygons.get(i).indexOf("from") + 5, searchedPolygons.get(i).indexOf("to") - 1);
            lastIndexForPolygon = searchedPolygons.get(i).substring(searchedPolygons.get(i).indexOf("to") + 3, searchedPolygons.get(i).indexOf(", size"));

            indexesForPolygons.add(firstIndexForPolygon + ", " + lastIndexForPolygon);
        }

        return indexesForPolygons;
    }

    public int indexOfTheBiggestPolygon(List<String> listCoordinates) {
        List<String> indexesForPolygon = indexesForPolygons(listCoordinates);
        int indexOfTheBiggestPolygon = 0;
        int size = 0;

        for (int i = 0; i < indexesForPolygon.size(); i++) {
            String firstIndex = indexesForPolygon.get(i).substring(0, indexesForPolygon.get(i).indexOf(","));
            String lastIndex = indexesForPolygon.get(i).substring(indexesForPolygon.get(i).indexOf(",") + 2);

            if (size < Integer.parseInt(lastIndex) - Integer.parseInt(firstIndex)) {
                size = Integer.parseInt(lastIndex) - Integer.parseInt(firstIndex);
                indexOfTheBiggestPolygon = i;
                //System.out.println(i+1 + ". firstIndex: " + firstIndex + ", lastIndex: " + lastIndex + ", size: " +size);
            }
        }

        return indexOfTheBiggestPolygon;
    }

    public int polygonsNumber(List<String> listCoordinates) {
        List<String> indexesForPolygon = searchPolygons(listCoordinates);

        int beginIndex = indexesForPolygon.get(indexesForPolygon.size() - 1).indexOf(".") + 1;
        int endIndex = indexesForPolygon.get(indexesForPolygon.size() - 1).indexOf(":");

        return Integer.parseInt(indexesForPolygon.get(indexesForPolygon.size() - 1).substring(beginIndex, endIndex));
    }

    public void printListString(List<String> listStrings) {
        System.out.print("\n");
        int counter = 1;
        for (String stringCordinates : listStrings) {
            System.out.println(counter + ". " + stringCordinates);
            counter++;
        }
    }

    public List<String> replacePolygon(List<String> geoJSONwithAllPolygons, int indexPolygonToReplace, List<String> coordinatesForOnePolygon) {
        List<String> listCoordinatesForAllPolygons = getListCoordinatesForCountryName(geoJSONwithAllPolygons, COUNTRY_NAME);
        List<String> indexesForPolygons = indexesForPolygons(listCoordinatesForAllPolygons);
        List<String> correctedCountry = new ArrayList<>();
        String rangeForChosenPolygon = indexesForPolygons(listCoordinatesForAllPolygons).get(indexPolygonToReplace);
        int firstIndexForChosenPolygon = Integer.parseInt(rangeForChosenPolygon.substring(0, rangeForChosenPolygon.indexOf(",")));
        int lastIndexForChosenPolygon = Integer.parseInt(rangeForChosenPolygon.substring(rangeForChosenPolygon.indexOf(",") + 2));

        int polygonsNumber = polygonsNumber(listCoordinatesForAllPolygons);

        for (int i1 = 0; i1 < geoJSONwithAllPolygons.size(); i1++) {
            if (i1 != 3)
                correctedCountry.add(geoJSONwithAllPolygons.get(i1));
            else {
                String correctedCoordinates = "";
                int polygonNumber = 0;

                for (int i2 = 0; i2 < firstIndexForChosenPolygon; i2++) {
                    if (i2 < firstIndexForChosenPolygon - 1) {
                        if (i2 != Integer.parseInt(indexesForPolygons.get(polygonNumber)
                                .substring(indexesForPolygons.get(polygonNumber).indexOf(",") + 2)))
                            correctedCoordinates += listCoordinatesForAllPolygons.get(i2) + ", ";
                        else {
                            correctedCoordinates += listCoordinatesForAllPolygons.get(i2) + " ] ], [ [ ";
                            polygonNumber++;
                        }
                    } else {
                        correctedCoordinates += listCoordinatesForAllPolygons.get(i2) + " ] ], [ [ ";
                        polygonNumber++;
                    }
                }

                for (int i2 = 0; i2 < coordinatesForOnePolygon.size(); i2++) {
                    if (i2 < coordinatesForOnePolygon.size() - 1)
                        correctedCoordinates += coordinatesForOnePolygon.get(i2) + ", ";
                    else {
                        if (indexPolygonToReplace + 1 < indexesForPolygons.size()) {
                            correctedCoordinates += coordinatesForOnePolygon.get(i2) + " ] ], [ [ ";
                            polygonNumber++;
                        } else {
                            correctedCoordinates += coordinatesForOnePolygon.get(i2);
                            polygonNumber++;
                        }
                    }
                }

                for (int i2 = lastIndexForChosenPolygon + 1; i2 < listCoordinatesForAllPolygons.size(); i2++) {
                    if (i2 != listCoordinatesForAllPolygons.size() - 1) {
                        if (i2 != Integer.parseInt(indexesForPolygons.get(polygonNumber)
                                .substring(indexesForPolygons.get(polygonNumber).indexOf(",") + 2)))
                            correctedCoordinates += listCoordinatesForAllPolygons.get(i2) + ", ";
                        else {
                            correctedCoordinates += listCoordinatesForAllPolygons.get(i2) + " ] ], [ [ ";
                            polygonNumber++;
                        }
                    } else
                        correctedCoordinates += listCoordinatesForAllPolygons.get(i2);
                }

                correctedCountry.add(geoJSONwithAllPolygons.get(i1).substring(0,
                        geoJSONwithAllPolygons.get(i1).indexOf("[ [ [") + 6) + correctedCoordinates + " ] ] ] } }");
            }
        }

        //fileWriter("src//geojson_files//chile//chile corrected.geojson", correctedCountry);

        return correctedCountry;
    }

    public String roundCoordinates(String coordinates) {
        double roundedLongitude = roundDouble(Double.parseDouble(coordinates.substring(2, coordinates.indexOf(","))));
        double roundedLatitude = roundDouble(Double.parseDouble(coordinates.substring(coordinates.indexOf(",") + 2, coordinates.length() - 2)));

        return "[ " + roundedLongitude + ", " + roundedLatitude + " ]";
    }

    public double roundDouble(double value) {
        if (PLACES < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, PLACES);
        value = value * factor;
        long tmp = Math.round(value);
        value = (double) tmp / factor;

        return value;
    }

    public List<String> searchPolygons(List<String> listCoordinates) {
        int firstIndexForPolygon = 0;
        int lastIndexForPolygon = 0;
        int numberPolygon = 0;
        int size = 0;
        List<String> searchedPolygons = new ArrayList<>();

        for (int i = 0; i < listCoordinates.size(); i++) {
            if ((listCoordinates.get(firstIndexForPolygon).equals(listCoordinates.get(i)) && i != 0)) {
                lastIndexForPolygon = i;
                numberPolygon += 1;
                size = lastIndexForPolygon - firstIndexForPolygon + 1;
                searchedPolygons.add("Indexes for polygon nr." + numberPolygon + ": from " + firstIndexForPolygon +
                        " to " + lastIndexForPolygon + ", size = " + size);
                firstIndexForPolygon = lastIndexForPolygon + 1;
                i++;
            }
        }

        return searchedPolygons;
    }
}