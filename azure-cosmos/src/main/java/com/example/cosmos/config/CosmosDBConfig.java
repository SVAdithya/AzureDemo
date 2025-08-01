package com.example.cosmos.config;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.cosmos.*;
import com.azure.cosmos.models.CosmosClientTelemetryConfig;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import com.azure.spring.data.cosmos.core.ResponseDiagnostics;
import com.azure.spring.data.cosmos.core.ResponseDiagnosticsProcessor;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import java.time.Duration;

// reference : https://learn.microsoft.com/en-us/java/api/overview/azure/spring-data-cosmos-readme?view=azure-java-stable

@Configuration
@EnableCosmosRepositories(basePackages = "com.example.cosmos")
public class CosmosDBConfig extends AbstractCosmosConfiguration {
	@Value("${azure.cosmos.uri}")
	private String uri;

	@Value("${azure.cosmos.key}")
	private String key;

	@Value("${azure.cosmos.database}")
	private String dbName;

	@Value("${azure.cosmos.queryMetricsEnabled}")
	private boolean queryMetricsEnabled;

	@Value("${azure.cosmos.indexMetricsEnabled}")
	private boolean indexMetricsEnabled;

	@Value("${azure.cosmos.maxDegreeOfParallelism}")
	private int maxDegreeOfParallelism;

	@Value("${azure.cosmos.maxBufferedItemCount}")
	private int maxBufferedItemCount;

	@Value("${azure.cosmos.consistency-level}")
	private String consistencyLevel;

	@Value("${azure.cosmos.responseContinuationTokenLimitInKb}")
	private int responseContinuationTokenLimitInKb;

	@Value("${azure.cosmos.diagnosticsThresholds.pointOperationLatencyThresholdInMS}")
	private int pointOperationLatencyThresholdInMS;

	@Value("${azure.cosmos.diagnosticsThresholds.nonPointOperationLatencyThresholdInMS}")
	private int nonPointOperationLatencyThresholdInMS;

	@Value("${azure.cosmos.diagnosticsThresholds.requestChargeThresholdInRU}")
	private int requestChargeThresholdInRU;

	@Value("${azure.cosmos.diagnosticsThresholds.payloadSizeThresholdInBytes}")
	private int payloadSizeThresholdInBytes;

	private AzureKeyCredential azureKeyCredential;

	@Bean
	public CosmosClientBuilder getCosmosClientBuilder() {
		this.azureKeyCredential = new AzureKeyCredential(key);
		DirectConnectionConfig directConnectionConfig = new DirectConnectionConfig();
		GatewayConnectionConfig gatewayConnectionConfig = new GatewayConnectionConfig();
		return new CosmosClientBuilder()
				.endpoint(uri)
				.credential(azureKeyCredential)
				.directMode(directConnectionConfig, gatewayConnectionConfig)
				.consistencyLevel(ConsistencyLevel.valueOf(consistencyLevel))
				.clientTelemetryConfig(
						new CosmosClientTelemetryConfig()
								.diagnosticsThresholds(
										new CosmosDiagnosticsThresholds()
												.setNonPointOperationLatencyThreshold(Duration.ofMillis(nonPointOperationLatencyThresholdInMS))
												.setPointOperationLatencyThreshold(Duration.ofMillis(pointOperationLatencyThresholdInMS))
												.setPayloadSizeThreshold(payloadSizeThresholdInBytes)
												.setRequestChargeThreshold(requestChargeThresholdInRU)
								)
								.diagnosticsHandler(CosmosDiagnosticsHandler.DEFAULT_LOGGING_HANDLER));
	}

	@Override
	public CosmosConfig cosmosConfig() {
		return CosmosConfig.builder()
				.enableQueryMetrics(queryMetricsEnabled)
				.enableIndexMetrics(indexMetricsEnabled)
				.maxDegreeOfParallelism(maxDegreeOfParallelism)
				.maxBufferedItemCount(maxBufferedItemCount)
				.responseContinuationTokenLimitInKb(responseContinuationTokenLimitInKb)
				.responseDiagnosticsProcessor(new ResponseDiagnosticsProcessorImplementation())
				.build();
	}

	@Override
	protected String getDatabaseName() {
		return dbName;
	}

	private static class ResponseDiagnosticsProcessorImplementation implements ResponseDiagnosticsProcessor {

		@Override
		public void processResponseDiagnostics(@Nullable ResponseDiagnostics responseDiagnostics) {
			System.out.println("Response Diagnostics {}"+ responseDiagnostics);
		}
	}

}
