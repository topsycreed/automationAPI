import controllers.FluentUserController;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static testdata.ApiTestData.DEFAULT_USER;

@Feature("FluentControllerTests")
@Tag("api")
class FluentControllerRestApiTests {
    FluentUserController fluentUserController = new FluentUserController();

    @BeforeEach
    @AfterEach
    void clear() {
        fluentUserController.deleteUserByName(DEFAULT_USER.getUsername());
    }

    @Test
    @Tag("smoke")
    @DisplayName("Check add user is returns 200 status ok")
    void checkAddUserTest() {
        fluentUserController.addDefaultUser()
                .statusCodeIs(200);
    }

    @Test
    @Tag("extended")
    @DisplayName("Check user added correctly")
    void checkAddUserExtendedTest() {
        String expectedId = fluentUserController.addUser(DEFAULT_USER)
                .statusCodeIs(200)
                .getJsonValue("message");

        fluentUserController.getUserByName(DEFAULT_USER.getUsername())
                .statusCodeIs(200)
                .jsonValueIs("id", expectedId)
                .jsonValueIs("username", DEFAULT_USER.getUsername())
                //...
                .jsonValueIs("email", DEFAULT_USER.getEmail());
    }
}
