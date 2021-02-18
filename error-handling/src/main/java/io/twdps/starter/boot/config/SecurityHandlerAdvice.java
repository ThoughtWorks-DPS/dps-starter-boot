package io.twdps.starter.boot.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

@ControllerAdvice
public class SecurityHandlerAdvice implements SecurityAdviceTrait {

}
