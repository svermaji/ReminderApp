package com.svws.user;

/**
 * Created by IntelliJ IDEA.
 * User: sverma
 * Date: Jan 23, 2007
 * Time: 3:53:43 PM
 */
/**
 * This class is a bean for table "sv_users"
 */
public class User
{
    private int userId;
    private String userName, password, first_name, last_name, gender;
    private String address, phone, birth_date, city, zip;
    private String state, country, email;

    public User (  )
    {
        userId=0;
        userName =  password =  first_name =  last_name =  gender = "";
        address =  phone =  birth_date =  city =  zip = "";
        state =  country =  email = "";
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getFirst_name()
    {
        return first_name;
    }

    public void setFirst_name(String first_name)
    {
        this.first_name = first_name;
    }

    public String getLast_name()
    {
        return last_name;
    }

    public void setLast_name(String last_name)
    {
        this.last_name = last_name;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getBirth_date()
    {
        return birth_date;
    }

    public void setBirth_date(String birth_date)
    {
        this.birth_date = birth_date;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getZip()
    {
        return zip;
    }

    public void setZip(String zip)
    {
        this.zip = zip;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
