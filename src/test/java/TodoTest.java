import io.github.bonigarcia.wdm.WebDriverManager;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TodoTest {

  WebDriver chromeDriver;
  WebDriverWait wait;
  Actions action;

  @BeforeClass
  public static void setupClass() {
    WebDriverManager.chromedriver().setup();
  }

  @Before
  public void setUp() {
    chromeDriver = new ChromeDriver();
    chromeDriver.get("http://localhost:3000/"); // FIXME move to setup
    wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(10));
    action = new Actions(chromeDriver);
  }

  @After
  public void tearDown() {
    chromeDriver.quit();
  }

  /* 1) GIVEN I am at the todoPage
WHEN I add text then press enter
THEN It is added to the list */
  @Test
  public void addTodoUpdatesTodoListTest() {

    WebElement todoInput = wait.until(
        ExpectedConditions.presenceOfElementLocated(By.id("add-todo"))
    );
    String todoExpected =
        "DemoTodo." + new SimpleDateFormat("HH.mm.ss").format(new java.util.Date());

    todoInput.clear();
    todoInput.sendKeys(todoExpected);
    todoInput.sendKeys(Keys.RETURN);

    wait.until(
        ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#todo-list > li"))
    );
    String todoActual = chromeDriver.findElements(By.cssSelector("#todo-list > li"))
        .stream()
        .filter(e -> e.getText().contains(todoExpected))
        .findFirst()
        .orElseThrow(
            () -> new NoSuchElementException("Expected TODO: " + todoExpected + " Was not found"))
        .getText();

    Assert.assertEquals(todoExpected, todoActual);
  }

  /*2) GIVEN I am at the todoPage
    AND there is an item on the list
    WHEN I hover over the item
    AND X out on the item
    THEN the item is removed */
  @Test
  public void removeTodoUpdatesListTest() {
    wait.until(
        ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#todo-list > li"))
    );
    WebElement todo = null;
    try {
      todo = chromeDriver.findElements(By.cssSelector("#todo-list > li"))
          .stream().findFirst().get();
    } catch(TimeoutException e) {
      throw new TimeoutException("There are no todos.  Please add a todo to test removing a todo");
    }

    // add tod if empty

    String debugTodo = todo.getText();

    action.moveToElement(todo).moveToElement(chromeDriver.findElement(By.cssSelector("#todo-list > li:nth-child(1) > div > button"))).click().build().perform();

    chromeDriver.navigate().refresh();
    wait.until(
        ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#todo-list"))
    );
    List<WebElement> sValue = wait.until(
        (chromeDriver) -> chromeDriver.findElements(By.cssSelector("#todo-list"))
    );

    String debugValue = sValue.get(0).getText();

        Assert.assertNotEquals(debugTodo, debugValue);
  }

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
