package io.twdps.starter.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.exception.RequestValidationException;
import io.twdps.starter.boot.exception.ResourceNotFoundException;
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
import org.zalando.problem.Problem;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

// https://github.com/zalando/problem-spring-web/tree/a3a45e57c7917f066474ef97a74d7307da1239e9/problem-spring-web

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(TestResource.class)
@ContextConfiguration(classes =
    {ErrorHandlerAdvice.class, ErrorHandlerConfig.class, IstioDisableSecurityConfig.class})
public class IstioDisableSecurityConfigTest {

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
