package dev.snowdrop.service;

import dev.snowdrop.PipeBuilderApp;
import dev.snowdrop.model.Configurator;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfiguratorSvc {

   private static final Logger logger = LoggerFactory.getLogger(PipeBuilderApp.class);

   public static Configurator LoadConfiguration(String configFile) {
      Configurator configurator = new Configurator();

      // Load the YAML file
      try {
         configurator = loadYaml(configFile);
      } catch(Exception e) {
         e.printStackTrace();
      }
      return configurator;
   }

   public static Configurator loadYaml(String filePath) {
      // Create a YAML parser
      Yaml yaml = new Yaml(new Constructor(Configurator.class, new LoaderOptions()));

      // Load the YAML using the configFile path
      try {
         InputStream inputStream = new FileInputStream(filePath);
         return yaml.load(inputStream);
      } catch (IOException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static void writeYaml(HasMetadata resource, String outputPath) {
      try {
         // Convert the resource(run) to YAML
         String yamlResource = Serialization.asYaml(resource);
         logger.debug("Created YAML: \n{}", yamlResource);

         // Write the YAML to the output
         String fileName = resource.getKind().toLowerCase() + "-" + resource.getMetadata().getName();
         writeYamlToFile(outputPath, fileName, yamlResource);
      } catch (Exception e) {
         logger.error(e.getMessage());
      }
   }

   public static void writeYamlToFile(String outputPath, String fileName, String yamlContent) {
      Path path = Paths.get(outputPath, fileName+".yaml");
      try {
         Files.write(path, yamlContent.getBytes());
         logger.info("### YAML file generated: " + path);
      } catch (IOException e) {
         logger.error("Failed to write YAML to file: " + e.getMessage());
      }
   }
}
