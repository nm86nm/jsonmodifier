import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JSONModifier {
    private static final int ABRIDGEMENT = 1;
    private static final int PLACES = 5;

    public static void main(String[] args) {
        JSONModifier jsonModifier = new JSONModifier();
        String fileName = "src//geojson_files//test//cyprus.geojson";
        List<String> fileContent = jsonModifier.createAbridgedGeoJSONFileWithOneCountry("Cyprus");
        jsonModifier.fileWriter(fileName, fileContent);
    }

    public List<String> abridgementOfTheCoordinatesList(List<String> listCoordinates){
        List<String> listIndexesPolygons = searchPolygons(listCoordinates);
        List<String> listAbridgmentCoordinates = new ArrayList<>();

        for(int i = 0; i<listIndexesPolygons.size(); i++) {
            String stringFirstIndexForPolygon = listIndexesPolygons.get(i).substring(listIndexesPolygons.get(i).indexOf("from") + 5, listIndexesPolygons.get(i).indexOf("to") - 1);
            String stringLastIndexForPolygon = listIndexesPolygons.get(i).substring(listIndexesPolygons.get(i).indexOf("to") + 3);
            int intFirstIndexForPolygon = Integer.parseInt(stringFirstIndexForPolygon);
            int intLastIndexForPolygon = Integer.parseInt(stringLastIndexForPolygon);

            listAbridgmentCoordinates.add(roundCoordinates(listCoordinates.get(Integer.valueOf(stringFirstIndexForPolygon))));

            while((intFirstIndexForPolygon + ABRIDGEMENT) < intLastIndexForPolygon){
                listAbridgmentCoordinates.add(roundCoordinates(listCoordinates.get(intFirstIndexForPolygon + ABRIDGEMENT)));
                intFirstIndexForPolygon += ABRIDGEMENT;
            }

            listAbridgmentCoordinates.add(roundCoordinates(listCoordinates.get(Integer.valueOf(stringLastIndexForPolygon))));
        }

        return listAbridgmentCoordinates;
    }

    public List<String> createAbridgedGeoJSONFileWithAllCountries(){
        List<String> fileWithAllCountries = fileReader("src\\geojson_files\\base.geojson");
        List<String> listCountries = fileReader("src\\geojson_files\\all countries in one file\\countries.geojson");
        List<String> abridgmentListCoordinates = new ArrayList<>();
        String stringCountry = "";

        for(int i=4; i<=258; i++){
            abridgmentListCoordinates = abridgementOfTheCoordinatesList(getListCoordinatesForCountryIndex(listCountries, i));

            if (polygonsNumber(getListCoordinatesForCountryIndex(listCountries, i)) == 1) {
                stringCountry = listCountries.get(i);
                stringCountry = stringCountry.substring(0,stringCountry.indexOf("[")+4);

                for (String tmp : abridgmentListCoordinates) {
                    stringCountry += tmp;
                    stringCountry += ", ";
                }

                stringCountry = stringCountry.substring(0, stringCountry.length() - 2);
                stringCountry += " ] ] } }";

                fileWithAllCountries.add(i, stringCountry);
            } else if (polygonsNumber(getListCoordinatesForCountryIndex(listCountries, i)) > 1) {
                stringCountry = listCountries.get(i);
                stringCountry = stringCountry.substring(0,stringCountry.indexOf("[")+6);
                List<String> listIndexesPolygons = searchPolygons(abridgmentListCoordinates);
                int firstIndexForPolygon = 0;
                int lastIndexForPolygon = 0;

                for(int i1=0; i1<listIndexesPolygons.size(); i1++) {
                    firstIndexForPolygon = Integer.parseInt(listIndexesPolygons.get(i1).substring(listIndexesPolygons.get(i1).indexOf("from") + 5, listIndexesPolygons.get(i1).indexOf("to") - 1));
                    lastIndexForPolygon = Integer.parseInt(listIndexesPolygons.get(i1).substring(listIndexesPolygons.get(i1).indexOf("to") + 3));

                    for(int i2=firstIndexForPolygon; i2<=lastIndexForPolygon; i2++)
                        stringCountry += abridgmentListCoordinates.get(i2) + ", ";

                    stringCountry = stringCountry.substring(0, stringCountry.length() - 2);
                    stringCountry += " ] ], [ [ ";
                }

                stringCountry += stringCountry.substring(0, stringCountry.length() - 6) + " ] } }";

                fileWithAllCountries.add(i, stringCountry);
            }
        }

        return fileWithAllCountries;
    }

    public List<String> createAbridgedGeoJSONFileWithOneCountry(String countryName){
        List<String> fileWithCountry = fileReader("src\\geojson_files\\base.geojson");
        List<String> listCountries = fileReader("src\\geojson_files\\all countries in one file\\countries.geojson");
        List<String> abridgmentListCoordinates = abridgementOfTheCoordinatesList(getListCoordinatesForCountryName(listCountries, countryName));
        String stringCountry = "";
        boolean foundCountryName = getCountryIndex(listCountries, countryName) > 0;

        if(foundCountryName) {
            if (polygonsNumber(getListCoordinatesForCountryName(listCountries, countryName)) == 1) {
                stringCountry = listCountries.get(getCountryIndex(listCountries, countryName));
                stringCountry = stringCountry.substring(0, stringCountry.indexOf("[")+4);

                for (String tmp : abridgmentListCoordinates) {
                    stringCountry += tmp;
                    stringCountry += ", ";
                }

                stringCountry = stringCountry.substring(0, stringCountry.length() - 2);
                stringCountry += " ] ] } }";

                fileWithCountry.add(4, stringCountry);
            } else if (polygonsNumber(getListCoordinatesForCountryName(listCountries, countryName)) > 1) {
                stringCountry = listCountries.get(getCountryIndex(listCountries, countryName));
                stringCountry = stringCountry.substring(0,stringCountry.indexOf("[")+6);
                List<String> listIndexesPolygons = searchPolygons(abridgmentListCoordinates);
                int firstIndexForPolygon = 0;
                int lastIndexForPolygon = 0;

                for(int i1=0; i1<listIndexesPolygons.size(); i1++) {
                    firstIndexForPolygon = Integer.parseInt(listIndexesPolygons.get(i1).substring(listIndexesPolygons.get(i1).indexOf("from") + 5, listIndexesPolygons.get(i1).indexOf("to") - 1));
                    lastIndexForPolygon = Integer.parseInt(listIndexesPolygons.get(i1).substring(listIndexesPolygons.get(i1).indexOf("to") + 3));

                    for(int i2=firstIndexForPolygon; i2<=lastIndexForPolygon; i2++)
                        stringCountry += abridgmentListCoordinates.get(i2) + ", ";

                    stringCountry = stringCountry.substring(0, stringCountry.length() - 2);
                    stringCountry += " ] ], [ [ ";
                }

                stringCountry += stringCountry.substring(0, stringCountry.length() - 6) + " ] } }";

                fileWithCountry.add(4, stringCountry);
            }
        } else
            System.out.println("Not Found Country Name " + countryName +  "!");

        return fileWithCountry;
    }

    public void allCountriesInSingleFiles(){
        List<String> listCountriesNames = getListCountriesNames(
                fileReader("src\\geojson_files\\all countries in one file\\countries.geojson"));

        int counterCountries=1;
        for(String tmp : listCountriesNames) {
            int indexBracket = tmp.indexOf("(")-1;

            if(tmp.substring(indexBracket+3).contains("("))
                indexBracket = tmp.indexOf("(", indexBracket+3)-1;

            tmp = tmp.substring(0, indexBracket);

            List<String> listFileContent = createAbridgedGeoJSONFileWithOneCountry(tmp);
            tmp = tmp.toLowerCase();

            for(int i=0; i<tmp.length(); i++){
                if(tmp.charAt(i) == ' ')
                    tmp = tmp.substring(0, i) + "_" + tmp.substring(i+1);
            }

            tmp += ".geojson";

            System.out.println(counterCountries + ". " + tmp);
            counterCountries++;

            fileWriter("src\\geojson_files\\every country in single files (a" + ABRIDGEMENT + "p" + PLACES + ")\\" + tmp, listFileContent);
        }
    }

    public List<String> fileReader(String fileName){
        List<String> listStringFile = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String stringLine;
            while ((stringLine = bufferedReader.readLine()) != null)
                listStringFile.add(stringLine);
        } catch(IOException e){
            System.out.println(e.getLocalizedMessage());
        }

        return listStringFile;
    }

    public void fileWriter(String fileName, List<String> listFileContent){
        String stringFileContent = "";

        for(String tmp : listFileContent) {
            stringFileContent += tmp;
            stringFileContent +="\n";
        }

        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(stringFileContent);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> geojsonRightHandRule(List<String> geojsonFile){
        List<String> rightHandGeoJsonFile = geojsonFile;

        return rightHandGeoJsonFile;
    }

    public List<String> getListCountriesNames(List<String> listCountries){
        List<String> listCountriesNames = new ArrayList<>();

        for(int i1=4; i1<=258; i1++){
            int indexQuote = listCountries.get(i1).indexOf("\"", 47);
            String countryName = listCountries.get(i1).substring(47, indexQuote);

            for(int i2=1; i2<4; i2++)
               indexQuote = listCountries.get(i1).indexOf("\"", indexQuote+1);

            String countryAbridgementName = listCountries.get(i1).substring(indexQuote+1,
                    listCountries.get(i1).indexOf("\"", indexQuote+1));

            listCountriesNames.add(countryName + " (" + countryAbridgementName + ")");
        }

        return listCountriesNames;
    }

    public int getCountryIndex(List<String> text, String countryName){
        int countryIndex = 4;
        for(int i=4; i<=258; i++){
            if(text.get(i).substring(47, text.get(i).indexOf(",", 48)-1).equals(countryName)) {
                countryIndex = i;
                break;
            }

            if(!text.get(258).contains(countryName) && i==258) {
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
        } catch(IndexOutOfBoundsException e){
            System.out.println(e.getLocalizedMessage());
        }

        return listCoordinates;
    }

    public List<String> getListCoordinatesForCountryName(List<String> listCountries, String countryName) {
        int countryIndex = getCountryIndex(listCountries, countryName);
        boolean foundNameCountry = true;

        if(countryIndex == 0)
            foundNameCountry = false;

        int startIndex = listCountries.get(countryIndex).indexOf("[");
        int endIndex = listCountries.get(countryIndex).indexOf("]") + 1;
        List<String> listCoordinates = new ArrayList<>();

        if(!foundNameCountry)
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
        } catch(IndexOutOfBoundsException e){
            System.out.println(e.getLocalizedMessage());
        }

        return listCoordinates;
    }

    public int polygonsNumber(List<String> listCoordinates){
        List<String> indexesForPolygon = searchPolygons(listCoordinates);

        int beginIndex = indexesForPolygon.get(indexesForPolygon.size()-1).indexOf(".") + 1;
        int endIndex = indexesForPolygon.get(indexesForPolygon.size()-1).indexOf(":");
        int numbersPolygon = Integer.parseInt(indexesForPolygon.get(indexesForPolygon.size()-1).substring(beginIndex, endIndex));

        return numbersPolygon;
    }

    public void printListString(List<String> listCoordinates){
        System.out.print("\n");
        int counter=1;
        for(String stringCordinates : listCoordinates) {
            System.out.println(counter + ". " + stringCordinates);
            counter++;
        }
    }

    public String roundCoordinates(String coordinates){
        double roundedLongitude = roundDouble(Double.parseDouble(coordinates.substring(2, coordinates.indexOf(","))));
        double roundedLatitude = roundDouble(Double.parseDouble(coordinates.substring(coordinates.indexOf(",")+2, coordinates.length()-2)));

        return "[ " + roundedLongitude + ", " + roundedLatitude + " ]";
    }

    public double roundDouble(double value) {
        if (PLACES < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, PLACES);
        value = value * factor;
        long tmp = Math.round(value);
        value = (double)tmp / factor;

        return value;
    }

    public List<String> searchPolygons(List<String> listCoordinates){
        int firstIndexForPolygon = 0;
        int lastIndexForPolygon = 0;
        int numberPolygon = 0;
        List<String> listIndexesPolygons = new ArrayList<>();

        for(int i=0; i<listCoordinates.size(); i++){
            if((listCoordinates.get(firstIndexForPolygon).equals(listCoordinates.get(i)) && i!=0)) {
                lastIndexForPolygon = i;
                numberPolygon += 1;
                listIndexesPolygons.add("Indexes for polygon nr." + numberPolygon + ": from " + firstIndexForPolygon + " to " + lastIndexForPolygon);
                firstIndexForPolygon = lastIndexForPolygon + 1;
                i++;
            }
        }

        return listIndexesPolygons;
    }
}
