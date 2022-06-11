@Auth
Feature: Login tests
  This is a feature of the authentication module.

  @login-fail
  @fail
  Scenario: I should be able to see the fail message when I login with wrong credentials
    Given open login page
    Given Enter email and password
    | email           | password  |
    | lorem@ipsum.com | lorem     |
    When Click login button
    Then I should see "Bad credentials!" message

  @login-fail
  @fail
  Scenario: I should be able to see the fail message after entering wrong credentials twice
    Given open login page
    Given Enter email and password
      | email           | password  |
      | lorem@ipsum.com | lorem     |
    When Click login button
    Given Enter email and password
      | email           | password  |
      | lorem@ipsum.com | lorem     |
    When Click login button
    Then I should see "Bad credentials!" message

  @login-success
  @success
  Scenario: Happy path for login
    Given open login page
    Given Enter email and password
      | email           | password          |
      | @user.email[1]  | @user.password[1] |
    When Click login button
    Then I should see "Login success.Please wait for redirect." message

  @forgot-password-fail
  @fail
  Scenario: I should be able to see the fail message after entering wrong email
    Given open forgot password page
    Given Enter "lorem@ipsum.com" for reset password
    When Click send reset button
    Then I should see "User not found!" message

  @forgot-password-fail
  @fail
  Scenario: I should be able to see the alert message after entering invalid email type
    Given open forgot password page
    Given Enter "lorem" for reset password
    When Click send reset button
    Then I should see "Invalid email" message

  @forgot-password-success
  @success
  Scenario: Happy path for forgot password
    Given open forgot password page
    Given Enter "@user.email[1]" for reset password
    When Click send reset button
    Then I should see "Password reset sent.Please check inbox." message