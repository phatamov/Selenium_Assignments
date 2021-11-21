import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

public class Utilities extends SortActualResult {

    public static List<String> getElementsText(List<WebElement> listOfElements) {
        List<String> actualResult = new ArrayList<>();
        for (WebElement element : listOfElements) {
            if (!element.getText().contains("Call for Price") || element.getText().endsWith("miles")) {
                actualResult.add(element.getText().replaceAll("[^0-9]", ""));
            }
        }

        for (int i = 0; i < actualResult.size(); i++) {
            if (actualResult.get(i).isEmpty()) {
                actualResult.remove(i);
                i--;
            }
        }
        return actualResult;
    }

    public static List<String> getTableText(List<WebElement> list) {
        List<String> elemTexts = new ArrayList<>();
        for (WebElement el : list) {
            if (!el.getText().isEmpty()) {
                elemTexts.add(el.getText());
            }
        }
        return elemTexts;
    }

}
