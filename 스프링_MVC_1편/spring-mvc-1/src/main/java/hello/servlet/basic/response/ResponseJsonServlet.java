package hello.servlet.basic.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servlet.basic.HelloData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "responseJsonServlet", urlPatterns = "/response-json")
public class ResponseJsonServlet extends HttpServlet {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        // application/json 은 스펙상 utf-8 형식을 사용하도록 정의되어 있다. 그래서 스펙에서 charset=utf-8 과
        // 같은 추가 파라미터를 지원하지 않는다. 따라서 application/json이라고만 사용해야지
        // application/json; charset=utf-8 이라고 전달하는 것은 의미 없는 파라미터를 추가한 것이 된다.
        // response.getWriter()를 사용하면 추가 파라미터를 자동으로 추가한다. 이때는  response.getOutputStream() 으로 출력하면 문제 없다.

        HelloData helloData = new HelloData();
        helloData.setUsername("cho");
        helloData.setAge(27);

        // {"username": "cho", "age": 27}
        String result = objectMapper.writeValueAsString(helloData);
        response.getWriter().write(result);
    }
}
