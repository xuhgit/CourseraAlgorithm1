/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String champion = "";
        double count = 0;
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            count += 1;
            if (champion.equals("")) {
                champion = word;
            } else {
                if (StdRandom.bernoulli(1.0 / count)) {
                    champion = word;
                }
            }
        }
        StdOut.println(champion);
    }
}
