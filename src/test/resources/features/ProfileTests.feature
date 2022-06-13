@Profile
Feature: Profile Feature
  This is a feature of the profile feature

  Background:
    Given open "login" page
    Given Enter email and password
      | email           | password          |
      | @user.email[1]  | @user.password[1] |
    When Click login button
    Then I should see "Login success.Please wait for redirect." message

  @Profile-success
  @success
  Scenario: I should be able to update my profile
    Given open "profile" page
    When I update my profile
      | name          | surname           | email           | company           | location          |
      | @user.name[1] | @user.surname[1]  | @user.email[1]  | @user.company[1]  | @user.location[1] |
    Then I should see "Your profile updated." message
