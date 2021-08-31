import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JSONModifier {
    private static final int ABRIDGEMENT = 1;
    private static final int PLACES = 3;

    public static void main(String[] args) {
        JSONModifier jsonModifier = new JSONModifier();
        String baseFilePath = "src//geojson_files//base.geojson";
        String countryName = "Canada";
        //String fileName = "src//geojson_files//every country in single files (a3p3)//" + countryName.toLowerCase() + ".geojson";
        /*String canadaAllPolygons = "src//geojson_files//canada all polygons.geojson";
        String canadaMainPolygon = "src//geojson_files//canada main polygon.geojson";*/
        //List<String> baseFile = jsonModifier.fileReader(baseFilePath);
        //List<String> listCoordinatesAllPolygons = jsonModifier.getListCoordinatesForCountryName(jsonModifier.fileReader(canadaAllPolygons), countryName);
        //List<String> listCoordinatesMainPolygon = jsonModifier.getListCoordinatesForCountryName(jsonModifier.fileReader(canadaIsland), countryName);
        //List<String> indexesForPolygons = jsonModifier.indexesForPolygons(listCoordinatesAllPolygons);
        //List<String> modificatedOfTheCanada = new ArrayList<>();

        String path = "src//geojson_files//canada//islands//";
        String[] canadaIslandNames = {"Baffin Island"};

        for(int i=0; i< canadaIslandNames.length; i++)
            jsonModifier.fileWriter(path + canadaIslandNames[i] + " corrected.geojson",
                    jsonModifier.correctedCoordinates(jsonModifier.fileReader(path + canadaIslandNames[i] + ".geojson")));

        /*List<String> test = jsonModifier.formattedCoordinates(jsonModifier.fileReader(canadaIsland));
        jsonModifier.printListString(test);*/

        // List<String> test = jsonModifier.abridgementOfTheCoordinatesList(jsonModifier.formattedCoordinates(jsonModifier.fileReader(canadaIsland)));
        //jsonModifier.fileWriter("src//geojson_files//canada//islands//Axel Heiberg Island.geojson", jsonModifier.createGeoJSONFileWithCoordinates(test));

        /*String rangeOfTheBiggestPolygon = jsonModifier.indexesForPolygons(listCoordinatesAllPolygons)
                .get(jsonModifier.indexOfTheBiggestPolygon(listCoordinatesAllPolygons));
        int firstIndexOfTheBiggestPolygon = Integer.parseInt(rangeOfTheBiggestPolygon.substring(0, rangeOfTheBiggestPolygon.indexOf(",")));
        int lastIndexOfTheBiggestPolygon = Integer.parseInt(rangeOfTheBiggestPolygon.substring(rangeOfTheBiggestPolygon.indexOf(",") + 2));

        for(int i1=0; i1<jsonModifier.fileReader(canadaAllPolygons).size(); i1++){
            if(i1 != 3)
                modificatedOfTheCanada.add(jsonModifier.fileReader(canadaAllPolygons).get(i1));
            else {
                String modificatedCoordinates = "";
                int polygonNumber = 0;

                for(int i2=0; i2<firstIndexOfTheBiggestPolygon; i2++) {
                    if(i2 != firstIndexOfTheBiggestPolygon-1) {
                        if(i2 != Integer.parseInt(indexesForPolygons.get(polygonNumber)
                                .substring(indexesForPolygons.get(polygonNumber).indexOf(",")+2)))
                            modificatedCoordinates += listCoordinatesAllPolygons.get(i2) + ", ";
                        else {
                            modificatedCoordinates += listCoordinatesAllPolygons.get(i2) + " ] ], [ [ ";
                            polygonNumber++;
                        }
                    } else {
                        modificatedCoordinates += listCoordinatesAllPolygons.get(i2) + " ] ], [ [ ";
                        polygonNumber++;
                    }
                }

                for(int i2=0; i2<listCoordinatesMainPolygon.size(); i2++) {
                    if(i2 < listCoordinatesMainPolygon.size()-1)
                        modificatedCoordinates += listCoordinatesMainPolygon.get(i2) + ", ";
                    else {
                        modificatedCoordinates += listCoordinatesMainPolygon.get(i2) + " ] ], [ [ ";
                        polygonNumber++;
                    }
                }

                for(int i2=lastIndexOfTheBiggestPolygon+1; i2<listCoordinatesAllPolygons.size(); i2++) {
                    if(i2 != listCoordinatesAllPolygons.size()-1){
                        if(i2 != Integer.parseInt(indexesForPolygons.get(polygonNumber)
                                .substring(indexesForPolygons.get(polygonNumber).indexOf(",")+2)))
                            modificatedCoordinates += listCoordinatesAllPolygons.get(i2) + ", ";
                        else{
                            modificatedCoordinates += listCoordinatesAllPolygons.get(i2) + " ] ], [ [ ";
                            polygonNumber++;
                        }
                    } else
                        modificatedCoordinates += listCoordinatesAllPolygons.get(i2);
                }

                modificatedOfTheCanada.add(jsonModifier.fileReader(canadaAllPolygons).get(i1).substring(0, 134) + modificatedCoordinates + " ] ] ] } }");
            }
        }

        jsonModifier.fileWriter("src//geojson_files//canada modified.geojson", modificatedOfTheCanada);*/
    }

    public List<String> abridgementOfTheCoordinatesList(List<String> listCoordinates) {
        List<String> listIndexesPolygons = searchPolygons(listCoordinates);
        List<String> listAbridgmentCoordinates = new ArrayList<>();

        for (int i = 0; i < listIndexesPolygons.size(); i++) {
            String stringFirstIndexForPolygon = listIndexesPolygons.get(i).substring(listIndexesPolygons.get(i).indexOf("from") + 5, listIndexesPolygons.get(i).indexOf("to") - 1);
            String stringLastIndexForPolygon = listIndexesPolygons.get(i).substring(listIndexesPolygons.get(i).indexOf("to") + 3);
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

    public String correctedCoordinates(List<String> geojsonFile){
        String data = "";

        for(int i=0; i<geojsonFile.get(0).length(); i++){
            switch (geojsonFile.get(0).charAt(i)){
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
            abridgmentListCoordinates = abridgementOfTheCoordinatesList(getListCoordinatesForCountryIndex(listCountries, i));

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
        List<String> abridgmentListCoordinates = abridgementOfTheCoordinatesList(getListCoordinatesForCountryName(listCountries, countryName));
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

    public List<String> fileReader(String fileName) {
        List<String> listStringFile = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String stringLine;
            while ((stringLine = bufferedReader.readLine()) != null)
                listStringFile.add(stringLine);
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
        String test = geoJsonFile.get(15).substring(10);

        for (int i=11; i<geoJsonFile.size()-5; i+=4)
            coordinates.add("[ " + geoJsonFile.get(i).substring(14) + geoJsonFile.get(i + 1).substring(13) + " ]");

        return coordinates;
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
        int countryIndex = 4;

        int startLine = 0;
        for (int i = 0; i < text.size(); i++) {
            if (text.get(i).contains("\"features\": ["))
                startLine = ++i;
        }

        for (int i = startLine; i <= text.size() - 2; i++) {                       // old version i<=258
            if (text.get(i).substring(47, text.get(i).indexOf(",", 48) - 1).equals(countryName)) {
                countryIndex = i;
                break;
            }

            if (!text.get(text.size() - 2).contains(countryName) && i == text.size() - 2) {
                countryIndex = 0;
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

    public List<String> getListCoordinatesForCountryName(List<String> listCountries, String countryName) {
        int countryIndex = getCountryIndex(listCountries, countryName);
        boolean foundNameCountry = true;

        if (countryIndex == 0)
            foundNameCountry = false;

        int startIndex = listCountries.get(countryIndex).indexOf("[");
        int endIndex = listCountries.get(countryIndex).indexOf("]") + 1;
        List<String> listCoordinates = new ArrayList<>();

        if (!foundNameCountry)
            listCoordinates.add("Not Found Country Name!");

        try {
            while (!listCountries.get(countryIndex).substring(endIndex, endIndex + 9).contains("}") && foundNameCountry) {
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

    public List<String> indexesForPolygons(List<String> listCoordinates) {
        List<String> searchedPolygons = searchPolygons(listCoordinates);
        List<String> indexesForPolygons = new ArrayList<>();
        String firstIndexForPolygon = "";
        String lastIndexForPolygon = "";

        for (int i = 0; i < searchedPolygons.size(); i++) {
            firstIndexForPolygon = searchedPolygons.get(i).substring(searchedPolygons.get(i).indexOf("from") + 5, searchedPolygons.get(i).indexOf("to") - 1);
            lastIndexForPolygon = searchedPolygons.get(i).substring(searchedPolygons.get(i).indexOf("to") + 3);

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

    public void printListString(List<String> listCoordinates) {
        System.out.print("\n");
        int counter = 1;
        for (String stringCordinates : listCoordinates) {
            System.out.println(counter + ". " + stringCordinates);
            counter++;
        }
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
        List<String> searchedPolygons = new ArrayList<>();

        for (int i = 0; i < listCoordinates.size(); i++) {
            if ((listCoordinates.get(firstIndexForPolygon).equals(listCoordinates.get(i)) && i != 0)) {
                lastIndexForPolygon = i;
                numberPolygon += 1;
                searchedPolygons.add("Indexes for polygon nr." + numberPolygon + ": from " + firstIndexForPolygon + " to " + lastIndexForPolygon);
                firstIndexForPolygon = lastIndexForPolygon + 1;
                i++;
            }
        }

        return searchedPolygons;
    }
}
