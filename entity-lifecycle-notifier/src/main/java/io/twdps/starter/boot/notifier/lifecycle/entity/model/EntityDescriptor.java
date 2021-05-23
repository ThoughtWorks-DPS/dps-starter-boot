package io.twdps.starter.boot.notifier.lifecycle.entity.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import io.twdps.starter.boot.notifier.lifecycle.entity.serde.EntityDescriptorDeserializer;
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

  /**
   * constructor, initializes the internal typename property.
   *
   * @param obj the object in its new state
   * @return entity descriptor
   */
  public static EntityDescriptor create(Object obj) {
    return EntityDescriptor.builder().typename(obj.getClass().getTypeName()).entity(obj).build();
  }

  @NonNull
  @Schema(description = "Typename of the entity object", example = "Account")
  private final String typename;

  @NonNull
  @Schema(description = "Current state of the entity")
  private final Object entity;
}
