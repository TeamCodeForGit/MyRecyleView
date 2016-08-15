package com.zhou12314.myrecyleview;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import com.zhou12314.recyleviewlibrary.MyRecyleView;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         final MyRecyleView myRecyleView = (MyRecyleView) findViewById(R.id.recyleView);
        List<String> list = new ArrayList<>();

        final MyRecyleViewAdapter myRecyleViewAdapter = new MyRecyleViewAdapter(list,MainActivity.this);
        myRecyleView.setLayoutManager(new LinearLayoutManager(this));
        myRecyleView.setAdapter(myRecyleViewAdapter);

        for(int i = 0;i<20; i++){
            list.add(i+"");
        }
        myRecyleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                myRecyleView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myRecyleViewAdapter.notifyDataSetChanged();
                    }
                },2000);
               myRecyleView.showProgress();
            }
         },2000);

        //手动设置RecyleView 条目为空的时候的状态
       // myRecyleView.setEmptyView(R.layout.empty);
        //显示为空条目的布局
       /// myRecyleView.showEmpty();
        //显示加载中的布局
      //  myRecyleView.showProgress();
        //显示加载失败的布局
     //   myRecyleView.showError();
          myRecyleView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
              @Override
              public void onRefresh() {
                  myRecyleView.postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          //下来刷新操作在这里设置
                          myRecyleViewAdapter.notifyDataSetChanged();
                      }
                  },2000);
              }
          });


    }
}
