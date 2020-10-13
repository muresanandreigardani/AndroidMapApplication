package com.example.mapapplcation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import kotlinx.android.synthetic.main.fragment_blank.*

class BlankFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var optionsToPaint: Array<String>
    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }


    override fun onResume() {
        super.onResume()
        display_zoom_buttons.setOnCheckedChangeListener { _, isChecked ->
            setFragmentResult("display_zoom_buttons", bundleOf("checkValue" to isChecked))
        }
        this.fillTheOptionToPaintSelectable()
    }

    private fun fillTheOptionToPaintSelectable() {
        optionsToPaint = resources.getStringArray(R.array.optionsToPaint)
        spinner = view!!.findViewById(R.id.spinner)
        this.context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.optionsToPaint,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                spinner.onItemSelectedListener = this
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // it is not necessary to implement this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        setFragmentResult("itemToPaint", bundleOf("itemToPaintOption" to optionsToPaint[position]))
    }


}
