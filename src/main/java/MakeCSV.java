import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MakeCSV<T> {

    public void execute(List<? extends CommonVO> list,String _clazz, String fileName, String savePath) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, IOException {

        if(list==null || list.isEmpty()){
            return;
        }

        Class<?> clazz = Class.forName(_clazz);
//        Constructor<?> constructor = clazz.getConstructor();
//        Object instacne = constructor.newInstance();

        Method toCsvHeadString = clazz.getDeclaredMethod("toCsvHeadString", null);
        Method toCsvBodyString = clazz.getDeclaredMethod("toCsvBodyString", null);

//        FileVO vo = new FileVO();
//        Object[] param = new Object[]{};
//        Object[] NULL = new Object[] {null};

        StringBuffer sb = new StringBuffer();
        String head = (String)toCsvHeadString.invoke(list.get(0) ,null);

        sb.append(head+"\n");
//        System.out.println(rt);
        for(int i=0;i<list.size();i++){
            String body = (String)toCsvBodyString.invoke(list.get(i) ,null);
            sb.append(body+"\n");
        }

        File file = new File(savePath + File.separator + fileName);
        FileWriter fw = new FileWriter(file);
        fw.write(sb.toString());

        fw.close();
        file.createNewFile();



    }

}
