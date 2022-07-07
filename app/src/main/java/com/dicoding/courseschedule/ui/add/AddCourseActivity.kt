package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.add.AddCourseViewModelFactory.Companion.createFactory
import com.dicoding.courseschedule.ui.home.HomeViewModelFactory.Companion.createFactory
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private lateinit var viewModel: AddCourseViewModel
    private lateinit var editCourseName: TextInputEditText
    private lateinit var editLecturer: TextInputEditText
    private lateinit var editNote: TextInputEditText
    private lateinit var selectDay: Spinner
    private lateinit var startTime: String
    private lateinit var endTime: String
    private lateinit var editStartTime: TextView
    private lateinit var editEndTime: TextView
    private val timePickerFragment = TimePickerFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        viewModel = ViewModelProvider(
            this, AddCourseViewModelFactory.createFactory(this)
        )[AddCourseViewModel::class.java]

        editStartTime = findViewById(R.id.add_start_time)
        editEndTime = findViewById(R.id.add_end_time)
        val pickStartTime: ImageButton = findViewById(R.id.add_clock_start)
        val pickEndTime: ImageButton = findViewById(R.id.add_clock_end)

        pickStartTime.setOnClickListener {
            timePickerFragment.show(supportFragmentManager, "start")
        }
        pickEndTime.setOnClickListener {
            timePickerFragment.show(supportFragmentManager, "end")
        }
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        if (tag == "start") {
            startTime = format.format(calendar.time)
            editStartTime.text = startTime
        } else {
            endTime = format.format(calendar.time)
            editEndTime.text = endTime
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_insert -> {
                editCourseName = findViewById(R.id.add_edit_course)
                editLecturer = findViewById(R.id.add_edit_lecturer)
                editNote = findViewById(R.id.add_edit_note)
                selectDay = findViewById(R.id.add_spinner_day)

                val courseName = editCourseName.text.toString().trim()
                val day = selectDay.selectedItemPosition
                val lecturer = editLecturer.text.toString().trim()
                val note = editNote.text.toString().trim()

                if (courseName.isEmpty() || lecturer.isEmpty() || note.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                    Toast.makeText(applicationContext, "Please fill in the form", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.insertCourse(courseName, day, startTime, endTime, lecturer, note)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}