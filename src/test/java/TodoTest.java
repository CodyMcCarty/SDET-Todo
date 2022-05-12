import io.github.bonigarcia.wdm.WebDriverManager;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TodoTest {

  WebDriver chromeDriver;

  @BeforeClass
  public static void setupClass() {
    WebDriverManager.chromedriver().setup();
  }

  @Before
  public void setUp() {
    chromeDriver = new ChromeDriver();
  }

  @After
  public void tearDown() {
    chromeDriver.quit();
  }

  /* 1) GIVEN I am at the todoPage
WHEN I add text then press enter
THEN It is added to the list */

  @Test //TODO: get code review
  public void addTodoUpdatesTodoListV1Test() {
    chromeDriver.get("http://localhost:3000/"); // FIXME move to setup
    WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(10));
    WebElement todoInput = wait.until( // this is impciet
        ExpectedConditions.presenceOfElementLocated(By.id("add-todo"))
    );
    String todoExpected = new SimpleDateFormat("HH.mm.ss").format(new java.util.Date());

    todoInput.clear();
    todoInput.sendKeys(todoExpected);
    todoInput.sendKeys(Keys.RETURN);

    wait.until(
        // FIXME change from xpath to cssSelector
        ExpectedConditions.visibilityOfAllElementsLocatedBy(By. xpath("//*[@id=\"todo-list\"]/li")));
    List<WebElement> todos = chromeDriver.findElements(By.xpath("//*[@id=\"todo-list\"]/li"));
    WebElement todoActual;
    try {
      todoActual = todos
          .stream()
          .filter(e -> e.getText().contains(todoExpected))
          .findFirst().get();
      // FIXME throw exception message about not finding the todoAdded / change to .assert
    } catch (Exception e) {
      todoActual = todos.get(todos.size() - 1);
    }
    Assert.assertEquals(todoExpected, todoActual.getText());
  }

  @Test
  public void addTodoUpdatesTodoListV2Test() {
    assert true;
  }



/*2) GIVEN I am at the todoPage
  AND there is an item on the list
  WHEN I hover over the item
  AND X out on the item
  THEN the item is removed */

/*3) GIVEN I am at the todoPage
  WHEN I click on the circle next to the item
  THEN the item is crossed out
  AND the item is greyed out */

/*4) GIVEN I am at the todoPage
  WHEN I hover over an item
  AND I click on the X next to the item
  THEN the list will collapse
  AND not reorder the list
  END */


}
