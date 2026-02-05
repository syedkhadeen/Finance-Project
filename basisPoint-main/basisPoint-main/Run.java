
import static java.lang.System.*;

public class Run {
    public static void main(String[] args) {
        String resultStr = "";
        try {
            RunBond runBond = new RunBond();
            resultStr = runBond.runBond(args);
            System.out.println(resultStr);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
