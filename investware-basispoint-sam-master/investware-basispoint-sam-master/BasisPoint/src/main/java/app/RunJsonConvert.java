package app;

public class RunJsonConvert {
    public static void main(String[] args) {
        JsonConvert jConvert = new JsonConvert(args[0], args[1]);
        jConvert.parseStringToJsonFile();
        System.out.println("Successfully created JSON file " + args[1]);
    }
}