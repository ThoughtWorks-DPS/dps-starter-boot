package io.twdps.starter.boot.notifier;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@AllArgsConstructor
@Schema(name = "EntityWrapper", description = "Metadata describing an entity")
@JsonDeserialize(using = EntityDescriptorDeserializer.class)
public class EntityDescriptor {

  public static EntityDescriptor create(Object obj) {
    return EntityDescriptor.builder()
        .typename(obj.getClass().getTypeName())
        .entity(obj)
        .build();
  }

  @NonNull
  @Schema(description = "Typename of the entity object", example = "Account")
  private final String typename;

  @NonNull
  @Schema(description = "Current state of the entity")
  private final Object entity;

}