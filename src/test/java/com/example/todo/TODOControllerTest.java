package com.example.todo;

import com.example.todo.controller.TODOController;
import com.example.todo.testHelper.TestHelper;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Main.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TODOControllerTest {
    private MockMvc mockMvc;
    @Autowired
    TODOController todoController;
    @Autowired
    TestHelper testHelper;
    @Autowired
    private WebApplicationContext wac;

    @Before
    public final void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void A_createTodoTest() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/todoEntity.json");

        mockMvc.perform(MockMvcRequestBuilders.post("/todo/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonObject.toString()))
                .andExpect(jsonPath("$.owner").value("Owner"))
                .andExpect(jsonPath("$.deadlineDate").value("2020-01-01"))
                .andExpect(jsonPath("$.description").value("description"))
                .andDo(print());
    }
}
