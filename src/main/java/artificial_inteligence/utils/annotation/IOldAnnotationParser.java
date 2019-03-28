package artificial_inteligence.utils.annotation;

import java.io.File;

@FunctionalInterface
public interface IOldAnnotationParser {
    Annotation createAnnotation(final File file) throws Exception;
}
