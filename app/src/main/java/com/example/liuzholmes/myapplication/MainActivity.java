package com.example.liuzholmes.myapplication;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.*;
import java.sql.*;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    private static final String TAG = "ASYNC_TASK";

    private class LogInTask extends AsyncTask <String, Integer, Boolean>
    {
        private String id ;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            Log.d(TAG,"onPreExecute");
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean match = false;
            Connection conn = null;
            Statement stmt = null;
            // JDBC 驱动名及数据库 URL
            String JDBC_DRIVER = "com.mysql.jdbc.Driver";
            String URL = "jdbc:mysql://123.207.26.102:3306/tongdao";
            String USER = "root";
            String PASS = "123456";
            try{
                // 注册 JDBC 驱动
                Class.forName(JDBC_DRIVER);

                // 打开链接
                System.out.println("连接数据库...");
                conn = DriverManager.getConnection(URL,USER,PASS);

                // 执行查询
                System.out.println(" 实例化Statement对...");
                stmt = conn.createStatement();
                String sql;
                sql = "SELECT password FROM user WHERE id = \'" + params[0] + "\'" ;
                id = params[0];
                System.out.println("查询语句为："+ sql);
                ResultSet rs = stmt.executeQuery(sql);

                // 展开结果集数据库
                while(rs.next()){
                    // 通过字段检索
                    String password_db = rs.getString("password");
                    System.out.println("对应用户的密码为："+ password_db);
                    if(params[1].equals(password_db))match = true;
                }
                // 完成后关闭
                rs.close();
                stmt.close();
                conn.close();
            }catch(SQLException se){
                // 处理 JDBC 错误
                se.printStackTrace();
            }catch(Exception e){
                // 处理 Class.forName 错误
                e.printStackTrace();
            }finally{
                // 关闭资源
                try{
                    if(stmt!=null) stmt.close();
                }catch(SQLException se2){
                }// 什么都不做
                try{
                    if(conn!=null) conn.close();
                }catch(SQLException se){
                    se.printStackTrace();
                }
            }
            return match;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            Log.d(TAG,"onProgressUpdate values[0]="+ values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Button text = (Button)findViewById(R.id.button_login);
            if(result)
            {
                text.setText("Success!");
                Intent intent=new Intent(MainActivity.this,MyFriendsActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
            else
            {
                text.setText("Failed!");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onClick_login(View view)
    {
        final TextInputLayout idText = (TextInputLayout) findViewById(R.id.textInputLayout_ID);
        final TextInputLayout passwordText = (TextInputLayout) findViewById(R.id.textInputLayout_Pas);
        Button text = (Button)findViewById(R.id.button_login);
        String id = idText.getEditText().getText().toString();
        String password = passwordText.getEditText().getText().toString();
        new LogInTask().execute(id,password);
    }
    public void onClick_ToSignUp(View view)
    {
        Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
        startActivity(intent);
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
