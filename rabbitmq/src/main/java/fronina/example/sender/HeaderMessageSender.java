package fronina.example.sender;

import fronina.example.ReportQuery;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "header")
public class HeaderMessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private HeadersExchange header;

    @RequestMapping(value = "/quarter/{regionId}/{username}", method = RequestMethod.GET)
    @ResponseBody
    public void quarter(@PathVariable Long regionId, @PathVariable String username) {
        ReportQuery query = new ReportQuery(regionId, username + "@ibs.ru");
        rabbitTemplate.convertAndSend(header.getName(), "", query, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setHeader("periodicity", "quarter");
                return message;
            }
        });
    }

    @RequestMapping(value = "/year/{regionId}/{username}", method = RequestMethod.GET)
    @ResponseBody
    public void year(@PathVariable Long regionId, @PathVariable String username) {
        ReportQuery query = new ReportQuery(regionId, username + "@ibs.ru");
        rabbitTemplate.convertAndSend(header.getName(), "", query, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setHeader("periodicity", "year");
                return message;
            }
        });
    }
}
