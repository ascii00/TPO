/**
 *
 *  @author Hrynevich Maksim S22659
 *
 */

package zad1;

import java.util.concurrent.FutureTask;
import java.util.List;
import java.util.concurrent.Callable;

public class ClientTask extends FutureTask<String> {

    public ClientTask(Callable<String> callable) {
        super(callable);
    }

    public static ClientTask create(Client c, List<String> reqs, boolean showSendRes){
        return new ClientTask(()->{
            c.connect();
            c.send("login " + c.getId());
            reqs.forEach((string)->{
                String sent = c.send(string);
                if(showSendRes)
                    System.out.println(sent);
            });
            return c.send("bye and log transfer");
        });
    }
}
