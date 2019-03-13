package com.ythink.newsdetail

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient


class NewsDetailHeader{

  private var mRootView: View? = null
  private var mWebView: WebView?= null

  fun getView(mRecyclerView: RecyclerView): View? {
    mRootView = LayoutInflater.from(mRecyclerView.context)
        .inflate(R.layout.item_news_content, mRecyclerView.parent as ViewGroup, false)
    initView()
    return mRootView
  }

  fun initView(){
    mRootView?.run {
      mWebView = findViewById(R.id.root_webview)
      initWebView(mWebView!!)

    }
  }


  private fun initWebView(rootWebView: WebView){
    //在xml中的WebView高度是固定的很小值
    val settings = rootWebView.getSettings()

    //屏蔽缩放控制显示及功能
    settings.setBuiltInZoomControls(false)
    settings.setBuiltInZoomControls(false)
    settings.setSupportZoom(false)

    settings.setJavaScriptEnabled(true)
    settings.setUseWideViewPort(true)
    settings.setDefaultTextEncodingName("utf-8")
    rootWebView.setHorizontalScrollBarEnabled(false)// 滚动条水平不显示
    rootWebView.setVerticalScrollBarEnabled(false) // 滚动条垂直不显示

    // 屏蔽长按复制效果
    rootWebView.setOnLongClickListener(View.OnLongClickListener { true })
    rootWebView.setWebViewClient(MyCustomWebViewClient())
    rootWebView.addJavascriptInterface(WebAppInterface(),"AndroidFunction")

    rootWebView.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
      if (event.action == KeyEvent.ACTION_DOWN) {
        if (keyCode == KeyEvent.KEYCODE_BACK && rootWebView.canGoBack()) {
          rootWebView.goBack()
          return@OnKeyListener true
        }
      }
      false
    })


    //此处是重点
    rootWebView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
      override fun onPreDraw(): Boolean {
        //往大了拉伸
        var newWebHeight = mWebView?.contentHeight!!
        if (newWebHeight < resizeHeight){
          newWebHeight = resizeHeight
        }

        if (oldWebHeight >= newWebHeight){
          return true
        }
        val param = mWebView?.layoutParams
        param?.height = newWebHeight
        mWebView?.layoutParams = param
        Log.i("NewsWebView","newWebHeight:$newWebHeight,oldWebHeight:$oldWebHeight")
        oldWebHeight = newWebHeight
        return true
      }

    })
  }

  var oldWebHeight = 0
  var resizeHeight = 0


  private inner class MyCustomWebViewClient : WebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {
      super.onPageFinished(view, url)
      view?.loadUrl("javascript:AndroidFunction.resize(document.body.scrollHeight)")
    }

  }

  fun loadUrl(url:String){
    mWebView?.loadUrl(url)
  }



  /**
   * WebView interface to communicate with Javascript
   */
  inner class WebAppInterface {

    @JavascriptInterface
    fun resize(height: Float) {

      mWebView?.run {
        //记录下通过js得到的内容高度
        resizeHeight = (height*resources.displayMetrics.density).toInt()
      }
    }
  }

}