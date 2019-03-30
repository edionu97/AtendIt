package artificial_inteligence.trainer;

import javafx.util.Pair;
import org.datavec.api.io.filters.RandomPathFilter;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.objdetect.ObjectDetectionRecordReader;
import org.datavec.image.recordreader.objdetect.impl.VocLabelProvider;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.ConvolutionMode;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.WorkspaceMode;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.objdetect.Yolo2OutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.transferlearning.FineTuneConfiguration;
import org.deeplearning4j.nn.transferlearning.TransferLearning;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.util.ModelSerializer;
import org.deeplearning4j.zoo.model.YOLO2;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.RmsProp;
import utils.ConstantsManager;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

public class YOLOTrainer {

    private static final int INPUT_WIDTH = 416;
    private static final int INPUT_HEIGHT = 416;
    private static final int CHANNELS = 3;

    private static final int GRID_WIDTH = 13;
    private static final int GRID_HEIGHT = 13;
    private static final int CLASSES_NUMBER = 1;
    private static final int BOXES_NUMBER = 5;

    private static final double[][] PRIOR_BOXES = {
            {0.57273, 0.677385},
            {1.87446, 2.06253},
            {3.33843, 5.47434},
            {7.88282, 3.52778},
            {9.77052, 9.16828}
    };

    private static final int BATCH_SIZE = 4;
    private static final int EPOCHS = 50;
    private static final double LEARNING_RATE = 0.00001;
    private static  final  int SEED = 1024;

    private static final double LAMBDA_COORD = 1.0;
    private static final double LAMBDA_NO_OBJECT = 0.5;



    private YOLOTrainer(){
        manager = ConstantsManager.getInstance();
        random = new Random(SEED);
    }


    /**
     * Performs training of the network
     * @param statsStorage: the stats storage used to display network train stats
     * @throws Exception: if something went wrong
     */
    public void doTrain(final StatsStorage statsStorage) throws  Exception{

        final File file = new File(
                manager.get("imageFileParentPath"),
                manager.get("imageFolderName")
        );

        if(!file.exists()){
            throw new Exception(
                    "Train image directory does not exist"
            );
        }

        Pair<InputSplit, InputSplit> trainTest = getTrainAndTestData(
                file, 1, .0
        );

        InputSplit trainData = trainTest.getKey();

        ObjectDetectionRecordReader recordReaderTrain = new ObjectDetectionRecordReader(
                INPUT_HEIGHT, INPUT_WIDTH, CHANNELS,
                GRID_HEIGHT, GRID_WIDTH, new VocLabelProvider(manager.get("imageFileParentPath")));
        recordReaderTrain.initialize(trainData);

        RecordReaderDataSetIterator train = new RecordReaderDataSetIterator(
                recordReaderTrain,
                BATCH_SIZE,
                1,
                1,
                true
        );
        train.setPreProcessor(new ImagePreProcessingScaler(0, 1));

        ComputationGraph pretrained = (ComputationGraph) YOLO2.builder().build().initPretrained();

        INDArray priors = Nd4j.create(PRIOR_BOXES);

        FineTuneConfiguration fineTuneConf = new FineTuneConfiguration.Builder()
                .seed(SEED)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .gradientNormalization(GradientNormalization.RenormalizeL2PerLayer)
                .gradientNormalizationThreshold(1.0)
                .updater(new RmsProp(LEARNING_RATE))
                .activation(Activation.IDENTITY).miniBatch(true)
                .trainingWorkspaceMode(WorkspaceMode.ENABLED)
                .build();


        ComputationGraph model = new TransferLearning.GraphBuilder(pretrained)
                .fineTuneConfiguration(fineTuneConf)
                .setInputTypes(InputType.convolutional(INPUT_HEIGHT, INPUT_WIDTH, CHANNELS))
                .removeVertexKeepConnections("conv2d_23")
                .addLayer("convolution2d_23",
                        new ConvolutionLayer.Builder(1, 1)
                                .nIn(1024)
                                .nOut(BOXES_NUMBER * (5 + CLASSES_NUMBER))
                                .stride(1, 1)
                                .convolutionMode(ConvolutionMode.Same)
                                .weightInit(WeightInit.UNIFORM)
                                .hasBias(false)
                                .activation(Activation.IDENTITY)
                                .build(), "leaky_re_lu_22")
                .removeVertexKeepConnections("outputs")
                .addLayer("outputs",
                        new Yolo2OutputLayer.Builder()
                                .lambbaNoObj(LAMBDA_NO_OBJECT)
                                .lambdaCoord(LAMBDA_COORD)
                                .boundingBoxPriors(priors)
                                .build(), "convolution2d_23")
                .setOutputs("outputs")
                .build();

        model.addListeners(new StatsListener(statsStorage));

        for(int i = 1; i < EPOCHS; ++i){
            train.reset();
            while (train.hasNext()){
                model.fit(train.next());
            }
            System.out.println(String.format("Finished epoch %s " , i));
        }

        ModelSerializer.writeModel(model, "detection_model.data", true);
        System.out.println(model.summary());
    }

    /**
     * Reads all the images from image folder and distribute images in two categories train and test randomly,
     * based on a percentage
     * @param imageDir: the file that represents the image directory
     * @param trainSize: the percentage of images that should be kept for training
     * @param testSize: the percentage of images that should be kept for testing
     * @return a pair of values where Pair.key represents train images and Pair.value represents test images
     */
    @SuppressWarnings("SameParameterValue")
    private Pair<InputSplit, InputSplit> getTrainAndTestData(final File imageDir, final double trainSize, final double testSize){

        ///Create a filter to keep only those images that have a corresponding annotation file in /annotation folder
        RandomPathFilter filter = new RandomPathFilter(random){
            @Override
            protected boolean accept(String name){

                final String imageAnnotation = name
                        .replace("/images/", "/annotations/")
                        .replace(".jpg", ".xml");

                try {
                    final URI uri = new URI(imageAnnotation);
                    return new File(uri).exists();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                return true;
            }
        };

        InputSplit[] data = new FileSplit(
                imageDir,
                NativeImageLoader.ALLOWED_FORMATS,
                random
        ).sample(filter, trainSize, testSize);


        return new Pair<>(data[0], data[1]);
    }

    /**
     * @return an instance of the YOLOTrainer class
     */
    public static YOLOTrainer getInstance(){

        if(_instance == null){
            synchronized (YOLOTrainer.class){
                if(_instance == null){
                    _instance = new YOLOTrainer();
                }
            }
        }

        return _instance;
    }

    /**
     * @return the trained detection_model
     * @throws Exception: if the detection_model does not exist
     */
    public static ComputationGraph getModel() throws Exception {

        final String modelName = ConstantsManager.getInstance().get("modelPath");

        File file = new File(modelName);

        if (!file.exists() || file.isDirectory()) {
            throw new Exception("Saved detection_model not found!");
        }

        return ModelSerializer.restoreComputationGraph(file);
    }

    private Random random;
    private ConstantsManager manager;
    private static YOLOTrainer _instance;
}
