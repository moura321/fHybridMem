package fHybridFuzzyModule;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Hashtable;
import helma.xmlrpc.*;

public class JavaServer {

    public JavaServer () {}

    public Hashtable sumAndDifference (int x, int y) {
        Hashtable result = new Hashtable();
        result.put("sum", x + y);
        result.put("difference", x - y);
        System.err.println("JavaServer");
        return result;
    }

    public Hashtable promote(String s, int max_size, int max_recency)throws IOException
    {
        double recommendation_value=0;
        int recommendation_address=0;
        Hashtable result = new Hashtable();
        FHybridMemT2FLS fHybridSystem = new FHybridMemT2FLS();

        String[] pages = s.split("\n");
        String[] values;
        int i=0;

        //System.out.println("Promotion Values");
        for(String p : pages)
        {
            values = p.split(";");
           
            double recency = Double.valueOf(values[0])/max_recency*10;//recency 
            // int address = values[1];//address
            double reads = Double.valueOf(values[2])/max_size*10;//reads
            double writes = Double.valueOf(values[3])/max_size*10;//writes
            //char type = values[4];//type

            //if (fHybridSystem.getPromotionValue(recency,reads,writes)>1)
            //{
                recommendation_value = fHybridSystem.getPromotionValue(recency,reads,writes);
                recommendation_address = Integer.valueOf(values[1]);

                result.put("" + i, recommendation_address);
                result.put("" + (i+1), recommendation_value);

                //System.out.println(recency + " " + reads + " " + writes + " " + recommendation_value);
                i+=2;
            //}
            //else
            //System.out.println(Integer.valueOf(values[1]) + " " + fHybridSystem.getPromotionValue(recency,reads,writes));
        }
        //System.out.print("-");
        return result;
    }

}
