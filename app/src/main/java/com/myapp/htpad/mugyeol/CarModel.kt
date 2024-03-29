package com.myapp.htpad.mugyeol


class CarModel(val data:List<Data>){

    class Data(val description:String,
               val licenseNumber:String,
               val capacity:Int,
               var favorite:Boolean,
               val vehicleIdx:Int
    ): Comparable<Data>  {

        override fun compareTo(other: Data): Int {
            if (this.favorite && !other.favorite) {
                return -1
            } else if (!this.favorite && other.favorite) {
                return 1
            }
            return 0
        }

    }


}
