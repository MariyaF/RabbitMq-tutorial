package fronina.example.sender;

import fronina.example.ReportQuery;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "topic")
public class TopicMessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicExchange topic;

    @RequestMapping(value = "/quarter/{regionId}/{username}", method = RequestMethod.GET)
    @ResponseBody
    public void quarter(@PathVariable Long regionId, @PathVariable String username) {
        ReportQuery query = new ReportQuery(regionId, username + "@ibs.ru");
        rabbitTemplate.convertAndSend(topic.getName(), "report.quarter." + username, query);
    }

    @RequestMapping(value = "/year/{regionId}/{username}", method = RequestMethod.GET)
    @ResponseBody
    public void year(@PathVariable Long regionId, @PathVariable String username) {
        ReportQuery query = new ReportQuery(regionId, username + "@ibs.ru");
        rabbitTemplate.convertAndSend(topic.getName(), "report.year." + username, query);
    }
}
