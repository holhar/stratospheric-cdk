package de.holhar.stratospheric.todoapp.cdk;

import static de.holhar.stratospheric.todoapp.cdk.Validations.requireNonEmpty;

import dev.stratospheric.cdk.Network;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

public class NetworkApp {

  public static void main(final String[] args) {
    App app = new App();

    String environmentName = (String) app.getNode().tryGetContext("environmentName");
    requireNonEmpty(environmentName, "context variable 'environmentName' must not be null");

    String accountId = (String) app.getNode().tryGetContext("accountId");
    requireNonEmpty(accountId, "context variable 'accountId' must not be null");

    String region = (String) app.getNode().tryGetContext("region");
    requireNonEmpty(region, "context variable 'region' must not be null");

    String sslCertificateArn = (String) app.getNode().tryGetContext("sslCertificateArn");
//    requireNonEmpty(sslCertificateArn, "context variable 'sslCertificateArn' must not be null");

    Environment awsEnvironment = makeEnv(accountId, region);

    Stack networkStack = new Stack(app, "NetworkStack", StackProps.builder()
        .stackName(environmentName + "-Network")
        .env(awsEnvironment)
        .build());

    Network network = new Network(
        networkStack,
        "Network",
        awsEnvironment,
        environmentName,
        new Network.NetworkInputParameters(sslCertificateArn));

    app.synth();
  }

  static Environment makeEnv(String account, String region) {
    return Environment.builder()
        .account(account)
        .region(region)
        .build();
  }

}

