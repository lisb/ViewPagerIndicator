package com.lisb.viewpageindicator3.sample

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.SimpleAdapter
import java.text.Collator
import java.util.*

class ListSamples : ListActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        var path = intent.getStringExtra("com.jakewharton.android.viewpagerindicator.sample.Path")
        if (path == null) {
            path = ""
        }
        listAdapter = SimpleAdapter(
            this,
            getData(path),
            android.R.layout.simple_list_item_1,
            arrayOf("title"),
            intArrayOf(android.R.id.text1)
        )
        listView.isTextFilterEnabled = true
    }

    private fun getData(prefix: String): List<Map<String, Any?>> {
        val myData: MutableList<Map<String, Any?>> = ArrayList()
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory("com.jakewharton.android.viewpagerindicator.sample.SAMPLE")
        val pm = packageManager
        val list = pm.queryIntentActivities(mainIntent, 0)
        val prefixPath: Array<String>?
        var prefixWithSlash = prefix
        if (prefix == "") {
            prefixPath = null
        } else {
            prefixPath = prefix.split("/".toRegex()).toTypedArray()
            prefixWithSlash = "$prefix/"
        }
        val len = list.size
        val entries: MutableMap<String, Boolean?> = HashMap()
        for (i in 0 until len) {
            val info = list[i]
            val labelSeq = info.loadLabel(pm)
            val label = labelSeq?.toString() ?: info.activityInfo.name
            if (prefixWithSlash.isEmpty() || label.startsWith(prefixWithSlash)) {
                val labelPath = label.split("/".toRegex()).toTypedArray()
                val nextLabel = if (prefixPath == null) labelPath[0] else labelPath[prefixPath.size]
                if (prefixPath?.size ?: 0 == labelPath.size - 1) {
                    addItem(
                        myData, nextLabel, activityIntent(
                            info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.name
                        )
                    )
                } else {
                    if (entries[nextLabel] == null) {
                        addItem(
                            myData,
                            nextLabel,
                            browseIntent(if (prefix == "") nextLabel else "$prefix/$nextLabel")
                        )
                        entries[nextLabel] = true
                    }
                }
            }
        }
        Collections.sort(myData, NAME_COMPARATOR)
        return myData
    }

    private fun activityIntent(pkg: String, componentName: String): Intent {
        val result = Intent()
        result.setClassName(pkg, componentName)
        return result
    }

    private fun browseIntent(path: String?): Intent {
        val result = Intent()
        result.setClass(this, ListSamples::class.java)
        result.putExtra("com.jakewharton.android.viewpagerindicator.sample.Path", path)
        return result
    }

    private fun addItem(data: MutableList<Map<String, Any?>>, name: String?, intent: Intent?) {
        val temp: MutableMap<String, Any?> = HashMap()
        temp["title"] = name
        temp["intent"] = intent
        data.add(temp)
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        val map = l.getItemAtPosition(position) as Map<*, *>
        val intent = map["intent"] as Intent?
        startActivity(intent)
    }

    companion object {
        private val NAME_COMPARATOR: Comparator<Map<String, Any?>> =
            object : Comparator<Map<String, Any?>> {
                private val collator = Collator.getInstance()
                override fun compare(map1: Map<String, Any?>, map2: Map<String, Any?>): Int {
                    return collator.compare(map1["title"], map2["title"])
                }
            }
    }
}