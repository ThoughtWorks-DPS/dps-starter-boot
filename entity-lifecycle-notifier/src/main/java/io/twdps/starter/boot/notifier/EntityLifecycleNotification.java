package io.twdps.starter.boot.notifier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@ToString
@Schema(name = "EntityLifecycleNotification", description = "Metadata describing an entity mutation")
public class EntityLifecycleNotification {

  public enum Operation {

    // ... enum values
    CREATED("CREATED"),
    UPDATED("UPDATED"),
    DELETED("DELETED");

    private static final Map<String, Operation> BY_LABEL = new HashMap<>();

    static {
      for (Operation e : values()) {
        BY_LABEL.put(e.label, e);
      }
    }

    public final String label;

    Operation(String label) {
      this.label = label;
    }

    public static Operation of(String label) {
      return BY_LABEL.get(label);
    }
  }

  /**
   * constructor, for serialization to/from JSON.
   *
   * @param version version string
   * @param operation operation being performed
   * @param timestamp when the operation took place
   * @param actor the agent performing the operation
   * @param entityDescriptor the resulting state of the entity
   */
  @JsonCreator
  public EntityLifecycleNotification(
      @NonNull @JsonProperty("version") String version,
      @NonNull @JsonProperty("operation") Operation operation,
      @NonNull @JsonProperty("timestamp") ZonedDateTime timestamp,
      @NonNull @JsonProperty("actor") URI actor,
      @NonNull @JsonProperty("entityDescriptor") EntityDescriptor entityDescriptor) {
    this.version = version;
    this.operation = operation;
    this.timestamp = timestamp;
    this.actor = actor;
    this.entityDescriptor = entityDescriptor;
  }

  @NonNull
  @Schema(description = "Schema version of the entity object", example = "0.0.1")
  private final String version;

  @NonNull
  @Schema(description = "Operation on entity (CREATE / UPDATE / DELETE", example = "CREATE")
  private final Operation operation;

  @NonNull
  @Schema(description = "Timestamp when the mutation occurred", example = "20210331T16:20:00.000Z")
  private final ZonedDateTime timestamp;

  @NonNull
  @Schema(description = "URI identifying the Actor (user / service) making the change", example = "user:uuid")
  private final URI actor;

  @NonNull
  @Schema(description = "Data detailing the state of the entity")
  private final EntityDescriptor entityDescriptor;

}
