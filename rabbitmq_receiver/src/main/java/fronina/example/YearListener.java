package fronina.example;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class YearListener implements MessageListener {

    @SneakyThrows
    public void onMessage(Message message) {
        String s = new String(message.getBody());
        ReportQuery reportQuery = new Gson().fromJson(s, ReportQuery.class);
        System.out.println("create year report for region id " + reportQuery.getRegionId());

        for (int i = 0; i <= 10; ++i) {
            System.out.println("process year report region id " + reportQuery.getRegionId() + ": " + 10 * i + "%");
            Thread.sleep(1000);
        }

        System.out.println("received year report for region id " + reportQuery.getRegionId() + " for " + reportQuery.getEmail());
    }

}
