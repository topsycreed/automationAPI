import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;

class SimpleGraphQLApiTests {
    @Test
    void getTest() {
        String query = "{\"query\":\"query{\\n getAllUsers{\\n id, firstName, lastName, email, gender, ipaddress\\n }\\n}\",\"variables\":null}";

        Response response = given()
                    .header("content-type","application/json")
                    .body(query)
                .when()
                    .post("https://graphql-api-ppql.onrender.com/graphql")
                .then()
                    .extract()
                    .response();
        System.out.println("Response status code: " + response.statusCode());
        System.out.println("Response body" + response.getBody().asPrettyString());
        JsonPath path = new JsonPath(response.getBody().asPrettyString());
        List<String> firstNames = path.get("data.getAllUsers.firstName");
        System.out.println(firstNames);
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        Assertions.assertThat(firstNames).contains("Inigo", "Jeffy", "Orson");
    }

    @Test
    void getHamcrestTest() {
        String query = "{\"query\":\"query{\\n getAllUsers{\\n id, firstName, lastName, email, gender, ipaddress\\n }\\n}\",\"variables\":null}";

        given()
                .header("content-type","application/json")
                .body(query)
                .when()
                .post("https://graphql-api-ppql.onrender.com/graphql")
                .then()
                .assertThat()
                .body("data.getAllUsers.firstName", hasItems("Wilbur", "Oriana", "Brade", "Sebastian"))
                .statusCode(200);
    }
}
