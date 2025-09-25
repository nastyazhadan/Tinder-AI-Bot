package com.javarush.telegram;

public class UserInfo {
    private String name; //Имя
    private String sex; //Пол
    private String age; //Возраст
    private String city; //Город
    private String occupation; //Профессия
    private String hobby; //Хобби
    private String handsome; //Красота, привлекательность
    private String wealth; //Доход, богатство
    private String annoys; //Меня раздражает в людях
    private String goals; //Цели знакомства

    private String fieldToString(String str, String description) {
        if (str != null && !str.isEmpty())
            return description + ": " + str + "\n";
        else
            return "";
    }

    @Override
    public String toString() {
        String result = "";

        result += fieldToString(name, "Имя");
        result += fieldToString(sex, "Пол");
        result += fieldToString(age, "Возраст");
        result += fieldToString(city, "Город");
        result += fieldToString(occupation, "Профессия");
        result += fieldToString(hobby, "Хобби");
        result += fieldToString(handsome, "Красота, привлекательность в баллах (максимум 10 баллов)");
        result += fieldToString(wealth, "Доход, богатство");
        result += fieldToString(annoys, "В людях раздражает");
        result += fieldToString(goals, "Цели знакомства");

        return result;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    public void setHobby(String hobby) {
        this.hobby = hobby;
    }
    public void setHandsome(String handsome) {
        this.handsome = handsome;
    }
    public void setWealth(String wealth) {
        this.wealth = wealth;
    }
    public void setAnnoys(String annoys) {
        this.annoys = annoys;
    }
    public void setGoals(String goals) {
        this.goals = goals;
    }
}
