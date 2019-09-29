package cafe.gophers.a3_most_basic_recyclerview

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView

class ImageRectAdapter(val data : List<String>, val countAlreadyMembers : Int) : RecyclerView.Adapter<ImageRectAdapter.MyViewHolder>() {

    class MyViewHolder : RecyclerView.ViewHolder {
        var header : TextView
        var divider: View
        var textView : TextView
        var button  : Button
        var ctx : Context

        constructor(layout: ConstraintLayout, context: Context) : super(layout) {
            header = layout.findViewById(R.id.header)
            divider = layout.findViewById(R.id.divider)
            textView = layout.findViewById(R.id.name_text_view)
            button = layout.findViewById(R.id.icon_button)
            ctx = context
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val constraintLayout = LayoutInflater.from(parent.context).inflate(R.layout.my_text_image_layout, parent, false) as ConstraintLayout

        return MyViewHolder(constraintLayout, parent.context)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item

        // Show header
        when (position) {
            0 -> {
                holder.header.visibility = View.VISIBLE
                holder.divider.visibility = View.VISIBLE
                val clr = ContextCompat.getColor(holder.ctx, R.color.colorPrimary2)
                holder.header.text = "Friends in app - ${countAlreadyMembers}"
                holder.header.setTextColor(clr)
                holder.divider.background = clr.toDrawable()

            }
            countAlreadyMembers -> {
                holder.header.visibility = View.VISIBLE
                holder.divider.visibility = View.VISIBLE
                holder.header.text = "Friends not yet in app - ${data.size - countAlreadyMembers}"
                holder.header.setTextColor(Color.GRAY)
                holder.divider.background = Color.GRAY.toDrawable()

            }
            else -> {
                holder.header.visibility = View.GONE
                holder.divider.visibility = View.GONE
            }
        }

        if (position < countAlreadyMembers) {

            // Show icon
            holder.button.visibility = View.VISIBLE
            holder.button.setOnClickListener {
                Toast.makeText(holder.ctx, "Added ${item} to your friends", Toast.LENGTH_SHORT).show()
            }
        } else {
            holder.button.visibility = View.INVISIBLE
        }





    }
}