package ru.tinkoff.edu.scrapper.persistence.service.utils;

import ru.tinkoff.edu.java.scrapper.persistence.dto.ChatDto;

import java.util.Map;

public class RequestDataProvider {
    public static final String TEST_URL = "https://github.com/vbandurin7/Tinkoff-Homeworks/pull/5/commits/43b784c661bab3937730d3671c721f074c0b0cb2";
    public static final Map<String, String> LINK_INFO = Map.of(
            "username", "vbandurin7",
            "repository", "Tinkoff-Homeworks"
    );
    public static final String TEST_URL_2= "https://stackoverflow.com/questions/76051046/what-should-i-do-to-load-all-mpnet-base-v2-model-from-sentence-transformers";
    public static final Map<String, String> LINK_INFO_2 = Map.of("question_id", "76051046");
    public static final ChatDto TEST_CHAT_DTO = new ChatDto(1);
    public static final String INSERT_LINK_SQL = "INSERT INTO link (url, link_info) VALUES (?, ?::jsonb)";
    public static final String INSERT_CHAT_SQL = "INSERT INTO chat VALUES (?)";
    public static final String INSERT_CHAT_LINK_SQL = "INSERT INTO chat_link VALUES (?,?)";
    public static final String COUNT_CHAT_LINK_SQL = "SELECT count(*) FROM chat_link WHERE link_url = ? AND chat_id = ?";
    public static final String COUNT_LINK_SQL = "SELECT count(*) FROM link WHERE url = ?";
    public static final String COUNT_CHAT_SQL = "SELECT count(*) FROM chat WHERE id = ?";
    public static final String CLEAR_LINK_SQL = "DELETE FROM link";
    public static final String CLEAR_CHAT_SQL = "DELETE FROM chat";
    public static final String CLEAR_CHAT_LINK_SQL = "DELETE FROM chat_link";
}
