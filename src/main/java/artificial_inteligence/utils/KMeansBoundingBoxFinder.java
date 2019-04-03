package artificial_inteligence.utils;


import artificial_inteligence.utils.annotation.Annotation;
import artificial_inteligence.utils.xmls.BndBox;
import artificial_inteligence.utils.xmls.Object;
import artificial_inteligence.utils.xmls.Size;
import artificial_inteligence.utils.xmls.Source;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.EuclideanDistance;
import utils.ConstantsManager;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.crypto.Data;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class KMeansBoundingBoxFinder {


    public KMeansBoundingBoxFinder(int clusters, int maxIter) {

        kMeans = new KMeans(
                clusters, maxIter, new EuclideanDistance()
        );

        try {
            this.unmarshaller = JAXBContext.newInstance(
                    Source.class,
                    Size.class,
                    Object.class,
                    BndBox.class,
                    Annotation.class
            ).createUnmarshaller();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        this.instances = getInstances();
    }


    public void getBoxes() {

        final  Dataset dataset = new DefaultDataset();
        dataset.addAll(instances);

        Dataset[] clusters = kMeans.cluster(dataset);

        for(final Dataset cluster : clusters){
            final Instance centroid = get2DCentroid(cluster);
            System.out.println(centroid.value(0) * 32 + " " +centroid.value(1) * 32);
        }
    }

    private List<Instance> getInstances() {

        final String annotationPath = ConstantsManager
                .getInstance()
                .get("annotationFolderPath");

        List<Instance> list = new ArrayList<>();

        for(final File file : Objects.requireNonNull(new File(annotationPath).listFiles())){
            try{

                Annotation annotation = (Annotation)unmarshaller.unmarshal(file);

                for(final Object object : annotation.getObject()){

                    final BndBox bndBox  = object.getBndbox();

                    double[] dst = new double[2];
                    dst[0] = (bndBox.getXmax() - bndBox.getXmin() + .0) / annotation.getSize().getWidth();
                    dst[1] = (bndBox.getYmax() - bndBox.getYmin() + .0) / annotation.getSize().getHeight();

                    list.add(
                            new SparseInstance(dst)
                    );
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        return list;
    }


    /**
     * Get the centroid of 2D cluster of points
     * @param cluster: the cluster of points
     * @return an 2D instance which represents the coordinates of the centroid
     */
    private Instance get2DCentroid(final Dataset cluster) {

        double xMean = .0, yMean = .0;
        for (Instance instance : cluster) {
            xMean += instance.value(0);
            yMean += instance.value(1);
        }

        final double[] means = {
                xMean / cluster.size(), yMean / cluster.size()
        };

        return new SparseInstance(means);
    }

    private final KMeans kMeans;
    private Unmarshaller unmarshaller;
    private final List<Instance> instances;
}
