package cn.inctech.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	/*@Test*/
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
	
	@Test
	public void step01__register_login() throws Exception{
		String url_01="/register.do";
		String url_02="/login.do";
		build_users().stream().forEach(e->{
			try {
				mvc.perform(MockMvcRequestBuilders.get(url_01).param("userPhone", e.get("userPhone"))
						.param("password", e.get("password"))
						.param("type", e.get("type")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
				mvc.perform(MockMvcRequestBuilders.get(url_02).param("userPhone", e.get("userPhone"))
						.param("password", e.get("password"))
						.param("type", e.get("type")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		});
	}
	
	public List<Map<String,String>> build_users(){
		List<Map<String,String>> users=new ArrayList<>();
		Map<String,String> u=null;
		String userPhone_prefix="1380411000";
		String type="1";
		for(int i=1;i<9;i++) {
			u=new HashMap<>();
			u.put("userPhone", userPhone_prefix+i);
			u.put("password", userPhone_prefix+i);
			u.put("type", type);
			users.add(u);
		}
		
		userPhone_prefix="user0";
		String userPhone_suffix="@inctecn.cn";
		type="2";
		for(int i=1;i<9;i++) {
			u=new HashMap<>();
			u.put("userPhone", userPhone_prefix+i+userPhone_suffix);
			u.put("password", userPhone_prefix+i);
			u.put("type", type);
			users.add(u);
		}
		return users;
	}

	MockMvc mvc;
    @Autowired WebApplicationContext context;

    @Before
    public void setupMockMvc() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    
	@Resource TeacherService ts;
}
