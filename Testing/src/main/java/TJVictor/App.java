package TJVictor;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.UnsupportedEncodingException;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws UnsupportedEncodingException {
        String str = "string转为utf-8<>&";



        System.out.println(StringEscapeUtils.escapeHtml4(null));
    }
}
