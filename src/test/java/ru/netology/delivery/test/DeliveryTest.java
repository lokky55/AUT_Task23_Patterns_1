package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.Keys.DELETE;

class DeliveryTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide()); // связывает Selenide c логгером
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");  // удаляем Listener после тестов
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] .input__control").sendKeys(DELETE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $(".checkbox__box").click();
        $$(".button__text").find(exactText("Запланировать")).click();
        $("[data-test-id=success-notification] .notification__content").
                shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate)).
                shouldBe(Condition.visible);
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] .input__control").sendKeys(DELETE);
        $("[data-test-id='date'] input").setValue(secondMeetingDate);
        $$(".button__text").find(exactText("Запланировать")).click();
        $("[data-test-id=replan-notification] .notification__content").
                shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?")).
                shouldBe(Condition.visible);
        $$(".button__text").find(exactText("Перепланировать")).click();
        $(".notification__content").
                shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate)).
                shouldBe(Condition.visible);
    }
}


