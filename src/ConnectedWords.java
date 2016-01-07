import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;
/**
 * Created by Heizenberg on 15-11-19.
 */
public class ConnectedWords {

    // A class that contains all connected words


List<String> list = new ArrayList<String>(Arrays.asList(" and "," for "," hundred ", " thousand "," any "," anymore "," anyless ",
        " he ", " she "," it "," they "," is ", " i ", " am ", " you ", " we ", " are "," his ", " her ", " your ", " yours "," yes "," no ",
        " had ", " have ", " has "," been ", " the "," nothing "," anything ", " something ", " some "," someway ", " freind ",
        " said ", " say ", " see ", " watch ", "ly ","ed ","ing "," was "," were "," off "," most "," many "," much "," few "," ago "," days "," weeks "," years "," hour "," hours "," minute "," minutes "," seconds ",
        " do "," does "," did "," don't "," doesn't "," didn't "," go "," goes "," will "," went ",
        " little ", " big "," large "," small ", " tall ", " short "," long "," exact "," approximat "," least ",
        " also ", " one ", " two ", " three ", " four ", " five ", " sure "," but "," not "," both "," its "," our "," ours "," if "," else "," better ",
            // list of words that is not very useful
            " besides ","decid","announc"," think ",
            " further ", " more ",
            " too ", " over ", " in ", "addition", "then", " of ", "equal", "importance", "equal", "another", "next", "afterward", "finally", "later", "last", " at ", " now ", "subsequently","subsequent", "then", "when","what","where","how","why", "who","soon", "after", " a "," an ", "short","time", "week”, “month”,”day", "minute", "meantime", "meanwhile", " on ","following", "length", "ultimately",
            "ultimate","present”,“first", "second", "third", "fourth", "fifth", "sixth", "finally", "final ","hence", "next ","from" ," here ", " to ", "begin", " with ", " all ", " out ", " before "," hand " ," handful "," soon ", " end ",
            "gradual”,“above", "behind", "below", "beyond"," there ", "right”,”left”,”nearby", "near"," by ","opposite", "other", "consider"," side ", "background", "directly","indirectly", " ahead ", " up "," head ","along"," wall ", " turn ", " top ", "across", "point","this","those",
            "that","adjacent”,“ for ", "example", "illustrate", "instance", " be ","specific","such"," as ","less","important","similar","same","way","result","according"," so ","consequence","consequent",
            "thus", "since", "therefore", "reason", "because","cause"," own ","their",
            "purpose", " mind ",
            " anger ", " angry ", " fear ", " afraid ", " happy "," good ", " bad ", " mood "," sad "," great "));
            // able: how about a function I will develop later to kill all words that end with able, still table. By killing these words I reduce document
            // Kill all adjectives endings and verbs endings
            // Should I take out the s. Take it if it cause trouble.
            // Take out the last line when doing the mood analysis
   List<String> special_chars = new ArrayList<String>(Arrays.asList("!","@","#","$","%","^","&","*","(",")","-","_","+","=","<",">",",",".","/","?",";",":"));


    List<String> one_chars = new ArrayList<String>(Arrays.asList(" b "," c "," d "," e "," f "," g "," h "," j "," k "," l "," m "," n "," o "," p "," q "," r "," s "," t "," u "," v "," w "," x "," y "," z "));
    public List<String> get_list()
    {
        return list;

    }
    public List<String> get_special_chars()
    {
        return special_chars;
    }


    public List<String> getOne_chars() {
        return one_chars;
    }
}
