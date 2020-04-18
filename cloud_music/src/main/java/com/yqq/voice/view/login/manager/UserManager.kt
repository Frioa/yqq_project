package com.yqq.voice.view.login.manager

import com.yqq.voice.model.user.User

class UserManager private constructor() {

    private var mUser: User? = null

    companion object {
        val instance: UserManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            UserManager()
        }
    }


    fun saveUser(user: User) {
        mUser = user
        saveLocal(user)
    }


    private fun saveLocal(user: User) {

    }

    fun getUser(): User? {
        return if (mUser == null) getLocal() else mUser
    }

    private fun getLocal(): User? {
        return null
    }

    fun hasLogin(): Boolean = getUser() != null

    private fun removeUser() {
        mUser = null
    }

    private fun removeLocal() {

    }
}