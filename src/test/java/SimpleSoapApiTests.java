import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static io.restassured.RestAssured.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SimpleSoapApiTests {
    private RequestSpecification requestSpecification;

    @BeforeAll
    void setup() {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://soap.qa-test.csssr.com/ws/")
                .log(LogDetail.ALL).build();
    }

    @Test
    void simpleSOAPTest() {
        String body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sch=\"http://csssr.com/schemas\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <sch:GetCompanyRequest>\n" +
                "         <sch:CompanyId>5e942ca330148f0001cd8806</sch:CompanyId>\n" +
                "      </sch:GetCompanyRequest>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        Response response = given(requestSpecification)
                .contentType("text/xml")
                .body(body)
                .post();
        System.out.println("Response status code: " + response.statusCode());
        System.out.println("Response body: ");
        response.getBody().prettyPrint();
        String xml = response.andReturn().asString();
        XmlPath xmlPath = new XmlPath(xml);
        String actualName = xmlPath.get("Envelope.Body.GetCompanyResponse.Company.Name");
        String actualId = xmlPath.get("Envelope.Body.GetCompanyResponse.Company.Id");

        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(actualName, "Test");
        Assertions.assertEquals(actualId, "5e942ca330148f0001cd8806");
    }
}
