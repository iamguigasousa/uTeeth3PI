package br.com.puccampinas.uteeth3pi.recycleview

class User {
    var name: String? = null
    var phone: String? = null

    constructor() {}
    constructor(name: String?, phone: String?) {
        this.name = name
        this.phone = phone
    }
}