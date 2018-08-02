package cn.inctech.app;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import cn.inctech.app.talents.service.TeacherService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
public class StartAppTests {

	/*@Test*/
	public void testMybatisGetStudentList() {
		ts.getStudentList(1001, 1, 1).stream().forEach(System.out::println);
		System.out.println(ts.getStudentList(1001, 1, 1).size());
	}
	
	/**
	 * mvc.perform(MockMvcRequestBuilders.get("/data/getMarkers")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("lat", "123.123").param("lon", "456.456")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
	 * @throws Exception
	 */
	@Test
    public void girlList() throws Exception {
        String test_url="/register.do";
		mvc.perform(MockMvcRequestBuilders.get("/getVcode.do"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
                /*.andExpect(MockMvcResultMatchers.content().string("abc"))*/;
		mvc.perform(MockMvcRequestBuilders.get(test_url)
				.param("userPhone", "13804111001").param("password", "1001").param("type", "1"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print());
        
    }

	MockMvc mvc;
    @Autowired WebApplicationContext context;

    @Before
    public void setupMockMvc() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    
	@Resource TeacherService ts;
}
