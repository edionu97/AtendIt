package artificial_inteligence.detector;

import java.util.ArrayList;
import java.util.List;

import artificial_inteligence.utils.NonMaxSuppression;
import artificial_inteligence.trainer.YOLOTrainer;
import artificial_inteligence.utils.xmls.BndBox;
import org.bytedeco.javacpp.opencv_core.Mat;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.layers.objdetect.DetectedObject;
import org.deeplearning4j.nn.layers.objdetect.Yolo2OutputLayer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;


public class YOLOModel implements IDetector<ComputationGraph> {

    public YOLOModel() {
        try {
            network = YOLOTrainer.getModel();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ComputationGraph getNetwork() {
        return network;
    }

    @Override
    public List<BndBox> detectObject(Mat image, double threshHold) throws Exception {

        // get the output layer of the network
        Yolo2OutputLayer outputLayer = (Yolo2OutputLayer) network.getOutputLayer(0);

        // load image
        NativeImageLoader loader = new NativeImageLoader(
                IMAGE_INPUT_W, IMAGE_INPUT_H, CHANNELS
        );

        INDArray ds = loader.asMatrix(image);

        ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(0, 1);

        //normalize image [0,1]
        scaler.transform(ds);

        INDArray results = network.outputSingle(ds);

        List<DetectedObject> detectedObjects = outputLayer.getPredictedObjects(
                results, threshHold
        );

        return getDetectedObjects(
                NonMaxSuppression.getObjects(detectedObjects)
        );
    }

    private List<BndBox> getDetectedObjects(final List<DetectedObject> objects) {

        List<BndBox> bndBoxes = new ArrayList<>();

        objects.forEach(obj -> {
            double[] xy1 = obj.getTopLeftXY();
            double[] xy2 = obj.getBottomRightXY();

            int x1 = (int) Math.round(IMAGE_INPUT_W * xy1[0] / GRID_W);
            int y1 = (int) Math.round(IMAGE_INPUT_H * xy1[1] / GRID_H);
            int x2 = (int) Math.round(IMAGE_INPUT_W * xy2[0] / GRID_W);
            int y2 = (int) Math.round(IMAGE_INPUT_H * xy2[1] / GRID_H);

            bndBoxes.add(
                    new BndBox(
                            x1,  x2, y1, y2
                    )
            );
        });

        return bndBoxes;
    }

    private ComputationGraph network;

    private final int GRID_W = YOLOTrainer.GRID_WIDTH;
    private final int GRID_H = YOLOTrainer.GRID_HEIGHT;
    private final int CHANNELS = YOLOTrainer.CHANNELS;
    private final int IMAGE_INPUT_W = YOLOTrainer.INPUT_WIDTH;
    private final int IMAGE_INPUT_H = YOLOTrainer.INPUT_HEIGHT;
}
