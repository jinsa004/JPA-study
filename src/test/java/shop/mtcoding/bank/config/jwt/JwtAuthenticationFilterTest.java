package shop.mtcoding.bank.config.jwt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.bank.config.dummy.DummyEntity;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.UserReqDto.LoginReqDto;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class JwtAuthenticationFilterTest extends DummyEntity {
    private static final String APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    // 테스트가 실행되기전에 먼저 실행되는 메서드
    @BeforeEach
    public void setUp() {
        User user = newUser("ssar");
        // User.builder()
        // .username("ssar")
        // .password("1234")
        // .email("ssar@nate.com")
        // .role(UserEnum.CUSTOMER)
        // .build(); 상속받은 DummyEntity에서 설정해주면 위의 코드와 주석코드가 같은 기능을 함.
        userRepository.save(user);
    }

    // public void tearDown() {} 테스트가 끝나기전에 실행되는 메서드

    @Test
    public void login_test() throws Exception {
        // given
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setUsername("ssar");
        loginReqDto.setPassword("1234");
        String requestBody = om.writeValueAsString(loginReqDto);
        System.out.println("테스트: " + requestBody);
        // when
        ResultActions resultActions = mvc
                .perform(post("/login").content(requestBody)
                        .contentType(APPLICATION_JSON_UTF8));
        String token = resultActions.andReturn().getResponse().getHeader("Authorization");
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println(token);
        System.out.println(responseBody);
        // then
        resultActions.andExpect(status().isOk());
        assertNotNull(token);
        assertTrue(token.startsWith("Bearer"));
        resultActions.andExpect(jsonPath("$.data.username").value("ssar"));
    }
}
