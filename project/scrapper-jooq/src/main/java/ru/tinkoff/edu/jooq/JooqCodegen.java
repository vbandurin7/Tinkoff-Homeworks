package ru.tinkoff.edu.jooq;

import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;

public class JooqCodegen {
    public static void main(String[] args) throws Exception {

        Target target = new Target()
                .withPackageName("ru.tinkoff.edu.java.scrapper.domain.jooq")
                .withDirectory("scrapper/src/main/java");

        Database database = new Database()
                .withName("org.jooq.meta.postgres.PostgresDatabase").
                withIncludes(".*").
                withExcludes("").
                withInputSchema("public");

        Generate options = new Generate()
                .withGeneratedAnnotation(true)
                .withGeneratedAnnotationDate(false)
                .withNullableAnnotation(true)
                .withNullableAnnotationType("org.jetbrains.annotations.Nullable")
                .withNonnullAnnotation(true)
                .withNonnullAnnotationType("org.jetbrains.annotations.NotNull")
                .withJpaAnnotations(false)
                .withValidationAnnotations(true)
                .withSpringAnnotations(true)
                .withConstructorPropertiesAnnotation(true)
                .withConstructorPropertiesAnnotationOnPojos(true)
                .withConstructorPropertiesAnnotationOnRecords(true)
                .withFluentSetters(false)
                .withDaos(false)
                .withPojos(true);

        Configuration configuration = new Configuration().withJdbc(
                        new Jdbc().
                                withDriver("org.postgresql.Driver").
                                withUrl("jdbc:postgresql://localhost:5432/scrapper").
                                withUsername("root").
                                withPassword("root")
                )
                .withGenerator(
                        new Generator()
                                .withDatabase(database)
                                .withGenerate(options)
                                .withTarget(target)
                );

        GenerationTool.generate(configuration);
    }
}