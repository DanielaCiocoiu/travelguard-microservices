package com.mentoring.TravelInsurance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentoring.TravelInsurance.controller.Setup;
import com.mentoring.TravelInsurance.model.BenefitAndLimit;
import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;
import com.mentoring.TravelInsurance.service.policy.PolicyServiceIml;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
public class PolicyServiceMockRestServiceServerUnitTest {

    @Autowired
    private PolicyServiceIml policyServiceIml;
    @Autowired
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private static final String get_All_Policies_URL = "http://localhost:8080/api/policies";
    private ObjectMapper mapper = new ObjectMapper();
    private static Person person;
    private static Policy policy;

    @BeforeAll
    static void initBeforeAll() {
        person = Setup.personBuilder("Ana Ion", 23, "romanian");
        policy = Setup.policyBuilder(1, true, Type.VISITOR_INSURANCE, BenefitAndLimit.ACCIDENT_RISK, 50.0, List.of(person));
    }
    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void givenMockingIsDoneByMockRestServiceServer_whenGetIsCalled_shouldReturnMockedObject() throws Exception {
        Policy policy111 = policy;
        policy111.setId(1);
        policy111.getPersonList().get(0).setId(1);
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI(get_All_Policies_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(policy111)));
        Policy expected = policyServiceIml.findById(1);
        assertEquals( policy111, expected);

    }

}
