import java.util.*;

public class SortActualResult {
    public static List<String> sortData(List<String> actual) {
        List<Integer> convertedData = new ArrayList<>();
        List<String> sortedData = new LinkedList<>();
        for (String s : actual) {
            convertedData.add(Integer.parseInt(s));
        }

        Collections.sort(convertedData);

        for (Integer e : convertedData) {
            sortedData.add(e.toString());
        }
        return sortedData;
    }
}
