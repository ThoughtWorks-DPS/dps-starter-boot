package io.twdps.starter.boot.data.provider;

import static org.assertj.core.api.Assertions.assertThat;

import io.twdps.starter.boot.data.model.AccountData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@JsonTest
@TestPropertySource(properties = {"spring.config.location=classpath:application-alternate.yml"})
@ContextConfiguration(classes = {AccountDataProperties.class})
public class AccountDataPropertiesTest {

  @Autowired private AccountDataProperties accountDataProperties;

  private final String firstName = "Agent";
  private final String lastName = "Smith";
  private final String pii = "eigenvalue";

  @Test
  public void dataPropertiesPopulated() {
    AccountData data = accountDataProperties.loadData().get("default");
    assertThat(data.getFirstName()).isNotNull();
    assertThat(data.getFirstName()).isEqualTo(firstName);
    assertThat(data.getLastName()).isEqualTo(lastName);
    assertThat(data.getPii()).isEqualTo(pii);
  }

  @Test
  public void collectionPropertiesPopulated() {
    List<AccountData> collection = accountDataProperties.loadCollections().get("default");
    assertThat(collection.size()).isEqualTo(2);
    AccountData data = collection.get(0);
    assertThat(data.getFirstName()).isNotNull();
    assertThat(data.getFirstName()).isEqualTo(firstName);
    assertThat(data.getLastName()).isEqualTo(lastName);
    assertThat(data.getPii()).isEqualTo(pii);
  }
}
