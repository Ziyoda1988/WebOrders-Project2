package Selenium_2;
import com.github.javafaker.Faker;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Automation2 {

    @Test
    public void webOrders() throws  InterruptedException, IOException {
        //        1. Launch Edge browser.
        WebDriver driver = new EdgeDriver();

        //        2. Navigate to http://secure.smartbearsoftware.com/samples/TestComplete12/WebOrders/Login.aspx
        driver.get("http://secure.smartbearsoftware.com/samples/TestComplete12/WebOrders/Login.aspx");
        driver.manage().window().maximize();
        System.out.println("Testing starts");

        //        3. Login using username Tester and password test
        WebElement userName = driver.findElement(By.xpath("//input[@name = 'ctl00$MainContent$username']"));
        userName.sendKeys("Tester");

        WebElement passWord = driver.findElement(By.xpath("//input[@name = 'ctl00$MainContent$password']"));
        passWord.sendKeys("test");

        WebElement login = driver.findElement(By.xpath("//input[@name = 'ctl00$MainContent$login_button']"));
        login.click();

        //        4. Click on Order link
        WebElement order = driver.findElement(By.xpath("//a[@href = 'Process.aspx']"));
        order.click();

        //        5. Enter a random product quantity between 1 and 100
        WebElement product = driver.findElement(By.xpath("//select[@name='ctl00$MainContent$fmwOrder$ddlProduct']"));
        product.click();

        List<WebElement> choseOptions = driver.findElements(By.xpath("//select[@name='ctl00$MainContent$fmwOrder$ddlProduct']/option"));//xpath custom
        int choseIndex = (int) (Math.random() * choseOptions.size());
        choseOptions.get(choseIndex).click();
        int quantity = (int) (Math.random() * 100) + 1;
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_txtQuantity")).sendKeys(Integer.toString(quantity));


        //        6. Click on Calculate and verify that the Total value is correct.
        //   The logic of calculating is as follows:
        //   Price per unit is 100.  The discount of 8 % is applied to quantities of 10+.
        //   So for example, if the quantity is 8, the Total should be 800.
        //   If the quantity is 20, the Total should be 1840.
        //   If the quantity is 77, the Total should be 7084. And so on
        driver.findElement(By.cssSelector("input[type='submit'][value='Calculate']")).click();
        int expectedTotal = (quantity < 10) ? quantity * 100 : (int) (quantity * 100 * 0.92);
        Thread.sleep(1000);
        WebElement actualTotalText = driver.findElement(By.name("ctl00$MainContent$fmwOrder$txtTotal"));
        System.out.println(actualTotalText.getText());

        //EXTRA: As an extra challenge, for steps 6-10 download 1000 row of corresponding realistic data from mockaroo.com
        // in a csv format and load it to your program and use the random row of data from there each time.
        Path reader = Path.of("src/test/java/Selenium_2/data..csv");
        List<String[]> dataRows = Files.readAllLines(reader) // read all lines from file
                .stream() // convert to stream
                .skip(1) // skip the header row
                .map(line -> line.split(",")) // split each line by comma
                .collect(Collectors.toList()); // collect into a list of String arrays

        // Get a random row of data from the list
        Random random = new Random();
        String[] randomDataRow = dataRows.get(random.nextInt(dataRows.size()));

        // Use the data from the row to enter customer information
        WebElement name1 = driver.findElement(By.id("ctl00_MainContent_fmwOrder_txtName"));
        name1.sendKeys(randomDataRow[0]);
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox2")).sendKeys(randomDataRow[1]);
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox3")).sendKeys(randomDataRow[2]);
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox4")).sendKeys(randomDataRow[3]);
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox5")).sendKeys(randomDataRow[4]);

        // Enter the random card number:
        //      If Visa is selected, the card number should be a visa number that starts with 4.
        //      If MasterCard is selected, card number should be a mastercard number that starts with 5.
        //      If American Express is selected, card number should be an amex number that starts with 3.
        List<WebElement> cardTypes = driver.findElements(By.name("ctl00$MainContent$fmwOrder$cardList"));
        int index = (int) (Math.random() * cardTypes.size());
        cardTypes.get(index).click();
        String cardNumber = "";
        int randomCardTypeIndex = 0;
        switch (randomCardTypeIndex) {
            case 0: // Visa
                cardNumber = "4" + new Faker().number().digits(15);
                break;
            case 1: // MasterCard
                cardNumber = "5" + new Faker().number().digits(15);
                break;
            case 2: // American Express
                cardNumber = "3" + new Faker().number().digits(14);
                break;
        }

        driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox6")).sendKeys(cardNumber);

        // Enter a valid expiration date (newer than the current date)s-13 Enter a valid expiration date (newer than the current date)
        int randomDate = 1 + (int) (Math.random() * 11);
        String expDate = "" + randomDate;
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox1")).sendKeys(expDate.length() == 2 ? expDate + "/" + new Faker().number().numberBetween(23, 40) : 0 + expDate + "/" + new Faker().number().numberBetween(23, 40));

        //        14. Click on the Process button
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_InsertButton")).click();

        //        15. Verify that the "New order has been successfully added" message appears on the page
        String successMessage = "New order has been successfully added.";
        Assert.assertTrue(driver.getPageSource().contains(successMessage));

        //        16. Click on the View All Orders link
        driver.findElement(By.linkText("View all orders")).click();

        //        17. Verify that the placed order details appear on the first row of the orders table

        WebElement ordersTable = driver.findElement(By.id("ctl00_MainContent_orderGrid"));
        WebElement firstRow = ordersTable.findElement(By.xpath("//table[@id='ctl00_MainContent_orderGrid']/tbody/tr[2]"));
        String actualName = firstRow.findElement(By.xpath("//*[@id=\"ctl00_MainContent_orderGrid\"]/tbody/tr[2]/td[2]")).getText();
        String street = firstRow.findElement(By.xpath("//*[@id=\"ctl00_MainContent_orderGrid\"]/tbody/tr[2]/td[6]")).getText();
        String city = firstRow.findElement(By.xpath("//*[@id=\"ctl00_MainContent_orderGrid\"]/tbody/tr[2]/td[7]")).getText();
        String state = firstRow.findElement(By.xpath("//*[@id=\"ctl00_MainContent_orderGrid\"]/tbody/tr[2]/td[8]")).getText();
        String zip = firstRow.findElement(By.xpath("//*[@id=\"ctl00_MainContent_orderGrid\"]/tbody/tr[2]/td[9]")).getText();

        Assert.assertEquals(actualName, randomDataRow[0]);
        Assert.assertEquals(street, randomDataRow[1]);
        Assert.assertEquals(city, randomDataRow[2]);
        Assert.assertEquals(state, randomDataRow[3]);
        Assert.assertEquals(zip, randomDataRow[4]);
        Assert.assertEquals(cardNumber, driver.findElement(By.xpath("//*[@id=\"ctl00_MainContent_orderGrid\"]/tbody/tr[2]/td[11]")).getText());

        System.out.println("Actualy :" + driver.findElement(By.xpath("//*[@id=\"ctl00_MainContent_orderGrid\"]/tbody/tr[2]")).getText());
        Thread.sleep(5000);
        System.out.println("Excepted :" + Arrays.deepToString(randomDataRow));

        //     18 Log out of the application
        driver.findElement(By.id("ctl00_logout")).click();

        // Quit the driver
        driver.quit();

    }
}
