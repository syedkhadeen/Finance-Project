import java.nio.file.Files;
import java.nio.file.Paths;

public class RunFromFile {
    public static void main(String[] args) {
        String resultStr = "";
        try {
            // Read JSON from file
            String jsonInput = new String(Files.readAllBytes(Paths.get("input.json")));
            
            // Create args array with the JSON
            String[] bondArgs = new String[]{jsonInput};
            
            RunBond runBond = new RunBond();
            resultStr = runBond.runBond(bondArgs);
            System.out.println(resultStr);
        } catch (Exception e) {
            System.out.println("{\"Error\":\"" + e.getMessage() + "\"}");
        }
    }
}
