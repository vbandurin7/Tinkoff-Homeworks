
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackoverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.response.client.GitHubResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.client.StackoverflowResponse;

import java.util.Optional;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        StackoverflowClient stackoverflowClient = new StackoverflowClient();
        final Optional<StackoverflowResponse> stackoverflowResponse = stackoverflowClient.fetchQuestion(75869717);

        GitHubClient gitHubClient = new GitHubClient();
        final Optional<GitHubResponse> gitHubResponse = gitHubClient.fetchRepository("vbandurin7", "Tinkoff-Homeworks");

        Thread.sleep(1);

        System.out.println();
        System.out.println(stackoverflowResponse);
        System.out.println(gitHubResponse);
    }
}
