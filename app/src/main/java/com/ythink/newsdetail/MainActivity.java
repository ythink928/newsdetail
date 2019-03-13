package com.ythink.newsdetail;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    NewsDetailHeader mNewsDetailHeader;
    NewsCommentAdapter mNewsCommentAdapter;
    View mLayoutHeader;
    View mLayoutTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        //RecyclerView初始化
        RecyclerView mRecyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mNewsCommentAdapter = new NewsCommentAdapter();
        mNewsDetailHeader = new NewsDetailHeader();
        mNewsCommentAdapter.addHeaderView(mNewsDetailHeader.getView(mRecyclerView));
        mRecyclerView.setAdapter(mNewsCommentAdapter);

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //模拟数据加载
                List<String> list = new ArrayList<>();
                for (int i=0;i<30;i++){
                    list.add("评论数据"+i);
                }
                mNewsCommentAdapter.setNewData(list);

                mNewsDetailHeader.loadUrl("http://www.baidu.com");
            }
        },3000);

        AppBarLayout appBarLayout = findViewById(R.id.layout_appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state.equals(State.IDLE)||state.equals(State.EXPANDED)){
                    mLayoutHeader.setVisibility(View.VISIBLE);
                    mLayoutTitle.setVisibility(View.GONE);
                }else if (state.equals(State.COLLAPSED)){
                    mLayoutHeader.setVisibility(View.GONE);
                    mLayoutTitle.setVisibility(View.VISIBLE);
                }
            }
        });

        mLayoutHeader = findViewById(R.id.layout_header);
        mLayoutTitle = findViewById(R.id.layout_title);

        //此处省略ImmersionBar
    }


}
