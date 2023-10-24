package com.intuit.graphql.adapter.rest

import com.fasterxml.jackson.databind.JsonNode
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.intuit.graphql.adapter.core.ServiceAdapterRequest
import com.intuit.graphql.adapter.core.ServiceAdapterResponse
import com.intuit.graphql.adapter.rest.utils.TestUtil
import org.junit.Rule
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

class RestAdapterSpec extends Specification {

    private static int WIREMOCK_PORT = 4050;
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(WIREMOCK_PORT);
    private RestAdapter addBookRestAdapter

    def setup() {
        addBookRestAdapter = RestAdapter.builder()
                .dslResource(AddBookServicePostSpec.addBookDSL)
                .serviceId("TEST_SERVICE")
                .svcConfig(TestUtil.createServiceConfiguration(TestUtil.TIMEOUT, WIREMOCK_PORT))
                .webClient(TestUtil.webClient)
                .build();
    }

    def "canExecuteRequest with body"() {
        given:
        ServiceAdapterRequest serviceAdapterRequest = AddBookServicePostSpec.addBookServiceAdapterRequest
        stubFor(any(urlPathMatching("/books"))
                .withRequestBody(containing('''"id":"book-1'''))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody('''
                            {
                                "id": "book-1",
                                "name": "The Book",
                                "price": 100
                            }
                        ''')
                        .withHeader("Content-Type", "application/json;charset=UTF-8")));
        when:
        ServiceAdapterResponse response = addBookRestAdapter.execute(serviceAdapterRequest).get()
        then:
        assert response.getData() != null
        JsonNode data = response.getData();
        assert data.has("addBook")
        assert data.size() == 1
        JsonNode addBook = data.get("addBook");
        assert addBook.has("id")
        assert addBook.has("price")
        assert addBook.has("name")
    }

    def "cannot Execute Request Due to Http 400 Error"() {
        given:
        ServiceAdapterRequest serviceAdapterRequest = AddBookServicePostSpec.addBookServiceAdapterRequest
        stubFor(any(urlPathMatching("/books"))
                .withRequestBody(containing('''"id":"book-1'''))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withBody('''
                            {
                                "error": "Bad Request"
                            }
                        ''')
                        .withHeader("Content-Type", "application/json;charset=UTF-8")))
        when:
        Throwable cause = null
        try {
            addBookRestAdapter.execute(serviceAdapterRequest).get()
        } catch (Exception e) {
            cause = e.getCause();
        }
        then:
        assert cause != null
        assert cause.getMessage().contains("Bad Request")
        assert cause.getMessage().contains("400")
        assert cause.class.simpleName == "ServiceAdapterException"
    }

    def "cannot execute Request due to HTTP 500 Error"() {
        given:
        ServiceAdapterRequest serviceAdapterRequest = AddBookServicePostSpec.addBookServiceAdapterRequest
        stubFor(any(urlPathMatching("/books"))
                .withRequestBody(containing('''"id":"book-1'''))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withBody('''
                            {
                                "error": "Internal Server Error"
                            }
                        ''')
                        .withHeader("Content-Type", "application/json;charset=UTF-8")));
        when:
        Throwable cause = null
        try {
            addBookRestAdapter.execute(serviceAdapterRequest).get()
        } catch (Exception e) {
            cause = e.getCause();
        }
        then:
        assert cause != null
        assert cause.getMessage().contains("Internal Server Error")
        assert cause.getMessage().contains("500")
        assert cause.class.simpleName == "ServiceAdapterException"
    }

    def "cannot execute Request due to HTTP 300 Error"() {
        given:
        ServiceAdapterRequest serviceAdapterRequest = AddBookServicePostSpec.addBookServiceAdapterRequest
        stubFor(any(urlPathMatching("/books"))
                .withRequestBody(containing('''"id":"book-1'''))
                .willReturn(aResponse()
                        .withStatus(300)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")))
        when:
        Throwable cause = null
        try {
            addBookRestAdapter.execute(serviceAdapterRequest).get()
        } catch (Exception e) {
            cause = e.getCause();
        }
        then:
        assert cause != null
        assert cause.getMessage().contains("300")
        assert cause.class.simpleName == "ServiceAdapterException"
    }
}
