package CreateScript;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ReadTableInfoAndCreateERD {

    static StringBuffer sb = new StringBuffer();

    static String filename = "D:\\#######우리가게###\\LGU+_AI우리가게매니저_DEF-02_테이블인덱스정의서_v0.95.xlsx";
//    static String filename = "D:\\#######우리가게###\\LGU+_AI우리가게매니저_DEF-02_테이블인덱스정의서_v0.95.xlsx";
//    static String filename = "C:\\문서\\04. 우리가게 패키지\\02. 선인수인계\\20220215_소호앱2차_AND-12_테이블인덱스정의서(soho)_v1.0.xlsx";

    public static void main(String[] args) throws IOException {

        // 경로에 있는 파일을 읽
        FileInputStream file = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        int totalSheetCount = workbook.getNumberOfSheets();

        // 문서표지, 개정이력 건너뛰기
        for(int sheetNum = 2 ; sheetNum < totalSheetCount ; sheetNum++){

            XSSFSheet sheet = workbook.getSheetAt(sheetNum); // 0 번째 시트를 가져온다

            String TABLE_NAME = sheet.getSheetName();
            String TABLE_COMMENT = sheet.getRow(2).getCell(5).toString().replaceAll("\n","");
            List<String> TABLE_PK_LIST = new ArrayList<String>();

            createTableStart("\"" + TABLE_COMMENT + "\"");

            //7번이 1번 컬럼
            for(int rowNum = 7 ; rowNum < 30 ; rowNum++){
                XSSFRow row = sheet.getRow(rowNum);
                String NO = row.getCell(0).toString();

                if("".equals(NO) || "인덱스".equals(NO)){
                    break;
                }

                String COLUMN_NAME = row.getCell(1).toString();
                String COLUMN_COMMENT = row.getCell(2).toString().replaceAll("\n","");;
                String COLUMN_DATA_TYPE = row.getCell(3).toString();
                String COLUMN_DATA_LENGTH = row.getCell(4).toString();
                boolean COLUMN_NULL_YN = "Y".equals(row.getCell(5).toString()) ? true : false;
                boolean COLUMN_PK_YN = "PK".equals(row.getCell(6).toString()) ? true : false;
                String COLUMN_DEFAULT_VALUE = row.getCell(7)==null ? "" : row.getCell(7).toString();

                createColumn(COLUMN_NAME,COLUMN_DATA_TYPE,COLUMN_DATA_LENGTH,COLUMN_NULL_YN,COLUMN_COMMENT,COLUMN_DEFAULT_VALUE);

                if(COLUMN_PK_YN){
                    TABLE_PK_LIST.add(COLUMN_NAME);
                }

            }

            createTableEnd(TABLE_COMMENT,TABLE_PK_LIST);

        }


        System.out.println(sb.toString());

        System.exit(0);

    }

    public static void createTableStart(String TABLE_NAME){
        sb.append("TABLE "+TABLE_NAME+"{\n");
    }

    public static void createTableEnd(String TABLE_COMMENT, List<String> TABLE_PK_LIST){
        //sb.append("\tPRIMARY KEY ("+String.join(",",TABLE_PK_LIST)+")\n");
        sb.append("}\n");
        sb.append("\n");
    }

    public static void createColumn(String COLUMN_NAME,String COLUMN_DATA_TYPE,String COLUMN_DATA_LENGTH,boolean COLUMN_NULL_YN,String COLUMN_COMMENT,String COLUMN_DEFAULT_VALUE){


        sb.append("\t \""+COLUMN_COMMENT + "\" " + mariadbDataTypeMigration(COLUMN_DATA_TYPE));

        if(!"".equals(COLUMN_DATA_LENGTH)){
            sb.append("("+((int)Float.parseFloat(COLUMN_DATA_LENGTH))+")\n");
        }

        //널 여부
/*        if(COLUMN_NULL_YN){
            //널 허용인 경우
            sb.append(" DEFAULT NULL");
        }else{
            //널 비허용인 경우
            sb.append(" NOT NULL");
            
            //디폴트 값 존재 여부
            if(!"".equals(COLUMN_DEFAULT_VALUE)){                
                if("AUTO_INCREMENT".equals(COLUMN_DEFAULT_VALUE.trim())){
                    //디폴트 값이 AUTO_INCREMENT
                    sb.append(" " + COLUMN_DEFAULT_VALUE);
                }else{
                    //디폴트 값 존재
                    sb.append(" DEFAULT " + COLUMN_DEFAULT_VALUE);
                }
            }else{
                //디폴트 값 없으면 오류
                //sb.append(" DEFAULT ''");
            }
        }*/

        //sb.append(" COMMENT '" + COLUMN_COMMENT +"' ,\n");

    }

    public static String mariadbDataTypeMigration(String dataType){

        if(dataType.equals("NUMBER")){
            dataType="INT\n";
        }

        return dataType;
    }

//    public static void createTableComment(String TABLE_NAME,String TABLE_COMMENT){
//        sb.append("ALTER TABLE " + TABLE_NAME + " COMMENT ='" + TABLE_COMMENT +"'\n");
//    }
//
//    public static void createColumnComment(String TABLE_NAME,String COLUMN_NAME,String COLUMN_COMMENT){
//        sb.append("ALTER TABLE " + TABLE_NAME + " MODIFY " + COLUMN_NAME +" COMMENT ='" + TABLE_COMMENT +"'\n");
//    }
}

