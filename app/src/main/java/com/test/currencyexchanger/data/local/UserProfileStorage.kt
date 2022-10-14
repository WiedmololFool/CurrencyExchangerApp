package com.test.currencyexchanger.data.local

import com.test.currencyexchanger.domain.model.UserProfile

interface UserProfileStorage {

    fun save(userProfile: UserProfile): Boolean

    fun get(): UserProfile

    fun clear(): Boolean
}