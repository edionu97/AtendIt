package artificial_inteligence.detector;

import artificial_inteligence.utils.xmls.BndBox;
import org.bytedeco.javacpp.opencv_core;
import org.deeplearning4j.nn.graph.ComputationGraph;

import java.util.List;

public interface IDetector<T> {

    T getNetwork();

    List<BndBox> detectObject(
            opencv_core.Mat image, double threshHold) throws Exception;
}
