package io.yoshizaki2104.heavyjarbuildrx.cli.utils;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 *
 */
public class JsonUtils {

    private static ObjectMapper objMapper = null;


    // ObjectMapperは都度生成すると重いため、静的に1度だけ生成する
    static{
        try{
            objMapper = new ObjectMapper();
        } catch(Throwable th){
            th.printStackTrace();
        }
    }

    /** コンストラクタ禁止 */
    private JsonUtils(){}


    public static <T> T parse(Class<T> dtoClazz, String json) throws IOException {
         return (T) objMapper.readValue(json, dtoClazz);
    }
    
 
}
