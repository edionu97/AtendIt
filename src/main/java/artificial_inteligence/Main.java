package artificial_inteligence;

import artificial_inteligence.utils.Annotation;
import artificial_inteligence.utils.xmls.BndBox;
import artificial_inteligence.utils.xmls.Object;
import artificial_inteligence.utils.xmls.Size;
import artificial_inteligence.utils.xmls.Source;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;

public class Main {
    public static void main(String ...args) throws  Exception{

        File file = new File("C:\\Users\\Eduard\\Desktop\\test.xml");

        JAXBContext context = JAXBContext.newInstance(
                Source.class,
                Size.class,
                Object.class,
                BndBox.class,
                Annotation.class
        );

        Marshaller marshaller = context.createMarshaller();

        Annotation annotation = new Annotation(
            new Size(100,100,3),
            new Object(
                    "Face",
                    new BndBox(
                            10,10,10,10
                    )
            ),
                "Folder",
                "Filename",
                "Path"
        );
        

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

        marshaller.marshal(annotation, file);
    }
}
