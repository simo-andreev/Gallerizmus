package sim.o.bg.gallerizmus.view

import android.app.Fragment
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.VERTICAL
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView


class GalleryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = RecyclerView(context)
        view.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        view.layoutManager = StaggeredGridLayoutManager(getColumnCount(), VERTICAL)

        view.adapter = GalleryRecyclerAdapter()
        return view
    }



    private fun getColumnCount(): Int {
        val displayMetrics = context.resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density // display width
        return Math.max(1, (dpWidth / 160).toInt())
    }
}


class GalleryRecyclerAdapter : RecyclerView.Adapter<GalleryRecyclerAdapter.ImageHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        return ImageHolder(parent.context)
    }

    override fun getItemCount(): Int {
        return 200
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun getItem(position: Int): Drawable {
        return ColorDrawable(Color.rgb(position, 1, 1))
    }


    class ImageHolder(context: Context) : RecyclerView.ViewHolder(ImageView(context)){
        fun bind(item: Drawable) {
            itemView as ImageView
            val layoutParams = RecyclerView.LayoutParams(MATCH_PARENT, 100)
            layoutParams.setMargins(10, 10, 10, 10)
            itemView.layoutParams = layoutParams
            itemView.setImageDrawable(item)
        }
    }
}