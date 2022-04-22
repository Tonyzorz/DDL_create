package CreateScript;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiFunctionScriptCreate {

    static StringBuffer sb = new StringBuffer();

    static String filename = "C:\\문서\\04. 우리가게 패키지\\06. AI우리가게매니저\\08. 연동규격서\\AI우리가게매니저_연동정의서_목록.xlsx";
//    static String filename = "C:\\문서\\04. 우리가게 패키지\\02. 선인수인계\\20220215_소호앱2차_AND-12_테이블인덱스정의서(soho)_v1.0.xlsx";

    public static void main(String[] args) throws IOException {

        // 경로에 있는 파일을 읽
        FileInputStream file = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        int totalSheetCount = workbook.getNumberOfSheets();

        // 문서표지, 개정이력 건너뛰기

        XSSFSheet sheet = workbook.getSheetAt(2); // 0 번째 시트를 가져온다

        start();

        for (int rowNum = 1; rowNum < 32; rowNum++) {
            XSSFRow row = sheet.getRow(rowNum);
            String NO = row.getCell(0).toString();

            String COMMENT = row.getCell(3).toString();
            String FUNCTION_NAME = row.getCell(4).toString();
            String URL = row.getCell(5).toString();
            String METHOD = row.getCell(6).toString();

            createFunction(COMMENT,FUNCTION_NAME,URL,METHOD);


        }

        end();
        System.out.println(sb.toString());

    }

    public static void start(){
        sb.append("export default {\n");
    }

    public static void end(){
        sb.append("}\n");
    }

    public static void createFunction(String COMMENT,String FUNCTION_NAME,String URL,String METHOD){

        String[] splitUrl;
        List<String> params = new ArrayList<String>();
        if("GET".equals(METHOD)){
            splitUrl = URL.split("/");
            for (String s : splitUrl) {
                if(s.trim().indexOf("{")==0){
                    String _s = s.trim().replaceAll("\\{","").replaceAll("\\}","");
                    params.add(_s);
                }
            }

            URL = URL.replaceAll("\\{","'+").replaceAll("\\}","+'");
        }


        sb.append("\t//"+COMMENT+"\n");
        sb.append(FUNCTION_NAME+"(");
        if(!"GET".equals(METHOD)){
            sb.append("params");
        }else{
            sb.append(String.join(",",params));
        }
        sb.append(") {\n");
        sb.append("    return instance({\n");
        sb.append("        url: '"+URL+"',\n");
        sb.append("        method: '"+METHOD+"',\n");
        if(!"GET".equals(METHOD)){
            sb.append("        data: params\n");
        }
        sb.append("    })\n");
        sb.append("},\n");

    }


}
