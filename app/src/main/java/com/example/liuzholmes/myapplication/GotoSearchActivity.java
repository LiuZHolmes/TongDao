package com.example.liuzholmes.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.sql.*;

public class GotoSearchActivity extends AppCompatActivity {

    public String user_id = "qyr";

    String[] movieName = new String[5];

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    private static final String TAG = "ASYNC_TASK";

    private class ShowItemsTask extends AsyncTask<String, Integer, Boolean>
    {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            Log.d(TAG,"onPreExecute");
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            CheckBox[] m_CheckBox = new CheckBox[5];
            EditText m_search_text = (EditText) findViewById(R.id.edit_message);
            String searchStr = m_search_text.getText().toString();
            m_CheckBox[0] = (CheckBox) findViewById(R.id.ags_cb_0);
            m_CheckBox[1] = (CheckBox) findViewById(R.id.ags_cb_1);
            m_CheckBox[2] = (CheckBox) findViewById(R.id.ags_cb_2);
            m_CheckBox[3] = (CheckBox) findViewById(R.id.ags_cb_3);
            for(int i = 0; i<5 ;i++) {
                m_CheckBox[4] = (CheckBox) findViewById(R.id.ags_cb_4);

                m_CheckBox[i].setText("");
            }

            boolean match = false;
            Connection conn = null;
            Statement stmt = null;
            int j = 0;
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
                sql = "SELECT movie_name FROM movie WHERE movie_name like \'%" + searchStr + "%\'" ;
                System.out.println(sql);
                ResultSet rs = stmt.executeQuery(sql);



                // 展开结果集数据库
                while(rs.next() && j<5){
                    // 通过字段检索
                    String SmovieName = rs.getString("movie_name");
                    System.out.println( SmovieName);
                    m_CheckBox[j].setSelected(false);
                    m_CheckBox[j].setText(SmovieName);
                    j++;
                    match = true;
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

            System.out.println( "return match");

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

        }

    }

    private class UserMovieTask extends AsyncTask<String, Integer, Boolean>
    {
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

            CheckBox[] m_CheckBox = new CheckBox[5];
            m_CheckBox[0] = (CheckBox) findViewById(R.id.ags_cb_0);
            m_CheckBox[1] = (CheckBox) findViewById(R.id.ags_cb_1);
            m_CheckBox[2] = (CheckBox) findViewById(R.id.ags_cb_2);
            m_CheckBox[3] = (CheckBox) findViewById(R.id.ags_cb_3);
            m_CheckBox[4] = (CheckBox) findViewById(R.id.ags_cb_4);

            try{
                // 注册 JDBC 驱动
                Class.forName(JDBC_DRIVER);

                // 打开链接
                System.out.println("连接数据库...");
                conn = DriverManager.getConnection(URL,USER,PASS);

                // 执行查询
                System.out.println(" 实例化Statement对...");
                stmt = conn.createStatement();

                for(int i=0;i<5;i++) {
                    String movie_name =  m_CheckBox[i].getText().toString();
                    if(movie_name.equals("") ||  !m_CheckBox[i].isChecked()) {
                        continue;
                    }

                    String sql;
                    sql = "INSERT INTO user_movie VALUES ( \'" + user_id + "\',\'" + movie_name + "\')"  ;
                    System.out.println(sql);
                    int rs = stmt.executeUpdate(sql);
                    if (rs != -1) // 插入成功
                    {
                        System.out.println("插入成功");
                        match = true;
                    }
                }


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
            return  match;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            Log.d(TAG,"onProgressUpdate values[0]="+ values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Button text = (Button)findViewById(R.id.button_SignUp);
            if(result)
            {
                text.setText("Success!");
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
        setContentView(R.layout.activity_goto_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CheckBox[] m_CheckBox = new CheckBox[5];
        m_CheckBox[0] = (CheckBox) findViewById(R.id.ags_cb_0);
        m_CheckBox[1] = (CheckBox) findViewById(R.id.ags_cb_1);
        m_CheckBox[2] = (CheckBox) findViewById(R.id.ags_cb_2);
        m_CheckBox[3] = (CheckBox) findViewById(R.id.ags_cb_3);
        m_CheckBox[4] = (CheckBox) findViewById(R.id.ags_cb_4);

        new ShowItemsTask().execute();

    }
    public void onClick_search(View view)
    {
        new ShowItemsTask().execute();
    }

    public void onClick_OK(View view) {
        new UserMovieTask().execute();
    }


}
