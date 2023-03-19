package ru.tinkoff.edu.java.linkParser;

import ru.tinkoff.edu.java.linkParser.parser.LinkParser;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Test {

    private static class Cat {
        int age;
       public Cat(int age) {
            this.age = age;
        }
    }
    public static void main(String[] args) throws MalformedURLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        LinkParser linkParser = new LinkParser();

        List<String> correctTests = new ArrayList<>(List.of(
                "https://github.com/sanyarnd/tinkoff-java-course-2022/",
                "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"
        ));

        List<String> incorrectTests = new ArrayList<>(List.of(
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
        ));

        for (String correctTest : correctTests) {
            System.out.println(linkParser.parseURL(new URL(correctTest)));
        }

        for (String incorrectTest : incorrectTests) {
            try {
                linkParser.parseURL(new URL(incorrectTest));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

}
