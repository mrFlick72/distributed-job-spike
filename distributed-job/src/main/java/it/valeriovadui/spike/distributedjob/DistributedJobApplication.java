package it.valeriovadui.spike.distributedjob;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;
import javax.jms.Session;

@SpringBootApplication
public class DistributedJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedJobApplication.class, args);
    }

    @Bean
    public SQSConnectionFactory sqsConnectionFactory(@Value("${AWS_ACCESS_KEY_ID}") String accessKey,
                                                     @Value("${AWS_SECRET_ACCESS_KEY}") String awsSecretKey,
                                                     @Value("${AWS_DEFAULT_REGION}") String awsRegion) {
        return new SQSConnectionFactory(
                new ProviderConfiguration(),
                AmazonSQSClientBuilder
                        .standard()
                        .withCredentials(
                                new AWSStaticCredentialsProvider(
                                        new BasicAWSCredentials(
                                                accessKey,
                                                awsSecretKey
                                        )
                                )
                        )
                        .withRegion(awsRegion)
                        .build()
        );
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory sqsConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(sqsConnectionFactory);
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setConcurrency("3-10");
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        return factory;
    }
}

@Component
class JobListener {

    @JmsListener(destination = "distributed-job-spike.fifo")
    public void onMessage(String message) {
        System.out.println(message);
    }

}