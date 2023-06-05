package br.com.puccampinas.uteeth3pi.recycleview

class User {
    var name: String? = null
    var phone: String? = null
    var uid: String? = null
    var fcmToken: String? = null

    constructor() {}
    constructor(name: String?, phone: String?, uid: String?, fcmToken: String?) {
        this.name = name
        this.phone = phone
        this.uid = uid
        this.fcmToken = fcmToken
    }
}