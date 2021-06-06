package io.twdps.starter.boot.data.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.twdps.starter.boot.data.model.AccountData;
import io.twdps.starter.boot.test.data.spi.DataNotFoundException;
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
@ContextConfiguration(classes = {AccountDataProperties.class, AccountDataFactory.class})
public class AccountDataFactoryTest {

  private static String NOT_FOUND = "notFound";

  @Autowired private AccountDataFactory accountDataProvider;
  @Autowired private AccountDataProperties accountDataProperties;

  @Test
  public void dataDefaultRecordPopulated() {
    AccountData control = accountDataProperties.loadData().get("default");
    AccountData data = accountDataProvider.getData();

    assertThat(data.getFirstName()).isNotNull();
    assertThat(data.getFirstName()).isEqualTo(control.getFirstName());
    assertThat(data.getLastName()).isEqualTo(control.getLastName());
    assertThat(data.getPii()).isEqualTo(control.getPii());
  }

  @Test
  public void dataNamedRecordPopulated() {
    AccountData control = accountDataProperties.loadData().get("raiders");
    AccountData data = accountDataProvider.getNamedData("raiders");

    assertThat(data.getFirstName()).isNotNull();
    assertThat(data.getFirstName()).isEqualTo(control.getFirstName());
    assertThat(data.getLastName()).isEqualTo(control.getLastName());
    assertThat(data.getPii()).isEqualTo(control.getPii());
  }

  @Test
  public void missingNamedRecordThrows() {

    assertThrows(
        DataNotFoundException.class,
        () -> {
          AccountData response = accountDataProvider.getNamedData(NOT_FOUND);
        });
  }

  @Test
  public void collectionDefaultCollectionPopulated() {
    List<AccountData> controlCollection = accountDataProperties.loadCollections().get("default");
    List<AccountData> collection = accountDataProvider.getDataCollection();

    assertThat(collection.size()).isEqualTo(controlCollection.size());
    AccountData data = collection.get(0);
    AccountData control = controlCollection.get(0);

    assertThat(data.getFirstName()).isNotNull();
    assertThat(data.getFirstName()).isEqualTo(control.getFirstName());
    assertThat(data.getLastName()).isEqualTo(control.getLastName());
    assertThat(data.getPii()).isEqualTo(control.getPii());
  }

  @Test
  public void collectionNamedCollectionPopulated() {
    List<AccountData> controlCollection = accountDataProperties.loadCollections().get("starwars");
    List<AccountData> collection = accountDataProvider.getNamedDataCollection("starwars");

    assertThat(collection.size()).isEqualTo(controlCollection.size());
    AccountData data = collection.get(0);
    AccountData control = controlCollection.get(0);

    assertThat(data.getFirstName()).isNotNull();
    assertThat(data.getFirstName()).isEqualTo(control.getFirstName());
    assertThat(data.getLastName()).isEqualTo(control.getLastName());
    assertThat(data.getPii()).isEqualTo(control.getPii());
  }

  @Test
  public void missingNamedCollectionThrows() {

    assertThrows(
        DataNotFoundException.class,
        () -> {
          List<AccountData> response = accountDataProvider.getNamedDataCollection(NOT_FOUND);
        });
  }
}
