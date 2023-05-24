package com.example.nexum

import android.content.Context
import android.util.AttributeSet
import android.widget.GridView

class ScrollableGridView(context: Context, attrs: AttributeSet) : GridView(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSpec = MeasureSpec.makeMeasureSpec(Int.MAX_VALUE shr 2, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, heightSpec)
        layoutParams.height = measuredHeight
    }
}