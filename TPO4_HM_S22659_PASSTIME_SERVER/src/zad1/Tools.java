/**
 *
 *  @author Hrynevich Maksim S22659
 *
 */

package zad1;


import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Tools {
    public static Options createOptionsFromYaml(String fileName) {

        Options options = null;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))){
            final String collect = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            Yaml yaml = new Yaml();

            Map<String, Object> load = yaml.load(collect);

            options = new Options(
                    String.valueOf(load.get("host"))
                    ,Integer.parseInt(String.valueOf(load.get("port")))
                    ,(Boolean) load.get("concurMode")
                    ,(Boolean) load.get("showSendRes")
                    ,(Map<String, List<String>>) load.get("clientsMap"));
        }catch (IOException e){
            e.printStackTrace();
        }
        return options;
    }
}
