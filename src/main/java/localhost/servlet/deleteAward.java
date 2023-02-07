package localhost.servlet;

import com.alibaba.fastjson.JSON;
import localhost.mapper.AwardsMapper;
import localhost.mapper.GetAwardRecordMapper;
import localhost.mapper.GetMissionRecordMapper;
import localhost.mapper.MissionsMapper;
import localhost.pojo.ReturnData;
import localhost.util.SqlSessionFactoryUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.annotation.Resource;
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

@WebServlet("/deleteAward")
public class deleteAward extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        GetAwardRecordMapper getAwardRecordMapper = sqlSession.getMapper(GetAwardRecordMapper.class);
        //        POST请求解决中文乱码
        request.setCharacterEncoding("UTF-8");
        //        接收前端数据
        int award_id = Integer.parseInt(request.getParameter("award_id"));

        String string_date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        string_date = sdf.format(new Date());

        System.out.println("/deleteMission【" + string_date + "】收到award_id:" + award_id);
        //        调用Mapper接口
        System.out.println("执行1");
        Integer result1 = getAwardRecordMapper.deleteGetAwardRecordByAwardId(award_id);
        System.out.println("执行2");
        Integer result2 = awardsMapper.deleteAwardById(award_id);



        System.out.println("result1:" + result1);
        System.out.println("result2:" + result2);
        //提交事务
        sqlSession.commit();
        //关闭mysql会话
        sqlSession.close();

//        解决跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        if (result2 > 0) {
//            删除成功
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(0);
            returnData.setMsg("删除成功");
            returnData.setData(result2);
            writer.write(JSON.toJSONString(returnData));

        } else {
//            删除失败
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(1);
            returnData.setMsg("删除操作的影响条数为0");
            returnData.setData(null);
            writer.write(JSON.toJSONString(returnData));
        }
    }
}
