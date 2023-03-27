package ru.otus.architect.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import ru.otus.architect.processor.visitors.IoCPluginListVisitor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

@SupportedAnnotationTypes(
        "ru.otus.architect.processor.IoCAdapterPlugin")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class IoCAdapterPluginProcessor extends AbstractProcessor {
    private final static String CLASS_POSTFIX = "AdapterPlugin";
    private final static String PACKAGE_NAME = "ru.otus.architect.ioc.plugins.gen";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            annotatedElements.forEach(element -> element.getAnnotationMirrors()
                    .stream()
                    .map(AnnotationMirror::getElementValues)
                    .map(Map::entrySet)
                    .flatMap(Set::stream)
                    .filter(entry -> entry.getKey().toString().equals("classes()"))
                    .map(Map.Entry::getValue)
                    .map(annotationValue -> annotationValue.accept(new IoCPluginListVisitor(), null))
                    .forEach(
                            classes -> classes.forEach(
                                    clazz -> writeBuilderFile(clazz, element))));
        }
        return true;
    }

    private void writeBuilderFile(Class<?> sourceClass, Element interfaceClass) {
        DependencyNameGenerator nameGenerator = DependencyNameGeneratorImpl.builder()
                .sourceClassName(sourceClass.getSimpleName())
                .interfaceClassName(interfaceClass.getSimpleName().toString())
                .build();
        try (PrintWriter out = new PrintWriter(
                processingEnv
                        .getFiler()
                        .createSourceFile(nameGenerator.getPluginClassName())
                        .openWriter())) {
            JavaFile.builder(
                            PACKAGE_NAME,
                            AdaptersPluginGenerator
                                    .builder()
                                    .sourceClass(sourceClass)
                                    .interfaceClass(interfaceClass)
                                    .out(out)
                                    .generator(nameGenerator)
                                    .build()
                                    .prepPlugin())
                    .build()
                    .writeTo(out);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
