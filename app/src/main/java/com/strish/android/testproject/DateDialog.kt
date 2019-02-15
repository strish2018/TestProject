package com.strish.android.testproject

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.dialog_date.*
import java.util.*

class DateDialog : DialogFragment() {

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

        date = arguments?.getSerializable(DATE) as Date
        title = arguments?.getSerializable(TITLE) as String

        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_date, null)

        dialog_date_date_picker.init(year, month, day, null)

        val builder = AlertDialog.Builder(activity)

        builder.setView(dialogView)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok
                ) { _: DialogInterface, _: Int ->
                    val date = GregorianCalendar(
                            dialog_date_date_picker.year,
                            dialog_date_date_picker.month,
                            dialog_date_date_picker.dayOfMonth
                    ).time

                    mListener?.setDate(date, title)
                }
                .setNegativeButton("cancel") { _, _ -> }
                .create()

        return builder.create()
    }

    companion object {

        private const val DATE = "date"
        private const val TITLE = "title"

        fun newInstance(date: Date, title: String): DateDialog {
            return DateDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(DATE, date)
                    putSerializable(TITLE, title)
                }
            }
        }
    }

}
