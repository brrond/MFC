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
        <td>s/updateUser</td>
        <td>Params: UUID of User (required, userId) and fields to change data (name/email/password)</td>
        <td>Returns JSON Object with field UUID of updated user</td>   
        <td>Update user. Change password and/or email and/or name</td>
    </tr>
    <tr>
        <td>s/getAllAccounts</td>
        <td>Params: UUID of User (userId)</td>
        <td>
            Returns JSON Array, each element contains JSON Object of Account<br>
            [{"id": "de1007b4-cbac-4252-a161-64534ee66a23","title": "salary","balance": 0.00,"creation": "2022-10-25T10:10:45.923961Z"}]
        </td>       
        <td>Returns all accounts that were created by current user</td>
    </tr>
    <tr>
        <td>s/deleteUser</td>
        <td>Params: UUID of User (userId)</td>
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
        <td>Params: UUID of User (userId), title String (title)</td>
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
        <td>Params: UUID of User (userId), title String (title)</td>
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
        <td colspan="4" style="text-align: center">api/operationtypes/</td>
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
