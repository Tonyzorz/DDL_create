import java.io.*;

public class RemoveComment {
    public static void main(String[] args) throws IOException {

        File file = new File("C:\\문서\\MQTT\\listeners.conf");

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line;
        StringBuffer sb = new StringBuffer();

        while ((line = br.readLine()) != null) {

            if(!line.matches("##.*")){
                if(!"".equals(line.trim())){
                    sb.append(line+"\n");
                }
            }
        }

        System.out.println(sb.toString());

    }
}
