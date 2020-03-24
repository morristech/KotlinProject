package com.webaddicted.kotlinproject.view.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowTextListBinding
import com.webaddicted.kotlinproject.view.base.BaseAdapter
import com.webaddicted.kotlinproject.view.fragment.TaskFrm
import java.util.*

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class TaskAdapter(private var taskFrm: TaskFrm, private var mTaskList: ArrayList<String>?) : BaseAdapter() {
    private var searchText: String? = null
    private val searchArray: List<String>

    init {
        this.searchArray = ArrayList()
        mTaskList?.let { this.searchArray.addAll(it) }
    }

    override fun getListSize(): Int {
        if (mTaskList == null) return 0
        return mTaskList?.size!!
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_text_list
    }

    override fun onBindTo(mRowBinding: ViewDataBinding, position: Int) {
        if (mRowBinding is RowTextListBinding) {
//            if (searchText != null && searchText?.length!! > 1) {
//                val completeText = mTaskList?.get(position)
//                val spannableString = SpannableString(mTaskList?.get(position))
//                var i = -1
//                while (completeText?.indexOf(searchText!!, i + 1).also { i = it!! } != -1) {
////                while ((i = completeText.indexOf(searchText!!, i + 1)) != -1) {
//                    val endText = searchText?.length!! + i
//                    val foregroundSpan = ForegroundColorSpan(Color.GREEN)
//                    spannableString.setSpan(
//                        foregroundSpan,
//                        i,
//                        endText,
//                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                    )
//                    i++
//                }
            mRowBinding.txtName.text = mTaskList?.get(position)
            val test = mTaskList?.get(position)
            val initial = test?.get(0)
            mRowBinding.txtInitial.text = initial.toString()

//            } else {
//                mRowBinding.txtName.setText(mTaskList?.get(position))
//            }
//            mRowBinding.card.setOnClickListener {
            onClickListener(mRowBinding,mRowBinding.card, position)
//        }
        }
    }

    override fun getClickEvent(mRowBinding: ViewDataBinding,view: View?, position: Int) {
        super.getClickEvent(mRowBinding,view, position)
        when (view?.id) {
            R.id.card -> mTaskList?.get(position)?.let { taskFrm.onClicks(it) }
        }
    }

    fun notifyAdapter(list: ArrayList<String>) {
        this.mTaskList = list
        notifyDataSetChanged()
    }

    fun filter(charText: String?) {
        var charText = charText
        charText = charText!!.toLowerCase(Locale.getDefault())
        searchText = charText
        mTaskList?.clear()
        if (charText == null && charText!!.length == 0) {
            mTaskList?.addAll(searchArray)
        } else {
            for (wp in searchArray) {
                if (wp.toLowerCase(Locale.getDefault()).contains(charText)) {
                    mTaskList?.add(wp)
                }
                //                else if (wp.toLowerCase(Locale.getDefault()).contains(charText)) {
                //                    mAction.add(wp);
                //                }
            }
        }
        notifyDataSetChanged()
    }
}
