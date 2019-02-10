package com.strish.android.testproject

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.widget.DatePicker

import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class DateDialog : DialogFragment() {

    private var mDatePicker: DatePicker? = null

    private var mListener: DateDialogListener? = null
    private var date: Date? = null
    private var title: String? = null

    interface DateDialogListener {
        fun setDate(d: Date, s: String?)
    }

    fun setListener(listener: DateDialogListener) {
        mListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        date = arguments!!.getSerializable(DATE) as Date
        title = arguments!!.getSerializable(TITLE) as String

        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val v = LayoutInflater.from(activity).inflate(R.layout.dialog_date, null)

        mDatePicker = v.findViewById(R.id.dialog_date_date_picker)
        mDatePicker!!.init(year, month, day, null)

        val builder = AlertDialog.Builder(activity)

        builder.setView(v)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok
                ) { dialog, which ->
                    val year = mDatePicker!!.year
                    val month = mDatePicker!!.month
                    val day = mDatePicker!!.dayOfMonth
                    val date = GregorianCalendar(year, month, day).time
                    mListener!!.setDate(date, title)
                }
                .setNegativeButton("cancel") { dialogInterface, i -> }
                .create()

        return builder.create()
    }

    companion object {

        val DATE = "date"
        val TITLE = "title"

        fun newInstance(date: Date, title: String): DateDialog {
            val args = Bundle()
            args.putSerializable(DATE, date)
            args.putSerializable(TITLE, title)
            val fragment = DateDialog()
            fragment.arguments = args
            return fragment
        }
    }

}
