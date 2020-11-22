package fronina.example.sender;

import fronina.example.ReportQuery;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "fanout")
public class FanoutMessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FanoutExchange fanout;

    @RequestMapping(value = "/{regionId}/{username}", method = RequestMethod.GET)
    @ResponseBody
    public void all(@PathVariable Long regionId, @PathVariable String username) {
        ReportQuery query = new ReportQuery(regionId, username + "@ibs.ru");
        rabbitTemplate.convertAndSend(fanout.getName(), "", query);
    }
}
