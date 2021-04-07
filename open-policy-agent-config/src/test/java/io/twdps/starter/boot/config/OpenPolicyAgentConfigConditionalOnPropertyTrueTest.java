package io.twdps.starter.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.example.api.test.resources.TestResource;
import io.twdps.starter.example.api.test.responses.TestResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(
    controllers = {TestResource.class},
    properties = {"starter.open-policy-agent-config.enabled=true"}
)
@ContextConfiguration(classes = {OpenPolicyAgentConfig.class})
public class OpenPolicyAgentConfigConditionalOnPropertyTrueTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private TestResource controller;

  private final String message = "message";
  private final String traceparent = "00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01";

  // TODO: Figure out testing, no csrf, no issues
  @Test
  void whenResourceFoundAndIstioEnabled_thenReturns200() throws Exception {
    Mockito.when(controller.findEntityById("foo"))
        .thenReturn(new ResponseEntity<>(new TestResponse(message), HttpStatus.OK));

    // when
    MockHttpServletResponse response =
        mockMvc
            .perform(get("/v1/example/test/foo")
                .header("traceparent", traceparent)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }

}
