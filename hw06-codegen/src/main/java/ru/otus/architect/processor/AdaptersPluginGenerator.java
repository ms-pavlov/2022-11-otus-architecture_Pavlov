package ru.otus.architect.processor;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import lombok.Builder;
import ru.otus.architect.ioc.IoCContainer;
import ru.otus.architect.ioc.methods.FactoryMethod;
import ru.otus.architect.ioc.plugun.IoCPlugin;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import java.io.PrintWriter;

@Builder
public class AdaptersPluginGenerator implements PluginGenerator {

    private final static String PACKAGE_NAME = "ru.otus.architect.ioc.plugins.gen";
    private final static String REGISTER_METHOD = "register(\"%s\", args -> {%s})";
    private final static String DEFAULT_METHOD = "throw new RuntimeException(\"Not support yet!\");";

    private final Class<?> sourceClass;
    private final Element interfaceClass;
    private final PrintWriter out;
    private final DependencyNameGenerator generator;

    public AdaptersPluginGenerator(Class<?> sourceClass, Element interfaceClass, PrintWriter out, DependencyNameGenerator generator) {
        this.sourceClass = sourceClass;
        this.interfaceClass = interfaceClass;
        this.out = out;
        this.generator = generator;
    }

    @Override
    public TypeSpec prepPlugin() {
        return TypeSpec.classBuilder(generator.getPluginClassName())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(IoCPlugin.class)
                .addField(String.class, "registrationName", Modifier.PRIVATE, Modifier.FINAL)
                .addField(String.class, "scopeNewName", Modifier.PRIVATE, Modifier.FINAL)
                .addField(IoCContainer.class, "container", Modifier.PRIVATE, Modifier.FINAL)
                .addMethod(prepContractor())
                .addMethod(prepRegisterMethod())
                .addMethod(prepExecuteMethod())
                .build();
    }

    private MethodSpec prepExecuteMethod() {
        MethodSpec.Builder builder = MethodSpec
                .methodBuilder("execute")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);
        interfaceClass.getEnclosedElements()
                .stream()
                .map(Element::getSimpleName)
                .map(Name::toString)
                .map(generator::getDependencyName)
                .map(this::getDependencyStatement)
                .forEach(builder::addStatement);

        return builder.build();
    }


    private String getDependencyStatement(String dependencyName) {
        return String.format(
                REGISTER_METHOD,
                dependencyName,
                DEFAULT_METHOD);
    }

    private MethodSpec prepRegisterMethod() {
        return MethodSpec
                .methodBuilder("register")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(String.class, "name")
                .addParameter(FactoryMethod.class, "method")
                .addStatement("container.resolve(registrationName, name, method)")
                .build();
    }

    private MethodSpec prepContractor() {
        return MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "registrationName")
                .addParameter(String.class, "scopeNewName")
                .addParameter(IoCContainer.class, "container")
                .addStatement("this.registrationName = registrationName")
                .addStatement("this.scopeNewName = scopeNewName")
                .addStatement("this.container = container")
                .build();
    }
}
