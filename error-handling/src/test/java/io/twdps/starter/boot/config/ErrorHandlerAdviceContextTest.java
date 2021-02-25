package io.twdps.starter.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.twdps.starter.boot.exception.RequestValidationException;
import io.twdps.starter.boot.exception.ResourceNotFoundException;
import io.twdps.starter.example.api.test.resources.TestResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@ContextConfiguration(classes = {ErrorHandlerAdvice.class, ErrorHandlerConfig.class, SecurityAllowConfig.class})
public class ErrorHandlerAdviceContextTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private TestResource controller;

  private final String message = "message";
  private final String traceparent = "00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01";


  @Test
  void whenResourceNotFound_thenReturns404() throws Exception {
    Mockito.when(controller.findEntityById("foo")).thenThrow(new ResourceNotFoundException("foo"));

    // when
    MockHttpServletResponse response =
        mockMvc
            .perform(get("/v1/example/test/foo")
                .header("X-B3-TraceId", traceparent)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

    String content = response.getContentAsString();
    Problem error = objectMapper.readValue(content, Problem.class);
    assertThat(error.getStatus().getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(error.getType().toString()).isEqualTo("https://starter.twdps.io/not-found");
    assertThat(error.getInstance().toString()).isEqualTo(String.format("https://starter.twdps.io/%s", traceparent));
    assertThat(error.getDetail()).isEqualTo("Resource 'foo' not found");
    // assertThat(error.getReference()).isEqualTo("/v1/example/accounts/foo");
  }

  @Test
  void whenHttpMessageNotReadable_thenReturns400() throws Exception {

    // when
    MockHttpServletResponse response =
        mockMvc
            .perform(
                post("/v1/example/test")
                    .header("X-B3-TraceId", traceparent)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"userName\": null}"))
            .andReturn()
            .getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    String content = response.getContentAsString();
    Problem error = objectMapper.readValue(content, Problem.class);
    assertThat(error.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(error.getType().toString()).isEqualTo("https://starter.twdps.io/request-validation");
    assertThat(error.getInstance().toString()).isEqualTo(String.format("https://starter.twdps.io/%s", traceparent));
    assertThat(error.getDetail()).contains("userName is marked non-null but is null");
    // assertThat(error.getReference()).isEqualTo("/v1/example/test");
  }

  @Test
  void whenRequestNotValid_thenReturns400() throws Exception {
    Mockito.when(controller.addEntity(Mockito.any()))
        .thenThrow(new RequestValidationException(message));

    // when
    MockHttpServletResponse response =
        mockMvc
            .perform(
                post("/v1/example/test")
                    .header("X-B3-TraceId", traceparent)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"userName\": \"user\"}"))
            .andReturn()
            .getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    String content = response.getContentAsString();
    Problem error = objectMapper.readValue(content, Problem.class);
    assertThat(error.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(error.getType().toString()).isEqualTo("https://starter.twdps.io/request-validation");
    assertThat(error.getInstance().toString()).isEqualTo(String.format("https://starter.twdps.io/%s", traceparent));
    assertThat(error.getDetail()).isEqualTo("Resource 'message' invalid request");
    // assertThat(error.getReference()).isEqualTo("/v1/example/test");
  }

  @Test
  void whenRequestBodyMissing_thenReturns400() throws Exception {

    // when
    MockHttpServletResponse response =
        mockMvc
            .perform(
                post("/v1/example/test")
                    .header("X-B3-TraceId", traceparent)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    String content = response.getContentAsString();
    Problem error = objectMapper.readValue(content, Problem.class);
    assertThat(error.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(error.getType().toString()).isEqualTo("https://starter.twdps.io/request-validation");
    assertThat(error.getInstance().toString()).isEqualTo(String.format("https://starter.twdps.io/%s", traceparent));
    assertThat(error.getDetail()).contains("Required request body is missing");
    // assertThat(error.getReference()).isEqualTo("/v1/example/test");
  }

  @Test
  void whenTraceHeaderIsMissing_thenReturnsNoTrace() throws Exception {

    // when
    MockHttpServletResponse response =
        mockMvc
            .perform(
                post("/v1/example/test")
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    String content = response.getContentAsString();
    Problem error = objectMapper.readValue(content, Problem.class);
    assertThat(error.getStatus().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(error.getType().toString()).isEqualTo("https://starter.twdps.io/request-validation");
    assertThat(error.getInstance().toString()).isEqualTo(String.format("https://starter.twdps.io/%s", "NOTRACE"));
    assertThat(error.getDetail()).contains("Required request body is missing");
    // assertThat(error.getReference()).isEqualTo("/v1/example/test");
  }
}
