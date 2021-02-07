package {{cookiecutter.PKG_TL_NAME}}.{{cookiecutter.PKG_ORG_NAME}}.{{cookiecutter.PKG_GROUP_NAME}}.model.{{cookiecutter.PKG_RESOURCE_NAME}};

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class {{cookiecutter.RESOURCE_NAME}}Model {

  @NonNull
  private final String userName;
  @NonNull
  private final String firstName;
  @NonNull
  private final String lastName;

  /** Create object from json.
   *
   * @param userName  username of account holder
   * @param firstName firstname of account holder
   * @param lastName lastname of account holder
   */
  @JsonCreator
  public AccountRequest(
      @JsonProperty("userName") String userName,
      @JsonProperty("firstName") String firstName,
      @JsonProperty("lastName") String lastName) {

    this.userName = userName;
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
