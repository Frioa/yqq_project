/*
package com.yqq.lib_audio.db

import android.database.sqlite.SQLiteDatabase
import com.yqq.lib_audio.app.AudioHelper
import com.yqq.lib_audio.mediaplayer.db.DaoMaster
import com.yqq.lib_audio.mediaplayer.db.DaoSession
import com.yqq.lib_audio.mediaplayer.db.FavouriteDao
import com.yqq.lib_audio.model.AudioBean
import com.yqq.lib_audio.model.Favourite

class GreenDaoHelper1 {

    companion object {
        private const val DB_NAME = "music_db"
        // 创建数据库与升级
        private var mHelper: DaoMaster.DevOpenHelper? = null
        // 最终创建号的数据库
        var mDb: SQLiteDatabase? = null
        // 管理数据库
        private lateinit var mDaoMaster: DaoMaster
        // 管理表
        private lateinit var mDaoSession: DaoSession

        fun initDatabase() {
            mHelper = DaoMaster.DevOpenHelper(AudioHelper.getContext(), DB_NAME)
            mDb = mHelper!!.writableDatabase
            mDaoMaster = DaoMaster(mDb)
            mDaoSession = mDaoMaster!!.newSession()
        }

        // 添加收藏
        @JvmStatic
        fun addFavourite(bean: AudioBean) {
            val dao = mDaoSession.favouriteDao
            val favourite = Favourite().apply {
                audioId = bean.id
                audioBean = bean
            }
            dao.insertOrReplaceInTx(favourite)
        }

        @JvmStatic

        fun removeFavourite(bean: AudioBean) {
            val dao = mDaoSession.favouriteDao
            val favourite = selectFavourite(bean)
            dao.delete(favourite)
        }

        @JvmStatic
        fun selectFavourite(bean: AudioBean): Favourite? {
            val dao = mDaoSession.favouriteDao
            return dao.queryBuilder().where(
                FavouriteDao.Properties.AudioId.eq(bean.id)
            ).unique()
        }
    }


}*/
