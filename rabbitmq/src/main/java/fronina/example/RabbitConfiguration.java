package fronina.example;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@EnableRabbit
@Configuration
public class RabbitConfiguration {
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory("localhost");
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        /* Перед отправкой сообщения должны быть каким-то образом сериализаованы, можно использовать любой формат и единственное требование:
        клиент должен уметь с ним работать.
        */
        //будем использовать json
        rabbitTemplate.setMessageConverter(new JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Queue quarterReportQueue() {
        /* queue: наименование очереди, если очередь с таким именем уже существует,
        то используется существующая очередь, иначе создается новая.

        durable: переживет ли очередь перезапуск сервера. Чтобы сообщения переживали перезапуск сервера
        очередь должна быть объявлена долговечной.
        Мы просто делаем дамп, храним его на диске, в случае «падения» поднимаем дамп с диска —
        и всё возвращается на круги своя (и никакие сообщения мы не потеряли, кроме периода даунтайма).
        по умолчанию: true

        exclusive: ограничена ли очередь только текущим соединением.
        по умолчанию: false

        autoDelete: будет ли очередь удаляться при простое.
        по умолчанию: false

        arguments: дополнительные аргументы.*/

        return new Queue("quarterReportQueue");
    }

    @Bean
    public Queue yearReportQueue() {
        return new Queue("yearReportQueue");
    }

    //direct exchange

    @Bean
    public DirectExchange direct() {
        return new DirectExchange("i.am.direct");
    }

    @Bean
    public Binding quarterReportDirectExchange(DirectExchange direct,
                                               Queue quarterReportQueue) {
        return BindingBuilder.bind(quarterReportQueue)
                .to(direct)
                .with("quarter");
    }

    @Bean
    public Binding yearReportDirectExchange(DirectExchange direct,
                                            Queue yearReportQueue) {
        return BindingBuilder.bind(yearReportQueue)
                .to(direct)
                .with("year");
    }

    //fanout exchange

    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange("i.am.fanout");
    }

    @Bean
    public Binding quarterReportFanoutExchange(FanoutExchange fanout,
                                               Queue quarterReportQueue) {
        return BindingBuilder.bind(quarterReportQueue)
                .to(fanout);
    }

    @Bean
    public Binding yearReportFanoutExchange(FanoutExchange fanout,
                                            Queue yearReportQueue) {
        return BindingBuilder.bind(yearReportQueue)
                .to(fanout);
    }

    //topic exchange

    @Bean
    public TopicExchange topic() {
        return new TopicExchange("i.am.topic");
    }

    @Bean
    public Binding quarterReportTopicExchange(TopicExchange topic,
                                              Queue quarterReportQueue) {
        return BindingBuilder.bind(quarterReportQueue)
                .to(topic).with("report.quarter.#");
    }

    @Bean
    public Binding yearReportTopicExchange(TopicExchange topic,
                                           Queue yearReportQueue) {
        return BindingBuilder.bind(yearReportQueue)
                .to(topic).with("report.year.#");
    }

    //header exchange

    @Bean
    public HeadersExchange header() {
        return new HeadersExchange("i.am.header");
    }

    @Bean
    public Binding quarterReportHeaderExchange(HeadersExchange header,
                                              Queue quarterReportQueue) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-match", "all");
        args.put("periodicity", "quarter");
        return BindingBuilder.bind(quarterReportQueue)
                .to(header).whereAll(args).match();
    }

    @Bean
    public Binding yearReportHeaderExchange(HeadersExchange header,
                                               Queue yearReportQueue) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-match", "all");
        args.put("periodicity", "year");
        return BindingBuilder.bind(yearReportQueue)
                .to(header).whereAll(args).match();
    }
}
