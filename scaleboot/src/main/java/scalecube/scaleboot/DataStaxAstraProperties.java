package scalecube.scaleboot;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

@ConfigurationProperties(prefix = "datastax.astra")
@AllArgsConstructor
@Data
public class DataStaxAstraProperties {
    private File secureConnectBundle;

}
