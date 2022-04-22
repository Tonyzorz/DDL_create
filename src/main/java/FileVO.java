import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileVO extends CommonVO{

    private String path;
    private String absolutePath;
    private String name;
    private String ext;

    @Override
    public String toCsvHeadString() {
        /*
        //private 필드는 참조할 수 없음.
        Class<?> clazz = this.getClass();
        Field[] fields = clazz.getFields();

        for(Field f : fields){
            System.out.println(f.getName());
        }
        */
        return "path,absolutePath,name,ext";
    }

    @Override
    public String toCsvBodyString() {
        return path+","+absolutePath+","+name+","+ext;
    }
}