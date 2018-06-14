package sim.o.bg.gallerizmus.view

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.database.Cursor
import android.database.MergeCursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONArray
import org.json.JSONObject
import sim.o.bg.gallerizmus.R
import kotlin.concurrent.timer


private const val REQUEST_CODE = 0b0000_0001
private const val READ_REQUEST_CODE = 0b0000_0010
private const val REQUEST_CODE_GET_PERMISSIONS = 0b0000_0011

class HomeActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setActionBar(toolbar)

        val fragment = GalleryFragment()
        fragment.setItems(queryCursors())
        fragmentManager.beginTransaction().add(R.id.homeScreenFragmentContainer, fragment).commit()

        val missingPermissions = mutableListOf<String>()
        arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE).forEach {
            if (checkSelfPermission(it) != PERMISSION_GRANTED) {
                missingPermissions.add(it)
            }
        }
    }

    private fun queryCursors() : MutableList<String> {
        val internalUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI
        val externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
                MediaStore.MediaColumns.DATA
        )

        // You eyeing dem oddly placed ')'s? Written that way, the query compiles to:
        // SELECT [...] (is_drm=0 OR is_drm IS NULL)) AND (_data IS NOT NULL) GROUP BY (bucket_display_name) | rather than:
        // SELECT [...] (is_drm=0 OR is_drm IS NULL)) AND (_data IS NOT NULL  GROUP BY  bucket_display_name)
        val internalCursor = contentResolver.query(internalUri, projection, null, null, null)
        val externalCursor = contentResolver.query(externalUri, projection, null, null, null)
        val mergedCursor = MergeCursor(arrayOf(internalCursor, externalCursor))

        var albums = mutableListOf<String>()
        val colPath = mergedCursor.getColumnIndex(MediaStore.MediaColumns.DATA)
        while (mergedCursor.moveToNext()) {
            albums.add(mergedCursor.getString(colPath))
        }

//        log("result", JSONObject(albums).toString())
        mergedCursor.close()

//        return System.currentTimeMillis() - start
        return albums
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        for ((index, permission) in permissions!!.withIndex()) {
            toast("$permission : ${grantResults!![index] == RESULT_OK}", true)
        }

        queryCursors()
    }

    private fun log(tag: String, text: String) {
        Log.wtf("TAAAG", text)
    }


    fun getCount(c: Context, album_name: String): String {
        val uriExternal = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val uriInternal = android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI

        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED)
        val cursorExternal = c.contentResolver.query(uriExternal, projection, "bucket_display_name = \"$album_name\"", null, null)
        val cursorInternal = c.contentResolver.query(uriInternal, projection, "bucket_display_name = \"$album_name\"", null, null)
        val cursor = MergeCursor(arrayOf<Cursor>(cursorExternal, cursorInternal))


        return cursor.count.toString() + " Photos"
    }

}

fun Activity.toast(text: String, log: Boolean = false) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    if (log) Log.wtf("TAAAG", text)
}
