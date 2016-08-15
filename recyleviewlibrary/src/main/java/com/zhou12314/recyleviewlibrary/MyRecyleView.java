package com.zhou12314.recyleviewlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zhou12314.recyleviewlibrary.swipe.SwipeRefreshLayout;


/**
 * Created by zhoug on 2016/8/9.
 */
public class MyRecyleView extends FrameLayout {
    protected boolean mClipToPadding;
    protected int mPadding;
    protected int mPaddingTop;
    protected int mPaddingBottom;
    protected int mPaddingLeft;
    protected int mPaddingRight;
    protected int mScrollbarStyle;
    private int mProgressId;
    private int mEmptyId;
    private int mErrorId;
    private ViewGroup mErrorView;
    private ViewGroup mLoadView;
    private ViewGroup mEmptyView;
    private View view;
    private RecyclerView recyclerView;
    protected RecyclerView.OnScrollListener mInternalOnScrollListener;
    protected RecyclerView.OnScrollListener mExternalOnScrollListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    protected android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener mRefreshListener;

    public MyRecyleView(Context context) {
        super(context);

        initViews(context);
    }

    public MyRecyleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initViews(context);
    }

    public MyRecyleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initViews(context);
    }

    private void initViews(Context context) {
        //判断该view是否属于可编辑状态
        if (isInEditMode()) {
            return;
        }
        view = LayoutInflater.from(context).inflate(R.layout.my_recyleview, this);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        if (mEmptyId != 0) {
            mEmptyView = (ViewGroup) view.findViewById(R.id.empty);
            LayoutInflater.from(getContext()).inflate(mEmptyId, mEmptyView);
        }
        if (mErrorId != 0) {
            mErrorView = (ViewGroup) view.findViewById(R.id.error);
            LayoutInflater.from(getContext()).inflate(mErrorId, mErrorView);
        }
        if (mProgressId != 0) {
            mLoadView = (ViewGroup) view.findViewById(R.id.loading);
            LayoutInflater.from(getContext()).inflate(mProgressId, mLoadView);
        }
        initRecyclerView();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return swipeRefreshLayout.dispatchTouchEvent(ev);
    }


    /**
     * Set the listener when refresh is triggered and enable the SwipeRefreshLayout
     *
     * @param listener
     */
    public void setRefreshListener(android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener listener) {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(listener);
        this.mRefreshListener = listener;
    }

    public void setRefreshing(final boolean isRefreshing){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(isRefreshing);
            }
        });
    }

    public void setRefreshing(final boolean isRefreshing, final boolean isCallbackListener){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(isRefreshing);
                if (isRefreshing&&isCallbackListener&&mRefreshListener!=null){
                    mRefreshListener.onRefresh();
                }
            }
        });
    }

    /**
     * Set the colors for the SwipeRefreshLayout states
     *
     * @param colRes1
     * @param colRes2
     * @param colRes3
     * @param colRes4
     */
    public void setRefreshingColorResources(@ColorRes int colRes1, @ColorRes int colRes2, @ColorRes int colRes3, @ColorRes int colRes4) {
        swipeRefreshLayout.setColorSchemeResources(colRes1, colRes2, colRes3, colRes4);
    }

    /**
     * Set the colors for the SwipeRefreshLayout states
     *
     * @param col1
     * @param col2
     * @param col3
     * @param col4
     */
    public void setRefreshingColor(int col1, int col2, int col3, int col4) {
        swipeRefreshLayout.setColorSchemeColors(col1, col2, col3, col4);
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.rcylerview);
        if (recyclerView != null) {
            //设置固定大小
            recyclerView.setHasFixedSize(true);
            recyclerView.setClipToPadding(mClipToPadding);
            mInternalOnScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (mExternalOnScrollListener != null) {
                        mExternalOnScrollListener.onScrollStateChanged(recyclerView, newState);
                    }
                }
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (mExternalOnScrollListener != null) {
                        mExternalOnScrollListener.onScrolled(recyclerView, dx, dy);
                    }
                }
            };
            recyclerView.addOnScrollListener(mInternalOnScrollListener);
            if (mPadding != -1.0f) {
                recyclerView.setPadding(mPadding, mPadding, mPadding, mPadding);
            } else {
                recyclerView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
            }
            if (mScrollbarStyle != -1f) {
                /**
                 * 　　insideOverlay：默认值，表示在padding区域内并且覆盖在view上
                 　　insideInset：表示在padding区域内并且插入在view后面
                 　　outsideOverlay：表示在padding区域外并且覆盖在view上，推荐这个
                 　　outsideInset：表示在padding区域外并且插入在view后面
                 */
                recyclerView.setScrollBarStyle(mScrollbarStyle);
            }
        }
    }

    /**
     * Set the scroll listener for the recycler
     *
     * @param listener
     */
    public void setOnScrollListener(RecyclerView.OnScrollListener listener) {
        mExternalOnScrollListener = listener;
    }

    /**
     * 设置recyleview布局管理器
     *
     * @param manager
     */
    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        recyclerView.setLayoutManager(manager);
    }


    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.superrecyclerview);
        try {
            mClipToPadding = a.getBoolean(R.styleable.superrecyclerview_recyclerClipToPadding, false);
            mPadding = (int) a.getDimension(R.styleable.superrecyclerview_recyclerPadding, -1.0f);
            mPaddingTop = (int) a.getDimension(R.styleable.superrecyclerview_recyclerPaddingTop, 0.0f);
            mPaddingBottom = (int) a.getDimension(R.styleable.superrecyclerview_recyclerPaddingBottom, 0.0f);
            mPaddingLeft = (int) a.getDimension(R.styleable.superrecyclerview_recyclerPaddingLeft, 0.0f);
            mPaddingRight = (int) a.getDimension(R.styleable.superrecyclerview_recyclerPaddingRight, 0.0f);
            mScrollbarStyle = a.getInteger(R.styleable.superrecyclerview_scrollbarStyle, -1);
            mEmptyId = a.getResourceId(R.styleable.superrecyclerview_layout_empty, 0);
            mProgressId = a.getResourceId(R.styleable.superrecyclerview_layout_progress, 0);
            mErrorId = a.getResourceId(R.styleable.superrecyclerview_layout_error, 0);
        } finally {
            a.recycle();
        }
    }

    private static class AdapterDataObserver extends RecyclerView.AdapterDataObserver {
        private MyRecyleView myRecyleView;
        public AdapterDataObserver(MyRecyleView myRecyleView) {
            this.myRecyleView = myRecyleView;
        }
        @Override
        public void onChanged() {
            super.onChanged();
            updateShow();
        }
        private void updateShow() {
             if(myRecyleView.getAdapter().getItemCount() == 0){
                 myRecyleView.showEmpty();
             }else {
                 myRecyleView.showRecycler();
             }
        }
        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            updateShow();
        }
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            updateShow();
        }
        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            updateShow();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            updateShow();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            updateShow();
        }
    }

    /**
     * Remove the adapter from the recycler
     */
    public void clear() {
        recyclerView.setAdapter(null);
    }


    private void hideAll() {
        mLoadView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mErrorView.setVisibility(GONE);
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    //展示一些状态view
    public void showError() {
        hideAll();
        mErrorView.setVisibility(View.VISIBLE);
    }

    public void showEmpty() {
        hideAll();
        mEmptyView.setVisibility(View.VISIBLE);
    }


    public void showProgress() {
        hideAll();
        mLoadView.setVisibility(View.VISIBLE);
    }


    public void showRecycler() {
        hideAll();
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void setEmptyView(View emptyView) {
        mEmptyView.removeAllViews();
        mEmptyView.addView(emptyView);
    }

    public void setProgressView(View progressView) {
        mLoadView.removeAllViews();
        mLoadView.addView(progressView);
    }

    public void setErrorView(View errorView) {
        mErrorView.removeAllViews();
        mErrorView.addView(errorView);
    }

    public void setEmptyView(int emptyView) {
        mEmptyView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(emptyView, mEmptyView);
    }

    public void setProgressView(int progressView) {
        mLoadView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(progressView, mLoadView);
    }

    public void setErrorView(int errorView) {
        mErrorView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(errorView, mErrorView);
    }

    //
    public void scrollToPosition(int position) {
        getRecyclerView().scrollToPosition(position);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * @return inflated error view or null
     */
    public View getErrorView() {
        if (mErrorView.getChildCount()>0)return mErrorView.getChildAt(0);
        return null;
    }
    /**
     * @return inflated progress view or null
     */
    public View getProgressView() {
        if (mLoadView.getChildCount()>0)return mLoadView.getChildAt(0);
        return null;
    }

    /**
     * @return inflated empty view or null
     */
    public View getEmptyView() {
        if (mEmptyView.getChildCount()>0)return mEmptyView.getChildAt(0);
        return null;
    }

    /**
     * 给item添加动画
     * @param animator
     */
    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        recyclerView.setItemAnimator(animator);
    }

    /**
     * 添加分割线
     * @param itemDecoration
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        recyclerView.addItemDecoration(itemDecoration);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        recyclerView.addItemDecoration(itemDecoration, index);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        recyclerView.removeItemDecoration(itemDecoration);
    }
    public  void setAdapter(RecyclerView.Adapter adapter){
        showRecycler();
        recyclerView.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new AdapterDataObserver(this));
        if(adapter == null || adapter.getItemCount() == 0){
            showEmpty();
        }
    }
    /**
     * @return the recycler adapter
     */
    public RecyclerView.Adapter getAdapter() {
        return recyclerView.getAdapter();
    }

}


