package localhost.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import localhost.mapper.MissionsMapper;
import localhost.mapper.UsersMapper;
import localhost.pojo.Mission;
import localhost.pojo.ReturnData;
import localhost.pojo.User;
import okhttp3.*;
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
import java.util.concurrent.TimeUnit;

@WebServlet("/wxLogin")
public class wxLogin extends HttpServlet {
    private final OkHttpClient okHttpClient =
            new OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES).build();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write("拒绝访问");
    }

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
        UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);

        //        POST请求解决中文乱码
        request.setCharacterEncoding("UTF-8");
        //        接收前端数据
        String appid = "wxb3145b98768a6cd8";
        String secret = "799ee0323f2385a9de0d6910df10f64f";
        String js_code = request.getParameter("js_code");
        //        解决跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String wxErrmsg="";
        if (js_code!=null){
            String code2SessionURL =
                    "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secret + "&js_code=" + js_code + "&grant_type=authorization_code";
            Request okrequest = new Request.Builder().url(code2SessionURL).get().build();
            //4.创建一个call对象,参数就是Request请求对象
            Call call = okHttpClient.newCall(okrequest);
            String openid = "";
            String respData = "";
            try {
                Response okresponse = call.execute();
                if (okresponse.isSuccessful()) {
                    respData = okresponse.body().string();
                    System.out.println(respData);
                    JSONObject jsonObject = JSONObject.parseObject(respData);
                    if (jsonObject.getString("openid")!=null){
                        openid = jsonObject.getString("openid");
                        System.out.println(openid);
                        User user = usersMapper.selectUserByOpenid(openid);
                        if (user != null) {
                            //用户已记录=>登录
                            ReturnData returnData = new ReturnData();
                            returnData.setErr_code(0);
                            returnData.setMsg("微信用户已存在");
                            returnData.setData(user);
                            writer.write(JSON.toJSONString(returnData));
                        } else {
                            //用户未记录=>记录=>登录
//                            确保微信的接口有返回openid
                            if (openid!=null){
                                User newUser = new User();
                                newUser.setOpenid(openid);
                                usersMapper.addUser(newUser);
                                ReturnData returnData = new ReturnData();
                                returnData.setErr_code(1);
                                returnData.setMsg("未记录用户，创建用户完成");
                                returnData.setData(newUser);
                                writer.write(JSON.toJSONString(returnData));
                            }else{
                                ReturnData returnData = new ReturnData();
                                returnData.setErr_code(2);
                                returnData.setMsg("openid为null");
                                returnData.setData(null);
                                writer.write(JSON.toJSONString(returnData));
                            }
                        }
                    }else{
                        wxErrmsg=jsonObject.getString("errmsg");
                        System.out.println(wxErrmsg);
                        ReturnData returnData = new ReturnData();
                        returnData.setErr_code(3);
                        returnData.setMsg("微信接口返回errcode非0");
                        returnData.setData(wxErrmsg);
                        writer.write(JSON.toJSONString(returnData));
                    }

                } else {
                    System.out.println("服务端错误");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            String string_date = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            string_date = sdf.format(new Date());

            System.out.println("/wxLogin" + "【" + string_date + "】收到js_code:" + js_code);
            //        调用Mapper接口

            sqlSession.commit();
            //        关闭mysql会话
            sqlSession.close();
        }else{
            //接收到js_code为null
            ReturnData returnData = new ReturnData();
            returnData.setErr_code(4);
            returnData.setMsg("js_code为null");
            returnData.setData(null);
            writer.write(JSON.toJSONString(returnData));
        }
    }
}
