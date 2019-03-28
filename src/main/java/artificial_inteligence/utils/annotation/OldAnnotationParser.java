package artificial_inteligence.utils.annotation;

import artificial_inteligence.utils.xmls.BndBox;
import artificial_inteligence.utils.xmls.Object;
import artificial_inteligence.utils.xmls.Size;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OldAnnotationParser implements  IOldAnnotationParser {

    public OldAnnotationParser(final String content, String outputDirName, int width, int height){
        this.fileContent = content;
        this.outputDirName = outputDirName;
        this.width = width;
        this.height = height;
    }

    @Override
    public Annotation createAnnotation(final File image) throws Exception {

        final int indexOfFile = fileContent.indexOf(image.getName());
        int x1, y1, w, h, blur, expression, illumination, invalid, occlusion, pose;

        // Get the next line after the filename
        Scanner reader = new Scanner(
                new BufferedReader(
                        new StringReader(
                                fileContent.substring(indexOfFile + image.getName().length() + 1)
                        )
                ));

        int N = reader.nextInt();

        List<Object> objects = new ArrayList<>();

        for (int i = 0; i < N; ++i) {

            x1 = reader.nextInt();y1 = reader.nextInt();
            w = reader.nextInt();h = reader.nextInt();

            // ignored
            blur = reader.nextInt();expression = reader.nextInt();
            illumination = reader.nextInt();invalid = reader.nextInt();
            occlusion = reader.nextInt();pose = reader.nextInt();

            objects.add(
                    new Object(
                            "Face",
                            new BndBox(
                                    x1, x1 + w, y1, y1 + h
                            )
                    )
            );

        }

        return new Annotation(
                new Size(
                        width, height, 3
                ),
                objects,
                outputDirName,
                image.getName(),
                image.getPath()
        );
    }

    private String fileContent;
    private String outputDirName;
    private int width, height;
}
