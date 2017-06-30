package neo4j.ir.nodes;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ali Asghar on 27/06/2017.
 */
public class User {

    @JsonProperty
    private int id;

    @JsonProperty
    private String userName;

    @JsonProperty
    private String password;

    @JsonProperty
    private String firstName;

    @JsonProperty
    private String lastName;

    @JsonProperty
    private int age;

    @JsonProperty
    private boolean male;


    public User() {

    }

    public User(String firstName, String lastName,String userName, String password, int id) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    @Override
    public boolean equals(Object u)
    {
        User user = (User)u;

        return this.userName.equals(user.getUserName());
    }
}
