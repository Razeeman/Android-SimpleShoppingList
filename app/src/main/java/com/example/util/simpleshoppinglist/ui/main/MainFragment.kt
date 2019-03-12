package com.example.util.simpleshoppinglist.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.util.simpleshoppinglist.R
import com.example.util.simpleshoppinglist.data.model.ListItem
import kotlinx.android.synthetic.main.main_fragment.view.*

class MainFragment : Fragment(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter

    private lateinit var textView: TextView

    /**
     * Fragment instantiation.
     */
    companion object {
        fun newInstance(presenter: MainContract.Presenter): MainFragment {
            return MainFragment().apply { this.presenter = presenter }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.main_fragment, container, false)

        textView = root.tv_test

        return root
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showItems(items: List<ListItem>) {
        textView.text = items[0].name
    }

    override fun showNoItems() {
        textView.text = "No items"
    }
}