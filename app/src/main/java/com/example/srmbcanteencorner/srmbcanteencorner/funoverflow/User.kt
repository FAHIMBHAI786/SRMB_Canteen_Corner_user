package com.example.srmbcanteencorner.srmbcanteencorner.funoverflow

class User {
    var userId: String? = null
    var name:String? = null
    var eid:String? = null
    var phone:String? = null
    var password:String? = null

constructor(){}
    constructor(userId:String?, name: String?, eid: String?, phone: String?, password: String?, ) {
        this.userId = userId
        this.name = name
        this.eid = eid
        this.phone = phone
        this.password = password

    }
}