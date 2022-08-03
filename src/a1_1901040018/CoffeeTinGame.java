package a1_1901040018;

import java.util.Arrays;
import java.util.Random;
/**
 * @overview A program that performs the coffee tin game on a tin of beans and
 * display result on the standard output.
 *
 * @author Viet Anh Nguyen
 */
public class CoffeeTinGame {

    /**
     * value for the green bean
     */
    private static final char GREEN = 'G';
    /**
     * value for the blue bean
     */
    private static final char BLUE = 'B';
    /**
     *  value for removed beans
     */
    private static final char REMOVED = '-';
    private static final char NULL = '\u0000';
    /**
     *  for BeanBag
     */
    private static final char[] BeanBag = new char[100];

    static {
        int half = (100 / 2) + (100 % 2);
        int index = half;
        char value = 'G';
        for (int i = 0; i < 100; i++) {
            if (i == index) {
                half = (half / 2) + (half % 2);
                index += half;
                value = 'B';
            }
            BeanBag[i] = value;
        }
    }

    /**
     * the main procedure
     *
     * @effects initialize a coffee tin {@link /TextIO#putf(String, Object...)}:
     * print the tin content {@link @tinGame(char[])}: perform the coffee tin
     * game on tin {@link /TextIO#putf(String, Object...)}: print the tin content
     * again if last bean is correct {@link /TextIO#putf(String, Object...)}:
     * print its color else {@link /TextIO#putf(String, Object...)}: print an
     * error message
     */
    public static void main(String[] args) {
        // initialise some beans
        char[][] tins = {
                {BLUE, BLUE, BLUE, GREEN, GREEN},
                {BLUE, BLUE, BLUE, GREEN, GREEN, GREEN},
                {GREEN},
                {BLUE},
                {BLUE, GREEN}
        };
        for (int i = 0; i < tins.length; i++) {
            char[] tin = tins[i];
            // expected last bean
            // p0 = green parity /\
            // (p0=1 -> last=GREEN) /\ (p0=0 -> last=BLUE)
            // count number of greens
            int greens = 0;
            for (char bean : tin) {
                if (bean == GREEN) {
                    greens++;
                }
            }
            final char last = (greens % 2 == 1) ? GREEN : BLUE;
            // print the content of tin before the game
            System.out.printf("%nTIN (%d Gs): %s %n", greens, Arrays.toString(tin));
            // perform the game
            char lastBean = tinGame(tin);
            // lastBean = last \/ lastBean != last

            // print the content of tin and last bean
            System.out.printf("tin after: %s %n", Arrays.toString(tin));

            // check if last bean as expected and print
            if (lastBean == last) {
                System.out.printf("last bean: %c%n", lastBean);
            } else {
                System.out.printf("Oops, wrong last bean: %c (expected: %c)%n", lastBean, last);
            }
        }
    }

    /**
     * Performs the coffee tin game to determine the color of the last bean
     * @requires tin is not null /\ tin.length > 0
     * @modifies tin
     * @effects <pre>
     *   take out two beans from tin
     *   if same colour
     *     throw both away, put one blue bean back
     *   else
     *     put green bean back
     *   let p0 = initial number of green beans
     *   if p0 = 1
     *     result = `G'
     *   else
     *     result = `B'
     * </pre>
     */
    public static char tinGame(char[] tin) {
        while (hasAtLeastTwoBeans(tin)) {
            // take two beans from tin
            char[] twoBeans = takeTwo(tin);
            // process beans to updateTin
            updateTin(tin, twoBeans);
        }
        return anyBean(tin);
    }

    /**
     * @effects if tin has at least two beans return true else return false
     */
    private static boolean hasAtLeastTwoBeans(char[] tin) {
        int count = 0;
        for (char bean : tin) {
            if (bean != REMOVED) {
                count++;
            }
            if (count >= 2) // if enough beans
            {
                return true;
            }
        }
        // if not enough beans
        return false;
    }

    /**
     * @requires tin has at least 2 beans left
     * @modifies tin
     * @effects remove any two beans from tin and return them
     */
    public static char[] takeTwo(char[] tin) {
        char first = takeOne(tin);
        char second = takeOne(tin);
        return new char[]{first, second};
    }

    /**
     * @requires tin has at least one bean
     * @modifies tin
     * @effects remove any bean from tin and return it
     */
    public static char takeOne(char[] tin) {
        int num = 0;
        char bean = 0;
        while (num != REMOVED) {
            num = randInt(tin.length);
            bean = tin[num];
            if (bean != REMOVED) {  // found one
                tin[num] = REMOVED;
                return bean;

            }
        }
        // no beans left
        return NULL;
    }

    /**
     * @requires tin has vacant positions for new beans
     * @modifies tin
     * @effects place bean into any vacant position in tin
     */
    private static void putIn(char[] tin, char bean) {
        for (int i = 0; i < tin.length; i++) {
            if (tin[i] == REMOVED) { // vacant position
                tin[i] = bean;
                break;
            }
        }
    }

    /**
     * @effects if there are beans in tin return any such bean else return
     * '\u0000' (null character)
     */
    private static char anyBean(char[] tin) {
        for (char bean : tin) {
            if (bean != REMOVED) {
                return bean;
            }
        }
        // no beans left
        return NULL;
    }

    /**
     * Get random number in limited range
     * @requires n > 0
     * @effects return random integer in range [0,n)
     */
    public static int randInt(int n) {
        Random ran = new Random();
        return ran.nextInt(n);
    }

    /**
     * Update tin
     * @requires takeTwo.length >= 2 /\ tin.length is not null /\ tin.length > 0
     * @modifies tin
     * @effects remove two beans and update tin as follows if are same color
     * throw both away, put one blue bean back else put green bean back
     */
    public static void updateTin(char[] tin, char[] takeTwo) {
        char b1 = takeTwo[0];
        char b2 = takeTwo[1];
        if (b1 == b2) {
            // take and remove beans from BeanBag until it is a BLUE
            boolean correct = false;
            while (correct == false) {
                char bean = getBean();
                if (bean == 'B') {
                    correct = true;
                } else {
                    correct = false;
                }
            }
            //put BLUE bean in tin
            putIn(tin, BLUE);

        } else {
            //put GREEN bean in tin
            putIn(tin, GREEN);
        }
    }
    /**
     * @modifies BeanBag
     * @effects take and remove one bean from BeanBag
     */
    public static char getBean() {
        int num = randInt(BeanBag.length);
        char bean = BeanBag[num];
        if (bean != REMOVED) {  // found one
            BeanBag[num] = REMOVED;
        }
        return bean;
    }
}