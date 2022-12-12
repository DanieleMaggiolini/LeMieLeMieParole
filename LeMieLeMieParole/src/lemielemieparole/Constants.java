package lemielemieparole;

public class Constants {
    public static final String LOGOUT = "esci";
    public static final int PORT = 1234;
    
    public static int random(int min, int max){
        return (int)(Math.random() * ((max-min)+1))+min;
    }
        
}