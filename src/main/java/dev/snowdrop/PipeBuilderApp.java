package dev.snowdrop;

import dev.snowdrop.factory.Type;
import dev.snowdrop.model.Configurator;
import dev.snowdrop.model.Domain;
import dev.snowdrop.service.ConfiguratorSvc;
import io.fabric8.tekton.pipeline.v1.Pipeline;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import static dev.snowdrop.factory.konflux.pipeline.Pipelines.createBuildRun;
import static dev.snowdrop.factory.konflux.pipeline.Pipelines.createBuilder;
import static dev.snowdrop.factory.tekton.pipeline.Pipelines.*;
import static dev.snowdrop.service.ApplicationComponentBuilder.createApplication;
import static dev.snowdrop.service.ApplicationComponentBuilder.createComponent;

@TopCommand
@Command(name = "pipelinebuilder", mixinStandardHelpOptions = true, description = "Application generating Tekton Pipeline for Konflux")
public class PipeBuilderApp implements Runnable {

   private static final Logger logger = LoggerFactory.getLogger(PipeBuilderApp.class);

   @Option(names = {"-c", "--configuration-path"}, description = "The path of the configuration file", required = true)
   String configuration;

   @Option(names = {"-o", "--output-path"}, description = "The output path", required = true)
   String outputPath;

   public static void main(String[] args) {
      int exitCode = new CommandLine(new PipeBuilderApp()).execute(args);
      System.exit(exitCode);
   }

   @Override
   public void run() {
      logger.info("#### Configuration path: {}", configuration);
      logger.debug("#### Output path: {}", outputPath);

      // Parse and validate the configuration
      Configurator cfg = ConfiguratorSvc.LoadConfiguration(configuration);

      if (cfg == null) {
         logger.error("Configuration file cannot be empty !");
         System.exit(1);
      }

      if (cfg.getFlavor() == null) {
         logger.error("Flavor is missing from the configuration yaml file !");
         System.exit(1);
      } else {
         logger.info("#### Flavor selected: {}", cfg.getFlavor().toUpperCase());
      }

      if (cfg.getPipeline().getDomain() == null) {
         cfg.getPipeline().setDomain(Domain.EXAMPLE.name());
      }
      logger.info("#### Pipeline domain selected: {}", cfg.getPipeline().getDomain());

      Pipeline pipeline = null;
      String resourcesPath = outputPath + "/" + cfg.getPipeline().getDomain() + "/" + cfg.getName();

      // Type: Tekton and Domain: example
      if (cfg.getFlavor().toUpperCase().equals(Type.TEKTON.name()) &&
          cfg.getPipeline().getDomain().toUpperCase().equals(Domain.EXAMPLE.name())) {
         ConfiguratorSvc.writeYaml(createExample(cfg), resourcesPath);
      }

      // Type: Tekton and Domain: buildpack
      if (cfg.getFlavor().toUpperCase().equals(Type.TEKTON.name()) &&
         cfg.getPipeline().getDomain().toUpperCase().equals(Domain.BUILDPACK.name())) {
         ConfiguratorSvc.writeYaml(createPackBuilderRun(cfg), resourcesPath);
      }

      // Type: Konflux and Domain: buildpack
      if (cfg.getFlavor().toUpperCase().equals(Type.KONFLUX.name()) &&
          cfg.getPipeline().getDomain().toUpperCase().equals(Domain.BUILDPACK.name())) {
         ConfiguratorSvc.writeYaml(createBuilder(cfg), resourcesPath);
      }

      // Type: Konflux and Domain: build
      if (cfg.getFlavor().toUpperCase().equals(Type.KONFLUX.name()) &&
          cfg.getPipeline().getDomain().toUpperCase().equals(Domain.BUILD.name())) {
         ConfiguratorSvc.writeYaml(createBuildRun(cfg), resourcesPath);
      }

      // Generate the Application, Component CR
      ConfiguratorSvc.writeYaml(createApplication(cfg),resourcesPath);
      ConfiguratorSvc.writeYaml(createComponent(cfg),resourcesPath);
   }
}
