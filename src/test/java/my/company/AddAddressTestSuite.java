package my.company;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Oleksandra_Zubal on 10/23/2018.
 */
public class AddAddressTestSuite {

    //Sign in with predefined credentials
    public void signIn(String username, String password) {
        open("http://a.testaddressbook.com/");
        $("#sign-in").click();
        $("#session_email").setValue(username);
        $("#session_password").setValue(password);
        $(byName("commit")).click();
    }

    //Get number of addresses in the table
    public int getNumberOfRowsInTable() {
        open("http://a.testaddressbook.com/addresses");
        return $$(".table tr").size();
    }

    //This test adds new address to the list and verifies that number of addresses increased by 1
    @Test
    public void addAddressHappyPath() {
        //Sign in to be able to add addresses
        signIn("test@example.com", "123456");

        //Store initial number of addresses in the table
        int intialNumberOfRows = getNumberOfRowsInTable();

        //Go to Addresses page
        $(byAttribute("data-test", "addresses")).click();

        //Click on Add new address
        $(byAttribute("data-test", "create")).click();

        //Fill in necessary data
       $("#address_first_name").setValue("Alex"); //set first name
        $("#address_last_name").setValue("Zeb"); //set last name
        $(byName("address[address1]")).setValue("Test addr 1"); //set address line 1
        $(byName("address[address2]")).setValue("Test addr 2"); //set address line 1
        $("#address_city").setValue("Lviv"); //set city
        $("#address_state").selectOption("California"); //choose state from the dropdown
        $("#address_zip_code").setValue("123456"); //set zip code
        $$(byName("address[country]")).get(1).setSelected(true); //select radio button Canada

        //Workaround to select random date in calendar
        $("#address_birthday").sendKeys(Keys.DOWN);
        $("#address_birthday").sendKeys(Keys.TAB);
        $("#address_birthday").sendKeys(Keys.DOWN);
        $("#address_birthday").sendKeys(Keys.TAB);
        $("#address_birthday").sendKeys(Keys.DOWN);

        //Set color value
        JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
        js.executeScript("document.getElementById('address_color').setAttribute('value', '#aff8ef')");

        $("#address_age").setValue("25"); //set age

        //Set path to some picture directly into element
        //Corresponding file should exist
        $("#address_picture").setValue("C:\\Users\\Oleksandra_Zubal\\Pictures\\TM.jpg");

        $(byAttribute("type", "tel")).setValue("12345678"); //set phone number
        $("#address_interest_read").setSelected(true); //set hobbie

        $(".btn.btn-primary").click(); //click on Create Address button

        //Verify that msg that address was added is shown
        $(byText("Address was successfully created.")).shouldBe(Condition.visible);

        //Store final number of addresses in the table
        int finalNumberOfRows = getNumberOfRowsInTable();

        //Verify that final number of rows is the same as (initial + 1 additional row)
        Assert.assertEquals(finalNumberOfRows, intialNumberOfRows+1);

    }
}

