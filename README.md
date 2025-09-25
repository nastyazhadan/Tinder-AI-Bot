# Tinder-AI-Bot
Telegram Tinder bot @dating_or_AI_bot

### **Overview**

Tinder AI Bot is a Java-based Telegram bot integrated with OpenAI ChatGPT.
It helps users build profiles, craft messages, and maintain conversations in a personalized way.

### **Features:**

1. [x] Multi-session support (MultiSessionTelegramBot)
2. [x] ChatGPT integration for replies and suggestions (ChatGPTService)
3. [x] Multiple dialog modes (DialogMode): profile, opener, message, date, free chat
4. [x] User profile management (UserInfo)

### **Project Structure:**

* ChatGPTService.java — handles ChatGPT API and conversation history
* DialogMode.java — defines bot dialog modes
* MultiSessionTelegramBot.java — multi-user session handling
* TinderBoltApp.java — main entry point
* UserInfo.java — user profile model

### **Run:**

1. Set environment variables.
2. Build and start.

### **Dependencies:**

* TelegramBots Java Library
* ChatGPT Java SDK
* Java 17+
