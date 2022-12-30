import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {

    public static void main(String[] args) {
        int num = Integer.parseInt(args[0]);

        RandomizedQueue<String> q = new RandomizedQueue<>();
        int total = 0;
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            total++;
            /** Reservoir Sampling
             * 1. Keep the first k items in memory.
             2. When the i-th item arrives (for $i>k$):
             * with probability $k/i$, keep the new item
             and discard an old one,
             selecting which to replace at random,
             each with chance $1/k$.
             * with probability $1-k/i$, ignore the new one
             */
            if (total > num && StdRandom.uniformInt(total) < num)  {
                q.dequeue(); // randomized dequeue()
            } else if (total > num) { continue; }

            q.enqueue(item);
        }

        for (int i = 0; i < num; i++) {
            StdOut.println(q.dequeue());
        }
    }
}
