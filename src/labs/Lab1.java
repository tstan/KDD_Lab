package labs;

import DocumentClasses.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author tstan
 */
public class Lab1 {
    
    public static void main(String args[]) throws IOException{
        
        DocumentCollection newDC = new DocumentCollection();
        newDC.documentCollection("documents.txt");
                
        /* print debug results for lab 1 */
        newDC.printResults();
        
        /* serialize document collection to file */
        try(ObjectOutputStream os = new ObjectOutputStream(new
                    FileOutputStream(new File("docvector")))){
            os.writeObject(newDC);
        } 
        catch(Exception e){
            System.out.println(e);
        }

    }
}
