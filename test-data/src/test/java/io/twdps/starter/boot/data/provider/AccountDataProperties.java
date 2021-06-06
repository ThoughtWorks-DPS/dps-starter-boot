package io.twdps.starter.boot.data.provider;

import io.twdps.starter.boot.data.model.AccountData;
import io.twdps.starter.boot.test.data.provider.GenericDataLoader;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("test-data.account")
public class AccountDataProperties extends GenericDataLoader<AccountData> {}
