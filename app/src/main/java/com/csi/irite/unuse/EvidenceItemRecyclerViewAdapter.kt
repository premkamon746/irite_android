package com.csi.irite.unuse

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.csi.irite.R
import com.csi.irite.placeholder.PlaceholderContent.PlaceholderItem
import com.csi.irite.room.database.AppDatabase
import com.csi.irite.room.dao.EvidenceDao
import com.csi.irite.room.data.Evidence

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class EvidenceItemRecyclerViewAdapter(
    val context: Context,
    private var items: List<Evidence>
)
    : RecyclerView.Adapter<EvidenceItemRecyclerViewAdapter.ViewHolder>(){

    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemDeleteClickListener: OnItemClickListener? = null

    var db: AppDatabase? = null
    val evidenceBagDao:EvidenceDao? = null

    //private val items: List<EvidenceBag> = java.util.ArrayList<EvidenceBag>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_evidence_item, parent, false)

        db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "database"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
        val evidenceDao: EvidenceDao = db!!.evidenceDao()

        return ViewHolder(itemView)
    }

    fun update(items: List<Evidence>){
        this.items = items
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        mOnItemClickListener = mItemClickListener
    }

    fun setOnItemDeleteClickListener(mItemClickListener: OnItemClickListener) {
        mOnItemDeleteClickListener = mItemClickListener
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.no.text = (position+1).toString()
        holder.evidence.text = item.evidence
        var space = "              "
        holder.distance_1.text = item.distance_1.toString()
        holder.distance_2.text = item.distance_2
        holder.distance_3.text = item.distance_3
        holder.distance_4.text = item.distance_4
        holder.angle.text = item.angle
        //holder.remark.text = item.remark

        holder.bt_delete.setOnClickListener({
            if (mOnItemDeleteClickListener != null) {
                mOnItemDeleteClickListener!!.onItemClick(holder.itemView, items[position])
            }
        })

        holder.bt_edit.setOnClickListener({
            if (mOnItemClickListener != null) {
                mOnItemClickListener!!.onItemClick(holder.itemView, items[position])
            }
        })
    }


    /*fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }*/

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val no: TextView = itemView.findViewById(R.id.no)
        val evidence: TextView = itemView.findViewById(R.id.evidence)
        val distance_1: TextView = itemView.findViewById(R.id.distance_1)
        val distance_2: TextView = itemView.findViewById(R.id.distance_2)
        val distance_3: TextView = itemView.findViewById(R.id.distance_3)
        val distance_4: TextView = itemView.findViewById(R.id.distance_4)/**/
        val angle: TextView = itemView.findViewById(R.id.angle)
        val remark: ImageView = itemView.findViewById(R.id.remark)

        val bt_delete: Button = itemView.findViewById(R.id.bt_delete)
        val bt_edit: Button = itemView.findViewById(R.id.bt_edit)


        override fun toString(): String {
            return super.toString() + " '" + evidence.text + "'"
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view:View, item: Evidence)
    }/**/
}