package com.javarush.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;

public class TinderBoltApp extends MultiSessionTelegramBot {
    public static final String TELEGRAM_BOT_NAME = System.getenv("TELEGRAM_BOT_NAME");
    public static final String TELEGRAM_BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
    public static final String OPEN_AI_TOKEN = System.getenv("OPEN_AI_TOKEN");
    public static final String AI_THINKING = "Подождите пару секунд - ChatGPT думает...";

    private final ChatGPTService chatGPT = new ChatGPTService(OPEN_AI_TOKEN);

    private DialogMode currentMode = null;
    private ArrayList<String> array = new ArrayList<>();

    private UserInfo me;
    private UserInfo she;
    private int questionCount;

    public TinderBoltApp() {super(TELEGRAM_BOT_NAME, TELEGRAM_BOT_TOKEN);
    }

    @Override
    public void onUpdateEventReceived(Update update) {
        String message = getMessageText();

        if (message.equals("/start")) {
            currentMode = DialogMode.MAIN;
            sendPhotoMessage("main");
            String text = loadMessage("main");
            sendTextMessage(text);

            showMainMenu("главное меню бота", "/start",
                    "генерация Tinder-профля \uD83D\uDE0E", "/profile",
                    "сообщение для знакомства \uD83E\uDD70", "/opener",
                    "переписка от вашего имени \uD83D\uDE08", "/message",
                    "переписка со звездами \uD83D\uDD25", "/date",
                    "задать вопрос чату GPT \uD83E\uDDE0", "/gpt");
            return;
        }

        if (message.equals("/gpt")) {
            currentMode = DialogMode.GPT;
            sendPhotoMessage("gpt");
            String text = loadMessage("gpt");
            sendTextMessage(text);
            return;
        }

        if (currentMode == DialogMode.GPT && !isMessageCommand()) {
            String prompt = loadPrompt("gpt");
            Message msg = sendTextMessage(AI_THINKING);
            String answer = chatGPT.sendMessage(prompt, message);
            updateTextMessage(msg, answer);
            return;
        }

        if (message.equals("/date")) {
            currentMode = DialogMode.DATE;
            sendPhotoMessage("date");
            String text = loadMessage("date");
            sendTextButtonsMessage(text,
                    "Ариана Гранде", "date_grande",
                    "Марго Робби", "date_robbie",
                    "Зендея", "date_zendaya",
                    "Райан Гослинг", "date_gosling",
                    "Том Харди", "date_hardy");
            return;
        }

        if (currentMode == DialogMode.DATE && !isMessageCommand()) {
            String query = getCallbackQueryButtonKey();
            if (query.startsWith("date_")) {
                sendPhotoMessage(query);
                sendTextMessage("Отличный выбор! \nТвоя задача - получить согласие на свидание за 5 сообщений.");

                String prompt = loadPrompt(query);
                chatGPT.setPrompt(prompt);
                return;
            }

            Message msg = sendTextMessage("Набирает текст...");
            String answer = chatGPT.sendMessage("Диалог с девушкой", message);
            updateTextMessage(msg, answer);
            return;
        }

        if (message.equals("/message")) {
            currentMode = DialogMode.MESSAGE;
            sendPhotoMessage("message");
            String text = loadMessage("message");
            sendTextButtonsMessage(text,
                    "Следующее сообщение", "message_next",
                    "Пригласить на свидание", "message_date");
            return;
        }

        if (currentMode == DialogMode.MESSAGE && !isMessageCommand()) {
            String query = getCallbackQueryButtonKey();
            if (query.startsWith("message_")) {
                String prompt = loadPrompt(query);
                String userChatHistory = String.join("\n\n", array);

                Message msg = sendTextMessage(AI_THINKING);
                String answer = chatGPT.sendMessage(prompt, userChatHistory);
                updateTextMessage(msg, answer);
                return;
            }

            array.add(message);
            return;
        }

        if (message.equals("/profile")) {
            currentMode = DialogMode.PROFILE;
            sendPhotoMessage("profile");

            me = new UserInfo();
            questionCount = 1;
            String text = loadMessage("profile");
            sendTextMessage(text);
            sendTextMessage("Сколько Вам лет?");
            return;
        }

        if (currentMode == DialogMode.PROFILE && !isMessageCommand()) {
            switch (questionCount) {
                case 1:
                    me.setAge(message);
                    questionCount = 2;
                    sendTextMessage("Кем Вы работаете/на кого учитесь?");
                    return;
                case 2:
                    me.setOccupation(message);
                    questionCount = 3;
                    sendTextMessage("У Вас есть хобби? Если да - какие?");
                    return;
                case 3:
                    me.setHobby(message);
                    questionCount = 4;
                    sendTextMessage("Что Вам не нравится в людях?");
                    return;
                case 4:
                    me.setAnnoys(message);
                    questionCount = 5;
                    sendTextMessage("Цель знакомства:");
                    return;
                case 5:
                    me.setGoals(message);

                    String aboutMyself = me.toString();
                    String prompt = loadPrompt("profile");
                    Message msg = sendTextMessage(AI_THINKING);
                    String answer = chatGPT.sendMessage(prompt, aboutMyself);
                    updateTextMessage(msg, answer);
                    return;
                default:
            }
        }

        if (message.equals("/opener")) {
            currentMode = DialogMode.OPENER;
            sendPhotoMessage("opener");
            String text = loadMessage("opener");
            sendTextMessage(text);

            she = new UserInfo();
            questionCount = 1;
            sendTextMessage("Имя девушки/парня:");
            return;
        }

        if (currentMode == DialogMode.OPENER && !isMessageCommand()) {
            switch (questionCount) {
                case 1:
                    she.setName(message);
                    questionCount = 2;
                    sendTextMessage("Сколько ей/ему лет?");
                    return;
                case 2:
                    she.setAge(message);
                    questionCount = 3;
                    sendTextMessage("Есть ли хобби и какие?");
                    return;
                case 3:
                    she.setHobby(message);
                    questionCount = 4;
                    sendTextMessage("Кем работает?");
                    return;
                case 4:
                    she.setOccupation(message);
                    questionCount = 5;
                    sendTextMessage("Цель знакомства:");
                    return;
                case 5:
                    she.setGoals(message);

                    String aboutFriend = she.toString();
                    String prompt = loadPrompt("opener");

                    Message msg = sendTextMessage(AI_THINKING);
                    String answer = chatGPT.sendMessage(prompt, aboutFriend);
                    updateTextMessage(msg, answer);
                    return;
                default:
            }
        }
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());
    }
}
