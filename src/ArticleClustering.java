import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
/**
 * Created by Heizenberg on 15-11-18.
 */
public class ArticleClustering {

    public static int number = 21;
    public static void main (String[] args) throws IOException {

        // Read the files here, send strings to preprocessing
        // Either I store strings in string array, or send each string to preprocessing and get a better string to store
        // I choose option 2

        // Iterate through file strings in a specified location, each file ends with an i
        String string= JOptionPane.showInputDialog("Please type in the directory: ");
        String[] files = new String[number];
        String[] IDs = new String[number];
        for (int i = 0; i < number; i++) {
            StringBuilder text = new StringBuilder();
            String NL = System.getProperty("line.separator");
            // Create the file here
            IDs[i]="Text"+i+".txt";
            Scanner scanner = new Scanner(new File(string+IDs[i]));
            try {
                while (scanner.hasNextLine()) {
                    // Add empty space on top of each line
                    text.append( scanner.nextLine() + NL);
                }
            } finally {
                scanner.close();
            }
            files[i] = text.toString();
            files[i] = files[i].toLowerCase();

        }
            ArticleClustering ac = new ArticleClustering();
            files = ac.data_preprocessing(files);



        // I need a method to look for important words representitive of News, business, sports, entertaintment
        // Insert each list in an array
        List<String> news_jargon = new ArrayList<String>(Arrays.asList("politics","russia","isis","isil","iraq","iran","israel","white house","congress","court","war","peace","police","bomb","blow","blew","policy","politician"
        ,"ally","enemy","alliance","united nations","security","obama","putin","china","authoriterian","democracy","democratic","republican","candidate","vote","allegation","scandal","terriorist"));
        List<String> sports_jargon = new ArrayList<String>(Arrays.asList("scor","playoff","barcelona","real madrid","soccor","football","basketball","national team","stadium","coach","humiliat","league","messi","ronaldo","yankees","mets","shequile","won","win","lose","lost","defeat"));
        List<String> business_jargon = new ArrayList<String>(Arrays.asList("opec","oil","silicon valley","wall street","interest rate","federal reserve","bank","marketing","advertising","shares","taxes","investment","yield","pension","spread","sales","stock","price","revenue","ceo","cfo","shareholder","stakeholder","board of directors","worker","unions","money","investment"));
        List<String> entertainment_jargon = new ArrayList<String>(Arrays.asList("singer","song","movie","review","actor","actress","oscar","grammy","emmy","tv","show","series","walking dead","birdman","jon stewart","adele","preview","trailer"
                ,"hype","camera","twitter","screen","sketch","snl","hillarious","funnyman","superstar","megastar","cast","spoiler alert","celebrity","album","wedding","stars","drama","comedy","romance","genre","pop","rock","dance","stand up","papparazi"));
        List<String> lst = new ArrayList<String>();
        lst.addAll(news_jargon);
        lst.addAll(sports_jargon);
        lst.addAll(business_jargon);
        lst.addAll(entertainment_jargon);
        WordMatrix[] words = new WordMatrix[files.length];
        for(int h=0;h<files.length;h++)
            words[h]=ac.count_words(lst,files[h]);
        // Count these words regardless
        // I have four word matrices for each genre, and I need them for each file
        WordMatrix[][] wms = new WordMatrix[files.length][4];
        // Decide the centroids here
        int max1=0;
        int max2=1;
        int max3=2;
        int max4=3;
        // Find the first file that contains more news words than anything else
        Boolean condition1 = false;
        Boolean condition2 = false;
        Boolean condition3 = false;
        Boolean condition4 = false;

        //Centroid1 string
        int y=0;
        while(!condition1 || !condition2 || !condition3 || !condition4)
        {
            wms[y][0]=ac.count_words(news_jargon,files[y]);
            wms[y][1]=ac.count_words(business_jargon,files[y]);
            wms[y][2]=ac.count_words(sports_jargon,files[y]);
            wms[y][3]=ac.count_words(entertainment_jargon,files[y]);
            if(!condition1 && wms[y][0].total>wms[y][1].total && wms[y][0].total>wms[y][2].total && wms[y][0].total>wms[y][3].total)
            {
                max1=y;
                condition1=true;
            }
            if(!condition2 && wms[y][1].total>wms[y][0].total && wms[y][1].total>wms[y][2].total && wms[y][1].total>wms[y][3].total)
            {
                max2=y;
                condition2=true;
            }
            if(!condition3 && wms[y][2].total>wms[y][0].total && wms[y][2].total>wms[y][1].total && wms[y][2].total>wms[y][3].total)
            {
                max3=y;
                condition3=true;
            }
            if(!condition4 && wms[y][3].total>wms[y][0].total && wms[y][3].total>wms[y][1].total && wms[y][3].total>wms[y][2].total)
            {
                max4=y;
                condition4=true;
            }
            // Double check that there should be centroids selected.


            y++;



        }

        ac.k_means(IDs,files,words,max1,max2,max3,max4);

    }



    private double union(WordMatrix wm1,WordMatrix wm2) {
        double total = 0;
        for(int i=0;i<wm1.index.length;i++)
        {
           total+=Math.max(wm1.index[i],wm2.index[i]);
        }



        return total;
    }

    private double intersection(WordMatrix wm1, WordMatrix wm2) {
        double result=0;
        for(int i = 0; i < wm1.value.size();i++)
        {
            if(wm1.index[i]*wm2.index[i]!=0)
            {
                result+=wm1.index[i]+wm2.index[i];
            }
        }


        return result;
    }

    public WordMatrix count_words(List<String> lst,String file)
    {
        double[] counts = new double[lst.size()];
        int total = 0;
        for(int i=0;i<lst.size();i++)
        {
            // Check how many times a string appears in the document
            int lastIndex = 0;
            int count = 0;

            while(lastIndex != -1){

                lastIndex = file.indexOf(lst.get(i), lastIndex);

                if(lastIndex != -1){
                    count++;
                    total++;
                    lastIndex += lst.get(i).length();
                }
            }
            counts[i]=count;
        }

        WordMatrix wm = new WordMatrix(lst,counts,total);
        return wm;

    }

    public  String[] data_preprocessing(String[] files) throws IOException
    {
        // Store a list of connected words
        ConnectedWords cw = new ConnectedWords();
        List<String> lst = cw.get_list();
        List<String> lst1 = cw.get_special_chars();
        List<String> one_chars = cw.getOne_chars();
        // Clean out connected words from each file
        //String[] new_text=new String[files.length];
        //String[] text_with_no_chars = new String[new_text.length];
        for(int i = 0 ; i < files.length ; i++) {
            for (int k = 0; k < lst1.size(); k++) {
                files[i] = files[i].replace(lst1.get(k), " ");
            }
            for (int j = 0; j < lst.size(); j++) {
                files[i] = files[i].replace(lst.get(j), " ");
            }
            for(int y=0; y < one_chars.size();y++)
                files[i]=files[i].replace(one_chars.get(y)," ");

        }
            // Make a function just for the title, the title will be the first line
            // Newsdesk words: ISIS, war, peace, violence, riot, US,Russia,Syria,Iraq,Iran,Israel,Afghan,
            // Sports: score,soccor,football,basketball,baseball,Yankees,Mets,Stadium,
            // Entertainment: grammy,oscars,emmys,Singer,songwriter,movie,review,
            return files;
        }

    public void k_means(String[] IDs,String[] files,WordMatrix[] words,int C1,int C2,int C3,int C4) throws IOException {
        // Phase 1: Initial Run
        // create 4 clusters for categoires
        // Use list data structure
        // I could just add my own random numbers
        List cluster1 = new ArrayList<Boid>();
        List cluster2 = new ArrayList<Boid>();
        List cluster3 = new ArrayList<Boid>();
        List cluster4 = new ArrayList<Boid>();
/*
        // Pick 4 random centroids and add them to each cluster head
        // Maybe instead of random, check how many words in each genre.
        int[] C = new int[4];
        for(int i = 0; i<4 ; i++)
        {
            C[i] = ThreadLocalRandom.current().nextInt(0, files.length + 1);
            if(i!=0 && C[i-1]==C[i])
                i--;
        }
*/
        Boid boid[] = new Boid[files.length];
        for(int i=0;i<files.length;i++)
            boid[i] = new Boid(IDs[i],files[i], words[i]);

        cluster1.add(boid[C1]);
        cluster2.add(boid[C2]);
        cluster3.add(boid[C3]);
        cluster4.add(boid[C4]);
        /*
        // It doesn't work because elements are out of sync with words
        List elements = new ArrayList<File>();
        for(int j=0;j<files.length;j++)
        {
            if(j!=C1 && j!=C2 && j!= C3 && j!= C4)
            {
                elements.add(files[j]);
            }
        }
        */
        // Calculate the distances for each cluster head
        // This is only the initial run
        // 1) I need to calculate the distance for each head to the rest of the elements.
        // 2) later on put a while loop, when I figure out the stoppage condition

        double[] dist_c1 = new double[files.length];
        double[] dist_c2 = new double[files.length];
        double[] dist_c3 = new double[files.length];
        double[] dist_c4 = new double[files.length];

        // This loop is for C1
        for(int x1=0;x1<files.length;x1++)
        {
            // Calculate the distance from cluster head 1
            if(C1!=x1)
                dist_c1[x1]=MinHash(words[C1],words[x1]);
        }
        for(int x2=0;x2<files.length;x2++)
        {
            if(C2!=x2)
                dist_c2[x2]=MinHash(words[C2],words[x2]);
        }
        for(int x3=0;x3<files.length;x3++)
        {
            if(C3!=x3)
                dist_c3[x3]=MinHash(words[C3],words[x3]);
        }
        for(int x4=0;x4<files.length;x4++)
        {
            if(C4!=x4)
                dist_c4[x4]=MinHash(words[C4],words[x4]);
        }

        // NOW: make a decission wherre should each file go based on distance
        for(int X=0;X<files.length;X++)
        {
            // Don't do it for C1-C4
            // Compare for each scenario
            if(X!=C1 && X!=C2 && X!=C3 && X!=C4) {
                if (dist_c1[X] < dist_c2[X] && dist_c1[X] < dist_c3[X] && dist_c1[X] < dist_c4[X])
                    cluster1.add(boid[X]);
                else if (dist_c2[X] < dist_c1[X] && dist_c2[X] < dist_c3[X] && dist_c2[X] < dist_c4[X])
                    cluster2.add(boid[X]);
                else if (dist_c3[X] < dist_c1[X] && dist_c3[X] < dist_c2[X] && dist_c3[X] < dist_c4[X])
                    cluster3.add(boid[X]);
                else
                    cluster4.add(boid[X]);
            }
        }

        // NOW: I passed the first run
        // Take the mean value. Create a centroid with average values of the cluster.
        // Sometime later put the while loop
        boolean condition = true;
        int count=0;
        do {
            int size_of_dictionary = words[0].value.size();
            // Just create a new average
            double[] index1 = new double[boid[0].wm.index.length];
            double[] avg1 = new double[boid[0].wm.index.length];
            int total1 = 0;
            for (int T1 = 0; T1 < cluster1.size(); T1++) {
                for (int t1 = 0; t1 < size_of_dictionary; t1++) {
                    index1[t1] = ((Boid) cluster1.get(T1)).wm.index[t1];
                    total1 += index1[t1];
                }
                for(int r=0;r<avg1.length;r++)
                    avg1[r]+=index1[r];
            }
            for(int s=0;s<avg1.length;s++) {
                if (cluster1.size() != 0)
                    avg1[s] = avg1[s] / cluster1.size();
            }
            WordMatrix c1 = new WordMatrix(words[0].value, avg1, total1);
            double[] index2 = new double[boid[0].wm.index.length];
            int total2 = 0;
            double[] avg2 = new double[boid[0].wm.index.length];
            for (int T2 = 0; T2 < cluster2.size(); T2++) {
                for (int t2 = 0; t2 < size_of_dictionary; t2++) {
                    index2[t2] = ((Boid) cluster2.get(T2)).wm.index[t2];
                    total2 += index2[t2];
                }
                for(int r=0;r<avg2.length;r++)
                    avg2[r]+=index2[r];
            }
            for(int s=0;s<avg2.length;s++){
                if(cluster2.size()!=0)
                avg2[s]=avg2[s]/cluster2.size();}
            WordMatrix c2 = new WordMatrix(words[0].value, avg2, total2);
            double[] index3 = new double[boid[0].wm.index.length];
            int total3 = 0;
            double[] avg3 = new double[boid[0].wm.index.length];
            for (int T3 = 0; T3 < cluster3.size(); T3++) {
                for (int t3 = 0; t3 < size_of_dictionary; t3++) {
                    index3[t3] = ((Boid) cluster3.get(T3)).wm.index[t3];
                    total3 += index3[t3];
                }
                for(int r=0;r<avg3.length;r++)
                    avg3[r]+=index3[r];
            }
            for(int s=0;s<avg3.length;s++){

                if(cluster3.size()!=0)
                    avg3[s]=avg3[s]/cluster3.size();}
            WordMatrix c3 = new WordMatrix(words[0].value, avg3, total1);
            double[] index4 = new double[boid[0].wm.index.length];
            int total4 = 0;
            double[] avg4 = new double[boid[0].wm.index.length];
            for (int T4 = 0; T4 < cluster4.size(); T4++) {
                for (int t4 = 0; t4 < size_of_dictionary; t4++) {
                    index4[t4] = ((Boid) cluster4.get(T4)).wm.index[t4];
                    total4 += index4[t4];
                }
                for(int r=0;r<avg4.length;r++)
                    avg4[r]+=index4[r];
            }
            for(int s=0;s<avg4.length;s++)
            {

            if(cluster4.size()!=0)
                avg4[s]=avg4[s]/cluster4.size();
            }
            WordMatrix c4 = new WordMatrix(words[0].value, avg4, total4);

            // Now, recacluate the distances
            // define big cluster
            List<Boid> boids = new ArrayList<Boid>();
            boids.addAll(cluster1);
            boids.addAll(cluster2);
            boids.addAll(cluster3);
            boids.addAll(cluster4);



            // This loop is for C1
            for (int x1 = 0; x1 < files.length; x1++) {
                // Calculate the distance from cluster head 1
                dist_c1[x1] = MinHash(c1, words[x1]);
            }
            for (int x2 = 0; x2 < files.length; x2++) {

                dist_c2[x2] = MinHash(c2, words[x2]);
            }
            for (int x3 = 0; x3 < files.length; x3++) {

                dist_c3[x3] = MinHash(c3, words[x3]);
            }
            for (int x4 = 0; x4 < files.length; x4++) {

                dist_c4[x4] = MinHash(c4, words[x4]);
            }

            // NOW: make a decission wherre should each file go based on distance
            List clustera = new ArrayList<Boid>();
            List clusterb = new ArrayList<Boid>();
            List clusterc = new ArrayList<Boid>();
            List clusterd = new ArrayList<Boid>();

            for (int X = 0; X < files.length; X++) {
                // The problem occur here where all boids just go to cluster 4 because of the else statment
                // Compare for each scenario
                if (dist_c1[X] <= dist_c2[X] && dist_c1[X] <= dist_c3[X] && dist_c1[X] <= dist_c4[X])
                    clustera.add(boid[X]);
                else if (dist_c2[X] <= dist_c1[X] && dist_c2[X] <= dist_c3[X] && dist_c2[X] <= dist_c4[X])
                    clusterb.add(boid[X]);
                else if (dist_c3[X] <= dist_c1[X] && dist_c3[X] <= dist_c2[X] && dist_c3[X] <= dist_c4[X])
                    clusterc.add(boid[X]);
                else if(dist_c4[X] <= dist_c1[X] && dist_c4[X] <= dist_c2[X] && dist_c4[X] <= dist_c3[X])
                    clusterd.add(boid[X]);

            }
            if(cluster1.equals(clustera) && cluster2.equals(clusterb) && cluster3.equals(clusterc) && cluster4.equals(clusterd))
                condition = false;
            double SSE=0;
            for(int s1=0;s1<dist_c1.length;s1++)
                SSE+=Math.pow(dist_c1[s1],2);
            for(int s1=0;s1<dist_c2.length;s1++)
                SSE+=Math.pow(dist_c2[s1],2);
            for(int s1=0;s1<dist_c3.length;s1++)
                SSE+=Math.pow(dist_c3[s1],2);
            for(int s1=0;s1<dist_c4.length;s1++)
                SSE+=Math.pow(dist_c4[s1],2);

            if(SSE<=20)
                condition=false;

            cluster1.clear();
            cluster2.clear();
            cluster3.clear();
            cluster4.clear();

            cluster1.addAll(clustera);
            cluster2.addAll(clusterb);
            cluster3.addAll(clusterc);
            cluster4.addAll(clusterd);

            clustera.clear();
            clusterb.clear();
            clusterc.clear();
            clusterd.clear();

            // If any of the clusters are empty, then add the initial centroid to continue the calculation

            if(cluster1.isEmpty())
                cluster1.add(boid[C1]);
            if(cluster2.isEmpty())
                cluster2.add(boid[C2]);
            if(cluster3.isEmpty())
                cluster3.add(boid[C3]);
            if(cluster4.isEmpty())
                cluster4.add(boid[C4]);


            count++;

        }while(condition);
        StoreClusters(cluster1,cluster2,cluster3,cluster4);
    }

    private void StoreClusters(List<Boid> cluster1,List<Boid> cluster2,List<Boid> cluster3,List<Boid> cluster4) throws IOException {
        String s = "/Users/Heizenberg/Documents/Predictive analytics/Homework 2/Articles/";
        String s1 = s+"cluster1/";
        File file1=new File(s1);
        file1.mkdir();
        for(int i=0;i<cluster1.size();i++)
        {
            File file = new File(s1+cluster1.get(i).ID);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(cluster1.get(i).textfile);
            bw.close();
        }
        String s2 = s+"cluster2/";
        File file2=new File(s2);
        file2.mkdir();
        for(int j=0;j<cluster2.size();j++)
        {
            File file = new File(s2+cluster2.get(j).ID);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(cluster2.get(j).textfile);
            bw.close();
        }
        String s3 = s+"cluster3/";
        File file3=new File(s3);
        file3.mkdir();
        for(int k=0;k<cluster3.size();k++)
        {
            File file = new File(s3+cluster3.get(k).ID);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(cluster3.get(k).textfile);
            bw.close();
        }
        String s4 = s+"cluster4/";
        File file4=new File(s4);
        file4.mkdir();
        for(int l=0;l<cluster4.size();l++)
        {
            File file = new File(s4+cluster4.get(l).ID);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(cluster4.get(l).textfile);
            bw.close();
        }
    }

    public double MinHash(WordMatrix wm1,WordMatrix wm2)
    {
        // Calculate the distance
        // Calculate the intersection of words

        double intersect = intersection(wm1,wm2);
        double union = union(wm1,wm2);

        double result = (intersect/union);

        return result;

    }

}
