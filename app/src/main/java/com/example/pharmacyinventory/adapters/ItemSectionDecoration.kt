package com.example.pharmacyinventory.adapters

import android.content.Context
import android.graphics.*
import android.icu.text.Transliterator
import android.os.Build
import android.text.BoringLayout
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmacyinventory.model.Entry
import com.example.pharmacyinventory.ui.SectionViewHolder
import java.io.PipedOutputStream

class ItemSectionDecoration(
    private val context: Context,
    private val getItemList: () -> MutableList<Entry>
) : RecyclerView.ItemDecoration() {

    private val dividerHeight = dipToPx(context, 0.8f)
    private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = Color.parseColor("#59E2D4F4")
    }

    private val sectionItemWidth: Int by lazy {
        getScreenWidth(context)
    }
    private val sectionItemHeight: Int by lazy {
        dipToPx(context, 30f)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val layoutManager = parent.layoutManager

        // just allow linear layout manager
        if (layoutManager !is LinearLayoutManager) return
        // just allow vertical orientation
        if (LinearLayoutManager.VERTICAL != layoutManager.orientation) return

        val list = getItemList()
        if (list.isEmpty()) return

        val position = parent.getChildAdapterPosition(view)
        if (0 == position) {
            /*
            if the item first item
            then should add a section
            */
            outRect.top = sectionItemHeight
            return
        }
        val currentModel = getItemList()[position]
        val previousModel = getItemList()[position - 1]

        if (currentModel.date != previousModel.date) {
            /*
            if the target value of current item is not the same as
            the target value of previous item
            the should add a section
            in this example, the target value is date, group by date
             */
            outRect.top = sectionItemHeight
        } else {
            // or add a divider line
            outRect.top = dividerHeight
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val childView = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(childView)
            val itemModel = getItemList()[position]

            if (getItemList().isNotEmpty() &&
                (0 == position || itemModel.date != getItemList()[position - 1].date)
            ) {
                val top = childView.top - sectionItemHeight
                drawSectionView(c, itemModel.date.toString(), top)
            } else {
                drawDivider(c, childView)
            }
        }
    }

    private fun drawDivider(canvas: Canvas, childView: View) {
        canvas.drawRect(
            0f,// left
            (childView.top - dividerHeight).toFloat(), // top
            childView.right.toFloat(), // right
            childView.top.toFloat(), // bottom
            dividerPaint
        )
    }

    private fun drawSectionView(canvas: Canvas, text: String, top: Int) {
        val view = SectionViewHolder(context)
        view.setDate(text)

        val bitmap = getViewGroupBitmap(view)
        val bitmapCanvas = Canvas(bitmap)
        view.draw(bitmapCanvas)

        canvas.drawBitmap(bitmap, 0f, top.toFloat(), null)
    }

    private fun getViewGroupBitmap(viewGroup: ViewGroup): Bitmap {
        val layoutParams = ViewGroup.LayoutParams(sectionItemWidth, sectionItemHeight)
        viewGroup.layoutParams = layoutParams

        viewGroup.measure(
            View.MeasureSpec.makeMeasureSpec(sectionItemWidth, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(sectionItemHeight, View.MeasureSpec.EXACTLY)
        )
        viewGroup.layout(0, 0, sectionItemWidth, sectionItemHeight)
        val bitmap = Bitmap.createBitmap(viewGroup.width, viewGroup.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        viewGroup.draw(canvas)

        return bitmap
    }

    private fun dipToPx(context: Context, dipValue: Float): Int {
        return (dipValue * context.resources.displayMetrics.density).toInt()
    }

    private fun getScreenWidth(context: Context): Int {
        val outMetrics = DisplayMetrics()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val display = context.display
            display?.getRealMetrics(outMetrics)
        } else {
            val display =
                (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            display.getMetrics(outMetrics)
        }
        return outMetrics.widthPixels
    }
}