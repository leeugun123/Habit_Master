package org.techtown.habit_master.Share

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter.CalendarViewHolder
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils.getDay3LettersName
import com.michalsvec.singlerowcalendar.utils.DateUtils.getDayNumber
import kotlinx.android.synthetic.main.item_calendar_day_selected.view.*
import kotlinx.android.synthetic.main.item_calendar_day_unselected.view.*
import org.techtown.habit_master.R
import org.techtown.habit_master.databinding.ActivityShareBinding
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList



class ShareActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityShareBinding

    private val calendar = Calendar.getInstance()
    private var currentMonth = 0

    private val shareList : ArrayList<Share> = ArrayList()
    lateinit var recyclerView: RecyclerView

    private val database = Firebase.database//파이어베이스 연동
    var shareHabits = database.getReference("Habits")
    private var shareTitle : String? = null
    //Habit 데이터 가져오기

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mBinding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        shareTitle = intent.getStringExtra("habitTitle")

        mBinding.shareTitle.text = shareTitle.toString()
        //습관제목 붙이기

        val rowCalendarManager = object : CalendarViewManager {

            override fun setCalendarViewResourceId(position: Int, date: Date, isSelected: Boolean): Int {

                val cal = Calendar.getInstance()
                cal.time = date

                return if(isSelected){
                    when (cal[Calendar.DAY_OF_WEEK]){
                        else -> R.layout.item_calendar_day_unselected
                    }//선택 했을 경우

                }

                else{
                    when(cal[Calendar.DAY_OF_WEEK]){
                        else -> R.layout.item_calendar_day_unselected
                    }//기본 상태

                }


            }

            override fun bindDataToCalendarView(holder: CalendarViewHolder, date: Date, position: Int, isSelected: Boolean) {

                holder.itemView.unselected_date.text = getDayNumber(date)
                holder.itemView.unselected_day.text = getDay3LettersName(date)

            }

        }

        //ViewCreated
        val rowCalendarChangesObserver = object: CalendarChangesObserver {
            @SuppressLint("SetTextI18n")
            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                super.whenSelectionChanged(isSelected, position, date)
            }


        }

        val rowSelectionManager = object : CalendarSelectionManager {

            override fun canBeItemSelected(position: Int, date: Date): Boolean {

                var str : String = date.toString()
                //Tue Aug 02 21:05:31 GMT+09:00 2022 , 파싱

                var year = str.substring(str.length-4 until str.length)
                //년
                var month = calMonth(str.substring(4 until 7))
                //달
                var day = str.substring(8 until 10)
                //일
                var touchDay = year+month+day

                recycleUpdate(touchDay)

                return true

            }

            private fun calMonth(month: String): String {

                //월을 추가적으로 구현해야 함

                if(month == "Aug"){
                    return "08"
                }
                else return "03"

            }

        }

        mBinding.rowCalendar.apply {

            calendarViewManager = rowCalendarManager
            calendarChangesObserver = rowCalendarChangesObserver
            calendarSelectionManager = rowSelectionManager

            this.setDates(getFutureDatesOfCurrentMonth())
            this.init()

        }

        val onlyDate : LocalDate = LocalDate.now()//오늘 날짜 가져오기
        val date : String = onlyDate.toString().replace("-","")

        mBinding.date.text = date//오늘 날짜

        recycleUpdate(date)//recyclerview 업데이트

        mBinding.uploadButton.setOnClickListener{

            val intent = Intent(this, UploadActivity::class.java)
            intent.putExtra("habitTitle",shareTitle.toString())
            intent.putExtra("habitDate",date)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            //지금 액티비티 그대로 유지하기

        }//이미지 저장하기

    }

    private fun recycleUpdate(date : String) {

        shareHabits = shareHabits.child(shareTitle.toString()).child("date").child(date)
        //선택한 것 가져오기

        shareHabits.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                shareList.clear()//데이터가 변경되었을때 다시 불러옴

                //뷰 터치시 여기까지는 들어옴

                for(ds in snapshot.children){

                    shareList.add(Share(ds.child("nickName").value.toString(),
                        ds.child("shareImg").value.toString(),
                        ds.child("carTime").value.toString(),
                        ds.child("description").value.toString(),
                        true))
                    //파베로부터 데이터 가져오기\


                }

                recyclerView = mBinding.shareRecyclerView
                recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                recyclerView.adapter = ShareAdapter(shareList,applicationContext)
                //리사이클러뷰 붙이기

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })



    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        currentMonth = calendar[Calendar.MONTH]
        return getDates(mutableListOf())
    }

    private fun getDates(list: MutableList<Date>): List<Date> {

        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)

        while (currentMonth == calendar[Calendar.MONTH]) {

            calendar.add(Calendar.DATE, +1)

            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }

        calendar.add(Calendar.DATE, -1)

        return list

    }

}

