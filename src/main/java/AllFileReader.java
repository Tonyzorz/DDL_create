import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AllFileReader {

    private static final String ROOT_DIR = "C:/workspace/sohowebui";
    private static List<FileVO> fileInfoList = new ArrayList<FileVO>();

    private static final String GIT_IGNORE_DIR = ROOT_DIR + File.separator + ".gitignore";

    public static void main(String[] args) throws IOException {

        readAllFile();

        try {
            new MakeCSV().execute(fileInfoList, "FileVO", "SOHOWEBUI_소스목록_20220329.csv", "C:/문서/04. 우리가게 패키지/03. 소스파악");
        }catch(Exception e){
            e.printStackTrace();
        }

    }


    public static void readAllFile() throws IOException {

        File file = new File(ROOT_DIR);

        readfile(file);
//
//        int count = 0;
//        for(FileVO fileInfo : fileInfoList){
//            System.out.print(++count + "\t");
//            System.out.println(fileInfo.toString());
//
//        }



    }

    public static void readfile(File file) throws IOException {

        String name = file.getName();
        String ext = name.substring(name.lastIndexOf(".")+1,name.length());
        String path = file.getPath().substring(ROOT_DIR.length()).replaceAll("\\\\","/");
        String absolutePath = file.getPath().replaceAll("\\\\","/");

        if(checkGitIgnore(path)){
            return;
        }

        if(checkHide(path)){
            return;
        }

        if(file.isDirectory()){
            File[] innerFiles = null;
            innerFiles = file.listFiles();

            for(File innerFile : innerFiles){
                readfile(innerFile);
            }
        }else{

            fileInfoList.add(FileVO.builder()
                    .ext(ext)
                    .name(name)
                    .path(path)
                    .absolutePath(absolutePath)
                    .build());
//            System.out.println(file.getName());
        }


    }

    /** git ignore리스트에 등록되면 출력하지 않는다. */
    public static boolean checkGitIgnore(String path) throws IOException {

        File gitignorefile = new File(GIT_IGNORE_DIR);
        
        try {


            FileReader fr = new FileReader(gitignorefile);
            BufferedReader br = new BufferedReader(fr);

            String str;
            while ((str = br.readLine()) != null) {

                if (path.matches("" + str + ".*")) {
                    return true;
                }
            }

        }catch(FileNotFoundException e){
           // System.out.println("gitignore 파일을 찾을 수 없음");
            return false;
        }

        return false;
    }

    /** .으로 시작하는 숨김 파일은 출력하지 않는다. */
    public static boolean checkHide(String path) throws IOException {
        if(path.matches("\\/\\..*")){
            return true;
        }
        return false;
    }


}

