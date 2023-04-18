package ru.tinkoff.edu.scrapper.persistence.service.utils;

public class RequestProvider {
    public static final String INSERT_LINK_SQL = "INSERT INTO link(url) VALUES (?)";
    public static final String INSERT_CHAT_SQL = "INSERT INTO chat VALUES (?)";
    public static final String INSERT_CHAT_LINK_SQL = "INSERT INTO chat_link VALUES (?,?)";
    public static final String COUNT_CHAT_LINK_SQL = "SELECT count(*) FROM chat_link WHERE link_url = ? AND chat_id = ?";
    public static final String COUNT_LINK_SQL = "SELECT count(*) FROM link WHERE url = ?";
    public static final String COUNT_CHAT_SQL = "SELECT count(*) FROM chat WHERE id = ?";
    public static final String CLEAR_LINK_SQL = "DELETE FROM link";
    public static final String CLEAR_CHAT_SQL = "DELETE FROM chat";
    public static final String CLEAR_CHAT_LINK_SQL = "DELETE FROM chat_link";
}
