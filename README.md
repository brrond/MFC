# Introduction

## REST

### Commands
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
            {"access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb0JyaWszNUBnbWFpbC5jb20iLCJpc3MiOiIvYXBpL3VzZXJzL2xvZ2luIiwiZXhwIjoxNjY2ODc4ODY5fQ.CCnK2XRMannGGnspXz46MuoXuN06k94hX262FyjfjI4","refresh_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb0JyaWszNUBnbWFpbC5jb20iLCJpc3MiOiIvYXBpL3VzZXJzL2xvZ2luIiwiZXhwIjoxNjY2ODgwMzY5fQ.VhTml4ItCFZS8aNXJNSGLdshRfHnYqeh9MpcEs39OGg"}
        </td>
        <td>Allows User to login in</td>
    </tr>
    <tr>
        <td>refresh</td>
        <td>As a Authorization Header must be paced refresh_token in format "Bearer REFRESH_TOKEN"</td>
        <td>Returns JSON Object with two fields: (see login)
        </td>
        <td>Allows User to get new access token by previously generated refresh_token</td>
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

### Security

All path with ```/s/``` in them are secured path. Only authorized user is allowed to access these methods. They contain and manipulate user data. Moreover, special filter checks if current user has access to specific data.
As main security method for REST API JSON-Web-Token (JWT) was chosen.
