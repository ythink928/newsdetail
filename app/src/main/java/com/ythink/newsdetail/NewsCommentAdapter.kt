package com.ythink.newsdetail

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class NewsCommentAdapter:BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_video_comment){
  override fun convert(helper: BaseViewHolder?, item: String?) {
    helper?.run {
      setText(R.id.tv_comment,item)
    }
  }

}