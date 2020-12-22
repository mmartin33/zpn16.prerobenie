package sk.zpn;

public class SystemoveParametre {

//    private static String uploadAdresar="/c:/zpn/upload/";
//    private static String tmpAdresar="/c:/zpn/tmp/";
//    private static String resourcesAdresar="/c:/zpn/resources/";

    private static String uploadAdresar="/opt/resources/chemat/zpn/upload/";
    private static String tmpAdresar="/opt/resources/chemat/zpn/tmp/";
    private static String resourcesAdresar="/opt/resources/chemat/zpn/resources/";


    public static String getUplodAdresar() {
        return uploadAdresar;
    }
    public static String getTmpAdresar() {
        return tmpAdresar;
    }
    public static String getResourcesAdresar() {
        return resourcesAdresar;
    }
}

