package com.myapp.htpad.mugyeol

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton

import com.google.gson.JsonObject
import com.myapp.htpad.mugyeol.util.MySharedPreference
import com.myapp.htpad.mugyeol.util.RetrofitClient

import java.util.ArrayList
import java.util.Collections

import retrofit2.Call
import retrofit2.Response

class CarlistActivity_kt : AppCompatActivity() {
    private var mAdapter: Adapter? = null
    private var searchEdit: EditText? = null
    private var cars: List<CarModel.Data>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carlist)
        searchEdit = findViewById(R.id.carlist_search)
        val recyclerView = findViewById<RecyclerView>(R.id.carlist_recyclerview)
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.carlist_swipe)

        mAdapter = Adapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter

        //검색 기능
        searchEdit!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                val text = searchEdit!!.text.toString()
                search(text)

            }
        })


        //최초 차량 정보 불러오기
        getCarinfo(object : Callback {
            override fun onSuccess() {
                // nothing
            }

            override fun onFail() {
                // nothing
            }
        })

        //새로고침
        swipeRefreshLayout.setOnRefreshListener {
            getCarinfo(object : Callback {
                override fun onSuccess() {
                    swipeRefreshLayout.isRefreshing = false
                }

                override fun onFail() {
                    swipeRefreshLayout.isRefreshing = false

                }
            })
        }


        //TODO search btn for test. 나중에 지울 것
        val search = findViewById<ImageView>(R.id.carlist_searchicon)
        search.setOnClickListener { MySharedPreference.getInstance(this@CarlistActivity_kt).removeToken() }
    }

    private fun getCarinfo(callback: Callback) {
        RetrofitClient.restApi.getCarlist("Bearer " + MySharedPreference.getInstance(this@CarlistActivity_kt).getString("token")).enqueue(object : retrofit2.Callback<CarModel> {
            override fun onResponse(call: Call<CarModel>, response: Response<CarModel>) {
                val carModel = response.body()
                cars = carModel?.data

                if (cars != null && cars!!.isNotEmpty()) {
                    //최초 데이터 불러올 경우 & 검색어 없을때 새로고침 하는 경우
                    if (searchEdit!!.text.toString().isEmpty()) {
                        //favorite 기준으로 정렬
                        cars!!.sorted()
                        Collections.sort<CarModel.Data>(cars!!)
                        mAdapter!!.addAll(cars!!)

                    } else {
                        //검색어 있는 경우 새로고침
                          search(searchEdit!!.text.toString())
                    }
                    callback.onSuccess()
                }
            }

            override fun onFailure(call: Call<CarModel>, t: Throwable) {
                Toast.makeText(this@CarlistActivity_kt, "데이터를 불러오는데 실패하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                callback.onFail()
                Log.d(TAG, "fail : $t")
            }
        })
    }

    private fun search(text: String) {

        val searchedList = ArrayList<CarModel.Data>()


        //검색어에 부합하는 객체만 searchedList에 추가
        for (i in cars!!.indices) {
            if (cars!![i].description.contains(text)
                    || cars!![i].licenseNumber.contains(text)
                    || qty_str(cars!![i].capacity).contains(text)) {

                searchedList.add(cars!![i])
            }
        }

        searchedList.sort()
        mAdapter!!.addAll(searchedList)
    }

    private interface Callback {

        fun onSuccess()
        fun onFail()

    }

    private fun qty_str(capacity: Int): String {
        val qty_ton = capacity / 1000.0

        //소수점 아래 없는 경우 int형으로 형변환
        return if (capacity % 1000 == 0) {
            qty_ton.toInt().toString() + "t"
        } else qty_ton.toString() + "t"

    }

    override fun onDestroy() {
        //자동 로그인 체크 하지 않은 경우 shared 정보 clear
        if (!MySharedPreference.getInstance(this).getBoolean("boxcheck", false)) {
            MySharedPreference.getInstance(this).removeToken()
        }
        super.onDestroy()

    }

    internal inner class Adapter(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var arrayList: List<CarModel.Data> = ArrayList()

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {

            val itemview = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_car, viewGroup, false)


            return MyViewHolder(itemview)
        }

         inner class MyViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
            internal val favoriteIcon: ToggleButton = itemView.findViewById(R.id.caritem_favoriteicon)
             internal val description: TextView = itemView.findViewById(R.id.caritem_description)
             internal val carNumber: TextView = itemView.findViewById(R.id.caritem_carid)
             internal val loadQty: TextView = itemView.findViewById(R.id.caritem_loadqty)
             internal val constraint: ConstraintLayout = itemView.findViewById(R.id.caritem_constraint)

         }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
            val viewHolder = holder as MyViewHolder
            viewHolder.description.text = arrayList[pos].description
            viewHolder.carNumber.text = arrayList[pos].licenseNumber
            //단위 L-> t으로 변환하여 setText
            viewHolder.loadQty.text = qty_str(arrayList[pos].capacity)
            viewHolder.favoriteIcon.isChecked = arrayList[pos].favorite


            // SET MARGINTOP FOR 첫번째 카드뷰
            if (pos == 0) {
                val firstmargintop = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        14f,
                        mContext.resources.displayMetrics
                ).toInt()
                val marginLayoutParams = viewHolder.constraint.layoutParams as ViewGroup.MarginLayoutParams
                marginLayoutParams.topMargin = firstmargintop
                viewHolder.constraint.layoutParams = marginLayoutParams
            }


            viewHolder.favoriteIcon.setOnCheckedChangeListener { buttonView, isChecked ->
                //view에서 바뀔때만 update
                if (buttonView.isPressed) {
                    updateFavorite(isChecked, viewHolder.adapterPosition, object : CarlistActivity_kt.Callback {
                        override fun onSuccess() {
                            if (isChecked) {
                                Toast.makeText(mContext, "즐겨찾기에 추가되었습니다", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(mContext, "즐겨찾기를 해제했습니다", Toast.LENGTH_SHORT).show()

                            }

                        }

                        override fun onFail() {
                            if (isChecked) {
                                Toast.makeText(mContext, "즐겨찾기 추가에 실패하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(mContext, "즐겨찾기 해제에 실패하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()

                            }
                            viewHolder.favoriteIcon.isChecked = isChecked

                        }
                    })
                }
            }


        }

        private fun updateFavorite(favorite: Boolean, pos: Int, callback: CarlistActivity_kt.Callback) {

            RetrofitClient.restApi.putFavorite("Bearer " + MySharedPreference.getInstance(mContext).getString("token"), arrayList[pos].vehicleIdx, favorite)
                    .enqueue(object : retrofit2.Callback<JsonObject> {
                        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                            val jsonObject = response.body()
                            val result = jsonObject?.get("data")?.asString
                            if (result != null) {
                                if (result == "ok") {
                                    //스크롤 할때 즐겨찾기 업데이트 된거 유지되도록
                                    arrayList[pos].favorite = favorite
                                    callback.onSuccess()
                                }
                            }

                        }

                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            Log.d(TAG, "" + t)
                            callback.onFail()

                        }
                    })

        }


        override fun getItemCount(): Int {
            return arrayList.size
        }

         fun addAll(cars: List<CarModel.Data>) {
            this.arrayList = cars
            notifyDataSetChanged()
        }

    }

    companion object {

        private val TAG = "카리스트"
    }


}

