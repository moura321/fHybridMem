/*
 * SimpleT1FLS.java
 *
 * Created on May 20th 2012
 *
 * Copyright 2012 Christian Wagner All Rights Reserved.
 */
package fHybridFuzzyModule;

import generic.Input;
import generic.Output;
import generic.Tuple;
import tools.JMathPlotter;
import type1.sets.T1MF_Gauangle;
import type1.sets.T1MF_Gaussian;
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

        /*if=0_0_1_1_2_0&then=0_0&
         * if=0_0_1_1_2_1&then=0_2&
         * if=0_0_1_1_2_2&then=0_2&
         * if=0_0_1_2_2_0&then=0_0&
         * if=0_0_1_2_2_1&then=0_1&
         * if=0_0_1_2_2_2&then=0_2&if=0_1_1_0_2_0&then=0_0&if=0_1_1_0_2_1&then=0_1&if=0_1_1_0_2_2&then=0_2&if=0_1_1_1_2_0&then=0_0&if=0_1_1_1_2_1&then=0_1&if=0_1_1_1_2_2&then=0_2&if=0_1_1_2_2_0&then=0_0&if=0_1_1_2_2_1&then=0_1&if=0_1_1_2_2_2&then=0_2&if=0_2_1_0_2_0&then=0_0&if=0_2_1_0_2_1&then=0_0&if=0_2_1_0_2_2&then=0_1&if=0_2_1_1_2_0&then=0_0&if=0_2_1_1_2_1&then=0_0&if=0_2_1_1_2_2&then=0_1&if=0_2_1_2_2_0&then=0_0&if=0_2_1_2_2_1&then=0_0&if=0_2_1_2_2_2&then=0_1
        
        
        */
        //Set up the rulebase and add rules
        rulebase = new T1_Rulebase(27);
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{highROA, lowReadFrequency, lowWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{highROA, lowReadFrequency, mediumWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{highROA, lowReadFrequency, highWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{highROA, lowReadFrequency, lowWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{highROA, lowReadFrequency, lowWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{highROA, lowReadFrequency, lowWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{highROA, lowReadFrequency, lowWriteFrequency}, lowPromotion));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{highROA, lowReadFrequency, lowWriteFrequency}, lowPromotion));
        

        
        //just an example of setting the discretisation level of an output - the usual level is 100
        tip.setDiscretisationLevel(50);        
        
        //get some outputs
        getTip(7,8);
        
        //plot some sets, discretizing each input into 100 steps.
        plotMFs("Food Quality Membership Functions", new T1MF_Interface[]{badFoodMF, greatFoodMF}, food.getDomain(), 100); 
        plotMFs("Service Level Membership Functions", new T1MF_Interface[]{unfriendlyServiceMF, okServiceMF, friendlyServiceMF}, service.getDomain(), 100);
        plotMFs("Level of Tip Membership Functions", new T1MF_Interface[]{lowTipMF, mediumTipMF, highTipMF}, tip.getDomain(), 100);
       
        //plot control surface
        //do either height defuzzification (false) or centroid d. (true)
        plotControlSurface(true, 100, 100);
        
        //print out the rules
        System.out.println("\n"+rulebase);        
    }
    
    /**
     * Basic method that prints the output for a given set of inputs.
     * @param foodQuality
     * @param serviceLevel 
     */
    private void getTip(double foodQuality, double serviceLevel)
    {
        //first, set the inputs
        food.setInput(foodQuality);
        service.setInput(serviceLevel);
        //now execute the FLS and print output
        System.out.println("The food was: "+food.getInput());
        System.out.println("The service was: "+service.getInput());
        System.out.println("Using height defuzzification, the FLS recommends a tip of"
                + "tip of: "+rulebase.evaluate(0).get(tip)); 
        System.out.println("Using centroid defuzzification, the FLS recommends a tip of"
                + "tip of: "+rulebase.evaluate(1).get(tip));     
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

    private void plotControlSurface(boolean useCentroidDefuzzification, int input1Discs, int input2Discs)
    {
        double output;
        double[] x = new double[input1Discs];
        double[] y = new double[input2Discs];
        double[][] z = new double[y.length][x.length];
        double incrX, incrY;
        incrX = food.getDomain().getSize()/(input1Discs-1.0);
        incrY = service.getDomain().getSize()/(input2Discs-1.0);

        //first, get the values
        for(int currentX=0; currentX<input1Discs; currentX++)
        {
            x[currentX] = currentX * incrX;        
        }
        for(int currentY=0; currentY<input2Discs; currentY++)
        {
            y[currentY] = currentY * incrY;
        }
        
        for(int currentX=0; currentX<input1Discs; currentX++)
        {
            food.setInput(x[currentX]);
            for(int currentY=0; currentY<input2Discs; currentY++)
            {
                service.setInput(y[currentY]);
                if(useCentroidDefuzzification)
                    output = rulebase.evaluate(1).get(tip);
                else
                    output = rulebase.evaluate(0).get(tip);
                z[currentY][currentX] = output;
            }    
        }
        
        //now do the plotting
        JMathPlotter plotter = new JMathPlotter(17, 17, 14);
        plotter.plotControlSurface("Control Surface",
                new String[]{food.getName(), service.getName(), "Tip"}, x, y, z, new Tuple(0.0,30.0), true);   
       plotter.show("Type-1 Fuzzy Logic System Control Surface for Tipping Example");
    }
    
    public static void main (String args[])
    {
        new SimpleT1FLS();
    }
}

