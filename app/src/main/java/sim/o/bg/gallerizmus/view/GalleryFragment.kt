package sim.o.bg.gallerizmus.view

import android.app.Fragment
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import android.widget.Button
import android.widget.ImageView


class GalleryFragment : Fragment() {

    private val adapter = GalleryRecyclerAdapter()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = RecyclerView(context)
        view.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        view.layoutManager = StaggeredGridLayoutManager(getColumnCount(), VERTICAL)

        view.adapter = adapter
        return view
    }

    private fun getColumnCount(): Int {
        val displayMetrics = context.resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density // display width
        return Math.max(1, (dpWidth / 160).toInt())
    }

    fun setItems(queryCursors: MutableList<String>) {
        adapter.items = queryCursors
    }
}


class GalleryRecyclerAdapter : RecyclerView.Adapter<GalleryRecyclerAdapter.ImageHolder>(){
    var items : List<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        return ImageHolder(parent.context)
    }

    override fun getItemCount(): Int {
        return Math.min(50, items!!.size)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    private fun getItem(position: Int): Bitmap? {
        val options = BitmapFactory.Options()
        options.inSampleSize = 10
        return BitmapFactory.decodeFile(items!![position], options)
    }

    class ImageHolder(context: Context) : RecyclerView.ViewHolder(ImageView(context)){
        fun bind(item: Bitmap) {
            itemView as ImageView
            val layoutParams = RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            layoutParams.setMargins(5, 5, 5, 5)
            itemView.layoutParams = layoutParams
            itemView.elevation = 8f
            itemView.setImageBitmap(item)
        }
    }
}