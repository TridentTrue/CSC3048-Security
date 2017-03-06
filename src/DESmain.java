import java.util.Arrays;

/**
 * Created by Adam on 28/02/2017.
 */
public class DESmain {
    public static void main(String[] args) {

        Keygen myKeygen = new Keygen(Keygen.binarySet(), Keygen.strSet("P10"), Keygen.strSet("P8"));

        System.out.println("Keygen Shared Key = " + Arrays.toString(myKeygen.getSharedKey())
                            + "\nKeygen P10 = " + Arrays.toString(myKeygen.getP10())
                            + "\nKeygen P8 = " + Arrays.toString(myKeygen.getP8()));

        myKeygen.perm10(myKeygen.getSharedKey(), myKeygen.getP10());
    }
}
