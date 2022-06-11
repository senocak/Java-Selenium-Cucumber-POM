@Auth
Feature: Login tests
  This is a feature of the authentication module.

  @login-fail1
  Scenario: I should be able to see the fail message when I login with wrong credentials
    Given open login page
    Given Enter email and password
    | email | password  |
    | lorem@ipsum.com | lorem |
    When Click login button
    Then I should see "Bad credentials!" message

  @login-fail2
  Scenario: I should be able to see the fail message after entering wrong credentials twice
    Given open login page
    Given Enter email and password
      | email | password  |
      | lorem@ipsum.com | lorem |
    When Click login button
    Given Enter email and password
      | email | password  |
      | lorem@ipsum.com | lorem |
    When Click login button
    Then I should see "Bad credentials!" message

  @forgot-password-fail1
  Scenario: I should be able to see the fail message after entering wrong email
    Given open forgot password page
    Given Enter "lorem@ipsum.com" for reset password
    When Click send reset button
    Then I should see "User not found!" message

  @forgot-password-success
  Scenario: Happy path for forgot password
    Given open forgot password page
    Given Enter "gmsqtggrwxfdrsnzxd@bvhrs.com" for reset password
    When Click send reset button
    Then I should see "Password reset sent.Please check inbox." message

  @login-success
  Scenario: Happy path for login
    Given open login page
    Given Enter email and password
      | email | password  |
      | @username1 | @password1 |
    When Click login button
    Then I should see "Login success.Please wait for redirect." message