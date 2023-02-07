package localhost.servlet;

import com.alibaba.fastjson.JSON;
import localhost.mapper.UsersMapper;
import localhost.pojo.ReturnData;
import localhost.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/login")
public class login extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //        加载mybatis核心配置文件，获取SqlSessionFactory对象
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

//        获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
//        执行sql
//        List<User> users = sqlSession.selectList("sql.selectAllUser");
        UsersMapper userMapper = sqlSession.getMapper(UsersMapper.class);

        //        POST请求解决中文乱码
        request.setCharacterEncoding("UTF-8");
        //        接收前端数据
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String string_date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        string_date = sdf.format(new Date());

        System.out.println("/login【" + string_date + "】收到username:" + username + ",password:" + password);
        //        调用Mapper接口
        User user = userMapper.login(username, password);
        System.out.println(user);

        //        关闭mysql会话
        sqlSession.close();

//        解决跨域
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");



        PrintWriter writer = response.getWriter();
        if (user != null) {
//            登录成功
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(0);
            returnData.setMsg("登录成功");
            returnData.setData(user);
            writer.write(JSON.toJSONString(returnData));

        } else {
//            登录失败
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(1);
            returnData.setMsg("登录失败");
            returnData.setData(null);
            writer.write(JSON.toJSONString(returnData));
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write("拒绝访问");
    }
}