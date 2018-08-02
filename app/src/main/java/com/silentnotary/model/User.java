package com.silentnotary.model;

/**
 * Created by albert on 10/3/17.
 */

public class User {

    public User() {
    }

    public static User createFake(){
        return new User(1.1f, 1001, 4, 3.05f, "facebookregurl", "testemail@gmail.com", "artur", "prm");
    }
    public User(float balance, long totalFileSizeBytes, int totalFileCount, float totalMonthlyFee, String facebookRegistrationUrl, String email, String firstName, String lastName) {
        Balance = balance;
        TotalFileSizeBytes = totalFileSizeBytes;
        TotalFileCount = totalFileCount;
        TotalMonthlyFee = totalMonthlyFee;
        FacebookRegistrationUrl = facebookRegistrationUrl;
        Email = email;
        FirstName = firstName;
        LastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (Float.compare(user.Balance, Balance) != 0) return false;
        if (TotalFileSizeBytes != user.TotalFileSizeBytes) return false;
        if (TotalFileCount != user.TotalFileCount) return false;
        if (Float.compare(user.TotalMonthlyFee, TotalMonthlyFee) != 0) return false;
        if (FacebookRegistrationUrl != null ? !FacebookRegistrationUrl.equals(user.FacebookRegistrationUrl) : user.FacebookRegistrationUrl != null)
            return false;
        if (Email != null ? !Email.equals(user.Email) : user.Email != null) return false;
        if (FirstName != null ? !FirstName.equals(user.FirstName) : user.FirstName != null)
            return false;
        return LastName != null ? LastName.equals(user.LastName) : user.LastName == null;

    }

    @Override
    public int hashCode() {
        int result = (Balance != +0.0f ? Float.floatToIntBits(Balance) : 0);
        result = 31 * result + (int) (TotalFileSizeBytes ^ (TotalFileSizeBytes >>> 32));
        result = 31 * result + TotalFileCount;
        result = 31 * result + (TotalMonthlyFee != +0.0f ? Float.floatToIntBits(TotalMonthlyFee) : 0);
        result = 31 * result + (FacebookRegistrationUrl != null ? FacebookRegistrationUrl.hashCode() : 0);
        result = 31 * result + (Email != null ? Email.hashCode() : 0);
        result = 31 * result + (FirstName != null ? FirstName.hashCode() : 0);
        result = 31 * result + (LastName != null ? LastName.hashCode() : 0);
        return result;
    }

    public float getBalance() {
        return Balance;
    }

    public void setBalance(float balance) {
        Balance = balance;
    }

    public long getTotalFileSizeBytes() {
        return TotalFileSizeBytes;
    }

    public void setTotalFileSizeBytes(long totalFileSizeBytes) {
        TotalFileSizeBytes = totalFileSizeBytes;
    }

    public int getTotalFileCount() {
        return TotalFileCount;
    }

    public void setTotalFileCount(int totalFileCount) {
        TotalFileCount = totalFileCount;
    }

    public float getTotalMonthlyFee() {
        return TotalMonthlyFee;
    }

    public void setTotalMonthlyFee(float totalMonthlyFee) {
        TotalMonthlyFee = totalMonthlyFee;
    }

    public String getFacebookRegistrationUrl() {
        return FacebookRegistrationUrl;
    }

    public void setFacebookRegistrationUrl(String facebookRegistrationUrl) {
        FacebookRegistrationUrl = facebookRegistrationUrl;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    float Balance;
    long TotalFileSizeBytes;
    int TotalFileCount;
    float TotalMonthlyFee;
    String FacebookRegistrationUrl;
    String Email;
    String FirstName;
    String LastName;
}
