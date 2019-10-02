package main;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class RestClient {

    static int counter = 0;
    static WebTarget target = ClientBuilder.newClient()
            //.target("http://localhost:8080")
            .target("http://35.247.121.242:8080/Umimamoru")
            .path("/umimamoru/config/flow")
            .queryParam("net", 0)
            .queryParam("loc", 1)
            .queryParam("direction", 180.0);

    public static void main(String[] args) {
        Timer timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    counter++;
                    double flow = -Math.cos(2 * Math.PI * (counter % 30) / 30) + 1;
                    WebTarget t = target
                            .queryParam("time", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()))
                            .queryParam("flow", flow);
                    t.request().post(Entity.json(null));

                    System.out.println(
                            ClientBuilder.newClient()
                                    //.target("http://localhost:8080")
                                    .target("http://35.247.121.242:8080/Umimamoru")
                                    .path("/umimamoru/net/flow")
                                    .queryParam("net", 0)
                                    .request().get(String.class));
                } catch (BadRequestException e) {
                    e.printStackTrace();
                }catch (InternalServerErrorException e){
                    System.out.println(e.getResponse().toString());
                }
            }

        }, 0, 5000);

    }
}
