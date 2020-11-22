package fronina.example.sender;

import fronina.example.ReportQuery;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "direct")
public class DirectMessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DirectExchange direct;

    @RequestMapping(value = "/quarter/{regionId}/{username}", method = RequestMethod.GET)
    @ResponseBody
    public void quarter(@PathVariable Long regionId, @PathVariable String username) {
        ReportQuery query = new ReportQuery(regionId, username + "@ibs.ru");
        rabbitTemplate.convertAndSend(direct.getName(), "quarter", query);
    }

    @RequestMapping(value = "/year/{regionId}/{username}", method = RequestMethod.GET)
    @ResponseBody
    public void year(@PathVariable Long regionId, @PathVariable String username) {
        ReportQuery query = new ReportQuery(regionId, username + "@ibs.ru");
        rabbitTemplate.convertAndSend(direct.getName(), "year", query);
    }
}
