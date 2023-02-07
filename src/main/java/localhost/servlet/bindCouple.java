package localhost.servlet;

import com.alibaba.fastjson.JSON;
import localhost.mapper.CouplesMapper;
import localhost.mapper.UsersMapper;
import localhost.pojo.Couple;
import localhost.pojo.ReturnData;
import localhost.pojo.User;
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

@WebServlet("/bindCouple")
public class bindCouple extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();

//        获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
//        执行sql
//        List<User> users = sqlSession.selectList("sql.selectAllUser");
        CouplesMapper couplesMapper = sqlSession.getMapper(CouplesMapper.class);
        UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
        //        POST请求解决中文乱码
        request.setCharacterEncoding("UTF-8");
        //        接收前端数据
        Integer boy_id = Integer.parseInt(request.getParameter("boy_id"));
        Integer girl_id = Integer.parseInt(request.getParameter("girl_id"));

        String string_date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        string_date = sdf.format(new Date());

        System.out.println("/bindCouple【" + string_date + "】收到boy_id:" + boy_id + ",girl_id:" + girl_id);

        //        解决跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();

        User boy = usersMapper.selectUserById(boy_id);
        User girl = usersMapper.selectUserById(girl_id);

        if (boy == null) {
            //            绑定失败
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(5);
            returnData.setMsg("男方id不存在");
            returnData.setData(null);
            writer.write(JSON.toJSONString(returnData));
        } else if (girl == null) {
            //            绑定失败
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(6);
            returnData.setMsg("女方id不存在");
            returnData.setData(null);
            writer.write(JSON.toJSONString(returnData));
        } else if (boy != null && girl != null) {
            //男方id和女方id合法后
            
            Couple coupleInfoFromBoy = couplesMapper.selectCoupleInfoByUserId(boy_id);
            Couple coupleInfoFromGirl = couplesMapper.selectCoupleInfoByUserId(girl_id);
            
            if (coupleInfoFromBoy != null && coupleInfoFromGirl != null) {
                //            绑定失败
                ReturnData returnData = new ReturnData();
                returnData.setErr_code(2);
                returnData.setMsg("绑定失败,双方都已有对象");
                returnData.setData(null);
                writer.write(JSON.toJSONString(returnData));
            } else if (coupleInfoFromBoy == null && coupleInfoFromGirl != null) {
                //            绑定失败
                ReturnData returnData = new ReturnData();
                returnData.setErr_code(3);
                returnData.setMsg("绑定失败,女方已有对象");
                returnData.setData(null);
                writer.write(JSON.toJSONString(returnData));
            } else if (coupleInfoFromBoy != null && coupleInfoFromGirl == null) {
                //            绑定失败
                ReturnData returnData = new ReturnData();
                returnData.setErr_code(4);
                returnData.setMsg("绑定失败,男方已有对象");
                returnData.setData(null);
                writer.write(JSON.toJSONString(returnData));
            } else {
                Couple couple = new Couple();
                couple.setBoy_id(boy_id);
                couple.setGirl_id(girl_id);
                Integer couple_id = couplesMapper.bindCouple(couple);
                System.out.println(couple_id);
                //记得提交事务
                sqlSession.commit();
                if (couple_id > 0) {
//            绑定成功
                    ReturnData returnData = new ReturnData();
                    returnData.setErr_code(0);
                    returnData.setMsg("绑定成功");
                    returnData.setData(couple.getId());
                    writer.write(JSON.toJSONString(returnData));

                } else {
//            绑定失败
                    ReturnData returnData = new ReturnData();
                    returnData.setErr_code(1);
                    returnData.setMsg("绑定失败【couple_id <= 0】");
                    returnData.setData(null);
                    writer.write(JSON.toJSONString(returnData));
                }
            }
        }
        //        关闭mysql会话
        sqlSession.close();


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write("拒绝访问");
    }
}
