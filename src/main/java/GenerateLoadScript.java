import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GenerateLoadScript {

    static final String HOST_DEV = "{DOMAIN}";
    static final String HOST_PROD = "";

    public static void main(String[] args) throws IOException, ParseException {

//        File file = new File("C:\\문서\\아돌라\\로드런너\\아이돌라이브 로드러너(Local).postman_collection.json");
        File file = new File("C:\\문서\\아돌라\\로드런너\\LoadRunner export.postman_collection.json");
//        File file = new File("C:\\문서\\아돌라\\로드런너\\아이돌라이브 로드러너.postman_collection.json");



        FileReader file_reader = new FileReader(file);
        int cur = 0;
        StringBuffer sb = new StringBuffer();
        while((cur = file_reader.read()) != -1){
            sb.append((char)cur);
        }

        JSONParser jsonParser = new JSONParser();

//        System.out.println(sb.toString());
        Object result = jsonParser.parse(sb.toString());

        JSONObject root = (JSONObject)result;

        // 인포
        JSONObject info = (JSONObject) root.get("info");

        // 전체를 감싸는 아이템 ( 0만 있음 )
        JSONArray allitem = (JSONArray) root.get("item");
        JSONObject allitem0 = (JSONObject) allitem.get(0);

        // 전체이름
        String allname = (String)allitem0.get("name");
        // 전체 아이템
        JSONArray items = (JSONArray)allitem0.get("item");

        for(int i=0;i<items.size();i++){

            JSONObject item = (JSONObject)items.get(i);

            // 아이템명
            String name = (String)item.get("name");
            // 아이템 request
            JSONObject request = (JSONObject)item.get("request");

            //메소드
            String method = (String)request.get("method");
            //헤더
            JSONArray header = (JSONArray) request.get("header");
            //url
            JSONObject url = (JSONObject) request.get("url");

            String raw = (String) url.get("raw");

            String script = makeScript(raw, name, method);

            System.out.println(script);

            // 아이템 response
            JSONArray response = (JSONArray)item.get("response");

//            System.out.println(name.toString());
        }



//        System.out.println();

//        System.out.println(sb.toString());
        file_reader.close();


    }

    public static String makeScript(String url, String name, String method){

        //host 컷
        url = url.replaceAll("http://","").replace("https://","");
        String path_querystring = url.substring(url.indexOf('/'),url.length());

        String[] path_querystring_array = path_querystring.split("\\?");
        String path = path_querystring.split("\\?")[0];
        String querystring = "";
        if(path_querystring_array.length>1){
            querystring = path_querystring.split("\\?")[1];
        }


//        System.out.println(path);
//        System.out.println(http);

        StringBuilder sb = new StringBuilder();

        sb.append("// "+name+"\n");
        sb.append("lr_start_transaction(\""+path+"\");\n");

        sb.append("web_custom_request(\""+path+"\",\n");
        sb.append("\t\"URL="+HOST_DEV+path_querystring+"\",\n");
        sb.append("\t\"Method="+method+"\",\n");
        sb.append("\t\"Mode=HTTP\",\n");
        sb.append("\t\"RecContentType=application/json\",\n");
        sb.append("\t\"EncType=application/json\",\n");
        sb.append("\t\"Body={\\\"\\\":\\\"\\\"}\",\n");
        sb.append("\tLAST);\n");

        sb.append("lr_end_transaction(\""+path+"\",LR_AUTO);\n");
        sb.append("lr_think_time(0.1);\n");
        sb.append("\n");

//        lr_start_transaction("init");
//
//        //web_set_sockets_option("SSL_VERSION","TLS");

//
//        lr_end_transaction("init",LR_AUTO);
//        lr_think_time(0.1);

        return sb.toString();
    }



}
