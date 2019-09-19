/*
 * SimpleT1FLS.java
 *
 * Created on May 20th 2012
 *
 * Copyright 2012 Christian Wagner All Rights Reserved.
 */
package fHybridFuzzyModule;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import generic.Input;
import generic.Output;
import generic.Tuple;
import tools.JMathPlotter;
import type1.sets.T1MF_Interface;
import type1.sets.T1MF_Trapezoidal;
import type1.sets.T1MF_Triangular;
import type1.system.T1_Antecedent;
import type1.system.T1_Consequent;
import type1.system.T1_Rule;
import type1.system.T1_Rulebase;

/**
 * A simple example of a type-1 FLS based on the "How much to tip the waiter"
 *  scenario.
 * We have two inputs: food quality and service level and as an output we would
 * like to generate the applicable tip.
 * @author Christian Wagner
 */
public class FHybridMemT1FLS
{
    Input recencyOfAccess, readFrequency, writeFrequency;    //the inputs to the FLS
    Output promotion;             //the output of the FLS
    T1_Rulebase rulebase;   //the rulebase captures the entire FLS
    
    StringBuffer sbf;//String Buffer to select all the text that will be printed on a file
    BufferedWriter bwr;
    
    public FHybridMemT1FLS()
    {
        //Define the inputs
    	recencyOfAccess = new Input("Recency of Access Level", new Tuple(0,10));
        readFrequency = new Input("Read Frequency Level", new Tuple(0,10)); 
        writeFrequency = new Input("Write Frequency Level", new Tuple(0,10));
        promotion = new Output("Promotion", new Tuple(0,10));               //a percentage for the tip
        
        //ROA = Recency of Access
        
        //Set up the membership functions (MFs) for each input and output
        T1MF_Trapezoidal highROAMF = new T1MF_Trapezoidal("MF for High Recency of Access", new double[] {0, 0, 2, 4});
        T1MF_Triangular mediumROAMF = new T1MF_Triangular("MF for Medium Recency of Access", 2, 4, 6);
        T1MF_Trapezoidal lowROAMF = new T1MF_Trapezoidal("MF for Low Recency of Access", new double[] {4, 6, 10, 10});

        T1MF_Trapezoidal lowReadFrequencyMF = new T1MF_Trapezoidal("MF for Low Read Frequency", new double[] {0, 0, 2, 5});
        T1MF_Trapezoidal mediumReadFrequencyMF = new T1MF_Trapezoidal("MF for Medium Read Frequency", new double[] {2, 4, 6, 8});
        T1MF_Trapezoidal highReadFrequencyMF = new T1MF_Trapezoidal("MF for High Read Frequency", new double[] {5, 8, 10, 10});
        
        T1MF_Trapezoidal lowWriteFrequencyMF = new T1MF_Trapezoidal("MF for Low Write Frequency", new double[] {0, 0, 2, 5});
        T1MF_Trapezoidal mediumWriteFrequencyMF = new T1MF_Trapezoidal("MF for Medium Write Frequency", new double[] {2, 4, 6, 8});
        T1MF_Trapezoidal highWriteFrequencyMF = new T1MF_Trapezoidal("MF for High Write Frequency", new double[] {5, 8, 10, 10});

        T1MF_Triangular lowPromotionMF = new T1MF_Triangular("Low Promotion", 0.0, 0.0, 5.0);
        T1MF_Triangular mediumPromotionMF = new T1MF_Triangular("Medium Promotion", 0.0, 5.0, 10.0);
        T1MF_Triangular highPromotionMF = new T1MF_Triangular("High Promotion", 5.0, 10.0, 10.0);

        //Set up the antecedents and consequents - note how the inputs are associated...
        T1_Antecedent highROA = new T1_Antecedent("HighROA", highROAMF, recencyOfAccess);
        T1_Antecedent mediumROA = new T1_Antecedent("MediumROA", mediumROAMF, recencyOfAccess);
        T1_Antecedent lowROA = new T1_Antecedent("LowROA", lowROAMF, recencyOfAccess);

        T1_Antecedent lowReadFrequency = new T1_Antecedent("LowRF", lowReadFrequencyMF, readFrequency);
        T1_Antecedent mediumReadFrequency = new T1_Antecedent("MediumRF", mediumReadFrequencyMF, readFrequency);
        T1_Antecedent highReadFrequency = new T1_Antecedent("HighRF", highReadFrequencyMF, readFrequency);
        
        T1_Antecedent lowWriteFrequency = new T1_Antecedent("LowRF", lowWriteFrequencyMF, writeFrequency);
        T1_Antecedent mediumWriteFrequency = new T1_Antecedent("MediumRF", mediumWriteFrequencyMF, writeFrequency);
        T1_Antecedent highWriteFrequency = new T1_Antecedent("HighRF", highWriteFrequencyMF, writeFrequency);

        T1_Consequent lowPromotion = new T1_Consequent("LowTip", lowPromotionMF, promotion);
        T1_Consequent mediumPromotion = new T1_Consequent("MediumTip", mediumPromotionMF, promotion);
        T1_Consequent highPromotion = new T1_Consequent("HighTip", highPromotionMF, promotion);

        //Set up the rulebase and add rules
        rulebase = new T1_Rulebase(27);
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{highROA, lowReadFrequency, lowWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{highROA, lowReadFrequency, mediumWriteFrequency}, mediumPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{highROA, lowReadFrequency, highWriteFrequency}, highPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{highROA, mediumReadFrequency, lowWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{highROA, mediumReadFrequency, mediumWriteFrequency}, highPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{highROA, mediumReadFrequency, highWriteFrequency}, highPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{highROA, highReadFrequency, lowWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{highROA, highReadFrequency, mediumWriteFrequency}, mediumPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{highROA, highReadFrequency, highWriteFrequency}, highPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mediumROA, lowReadFrequency, lowWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mediumROA, lowReadFrequency, mediumWriteFrequency}, mediumPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mediumROA, lowReadFrequency, highWriteFrequency}, highPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mediumROA, mediumReadFrequency, lowWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mediumROA, mediumReadFrequency, mediumWriteFrequency}, mediumPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mediumROA, mediumReadFrequency, highWriteFrequency}, highPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mediumROA, highReadFrequency, lowWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mediumROA, highReadFrequency, mediumWriteFrequency}, mediumPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mediumROA, highReadFrequency, highWriteFrequency}, highPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{lowROA, lowReadFrequency, lowWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{lowROA, lowReadFrequency, mediumWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{lowROA, lowReadFrequency, highWriteFrequency}, mediumPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{lowROA, mediumReadFrequency, lowWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{lowROA, mediumReadFrequency, mediumWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{lowROA, mediumReadFrequency, highWriteFrequency}, mediumPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{lowROA, highReadFrequency, lowWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{lowROA, highReadFrequency, mediumWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{lowROA, highReadFrequency, highWriteFrequency}, mediumPromotion));
        

        
        //just an example of setting the discretisation level of an output - the usual level is 100
        promotion.setDiscretisationLevel(100);        
                
        //plot some sets, discretizing each input into 100 steps.
        plotMFs("Recency of Acess Membership Functions", new T1MF_Interface[]{lowROAMF, mediumROAMF, highROAMF}, recencyOfAccess.getDomain(), 100); 
        plotMFs("Read Frequency Membership Functions", new T1MF_Interface[]{lowReadFrequencyMF, mediumReadFrequencyMF, highReadFrequencyMF}, readFrequency.getDomain(), 100);
        plotMFs("Write Frequency Membership Functions", new T1MF_Interface[]{lowWriteFrequencyMF, mediumWriteFrequencyMF, highWriteFrequencyMF}, writeFrequency.getDomain(), 100);   
        plotMFs("Promotion Membership Functions", new T1MF_Interface[]{lowPromotionMF, mediumPromotionMF, highPromotionMF}, promotion.getDomain(), 100);   
        
        //plot control surface
        //do either height defuzzification (false) or centroid d. (true)
        //plotControlSurface(true, 100, 100);
        
        //print out the rules
        System.out.println("\n"+rulebase);        
    }
    
    /**
     * Basic method that prints the output for a given set of inputs.
     * @param foodQuality
     * @param serviceLevel 
     */
    private String getPromotionValue(double recencyOfAccessLevel, double readFrequencyLevel, double writeFrequencyLevel)
    {
    	String outputString;
       //first, set the inputs
       recencyOfAccess.setInput(recencyOfAccessLevel);
	   readFrequency.setInput(readFrequencyLevel);
	   writeFrequency.setInput(writeFrequencyLevel);
	   //now execute the FLS
	   //Using height defuzzification:
	   //rulebase.evaluate(0).get(promotion); 
	   //Using centroid defuzzification
	   //rulebase.evaluate(1).get(promotion)
       /*System.out.println("ROA: "+ recencyOfAccess.getInput()
       + " | RF: " + readFrequency.getInput()
       + " | WF: " + writeFrequency.getInput() 
       + " | Promotion: " + rulebase.evaluate(1).get(promotion)
    		   ); 
        */
	   outputString = String.format("ROA: %f | RF: %f | WF: %f  | Promotion: %f \n",
        recencyOfAccess.getInput(), readFrequency.getInput(), writeFrequency.getInput(), rulebase.evaluate(1).get(promotion));
	   
	   return outputString;
    		   
    }
    
    private void plotMFs(String name, T1MF_Interface[] sets, Tuple xAxisRange, int discretizationLevel)
    {
        JMathPlotter plotter = new JMathPlotter(17,17,15);
        for (int i=0;i<sets.length;i++)
        {
            plotter.plotMF(sets[i].getName(), sets[i], discretizationLevel, xAxisRange, new Tuple(0.0,1.0), false);
        }
        plotter.show(name);
    }

    public static void main (String args[]) throws IOException
    {
    	FileWriter fw = new FileWriter("output_1.txt");
        BufferedWriter writeFileBuffer = new BufferedWriter(fw);
        FHybridMemT1FLS fHybridSystem = new FHybridMemT1FLS();
        
        //get some outputs
        for(int i = 0; i < 10; i++) {
        	for(int j = 0; j < 10; j++) {
        		for(int k = 0; k < 10; k++)
        			writeFileBuffer.write(fHybridSystem.getPromotionValue(i,j,k));
        	}
        }
       writeFileBuffer.close();
    }
}

