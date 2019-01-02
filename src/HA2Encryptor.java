import java.util.ArrayList;

/**
 * Created by Rhys on 24/03/2017.
 */
public class HA2Encryptor {

    private int IV;

    public String inputText;

    public ArrayList<Integer> plainTextValues = new ArrayList<>();
    public ArrayList<Integer> encryptedValues = new ArrayList<>();

    public HA2Encryptor() {
        IV = 76;
        inputText = "markfrequency";
    }
    public String encrypt(String in) {

        inputText = in;

        inputText = inputText.toLowerCase();
        inputText = inputText.replaceAll("[^a-zA-Z]", "");

        for(int i = 0; i < inputText.length(); i++) {

            int temp = inputText.charAt(i) - 97;
            plainTextValues.add(temp);

        }
        System.out.println("Values: " + plainTextValues.toString());

        return encryptMyValues(inputText);

    }

    public String encryptMyValues(String in) {

        int value;

        for(int i = 0; i < plainTextValues.size(); i++) {

            if(i == 0)
                IV = 76;
            else
                IV = encryptedValues.get(i - 1);

            value = 0;

            //Add IV
            if(plainTextValues.get(i) + IV > 99) {
                value = (plainTextValues.get(i) + IV) - 100;
            }
            else {
                value = (plainTextValues.get(i) + IV);
            }
            //Time by 7 and Mod
            value = (value * 7) % 100;

            //Switch values
            if(value < 10) {
                value = value * 10;
            }
            else {
                int val1 = value / 10;
                int val2 = value % 10;

                StringBuilder bill = new StringBuilder();
                bill.append(val2);
                bill.append(val1);

                String hm = bill.toString();
                value = Integer.valueOf(hm);

            }

            //Add IV
            if(value + IV > 99)
                value += IV - 100;
            else
                value += IV;

            //Store value
            encryptedValues.add(value);
        }
        System.out.println("Encrypted: " + encryptedValues.toString());
        return encryptedValues.toString();
    }
}
