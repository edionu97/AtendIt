package artificial_inteligence;

import artificial_inteligence.trainer.YOLOTrainer;
import artificial_inteligence.utils.TrainFileIterator;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.storage.FileStatsStorage;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import utils.ConstantsManager;

import java.io.File;


public class Main {

    public static void main(String... args) throws Exception {

        //annotateData();

        final StatsStorage statsStorage = new InMemoryStatsStorage();
        UIServer
                .getInstance()
                .attach(
                        statsStorage
                );

        YOLOTrainer.getInstance().doTrain(statsStorage);
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
