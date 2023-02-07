package localhost.servlet;

import com.alibaba.fastjson.JSON;
import localhost.mapper.GetAwardRecordMapper;
import localhost.mapper.GetMissionRecordMapper;
import localhost.pojo.GetAwardRecord;
import localhost.pojo.GetMissionRecord;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/selectGetMissionRecordByUserId")
public class selectGetMissionRecordByUserId extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write("拒绝访问");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //        加载mybatis核心配置文件，获取SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();
//        解决跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/html;charset=utf-8");
//        获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        GetMissionRecordMapper getMissionRecordMapper =sqlSession.getMapper(GetMissionRecordMapper.class);

        //        POST请求解决中文乱码
        request.setCharacterEncoding("UTF-8");
        //        接收前端数据
        int user_id = Integer.parseInt(request.getParameter("user_id")) ;

        String string_date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        string_date = sdf.format(new Date());

        System.out.println("/selectGetMissionRecordByUserId【" + string_date + "】收到user_id:" + user_id);
        //        调用Mapper接口
        List<GetMissionRecord> getAwardRecords = getMissionRecordMapper.selectGetMissionRecordByUserId(user_id);
        System.out.println();

        //        关闭mysql会话
        sqlSession.close();


        PrintWriter writer = response.getWriter();
        if (getAwardRecords != null) {
//            查询有结果
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(0);
            returnData.setMsg("查询有结果");
            returnData.setData(getAwardRecords);
            writer.write(JSON.toJSONString(returnData));

        } else {
//            查询无结果
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(1);
            returnData.setMsg("查询无结果");
            returnData.setData(null);
            writer.write(JSON.toJSONString(returnData));
        }
    }
}
