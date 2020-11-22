package fronina.example;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@EnableRabbit
@Configuration
public class RabbitConfiguration {
    @Bean
    public Queue quarterReportQueue() {
        return new Queue("quarterReportQueue");
    }

    @Bean
    public Queue yearReportQueue() {
        return new Queue("yearReportQueue");
    }

    //create MessageListenerContainer using default connection factory
    @Bean
    public MessageListenerContainer quarterMessageListenerContainer() {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory());
        simpleMessageListenerContainer.setQueues(quarterReportQueue());
        simpleMessageListenerContainer.setMessageListener(new QuarterListener());
        simpleMessageListenerContainer.setTaskExecutor(Executors.newFixedThreadPool(3));
        simpleMessageListenerContainer.setConcurrentConsumers(3);
        return simpleMessageListenerContainer;
    }

    //create MessageListenerContainer using default connection factory
    @Bean
    public MessageListenerContainer yearMessageListenerContainer() {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory());
        simpleMessageListenerContainer.setQueues(yearReportQueue());
        simpleMessageListenerContainer.setMessageListener(new YearListener());
        return simpleMessageListenerContainer;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory("localhost");
        return connectionFactory;
    }

}
