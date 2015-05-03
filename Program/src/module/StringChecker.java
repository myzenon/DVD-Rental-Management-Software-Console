package module;

public class StringChecker {

    public static boolean checkAlphabet(String text) {
        for(int i = 0; i < text.length() ;i++) {
            if(((text.charAt(i) < 'A') || (text.charAt(i) > 'Z')) && ((text.charAt(i) < 'a') || (text.charAt(i) > 'z')))
                return false;
        }
        return true;
    }

    public static boolean checkNumber(String text) {
        for(int i = 0; i < text.length() ;i++) {
            if((text.charAt(i) < '0') || (text.charAt(i) > '9')) {
                return false;
            }
        }
        return true;
    }

//    public static boolean checkHouseNo(String text) {
//        for(int i = 0; i < text.length() ;i++) {
//            if(((text.charAt(i) < '0') || (text.charAt(i) > '9')) && (text.charAt(i) != '/'))
//                return false;
//        }
//        return true;
//    }
}
