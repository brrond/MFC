package ua.nix.onishchenko.mfc.rest.controller;

import org.apache.tomcat.util.json.JSONParser;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// TODO: Method of same type (creation, update, get, deletion) are almost identical. Replace them.

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = {ua.nix.onishchenko.mfc.rest.RESTApplication.class})
public class ControllersTest {

    @Autowired
    private MockMvc mockMvc;

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private static UUID userId;
    private static UUID accountId;
    private static UUID operationTypeId;
    private static UUID operationId;
    private static String accessToken;

    @Test
    @Order(1)
    public void createUser() throws Exception {
        var result = new JSONParser(
                this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/users/register")
                            .contentType(APPLICATION_JSON_UTF8)
                            .content("{\"name\":\"Super Name\", \"email\":\"Super@mail.com\", \"password\":\"qwerty\"}"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("id")))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .parseObject();
        assertDoesNotThrow(() -> userId = UUID.fromString(result.get("id").toString()));
    }

    @Test
    @Order(2)
    public void login() throws Exception {
        var result = new JSONParser(
                this.mockMvc.perform(
                                MockMvcRequestBuilders.post("/api/users/login")
                                        .param("email", "Super@mail.com")
                                        .param("password", "qwerty"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("access_token")))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .parseObject();
        assertDoesNotThrow(() -> accessToken = result.get("access_token").toString());
    }

    @Test
    @Order(3)
    public void refresh() throws Exception {
        var result = new JSONParser(
                this.mockMvc.perform(
                                MockMvcRequestBuilders.get("/api/users/refresh")
                                        .header(AUTHORIZATION, "Bearer " + accessToken))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("access_token")))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .parseObject();
        assertDoesNotThrow(() -> accessToken = result.get("access_token").toString());
    }

    @Test
    @Order(4)
    public void getAllAccounts() throws Exception {
        var result = new JSONParser(
                this.mockMvc.perform(
                                MockMvcRequestBuilders.get("/api/users/s/getAllAccounts")
                                        .header(AUTHORIZATION, "Bearer " + accessToken)
                        )
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .parseArray();
        assertTrue(result.isEmpty());
    }

    @Test
    @Order(5)
    public void updateUser() throws Exception {
        var result = new JSONParser(
                this.mockMvc.perform(
                                MockMvcRequestBuilders.put("/api/users/s/updateUser")
                                        .contentType(APPLICATION_JSON_UTF8)
                                        .content("{\"userId\":\""+ userId +"\",\"email\":\"Super@gmail.com\",\"name\":\"Changed Name\",\"password\":\"ytrewq\"}")
                                        .header(AUTHORIZATION, "Bearer " + accessToken)
                        )
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .parseObject();
        AtomicReference<UUID> secondUUID = new AtomicReference<>();
        assertDoesNotThrow(() -> secondUUID.set(UUID.fromString(result.get("id").toString())));
        assertEquals(userId, secondUUID.get());
    }

    @Test
    @Order(6)
    public void createAccount() throws Exception {
        var result = new JSONParser(
                this.mockMvc.perform(
                                MockMvcRequestBuilders.post("/api/accounts/s/createAccount")
                                        .header(AUTHORIZATION, "Bearer " + accessToken)
                                        .param("title", "my first account")
                        )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("id")))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .parseObject();
        assertDoesNotThrow(() -> accountId = UUID.fromString(result.get("id").toString()));
    }

    @Test
    @Order(7)
    public void updateAccount() throws Exception {
        var result = new JSONParser(
                this.mockMvc.perform(
                                MockMvcRequestBuilders.put("/api/accounts/s/updateAccount")
                                        .header(AUTHORIZATION, "Bearer " + accessToken)
                                        .param("accountId", accountId.toString())
                                        .param("title", "my account")
                        )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("id")))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .parseObject();
        AtomicReference<UUID> secondUUID = new AtomicReference<>();
        assertDoesNotThrow(() -> secondUUID.set(UUID.fromString(result.get("id").toString())));
        assertEquals(accountId, secondUUID.get());
    }

    @Test
    @Order(8)
    public void getGeneralInfo() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/accounts/s/getGeneralInfo")
                                .header(AUTHORIZATION, "Bearer " + accessToken)
                                .param("accountId", accountId.toString())
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"title\":\"my account\"")))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(9)
    public void createOperationType() throws Exception {
        var result = new JSONParser(
                this.mockMvc.perform(
                                MockMvcRequestBuilders.post("/api/operationtypes/s/createOperationType")
                                        .header(AUTHORIZATION, "Bearer " + accessToken)
                                        .param("title", "kfjlkwjflke")
                        )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("id")))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .parseObject();
        assertDoesNotThrow(() -> operationTypeId = UUID.fromString(result.get("id").toString()));
    }

    @Test
    @Order(10)
    public void updateOperationType() throws Exception {
        var result = new JSONParser(
                this.mockMvc.perform(
                                MockMvcRequestBuilders.put("/api/operationtypes/s/updateOperationType")
                                        .header(AUTHORIZATION, "Bearer " + accessToken)
                                        .param("operationTypeId", operationTypeId.toString())
                                        .param("title", "Salary")
                        )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("id")))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .parseObject();
        AtomicReference<UUID> secondUUID = new AtomicReference<>();
        assertDoesNotThrow(() -> secondUUID.set(UUID.fromString(result.get("id").toString())));
        assertEquals(operationTypeId, secondUUID.get());
    }

    @Test
    @Order(11)
    public void getGeneralInfo_OperationType() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/operationtypes/s/getGeneralInfo")
                                .header(AUTHORIZATION, "Bearer " + accessToken)
                                .param("operationTypeId", operationTypeId.toString())
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"title\":\"Salary\"")))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(12)
    public void createOperation() throws Exception {
        var result = new JSONParser(
                this.mockMvc.perform(
                                MockMvcRequestBuilders.post("/api/operations/s/createOperation")
                                        .header(AUTHORIZATION, "Bearer " + accessToken)
                                        .param("sum", "20.5")
                                        .param("operationTypeId", operationTypeId.toString())
                                        .param("accountId", accountId.toString())
                        )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("id")))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .parseObject();
        assertDoesNotThrow(() -> operationId = UUID.fromString(result.get("id").toString()));
    }

    @Test
    @Order(13)
    public void getAllOperations() throws Exception {
        var result = new JSONParser(
                this.mockMvc.perform(
                                MockMvcRequestBuilders.get("/api/accounts/s/getAllOperations")
                                        .header(AUTHORIZATION, "Bearer " + accessToken)
                                        .param("accountId", accountId.toString())
                        )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("id")))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .parseArray();
        assertEquals(1, result.size());
    }


    @Test
    @Order(700)
    public void deleteOperation() throws Exception {
        var result = new JSONParser(
                this.mockMvc.perform(
                                MockMvcRequestBuilders.delete("/api/operations/s/deleteOperation")
                                        .header(AUTHORIZATION, "Bearer " + accessToken)
                                        .param("operationId", operationId.toString())
                        )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("id")))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .parseObject();
        AtomicReference<UUID> secondUUID = new AtomicReference<>();
        assertDoesNotThrow(() -> secondUUID.set(UUID.fromString(result.get("id").toString())));
        assertEquals(operationId, secondUUID.get());
    }

    @Test
    @Order(800)
    public void deleteOperationType() throws Exception {
        var result = new JSONParser(
                this.mockMvc.perform(
                                MockMvcRequestBuilders.delete("/api/operationtypes/s/deleteOperationType")
                                        .header(AUTHORIZATION, "Bearer " + accessToken)
                                        .param("operationTypeId", operationTypeId.toString())
                        )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("id")))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .parseObject();
        AtomicReference<UUID> secondUUID = new AtomicReference<>();
        assertDoesNotThrow(() -> secondUUID.set(UUID.fromString(result.get("id").toString())));
        assertEquals(operationTypeId, secondUUID.get());
    }

    @Test
    @Order(900)
    public void deleteAccount() throws Exception {
        var result = new JSONParser(
                this.mockMvc.perform(
                                MockMvcRequestBuilders.delete("/api/accounts/s/deleteAccount")
                                        .header(AUTHORIZATION, "Bearer " + accessToken)
                                        .param("accountId", accountId.toString())
                        )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("id")))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .parseObject();
        AtomicReference<UUID> secondUUID = new AtomicReference<>();
        assertDoesNotThrow(() -> secondUUID.set(UUID.fromString(result.get("id").toString())));
        assertEquals(accountId, secondUUID.get());
    }

    @Test
    @Order(1000)
    public void delete() throws Exception {
        var result = new JSONParser(
                this.mockMvc.perform(
                                MockMvcRequestBuilders.delete("/api/users/s/deleteUser")
                                        .header(AUTHORIZATION, "Bearer " + accessToken)
                        )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("id")))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .parseObject();
        AtomicReference<UUID> secondUUID = new AtomicReference<>();
        assertDoesNotThrow(() -> secondUUID.set(UUID.fromString(result.get("id").toString())));
        assertEquals(userId, secondUUID.get());
    }

    @Test
    @Order(2000)
    public void createUser_REturnError() throws Exception {
        var result = new JSONParser(
                this.mockMvc.perform(
                                MockMvcRequestBuilders.post("/api/users/register")
                                        .contentType(APPLICATION_JSON_UTF8)
                                        .content("{\"name\":\"Super Name\", \"password\":\"qwerty\"}"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("must be specified")))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .parseObject();
    }

}
