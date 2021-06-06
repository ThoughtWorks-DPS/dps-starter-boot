package io.twdps.starter.boot.data.provider;

import io.twdps.starter.boot.data.model.AccountData;
import io.twdps.starter.boot.test.data.provider.GenericDataFactory;
import io.twdps.starter.boot.test.data.spi.DataLoader;
import org.springframework.stereotype.Component;

@Component
public class AccountDataFactory extends GenericDataFactory<AccountData> {

  AccountDataFactory(DataLoader<AccountData> loader) {
    super(loader);
  }
}
