@Login
Feature: Login tests
  As Customer, I can login to fleetvaluationsystem.com

  @fail1
  Scenario: I should be able to see the fail message
    Given open login page
    Given Enter email and password
    | email | password  |
    | lorem@ipsum.com | lorem |
    When Click login button
    Then I should see "Bad credentials!" message

  @fail2
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

  @success
  Scenario: Happy path
    Given open login page
    Given Enter email and password
      | email | password  |
      | slogrzcddjvcfsykax@bvhrk.com | demo |
    When Click login button
    Then I should see "Login success.Please wait for redirect." message