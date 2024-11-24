package org.example;

public abstract class User {
    protected String username;
    protected String password;
    protected String name;
    protected String securityQuestion;
    protected String Answer;
    protected int id;

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public User(int id, String username, String password, String name, String securityQuestion, String Answer) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.securityQuestion = securityQuestion;
        this.Answer = Answer;
        this.id = id;
    }

    public User(String username, String password, String name, String securityQuestion, String Answer) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.securityQuestion = securityQuestion;
        this.Answer = Answer;
    }

    public User(int id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract String getRole();
}
