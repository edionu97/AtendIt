package artificial_inteligence.utils;

import org.deeplearning4j.nn.layers.objdetect.DetectedObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class NonMaxSuppression {

    /**
     * Remove all boxes that have the IOU with max detected object greater than 0.4
     * @param detectedObjects: all detected objects
     * @param maxObjectDetect: the object with higher confidence
     */
    private static void removeObjectsIntersectingWithMax(ArrayList<DetectedObject> detectedObjects, DetectedObject maxObjectDetect) {

        double[] bottomRightXY1 = maxObjectDetect.getBottomRightXY();
        double[] topLeftXY1 = maxObjectDetect.getTopLeftXY();

        List<DetectedObject> removeIntersectingObjects = new ArrayList<>();

        for (DetectedObject detectedObject : detectedObjects) {

            double[] topLeftXY = detectedObject.getTopLeftXY();
            double[] bottomRightXY = detectedObject.getBottomRightXY();

            double iox1 = Math.max(topLeftXY[0], topLeftXY1[0]);
            double ioy1 = Math.max(topLeftXY[1], topLeftXY1[1]);

            double iox2 = Math.min(bottomRightXY[0], bottomRightXY1[0]);
            double ioy2 = Math.min(bottomRightXY[1], bottomRightXY1[1]);

            double inter_area = (ioy2 - ioy1) * (iox2 - iox1);

            double box1_area = (bottomRightXY1[1] - topLeftXY1[1]) * (bottomRightXY1[0] - topLeftXY1[0]);
            double box2_area = (bottomRightXY[1] - topLeftXY[1]) * (bottomRightXY[0] - topLeftXY[0]);

            double union_area = box1_area + box2_area - inter_area;
            double iou = inter_area / union_area;

            if (iou > 0.4) {
                removeIntersectingObjects.add(detectedObject);
            }
        }

        detectedObjects.removeAll(removeIntersectingObjects);
    }

    /**
     * @param predictedObjects: All the objects predicted by network
     * @return a list of non intersecting objects
     */
    public static List<DetectedObject> getObjects(List<DetectedObject> predictedObjects) {

        ArrayList<DetectedObject> objects = new ArrayList<>();

        if (predictedObjects == null) {
            return null;
        }

        ArrayList<DetectedObject> detectedObjects = new ArrayList<>(predictedObjects);
        while (!detectedObjects.isEmpty()) {
            Optional<DetectedObject> max = detectedObjects.stream().max(
                    Comparator.comparingDouble(DetectedObject::getConfidence)
            );

            DetectedObject maxObjectDetect = max.get();
            removeObjectsIntersectingWithMax(detectedObjects, maxObjectDetect);
            detectedObjects.remove(maxObjectDetect);
            objects.add(maxObjectDetect);
        }

        return objects;
    }
}
