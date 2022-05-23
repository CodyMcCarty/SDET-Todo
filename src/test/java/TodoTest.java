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
    wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(5));
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

    // try catch: if no todos to delete then add one
    try {
      wait.until(
          ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#todo-list"))
      );
      List<WebElement> todos = wait.until(
          (chromeDriver) -> chromeDriver.findElements(By.cssSelector("#todo-list"))
      );
    } catch(Exception e) {
      WebElement todoInput = wait.until(
          ExpectedConditions.presenceOfElementLocated(By.id("add-todo"))
      );
      String todoExpected =
          "DemoTodo." + new SimpleDateFormat("HH.mm.ss").format(new java.util.Date());

      todoInput.clear();
      todoInput.sendKeys(todoExpected);
      todoInput.sendKeys(Keys.RETURN);
      chromeDriver.navigate().refresh();
    }

    // arrange
    wait.until(
        ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#todo-list > li"))
    );
    WebElement todo = chromeDriver.findElements(By.cssSelector("#todo-list > li"))
        .stream().findFirst().get();
    String unexpected = todo.getText();

    // act
    action.moveToElement(todo).moveToElement(chromeDriver.findElement(By.cssSelector("#todo-list > li:nth-child(1) > div > button"))).click().build().perform();

    // assert
    List<WebElement> todosUpdated = null;
    try {
      wait.until(
          ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#todo-list"))
      );
      todosUpdated = wait.until(
          (chromeDriver) -> chromeDriver.findElements(By.cssSelector("#todo-list"))
      );
    } catch (Exception e) {
      todosUpdated = chromeDriver.findElements(By.cssSelector("#todo-list"));
    }
    String actual = todosUpdated.get(0).getText();
    Assert.assertNotEquals(unexpected, actual);
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
