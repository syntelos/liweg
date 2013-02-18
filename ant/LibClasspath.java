
import java.util.StringTokenizer;

public class LibClasspath {

    final static String PathSep = System.getProperty("path.separator");




    public static void main(String[] argv){
        try {
            for (int argx = 0, argc = argv.length; argx < argc; argx++){
                String arg = argv[argx];

                for (String path : arg.split(PathSep)){

                    int clean = path.indexOf("lib/");
                    if (-1 < clean){
                        if (0 < clean){
                            path = path.substring(clean);
                        }
                        System.out.printf("%s ",path);
                    }
                }
            }
            System.out.println();
            System.exit(0);
        }
        catch (Exception exc){
            exc.printStackTrace();
            System.exit(1);
        }
    }
}
