package localhost.servlet;

import com.alibaba.fastjson.JSON;
import localhost.mapper.GetAwardRecordMapper;
import localhost.pojo.ReturnData;
import localhost.util.SqlSessionFactoryUtil;
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

@WebServlet("/finishConsumeAward")
public class finishConsumeAward extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write("拒绝访问");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // POST请求解决中文乱码
        request.setCharacterEncoding("UTF-8");
        // 解决跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 设置响应的网络文件的类型和网页的编码
        response.setContentType("text/html;charset=utf-8");
        // 获取response的写入对象
        PrintWriter writer = response.getWriter();

        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();

//        接收前端数据
        int id = Integer.parseInt(request.getParameter("id"));
        // 执行sql
        GetAwardRecordMapper getAwardRecordMapper = sqlSession.getMapper(GetAwardRecordMapper.class);
        Integer changeAwardStatusResult = getAwardRecordMapper.changeAwardStatus(id, 2);
        if(changeAwardStatusResult>0){
            //提交事务
            sqlSession.commit();
            ReturnData returnData = new ReturnData();
            returnData.setMsg("完成兑现成功");
            returnData.setErr_code(0);
            returnData.setData(changeAwardStatusResult);
            writer.write(JSON.toJSONString(returnData));
        }else{
            //回滚事务
//            sqlSession.rollback();
            ReturnData returnData = new ReturnData();
            returnData.setMsg("完成兑现失败");
            returnData.setErr_code(1);
            returnData.setData(changeAwardStatusResult);
            writer.write(JSON.toJSONString(returnData));
        }

        //关闭mysql会话
        sqlSession.close();
    }
}
