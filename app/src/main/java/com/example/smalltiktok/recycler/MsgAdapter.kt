package com.example.smalltiktok.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.smalltiktok.R
import java.util.*

class MsgAdapter(myDataset: List<MsgData>?) :
    RecyclerView.Adapter<MsgAdapter.MyViewHolder>(), ItemTouchAdapter{
    private val mDataset: MutableList<MsgData>? = ArrayList<MsgData>()
    private var mItemClickListener: IOnItemClickListener? = null

    class MyViewHolder(private val contentView: View) : ViewHolder(contentView) {

        private val msgTitle: TextView = contentView.findViewById(R.id.title)
        private val msgContent: TextView = contentView.findViewById(R.id.content)
        private val msgSendTime: TextView = contentView.findViewById(R.id.sendTime)
        private val msgUserImage: ImageView = contentView.findViewById(R.id.imageView)

        fun onBind(position: Int, data: MsgData) {
            msgTitle.text = data.title
            msgContent.text = data.content
            msgSendTime.text = data.time
            msgUserImage.setImageResource(data.imageID)
        }

        fun setOnClickListener(listener: View.OnClickListener?) {
            if (listener != null) {
                contentView.setOnClickListener(listener)
            }
        }

        fun setOnLongClickListener(listener: OnLongClickListener?) {
            if (listener != null) {
                contentView.setOnLongClickListener(listener)
            }
        }
    }

    fun setOnItemClickListener(listener: IOnItemClickListener?) {
        mItemClickListener = listener
    }

    var onItemDragListener : OnItemDragListener? = null

    interface OnItemDragListener {
        fun onDrag(holder: MyViewHolder)
    }

    fun addData(position: Int, data: MsgData) {
        mDataset!!.add(position, data)
        notifyItemInserted(position)
        if (position != mDataset.size) {
            //刷新改变位置item下方的所有Item的位置,避免索引错乱
            notifyItemRangeChanged(position, mDataset.size - position)
        }
    }

    fun removeData(position: Int) {
        if (null != mDataset && mDataset.size > position) {
            mDataset.removeAt(position)
            notifyItemRemoved(position)
            if (position != mDataset.size) {
                //刷新改变位置item下方的所有Item的位置,避免索引错乱
                notifyItemRangeChanged(position, mDataset.size - position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.msg_recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.onBind(position, mDataset!![position])
        holder.setOnClickListener(View.OnClickListener {
            if (mItemClickListener != null) {
                mItemClickListener!!.onItemCLick(position, mDataset[position])
            }
        })
        holder.setOnLongClickListener(OnLongClickListener {
            if (mItemClickListener != null) {
                mItemClickListener!!.onItemLongCLick(position, mDataset[position])
            }
            false
        })
    }
    override fun getItemCount(): Int {
        return mDataset!!.size
    }

    interface IOnItemClickListener {
        fun onItemCLick(position: Int, data: MsgData?)
        fun onItemLongCLick(position: Int, data: MsgData?)
    }

    init {
        mDataset!!.addAll(myDataset!!)
    }

    override fun onItemDismiss(position: Int) {
        if (null != mDataset && mDataset.size > position) {
            mDataset.removeAt(position)
            notifyItemRemoved(position)
            if (position != mDataset.size) {
                //刷新改变位置item下方的所有Item的位置,避免索引错乱
                notifyItemRangeChanged(position, mDataset.size - position)
            }
        }
    }
}
