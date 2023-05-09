package ru.tinkoff.edu.java.linkParser;

import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.linkParser.parser.LinkParser;
import ru.tinkoff.edu.java.linkParser.parserResult.GitHubResult;
import ru.tinkoff.edu.java.linkParser.parserResult.ParseResult;
import ru.tinkoff.edu.java.linkParser.parserResult.StackOverflowResult;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class LinkParserTest {

    LinkParser linkParser = new LinkParser();

    @ParameterizedTest
    @MethodSource("incorrectTestsProvider")
    void parseURL_invalidLinks_exceptionThrown(String link) {
        assertThrows(RuntimeException.class, () -> linkParser.parseURL(URI.create(link)));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("gitHubLinksProvider")
    void parseURL_validGitHubLinks_GitHubResultExpected(List<String> vals) {
        //given
        URI url = URI.create(vals.get(0));
        GitHubResult expectedResult = new GitHubResult(vals.get(1), vals.get(2));

        //when
        ParseResult parseResult = linkParser.parseURL(url);

        //then
        assertThat(parseResult).isEqualTo(expectedResult);
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("stackoverflowLinksProvider")
    void parseURL_validStackoverflowLinks_stackoverflowResultExpected(List<String> vals) {
        //given
        URI url = URI.create(vals.get(0));
        StackOverflowResult expectedResult = new StackOverflowResult(vals.get(1));

        //when
        ParseResult parseResult = linkParser.parseURL(url);

        //then
        assertThat(parseResult).isEqualTo(expectedResult);
    }



    static Stream<String> incorrectTestsProvider() {
        return Stream.of(
                "https://github.com/sanyarnd/tinkoff-java-co@urse-2022/",
                "https://github.com/sanyarnd//",
                "https://github.com/a/b/",
                "https://github.com/........../2b/",
                "https://github.com/      asdfasdfasd/2b/",
                "https://github.com/test/   w2b/",
                "https://stackoverflow.com/quesations/1642028/what-is-the-operator-in-c",
                "https://stackoverflow.com/quesations/v1642028/what-is-the-operator-in-c",
                "https://stackoverflow.com/questions//what-is-the-operator-in-c",
                "https://stackoverflow.com/questions/   23412/what-is-the-operator-in-c",
                "https://stackoverflow.com/questions/23  412/what-is-the-operator-in-c",
                "https://stackoverflow.com/questions  /23412/what-is-the-operator-in-c"
        );
    }

     static Stream<List<String>> gitHubLinksProvider() {
         return Stream.of(
                 List.of("https://github.com/sanyarnd/tinkoff-java-course-2022/", "sanyarnd", "tinkoff-java-course-2022"),
                 List.of("https://github.com/vbandurin7/Tinkoff-Homeworks/", "vbandurin7", "Tinkoff-Homeworks")
         );
     }

    static Stream<List<String>> stackoverflowLinksProvider() {
        return Stream.of(
                List.of("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c", "1642028"),
                List.of("https://stackoverflow.com/questions/128/helloWorld", "128")
        );
    }
}
