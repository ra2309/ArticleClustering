import java.util.List;

/**
 * Created by Heizenberg on 15-11-21.
 */
public class WordMatrix {

        public final List<String> value;
        public final double[] index;
        public final int total;

        public WordMatrix(List<String> value, double[] index,int total) {
            this.value = value;
            this.index = index;
            this.total=total;
        }

}

