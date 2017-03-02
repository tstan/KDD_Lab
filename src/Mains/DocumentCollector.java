package Mains;

import DocumentClasses.DocumentCollection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Create a documentcollection using documents.txt and serialize to a file
 *
 * Prepares documents into text vectors to use in CosineCloseness and OkapiCloseness
 * @author tstan
 */
public class DocumentCollector {
    
    public static void main(String args[]) throws IOException{
        
        DocumentCollection newDC = new DocumentCollection();
        newDC.documentCollection("documents.txt", "document");
                
        /* print debug results for lab 1 */
        //newDC.printResults();
        
        /* serialize document collection to file */
        try(ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File("docvector")))){
            os.writeObject(newDC);
        } 
        catch(Exception e){
            System.out.println(e);
        }

    }
}
