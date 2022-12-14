# Introduction

Personal finance is the financial management which an individual or a family unit performs to budget, save, and spend monetary resources over time, taking into account various financial risks and future life events.

Personal finance is a vital part of not only managing your day-to-day financial needs but also planning your financial future. The sooner you get a grip on personal finance, the better your long-term financial prospects will be for things like investing or planning for retirement.

## How it was before
<img src="doc/process_diagram.png" alt="">
<br>
<img src="doc/process_diagram_2.png" alt="">
<br>
<img src="doc/process_diagram_3.png" alt="">

## New approach

<img src="doc/process_diagram_4.png" alt="">
<br>
<img src="doc/process_diagram_5.png" alt="">

## Architecture

The main idea is to create standalone REST API, which will process user's requests. Requests could be from any other tool. As instance Frontend application will be created.

### Database

<img src="doc/db.png" alt="">

# REST

## List of used technologies

<ul>
    <li>Java</li>    
    <li>Spring Boot</li>
    <li>Spring Security</li>
    <li>PostgreSQL</li>
    <li>Flyway</li>
    <li>Tests (SpringTests + JUnit)</li>
    <li>Tests (SpringMock)</li>
    <li>Maven</li>
</ul>

## Class Diagram

<img src="doc/rest_class_diagram.png" alt="">

## Commands
<table>
    <tr>
        <td>Link</td>
        <td>Input</td>
        <td>Returns on success</td>
        <td>Description</td>
    </tr>
    <tr></tr>
    <tr>
        <td colspan="4" style="text-align: center">api/users/</td>
    </tr>
    <tr>
        <td>register</td>
        <td>RequestBody: with fields : name, password, email</td>
        <td>
            Returns JSON Object with field UUID of new created user<br>
            {"id": "de1007b4-cbac-4252-a161-64534ee66a23"}
        </td>
        <td>Register new user in DB</td>
    </tr>
    <tr>
        <td>login</td>
        <td>Params: String email, String password</td>
        <td>
            Returns JSON Ojbect with two fields: access_token and refresh_token:<br>
            {"access_token": "","refresh_token": ""}
        </td>
        <td>Allows User to login in</td>
    </tr>
    <tr>
        <td>refresh</td>
        <td>As Authorization Header must be placed refresh_token in format "Bearer REFRESH_TOKEN"</td>
        <td>Returns JSON Object with two fields: (see login)</td>
        <td>Allows User to get new access token by previously generated refresh_token</td>
    </tr>
    <tr>
        <td>isActive</td>
        <td></td>
        <td>Returns JSON Object with one field: error. If error is "true" then toke is active else the actual error provided</td>
        <td>Allows user to make sure that token is active. Works with access tokens</td>
    </tr>
    <tr>
        <td>s/getGeneralInfo</td>
        <td></td>
        <td>
            Returns JSON Object with information about user
            {"name": "Firstname Lastname","email": "BoBrik35@gmail.com","creation": "2022-10-25T04:33:09.759390Z"}
        </td>   
        <td>Returns general information about user</td>
    </tr>
    <tr>
        <td>s/updateUser</td>
        <td>Params: RequestBody with fields to change data (name/email/password)</td>
        <td>Returns JSON Object with field UUID of updated user</td>   
        <td>Update user. Change password and/or email and/or name</td>
    </tr>
    <tr>
        <td>s/getAllAccounts</td>
        <td></td>
        <td>
            Returns JSON Array, each element contains JSON Object of Account<br>
            [{"id": "de1007b4-cbac-4252-a161-64534ee66a23","title": "salary","balance": 0.00,"creation": "2022-10-25T10:10:45.923961Z"}]
        </td>       
        <td>Returns all accounts that were created by current user</td>
    </tr>
    <tr>
        <td>s/getAllOperationTypes</td>
        <td></td>
        <td>
            Returns JSON Array, each element contains JSON Object of OperationType<br>
            [{"id": "de1007b4-cbac-4252-a161-64534ee66a23","title": "some_title"}]
        </td>       
        <td>Returns all OperationTypes that were created by current user</td>
    </tr>
    <tr>
        <td>s/deleteUser</td>
        <td></td>
        <td>Returns JSON Object with field UUID of deleted account</td>
        <td>Delete user from DB</td>
    </tr>
    <tr>
        <td colspan="4" style="text-align: center">api/accounts/</td>
    </tr>
    <tr>
        <td>s/getGeneralInfo</td>
        <td>Params: UUID of Account (accountId)</td>
        <td>
            Return JSON Object that represents Account<br>
            {"id": "de1007b4-cbac-4252-a161-64534ee66a23","title": "salary","balance": 0.00,"creation": "2022-10-25T10:10:45.923961Z"}
        </td>
        <td>Returns general purpose information about financial account of user</td>
    </tr>
    <tr>
        <td>s/getAllOperations</td>
        <td>Params: UUID of Account (accountId) </td>
        <td>
            Return JSON Array of financial Operations<br>
            [{"id": "baf1e2b3-034e-4ef8-b74e-752575b93c9d","typeStr": "From NIX","sum": 100.00,"creation": null}]
        </td>
        <td>Returns all operations of this account</td>
    </tr>
    <tr>
        <td>s/createAccount</td>
        <td>Params: String title of the account</td>
        <td>
            Returns UUID of new created account<br>
            {"id": "de1007b4-cbac-4252-a161-64534ee66a23"}
        </td>
        <td>Create new Account for User</td>
    </tr>
    <tr>
        <td>s/updateAccount</td>
        <td>Params: UUID of Account (accountId), title String (title)</td>
        <td>Returns UUID of changed account</td>
        <td>Updates Account title in BD</td>
    </tr>
    <tr>
        <td>s/deleteAccount</td>
        <td>Params: UUID of Account (accountId)</td>
        <td>Returns UUID of deleted account</td>
        <td>Deletes Account from BD</td>
    </tr>
    <tr>
        <td colspan="4" style="text-align: center">api/operationtypes/</td>
    </tr>
    <tr>
        <td>s/getGeneralInfo</td>
        <td>Params: UUID of OperationType (operationTypeId)</td>
        <td>
            Returns JSON Object that represents operationType<br>
            {"operationType": {"id": "8766f6d2-7338-4ff2-931f-35ff9934d785","title": "From NIX"}}
        </td>
        <td>Returns general purpose information about OperationType</td>
    </tr>
    <tr>
        <td>s/getAllOperations</td>
        <td>Params: UUID of OperationType (operationTypeId)</td>
        <td>
            Returns JSON Array of JSON Objects which represents Operation<br>
            [{"id": "baf1e2b3-034e-4ef8-b74e-752575b93c9d","operationType": "From NIX","sum": 100.00,"creation": null}]
        </td>
        <td>Returns all operation of current type</td>
    </tr>
    <tr>
        <td>s/createOperationType</td>
        <td>Params: String title</td>
        <td>Returns UUID id of new created OperationType</td>
        <td>Create OperationType</td>
    </tr>
    <tr>
        <td>s/updateOperationType</td>
        <td>Params: UUID of OperationType (operationTypeId), title String (title)</td>
        <td>Returns UUID of updated OperationType</td>
        <td>Update title of OperationType</td>
    </tr>
    <tr>
        <td>s/deleteOperationType</td>
        <td>Params: UUID of OperationType (operationTypeId)</td>
        <td>Returns UUID of deleted OperationType</td>
        <td>Deletes OperationType from BD</td>
    </tr>
    <tr>
        <td colspan="4" style="text-align: center">api/operation/</td>
    </tr>
    <tr>
        <td>s/createOperation</td>
        <td>Params: UUID of Account (accountId), sum BigDecimal (sum), UUID of OperationType (operationTypeId), LocalDateTimeString creation timepoint (localDateTime)</td>
        <td>Returns UUID of created Operation</td>
        <td>Create new Operation</td>
    </tr>
    <tr>
        <td>s/deleteOperation</td>
        <td>Params: UUID of Operation</td>
        <td>Returns JSON Array with UUID of Operation that was deleted</td>
        <td>Delete Operation from DB</td>
    </tr>
</table>

## Security

All path with ```/s/``` in them are secured path. All queries to ```/s/``` must contain header ```Authorization``` with value ```Bearer {ACCESS_TOKEN}```. 
Only authorized user is allowed to access these methods. They contain and manipulate user data. Moreover, special filter checks if current user has access to specific data.
As main security method for REST API JSON-Web-Token (JWT) was chosen.

## Tests

As the result of testing 93% of methods and 69% of lines were covered:

<img src='doc/tests.png' alt="list of tests">
<br>
<img src='doc/tests_coverage.png' alt="coverage 93% methods 69% lines">
