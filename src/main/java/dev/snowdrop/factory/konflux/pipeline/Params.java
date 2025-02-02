package dev.snowdrop.factory.konflux.pipeline;

import io.fabric8.tekton.pipeline.v1.*;

import java.util.ArrayList;
import java.util.List;

public class Params {

   public static List<ParamSpec> KONFLUX_PIPELINE_PARAMS() {
      List<ParamSpec> params = new ArrayList<>();
      // Code generated using TektonJavaCodeGenerator and files/params.yaml
      params.add(new ParamSpecBuilder().withDescription("Source Repository URL").withName("git-url").withType("string").build());
      params.add(new ParamSpecBuilder().withDescription("Revision of the Source Repository").withName("revision").withType("string").withNewDefault("").build());
      params.add(new ParamSpecBuilder().withDescription("Fully Qualified Output Image").withName("output-image").withType("string").build());
      params.add(new ParamSpecBuilder().withDescription("The path to your source code").withName("path-context").withType("string").withNewDefault(".").build());
      params.add(new ParamSpecBuilder().withDescription("Path to the Dockerfile").withName("dockerfile").withType("string").withNewDefault("Dockerfile").build());
      params.add(new ParamSpecBuilder().withDescription("Force rebuild image").withName("rebuild").withType("string").withNewDefault("false").build());
      params.add(new ParamSpecBuilder().withDescription("Skip checks against built image").withName("skip-checks").withType("string").withNewDefault("false").build());
      params.add(new ParamSpecBuilder().withDescription("Execute the build with network isolation").withName("hermetic").withType("string").withNewDefault("false").build());
      params.add(new ParamSpecBuilder().withDescription("Build dependencies to be prefetched by Cachi2").withName("prefetch-input").withType("string").withNewDefault("").build());
      params.add(new ParamSpecBuilder().withDescription("Java build").withName("java").withType("string").withNewDefault("false").build());
      params.add(new ParamSpecBuilder().withDescription("Image tag expiration time, time values could be something like 1h, 2d, 3w for hours, days, and weeks, respectively.").withName("image-expires-after").withType("null").withNewDefault("").build());
      params.add(new ParamSpecBuilder().withDescription("Build a source image").withName("build-source-image").withType("string").withNewDefault("false").build());
      params.add(new ParamSpecBuilder().withDescription("Array of --build-arg values (\"arg=value\" strings) for buildah").withName("build-args").withType("array").withNewDefault("[]").build());
      params.add(new ParamSpecBuilder().withDescription("Path to a file with build arguments for buildah, see https://www.mankier.com/1/buildah-build#--build-arg-file").withName("build-args").withType("string").withNewDefault("").build());

      // TODO To be reviewed
      params.add(new ParamSpecBuilder().withDescription("A boolean indicating whether we would like to execute a step").withName("enable-sbom").withType("string").withNewDefault("true").build());
      params.add(new ParamSpecBuilder().withDescription("Format to be used to export/show the SBOM. Format available for grype are 'json table cyclonedx cyclonedx-json sarif template'").withName("grype-sbom-format").withType("string").withNewDefault("table").build());
      params.add(new ParamSpecBuilder().withDescription("Skip optional checks, set false if you want to run optional checks").withName("skip-optional").withType("string").withNewDefault("true").build());
      params.add(new ParamSpecBuilder().withDescription("Snyk Token Secret Name").withName("snyk-secret").withType("string").withNewDefault("").build());
      return params;
   }

   public static List<Param> KONFLUX_PIPELINERUN_PARAMS() {
      List<Param> params = new ArrayList<>();
      // Code generated using TektonJavaCodeGenerator and files/params.yaml
      params.add(new ParamBuilder().withName("git-url").withValue(new ParamValue("{{source_url}}")).build());
      params.add(new ParamBuilder().withName("revision").withValue(new ParamValue("{{revision}}")).build());
      params.add(new ParamBuilder().withName("dockerfile").withValue(new ParamValue("src/main/docker/Dockerfile.jvm")).build());
      params.add(new ParamBuilder().withName("output-image").withValue(new ParamValue("quay.io/ch007m/user-ns1/my-quarkus/quarkus-1:{{revision}}")).build());
      params.add(new ParamBuilder().withName("path-context").withValue(new ParamValue(".")).build());
      return params;
   }
}
