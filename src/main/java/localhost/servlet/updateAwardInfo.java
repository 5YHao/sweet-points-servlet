package localhost.servlet;

import com.alibaba.fastjson.JSON;
import localhost.mapper.AwardsMapper;
import localhost.mapper.UsersMapper;
import localhost.pojo.Award;
import localhost.pojo.ReturnData;
import localhost.pojo.User;
import localhost.util.SqlSessionFactoryUtil;
import org.apache.ibatis.annotations.Param;
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

@WebServlet("/updateAwardInfo")
public class updateAwardInfo extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write("拒绝访问");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //        加载mybatis核心配置文件，获取SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();

//        获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
//        执行sql
//        List<User> users = sqlSession.selectList("sql.selectAllUser");
        AwardsMapper awardsMapper = sqlSession.getMapper(AwardsMapper.class);

        //        POST请求解决中文乱码
        request.setCharacterEncoding("UTF-8");
        //        接收前端数据
        Award award = new Award();
        award.setId(Integer.parseInt(request.getParameter("id")));
        award.setName(request.getParameter("name"));
        award.setDescription(request.getParameter("description"));
        award.setPrice(Integer.parseInt(request.getParameter("price")));

        String string_date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        string_date = sdf.format(new Date());

        System.out.println("/updateAwardInfo" + "【" + string_date + "】收到award:" + award.toString());
        //        调用Mapper接口
        Integer result = awardsMapper.updateAwardInfo(award);
        System.out.println(award.toString());


        sqlSession.commit();
        //        关闭mysql会话
        sqlSession.close();

//        解决跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        if (result > 0) {
//            查询有结果
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(0);
            returnData.setMsg("修改成功");
            returnData.setData(award);
            writer.write(JSON.toJSONString(returnData));

        } else {
//            查询无结果
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(1);
            returnData.setMsg("修改操作无影响记录");
            returnData.setData(null);
            writer.write(JSON.toJSONString(returnData));
        }
    }
}
