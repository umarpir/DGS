package com.dgs.dgs_backend.rest.controller;

import com.dgs.dgs_backend.DgsBackendApplication;
import com.dgs.dgs_backend.rest.dto.ClientOrganisationDTO;
import com.dgs.dgs_backend.service.ClientOrganisationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.path.json.config.JsonPathConfig;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.util.List;

import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.config.RestAssuredConfig.newConfig;
import static java.nio.charset.Charset.defaultCharset;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {DgsBackendApplication.class})
@ActiveProfiles("test")
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Transactional
public class ClientOrganisationControllerITest {

    @Autowired
    ClientOrganisationService clientOrganisationService;

    @BeforeEach
    void setUp(@LocalServerPort int serverPort) {
        RestAssured.port = serverPort;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.config = newConfig().jsonConfig(jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL));
    }
    @Test
    public void expectGetAllClientOrganisationsSuccess() throws IOException, JSONException {
        String expectedResponse = getStringResponseFromFile("/files/organisation/getAllOrganisations-200-success-response.json");
        String response = RestAssured.given()
                .when().log().all()
                .header(CONTENT_TYPE, "application/json")
                .get("/dgs-api/v1/organisations")
                .then()
                .statusCode(HttpStatus.OK.value())
                        .extract().asString();
        JSONAssert.assertEquals(expectedResponse, response,true);
    }
    @Test
    public void expectGetClientOrganisationSuccess() throws IOException, JSONException {
        String expectedResponse = getStringResponseFromFile("/files/organisation/getOrganisation-200-success-response.json");
        String response = RestAssured.given()
                .when().log().all()
                .header(CONTENT_TYPE, "application/json")
                .get("/dgs-api/v1/organisations/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().asString();
        JSONAssert.assertEquals(expectedResponse, response,true);
    }
    @Test
    public void expectGetClientOrganisationthrows404Exception() throws IOException, JSONException {
        String expectedResponse = getStringResponseFromFile("/files/organisation/getOrganisation-404-not-found-response.json");
        String response = RestAssured.given()
                .when().log().all()
                .header(CONTENT_TYPE, "application/json")
                .get("/dgs-api/v1/organisations/10")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract().asString();
        JSONAssert.assertEquals(expectedResponse, response,true);
    }
    @Test
    public void expectDeleteClientOrganisationSuccess() throws IOException, JSONException {
        String expectedResponse = "Organisation deleted successfully";
        RestAssured.given()
                .when().log().all()
                .header(CONTENT_TYPE, "application/json")
                .delete("/dgs-api/v1/organisations/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(is(expectedResponse));
    }
    @Test
    public void expectDeleteClientOrganisationthrows404Exception() throws IOException, JSONException {
        String expectedResponse = getStringResponseFromFile("/files/organisation/getOrganisation-404-not-found-response.json");
        String response = RestAssured.given()
                .when().log().all()
                .header(CONTENT_TYPE, "application/json")
                .delete("/dgs-api/v1/organisations/10")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract().asString();
        JSONAssert.assertEquals(expectedResponse, response,true);
    }
    @Test
    public void expectSaveClientOrganisation200Success() throws IOException, JSONException {
        String expectedResponse = getStringResponseFromFile("/files/organisation/saveOrganisation-200-response.json");
        String request = getStringResponseFromFile("/files/organisation/saveOrganisation-200-request.json");
        String response = RestAssured.given()
                .when().log().all()
                .header(CONTENT_TYPE, "application/json")
                .body(request)
                .post("/dgs-api/v1/organisations/")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().asString();
        JSONAssert.assertEquals(expectedResponse, response,true);
    }
    @Test
    public void expectPutClientOrganisation200Success() throws IOException, JSONException {
        String expectedResponse = getStringResponseFromFile("/files/organisation/putOrganisation-200-response.json");
        String request = getStringResponseFromFile("/files/organisation/putOrganisation-200-request.json");
        String response = RestAssured.given()
                .when().log().all()
                .header(CONTENT_TYPE, "application/json")
                .body(request)
                .put("/dgs-api/v1/organisations/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().asString();
        JSONAssert.assertEquals(expectedResponse, response,true);
    }

    //TODO: More tests for different error responses!
    private String getStringResponseFromFile(String path) throws IOException {
        return StreamUtils.copyToString(new ClassPathResource(path).getInputStream(), defaultCharset());
    }
    public List<ClientOrganisationDTO> mockClientOrganisationResponseMapper(String value) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<ClientOrganisationDTO> map = mapper.readValue(value, new TypeReference<List<ClientOrganisationDTO>>(){});
        return map;
    }
}
