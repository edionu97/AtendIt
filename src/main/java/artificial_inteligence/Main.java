package artificial_inteligence;

import artificial_inteligence.utils.TrainFileIterator;
import utils.ConstantsManager;

import java.io.File;


public class Main {

    public static void main(String... args) throws Exception {

        //annotateData();

    }

    private  static  void annotateData() throws Exception{
        final ConstantsManager manager = ConstantsManager.getInstance();

        System.load(
                manager.get("opencv")
        );

        final String folderToProcess = manager.get("imageFolderName");
        final String imageFileDir = manager.get("imageFileParentPath");
        final String annotationDirPath = manager.get("annotationFolderPath");
        final String oldAnnotationDetailsPath = manager.get("oldAnnotationFolderPath");


        TrainFileIterator fileIterator = new TrainFileIterator(
                new File(oldAnnotationDetailsPath)
        );

        File annotationDir = new File(annotationDirPath);

        if(annotationDir.mkdirs()){
            System.out.println(annotationDir.getName() + " created!");
        }
        else System.out.println("Folder exists!");

        fileIterator.processFile(
                new File(imageFileDir),
                folderToProcess,
                annotationDir
        );
    }

}
